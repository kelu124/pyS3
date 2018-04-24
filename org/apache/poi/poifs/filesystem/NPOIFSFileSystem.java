package org.apache.poi.poifs.filesystem;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.EmptyFileException;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.poifs.dev.POIFSViewable;
import org.apache.poi.poifs.filesystem.BlockStore.ChainLoopDetector;
import org.apache.poi.poifs.nio.ByteArrayBackedDataSource;
import org.apache.poi.poifs.nio.DataSource;
import org.apache.poi.poifs.nio.FileBackedDataSource;
import org.apache.poi.poifs.property.DirectoryProperty;
import org.apache.poi.poifs.property.DocumentProperty;
import org.apache.poi.poifs.property.NPropertyTable;
import org.apache.poi.poifs.storage.BATBlock;
import org.apache.poi.poifs.storage.BATBlock.BATBlockAndIndex;
import org.apache.poi.poifs.storage.BlockAllocationTableReader;
import org.apache.poi.poifs.storage.BlockAllocationTableWriter;
import org.apache.poi.poifs.storage.HeaderBlock;
import org.apache.poi.poifs.storage.HeaderBlockConstants;
import org.apache.poi.poifs.storage.HeaderBlockWriter;
import org.apache.poi.util.CloseIgnoringInputStream;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LongField;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class NPOIFSFileSystem extends BlockStore implements POIFSViewable, Closeable {
    private static final POILogger LOG = POILogFactory.getLogger(NPOIFSFileSystem.class);
    private List<BATBlock> _bat_blocks;
    private DataSource _data;
    private HeaderBlock _header;
    private NPOIFSMiniStore _mini_store;
    private NPropertyTable _property_table;
    private DirectoryNode _root;
    private List<BATBlock> _xbat_blocks;
    private POIFSBigBlockSize bigBlockSize;

    public static InputStream createNonClosingInputStream(InputStream is) {
        return new CloseIgnoringInputStream(is);
    }

    private NPOIFSFileSystem(boolean newFS) {
        this.bigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
        this._header = new HeaderBlock(this.bigBlockSize);
        this._property_table = new NPropertyTable(this._header);
        this._mini_store = new NPOIFSMiniStore(this, this._property_table.getRoot(), new ArrayList(), this._header);
        this._xbat_blocks = new ArrayList();
        this._bat_blocks = new ArrayList();
        this._root = null;
        if (newFS) {
            this._data = new ByteArrayBackedDataSource(new byte[(this.bigBlockSize.getBigBlockSize() * 3)]);
        }
    }

    public NPOIFSFileSystem() {
        this(true);
        this._header.setBATCount(1);
        this._header.setBATArray(new int[]{1});
        BATBlock bb = BATBlock.createEmptyBATBlock(this.bigBlockSize, false);
        bb.setOurBlockIndex(1);
        this._bat_blocks.add(bb);
        setNextBlock(0, -2);
        setNextBlock(1, -3);
        this._property_table.setStartBlock(0);
    }

    public NPOIFSFileSystem(File file) throws IOException {
        this(file, true);
    }

    public NPOIFSFileSystem(File file, boolean readOnly) throws IOException {
        this(null, file, readOnly, true);
    }

    public NPOIFSFileSystem(FileChannel channel) throws IOException {
        this(channel, true);
    }

    public NPOIFSFileSystem(FileChannel channel, boolean readOnly) throws IOException {
        this(channel, null, readOnly, false);
    }

    private NPOIFSFileSystem(FileChannel channel, File srcFile, boolean readOnly, boolean closeChannelOnError) throws IOException {
        this(false);
        if (srcFile != null) {
            try {
                if (srcFile.length() == 0) {
                    throw new EmptyFileException();
                }
                FileBackedDataSource d = new FileBackedDataSource(srcFile, readOnly);
                channel = d.getChannel();
                this._data = d;
            } catch (IOException e) {
                if (closeChannelOnError && channel != null) {
                    channel.close();
                }
                throw e;
            } catch (RuntimeException e2) {
                if (closeChannelOnError && channel != null) {
                    channel.close();
                }
                throw e2;
            }
        }
        this._data = new FileBackedDataSource(channel, readOnly);
        ByteBuffer headerBuffer = ByteBuffer.allocate(512);
        IOUtils.readFully(channel, headerBuffer);
        this._header = new HeaderBlock(headerBuffer);
        readCoreContents();
    }

    public NPOIFSFileSystem(InputStream stream) throws IOException {
        this(false);
        ReadableByteChannel channel = null;
        boolean success = false;
        try {
            channel = Channels.newChannel(stream);
            ByteBuffer headerBuffer = ByteBuffer.allocate(512);
            IOUtils.readFully(channel, headerBuffer);
            this._header = new HeaderBlock(headerBuffer);
            BlockAllocationTableReader.sanityCheckBlockCount(this._header.getBATCount());
            long maxSize = BATBlock.calculateMaximumSize(this._header);
            if (maxSize > 2147483647L) {
                throw new IllegalArgumentException("Unable read a >2gb file via an InputStream");
            }
            ByteBuffer data = ByteBuffer.allocate((int) maxSize);
            headerBuffer.position(0);
            data.put(headerBuffer);
            data.position(headerBuffer.capacity());
            IOUtils.readFully(channel, data);
            success = true;
            this._data = new ByteArrayBackedDataSource(data.array(), data.position());
            if (channel != null) {
                channel.close();
            }
            closeInputStream(stream, true);
            readCoreContents();
        } catch (Throwable th) {
            if (channel != null) {
                channel.close();
            }
            closeInputStream(stream, success);
        }
    }

    private void closeInputStream(InputStream stream, boolean success) {
        try {
            stream.close();
        } catch (IOException e) {
            if (success) {
                throw new RuntimeException(e);
            }
            LOG.log(7, "can't close input stream", e);
        }
    }

    public static boolean hasPOIFSHeader(InputStream inp) throws IOException {
        inp.mark(8);
        byte[] header = new byte[8];
        int bytesRead = IOUtils.readFully(inp, header);
        LongField signature = new LongField(0, header);
        if (inp instanceof PushbackInputStream) {
            ((PushbackInputStream) inp).unread(header, 0, bytesRead);
        } else {
            inp.reset();
        }
        if (signature.get() == HeaderBlockConstants._signature) {
            return true;
        }
        return false;
    }

    public static boolean hasPOIFSHeader(byte[] header8Bytes) {
        if (new LongField(0, header8Bytes).get() == HeaderBlockConstants._signature) {
            return true;
        }
        return false;
    }

    private void readCoreContents() throws IOException {
        int fatAt;
        int i;
        this.bigBlockSize = this._header.getBigBlockSize();
        ChainLoopDetector loopDetector = getChainLoopDetector();
        for (int fatAt2 : this._header.getBATArray()) {
            readBAT(fatAt2, loopDetector);
        }
        int remainingFATs = this._header.getBATCount() - this._header.getBATArray().length;
        int nextAt = this._header.getXBATIndex();
        for (i = 0; i < this._header.getXBATCount(); i++) {
            loopDetector.claim(nextAt);
            BATBlock xfat = BATBlock.createBATBlock(this.bigBlockSize, getBlockAt(nextAt));
            xfat.setOurBlockIndex(nextAt);
            nextAt = xfat.getValueAt(this.bigBlockSize.getXBATEntriesPerBlock());
            this._xbat_blocks.add(xfat);
            int xbatFATs = Math.min(remainingFATs, this.bigBlockSize.getXBATEntriesPerBlock());
            for (int j = 0; j < xbatFATs; j++) {
                fatAt2 = xfat.getValueAt(j);
                if (fatAt2 == -1 || fatAt2 == -2) {
                    break;
                }
                readBAT(fatAt2, loopDetector);
            }
            remainingFATs -= xbatFATs;
        }
        this._property_table = new NPropertyTable(this._header, this);
        List<BATBlock> sbats = new ArrayList();
        this._mini_store = new NPOIFSMiniStore(this, this._property_table.getRoot(), sbats, this._header);
        nextAt = this._header.getSBATStart();
        for (i = 0; i < this._header.getSBATCount() && nextAt != -2; i++) {
            loopDetector.claim(nextAt);
            BATBlock sfat = BATBlock.createBATBlock(this.bigBlockSize, getBlockAt(nextAt));
            sfat.setOurBlockIndex(nextAt);
            sbats.add(sfat);
            nextAt = getNextBlock(nextAt);
        }
    }

    private void readBAT(int batAt, ChainLoopDetector loopDetector) throws IOException {
        loopDetector.claim(batAt);
        BATBlock bat = BATBlock.createBATBlock(this.bigBlockSize, getBlockAt(batAt));
        bat.setOurBlockIndex(batAt);
        this._bat_blocks.add(bat);
    }

    private BATBlock createBAT(int offset, boolean isBAT) throws IOException {
        BATBlock newBAT = BATBlock.createEmptyBATBlock(this.bigBlockSize, !isBAT);
        newBAT.setOurBlockIndex(offset);
        this._data.write(ByteBuffer.allocate(this.bigBlockSize.getBigBlockSize()), (long) ((offset + 1) * this.bigBlockSize.getBigBlockSize()));
        return newBAT;
    }

    protected ByteBuffer getBlockAt(int offset) throws IOException {
        try {
            return this._data.read(this.bigBlockSize.getBigBlockSize(), ((long) (offset + 1)) * ((long) this.bigBlockSize.getBigBlockSize()));
        } catch (IndexOutOfBoundsException e) {
            IndexOutOfBoundsException wrapped = new IndexOutOfBoundsException("Block " + offset + " not found");
            wrapped.initCause(e);
            throw wrapped;
        }
    }

    protected ByteBuffer createBlockIfNeeded(int offset) throws IOException {
        try {
            return getBlockAt(offset);
        } catch (IndexOutOfBoundsException e) {
            long startAt = (long) ((offset + 1) * this.bigBlockSize.getBigBlockSize());
            this._data.write(ByteBuffer.allocate(getBigBlockSize()), startAt);
            return getBlockAt(offset);
        }
    }

    protected BATBlockAndIndex getBATBlockAndIndex(int offset) {
        return BATBlock.getBATBlockAndIndex(offset, this._header, this._bat_blocks);
    }

    protected int getNextBlock(int offset) {
        BATBlockAndIndex bai = getBATBlockAndIndex(offset);
        return bai.getBlock().getValueAt(bai.getIndex());
    }

    protected void setNextBlock(int offset, int nextBlock) {
        BATBlockAndIndex bai = getBATBlockAndIndex(offset);
        bai.getBlock().setValueAt(bai.getIndex(), nextBlock);
    }

    protected int getFreeBlock() throws IOException {
        int numSectors = this.bigBlockSize.getBATEntriesPerBlock();
        int offset = 0;
        for (BATBlock bat : this._bat_blocks) {
            if (bat.hasFreeSectors()) {
                for (int j = 0; j < numSectors; j++) {
                    if (bat.getValueAt(j) == -1) {
                        return offset + j;
                    }
                }
                continue;
            }
            offset += numSectors;
        }
        BATBlock bat2 = createBAT(offset, true);
        bat2.setValueAt(0, -3);
        this._bat_blocks.add(bat2);
        if (this._header.getBATCount() >= 109) {
            BATBlock xbat = null;
            for (BATBlock x : this._xbat_blocks) {
                if (x.hasFreeSectors()) {
                    xbat = x;
                    break;
                }
            }
            if (xbat == null) {
                xbat = createBAT(offset + 1, false);
                xbat.setValueAt(0, offset);
                bat2.setValueAt(1, -4);
                offset++;
                if (this._xbat_blocks.size() == 0) {
                    this._header.setXBATStart(offset);
                } else {
                    ((BATBlock) this._xbat_blocks.get(this._xbat_blocks.size() - 1)).setValueAt(this.bigBlockSize.getXBATEntriesPerBlock(), offset);
                }
                this._xbat_blocks.add(xbat);
                this._header.setXBATCount(this._xbat_blocks.size());
            } else {
                for (int i = 0; i < this.bigBlockSize.getXBATEntriesPerBlock(); i++) {
                    if (xbat.getValueAt(i) == -1) {
                        xbat.setValueAt(i, offset);
                        break;
                    }
                }
            }
        } else {
            int[] newBATs = new int[(this._header.getBATCount() + 1)];
            System.arraycopy(this._header.getBATArray(), 0, newBATs, 0, newBATs.length - 1);
            newBATs[newBATs.length - 1] = offset;
            this._header.setBATArray(newBATs);
        }
        this._header.setBATCount(this._bat_blocks.size());
        return offset + 1;
    }

    protected long size() throws IOException {
        return this._data.size();
    }

    protected ChainLoopDetector getChainLoopDetector() throws IOException {
        return new ChainLoopDetector(this, this._data.size());
    }

    NPropertyTable _get_property_table() {
        return this._property_table;
    }

    public NPOIFSMiniStore getMiniStore() {
        return this._mini_store;
    }

    void addDocument(NPOIFSDocument document) {
        this._property_table.addProperty(document.getDocumentProperty());
    }

    void addDirectory(DirectoryProperty directory) {
        this._property_table.addProperty(directory);
    }

    public DocumentEntry createDocument(InputStream stream, String name) throws IOException {
        return getRoot().createDocument(name, stream);
    }

    public DocumentEntry createDocument(String name, int size, POIFSWriterListener writer) throws IOException {
        return getRoot().createDocument(name, size, writer);
    }

    public DirectoryEntry createDirectory(String name) throws IOException {
        return getRoot().createDirectory(name);
    }

    public DocumentEntry createOrUpdateDocument(InputStream stream, String name) throws IOException {
        return getRoot().createOrUpdateDocument(name, stream);
    }

    public boolean isInPlaceWriteable() {
        if ((this._data instanceof FileBackedDataSource) && ((FileBackedDataSource) this._data).isWriteable()) {
            return true;
        }
        return false;
    }

    public void writeFilesystem() throws IOException {
        if (!(this._data instanceof FileBackedDataSource)) {
            throw new IllegalArgumentException("POIFS opened from an inputstream, so writeFilesystem() may not be called. Use writeFilesystem(OutputStream) instead");
        } else if (((FileBackedDataSource) this._data).isWriteable()) {
            syncWithDataSource();
        } else {
            throw new IllegalArgumentException("POIFS opened in read only mode, so writeFilesystem() may not be called. Open the FileSystem in read-write mode first");
        }
    }

    public void writeFilesystem(OutputStream stream) throws IOException {
        syncWithDataSource();
        this._data.copyTo(stream);
    }

    private void syncWithDataSource() throws IOException {
        this._mini_store.syncWithDataSource();
        NPOIFSStream propStream = new NPOIFSStream(this, this._header.getPropertyStart());
        this._property_table.preWrite();
        this._property_table.write(propStream);
        new HeaderBlockWriter(this._header).writeBlock(getBlockAt(-1));
        for (BATBlock bat : this._bat_blocks) {
            BlockAllocationTableWriter.writeBlock(bat, getBlockAt(bat.getOurBlockIndex()));
        }
        for (BATBlock bat2 : this._xbat_blocks) {
            BlockAllocationTableWriter.writeBlock(bat2, getBlockAt(bat2.getOurBlockIndex()));
        }
    }

    public void close() throws IOException {
        this._data.close();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("two arguments required: input filename and output filename");
            System.exit(1);
        }
        InputStream istream = new FileInputStream(args[0]);
        try {
            FileOutputStream ostream = new FileOutputStream(args[1]);
            NPOIFSFileSystem fs;
            try {
                fs = new NPOIFSFileSystem(istream);
                fs.writeFilesystem(ostream);
                fs.close();
                ostream.close();
            } catch (Throwable th) {
                ostream.close();
            }
        } finally {
            istream.close();
        }
    }

    public DirectoryNode getRoot() {
        if (this._root == null) {
            this._root = new DirectoryNode(this._property_table.getRoot(), this, null);
        }
        return this._root;
    }

    public DocumentInputStream createDocumentInputStream(String documentName) throws IOException {
        return getRoot().createDocumentInputStream(documentName);
    }

    void remove(EntryNode entry) throws IOException {
        if (entry instanceof DocumentEntry) {
            new NPOIFSDocument((DocumentProperty) entry.getProperty(), this).free();
        }
        this._property_table.removeProperty(entry.getProperty());
    }

    public Object[] getViewableArray() {
        if (preferArray()) {
            return getRoot().getViewableArray();
        }
        return new Object[0];
    }

    public Iterator<Object> getViewableIterator() {
        if (preferArray()) {
            return Collections.emptyList().iterator();
        }
        return getRoot().getViewableIterator();
    }

    public boolean preferArray() {
        return getRoot().preferArray();
    }

    public String getShortDescription() {
        return "POIFS FileSystem";
    }

    public int getBigBlockSize() {
        return this.bigBlockSize.getBigBlockSize();
    }

    public POIFSBigBlockSize getBigBlockSizeDetails() {
        return this.bigBlockSize;
    }

    protected int getBlockStoreBlockSize() {
        return getBigBlockSize();
    }

    @Internal
    public NPropertyTable getPropertyTable() {
        return this._property_table;
    }

    @Internal
    public HeaderBlock getHeaderBlock() {
        return this._header;
    }
}
