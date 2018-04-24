package org.apache.poi.poifs.filesystem;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.poifs.dev.POIFSViewable;
import org.apache.poi.poifs.property.DirectoryProperty;
import org.apache.poi.poifs.property.Property;
import org.apache.poi.poifs.property.PropertyTable;
import org.apache.poi.poifs.storage.BATBlock;
import org.apache.poi.poifs.storage.BlockAllocationTableWriter;
import org.apache.poi.poifs.storage.BlockList;
import org.apache.poi.poifs.storage.BlockWritable;
import org.apache.poi.poifs.storage.HeaderBlock;
import org.apache.poi.poifs.storage.HeaderBlockConstants;
import org.apache.poi.poifs.storage.HeaderBlockWriter;
import org.apache.poi.poifs.storage.RawDataBlockList;
import org.apache.poi.poifs.storage.SmallBlockTableWriter;
import org.apache.poi.util.CloseIgnoringInputStream;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LongField;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class OPOIFSFileSystem implements POIFSViewable {
    private static final POILogger _logger = POILogFactory.getLogger(OPOIFSFileSystem.class);
    private List<OPOIFSDocument> _documents;
    private PropertyTable _property_table;
    private DirectoryNode _root;
    private POIFSBigBlockSize bigBlockSize;

    public static InputStream createNonClosingInputStream(InputStream is) {
        return new CloseIgnoringInputStream(is);
    }

    public OPOIFSFileSystem() {
        this.bigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
        this._property_table = new PropertyTable(new HeaderBlock(this.bigBlockSize));
        this._documents = new ArrayList();
        this._root = null;
    }

    public OPOIFSFileSystem(InputStream stream) throws IOException {
        this();
        boolean success = false;
        try {
            this.bigBlockSize = new HeaderBlock(stream).getBigBlockSize();
            RawDataBlockList data_blocks = new RawDataBlockList(stream, this.bigBlockSize);
            success = true;
        } finally {
            closeInputStream(stream, success);
        }
    }

    protected void closeInputStream(InputStream stream, boolean success) {
        if (stream.markSupported() && !(stream instanceof ByteArrayInputStream)) {
            String msg = "POIFS is closing the supplied input stream of type (" + stream.getClass().getName() + ") which supports mark/reset.  " + "This will be a problem for the caller if the stream will still be used.  " + "If that is the case the caller should wrap the input stream to avoid this close logic.  " + "This warning is only temporary and will not be present in future versions of POI.";
            _logger.log(5, new Object[]{msg});
        }
        try {
            stream.close();
        } catch (IOException e) {
            if (success) {
                throw new RuntimeException(e);
            }
            _logger.log(7, new Object[]{"can't close input stream", e});
        }
    }

    public static boolean hasPOIFSHeader(InputStream inp) throws IOException {
        return hasPOIFSHeader(IOUtils.peekFirst8Bytes(inp));
    }

    public static boolean hasPOIFSHeader(byte[] header8Bytes) {
        if (new LongField(0, header8Bytes).get() == HeaderBlockConstants._signature) {
            return true;
        }
        return false;
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

    public void writeFilesystem(OutputStream stream) throws IOException {
        this._property_table.preWrite();
        SmallBlockTableWriter sbtw = new SmallBlockTableWriter(this.bigBlockSize, this._documents, this._property_table.getRoot());
        BlockAllocationTableWriter bat = new BlockAllocationTableWriter(this.bigBlockSize);
        List<Object> bm_objects = new ArrayList();
        bm_objects.addAll(this._documents);
        bm_objects.add(this._property_table);
        bm_objects.add(sbtw);
        bm_objects.add(sbtw.getSBAT());
        Iterator<Object> iter = bm_objects.iterator();
        while (iter.hasNext()) {
            BATManaged bmo = (BATManaged) iter.next();
            int block_count = bmo.countBlocks();
            if (block_count != 0) {
                bmo.setStartBlock(bat.allocateSpace(block_count));
            }
        }
        int batStartBlock = bat.createBlocks();
        HeaderBlockWriter header_block_writer = new HeaderBlockWriter(this.bigBlockSize);
        BATBlock[] xbat_blocks = header_block_writer.setBATBlocks(bat.countBlocks(), batStartBlock);
        header_block_writer.setPropertyStart(this._property_table.getStartBlock());
        header_block_writer.setSBATStart(sbtw.getSBAT().getStartBlock());
        header_block_writer.setSBATBlockCount(sbtw.getSBATBlockCount());
        List<Object> writers = new ArrayList();
        writers.add(header_block_writer);
        writers.addAll(this._documents);
        writers.add(this._property_table);
        writers.add(sbtw);
        writers.add(sbtw.getSBAT());
        writers.add(bat);
        for (Object add : xbat_blocks) {
            writers.add(add);
        }
        iter = writers.iterator();
        while (iter.hasNext()) {
            ((BlockWritable) iter.next()).writeBlocks(stream);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("two arguments required: input filename and output filename");
            System.exit(1);
        }
        FileInputStream istream = new FileInputStream(args[0]);
        FileOutputStream ostream = new FileOutputStream(args[1]);
        new OPOIFSFileSystem(istream).writeFilesystem(ostream);
        istream.close();
        ostream.close();
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

    void addDocument(OPOIFSDocument document) {
        this._documents.add(document);
        this._property_table.addProperty(document.getDocumentProperty());
    }

    void addDirectory(DirectoryProperty directory) {
        this._property_table.addProperty(directory);
    }

    void remove(EntryNode entry) {
        this._property_table.removeProperty(entry.getProperty());
        if (entry.isDocumentEntry()) {
            this._documents.remove(((DocumentNode) entry).getDocument());
        }
    }

    private void processProperties(BlockList small_blocks, BlockList big_blocks, Iterator<Property> properties, DirectoryNode dir, int headerPropertiesStartAt) throws IOException {
        while (properties.hasNext()) {
            DirectoryNode parent;
            Property property = (Property) properties.next();
            String name = property.getName();
            if (dir == null) {
                parent = getRoot();
            } else {
                parent = dir;
            }
            if (property.isDirectory()) {
                DirectoryNode new_dir = (DirectoryNode) parent.createDirectory(name);
                new_dir.setStorageClsid(property.getStorageClsid());
                processProperties(small_blocks, big_blocks, ((DirectoryProperty) property).getChildren(), new_dir, headerPropertiesStartAt);
            } else {
                OPOIFSDocument document;
                int startBlock = property.getStartBlock();
                int size = property.getSize();
                if (property.shouldUseSmallBlocks()) {
                    document = new OPOIFSDocument(name, small_blocks.fetchBlocks(startBlock, headerPropertiesStartAt), size);
                } else {
                    document = new OPOIFSDocument(name, big_blocks.fetchBlocks(startBlock, headerPropertiesStartAt), size);
                }
                parent.createDocument(document);
            }
        }
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
}
