package org.apache.poi.poifs.filesystem;

import java.io.ByteArrayOutputStream;
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
import org.apache.poi.poifs.property.DocumentProperty;
import org.apache.poi.poifs.property.Property;
import org.apache.poi.poifs.storage.BlockWritable;
import org.apache.poi.poifs.storage.DataInputBlock;
import org.apache.poi.poifs.storage.DocumentBlock;
import org.apache.poi.poifs.storage.ListManagedBlock;
import org.apache.poi.poifs.storage.RawDataBlock;
import org.apache.poi.poifs.storage.SmallDocumentBlock;
import org.apache.poi.util.HexDump;

public final class OPOIFSDocument implements BATManaged, BlockWritable, POIFSViewable {
    private static final DocumentBlock[] EMPTY_BIG_BLOCK_ARRAY = new DocumentBlock[0];
    private static final SmallDocumentBlock[] EMPTY_SMALL_BLOCK_ARRAY = new SmallDocumentBlock[0];
    private final POIFSBigBlockSize _bigBigBlockSize;
    private BigBlockStore _big_store;
    private DocumentProperty _property;
    private int _size;
    private SmallBlockStore _small_store;

    private static final class BigBlockStore {
        private final POIFSBigBlockSize _bigBlockSize;
        private final String _name;
        private final POIFSDocumentPath _path;
        private final int _size;
        private final POIFSWriterListener _writer;
        private DocumentBlock[] bigBlocks;

        BigBlockStore(POIFSBigBlockSize bigBlockSize, DocumentBlock[] blocks) {
            this._bigBlockSize = bigBlockSize;
            this.bigBlocks = (DocumentBlock[]) blocks.clone();
            this._path = null;
            this._name = null;
            this._size = -1;
            this._writer = null;
        }

        BigBlockStore(POIFSBigBlockSize bigBlockSize, POIFSDocumentPath path, String name, int size, POIFSWriterListener writer) {
            this._bigBlockSize = bigBlockSize;
            this.bigBlocks = new DocumentBlock[0];
            this._path = path;
            this._name = name;
            this._size = size;
            this._writer = writer;
        }

        boolean isValid() {
            return this.bigBlocks.length > 0 || this._writer != null;
        }

        DocumentBlock[] getBlocks() {
            if (isValid() && this._writer != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream(this._size);
                this._writer.processPOIFSWriterEvent(new POIFSWriterEvent(new DocumentOutputStream(stream, this._size), this._path, this._name, this._size));
                this.bigBlocks = DocumentBlock.convert(this._bigBlockSize, stream.toByteArray(), this._size);
            }
            return this.bigBlocks;
        }

        void writeBlocks(OutputStream stream) throws IOException {
            if (!isValid()) {
                return;
            }
            if (this._writer != null) {
                DocumentOutputStream dstream = new DocumentOutputStream(stream, this._size);
                this._writer.processPOIFSWriterEvent(new POIFSWriterEvent(dstream, this._path, this._name, this._size));
                dstream.writeFiller(countBlocks() * this._bigBlockSize.getBigBlockSize(), DocumentBlock.getFillByte());
                return;
            }
            for (DocumentBlock writeBlocks : this.bigBlocks) {
                writeBlocks.writeBlocks(stream);
            }
        }

        int countBlocks() {
            if (!isValid()) {
                return 0;
            }
            if (this._writer == null) {
                return this.bigBlocks.length;
            }
            return ((this._size + this._bigBlockSize.getBigBlockSize()) - 1) / this._bigBlockSize.getBigBlockSize();
        }
    }

    private static final class SmallBlockStore {
        private final POIFSBigBlockSize _bigBlockSize;
        private final String _name;
        private final POIFSDocumentPath _path;
        private final int _size;
        private SmallDocumentBlock[] _smallBlocks;
        private final POIFSWriterListener _writer;

        SmallBlockStore(POIFSBigBlockSize bigBlockSize, SmallDocumentBlock[] blocks) {
            this._bigBlockSize = bigBlockSize;
            this._smallBlocks = (SmallDocumentBlock[]) blocks.clone();
            this._path = null;
            this._name = null;
            this._size = -1;
            this._writer = null;
        }

        SmallBlockStore(POIFSBigBlockSize bigBlockSize, POIFSDocumentPath path, String name, int size, POIFSWriterListener writer) {
            this._bigBlockSize = bigBlockSize;
            this._smallBlocks = new SmallDocumentBlock[0];
            this._path = path;
            this._name = name;
            this._size = size;
            this._writer = writer;
        }

        boolean isValid() {
            return this._smallBlocks.length > 0 || this._writer != null;
        }

        SmallDocumentBlock[] getBlocks() {
            if (isValid() && this._writer != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream(this._size);
                this._writer.processPOIFSWriterEvent(new POIFSWriterEvent(new DocumentOutputStream(stream, this._size), this._path, this._name, this._size));
                this._smallBlocks = SmallDocumentBlock.convert(this._bigBlockSize, stream.toByteArray(), this._size);
            }
            return this._smallBlocks;
        }
    }

    public OPOIFSDocument(String name, RawDataBlock[] blocks, int length) throws IOException {
        this._size = length;
        if (blocks.length == 0) {
            this._bigBigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
        } else {
            this._bigBigBlockSize = blocks[0].getBigBlockSize() == 512 ? POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS : POIFSConstants.LARGER_BIG_BLOCK_SIZE_DETAILS;
        }
        this._big_store = new BigBlockStore(this._bigBigBlockSize, convertRawBlocksToBigBlocks(blocks));
        this._property = new DocumentProperty(name, this._size);
        this._small_store = new SmallBlockStore(this._bigBigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
        this._property.setDocument(this);
    }

    private static DocumentBlock[] convertRawBlocksToBigBlocks(ListManagedBlock[] blocks) throws IOException {
        DocumentBlock[] result = new DocumentBlock[blocks.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new DocumentBlock((RawDataBlock) blocks[i]);
        }
        return result;
    }

    private static SmallDocumentBlock[] convertRawBlocksToSmallBlocks(ListManagedBlock[] blocks) {
        if (blocks instanceof SmallDocumentBlock[]) {
            return (SmallDocumentBlock[]) blocks;
        }
        SmallDocumentBlock[] result = new SmallDocumentBlock[blocks.length];
        System.arraycopy(blocks, 0, result, 0, blocks.length);
        return result;
    }

    public OPOIFSDocument(String name, SmallDocumentBlock[] blocks, int length) {
        this._size = length;
        if (blocks.length == 0) {
            this._bigBigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
        } else {
            this._bigBigBlockSize = blocks[0].getBigBlockSize();
        }
        this._big_store = new BigBlockStore(this._bigBigBlockSize, EMPTY_BIG_BLOCK_ARRAY);
        this._property = new DocumentProperty(name, this._size);
        this._small_store = new SmallBlockStore(this._bigBigBlockSize, blocks);
        this._property.setDocument(this);
    }

    public OPOIFSDocument(String name, POIFSBigBlockSize bigBlockSize, ListManagedBlock[] blocks, int length) throws IOException {
        this._size = length;
        this._bigBigBlockSize = bigBlockSize;
        this._property = new DocumentProperty(name, this._size);
        this._property.setDocument(this);
        if (Property.isSmall(this._size)) {
            this._big_store = new BigBlockStore(bigBlockSize, EMPTY_BIG_BLOCK_ARRAY);
            this._small_store = new SmallBlockStore(bigBlockSize, convertRawBlocksToSmallBlocks(blocks));
            return;
        }
        this._big_store = new BigBlockStore(bigBlockSize, convertRawBlocksToBigBlocks(blocks));
        this._small_store = new SmallBlockStore(bigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
    }

    public OPOIFSDocument(String name, ListManagedBlock[] blocks, int length) throws IOException {
        this(name, POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, blocks, length);
    }

    public OPOIFSDocument(String name, POIFSBigBlockSize bigBlockSize, InputStream stream) throws IOException {
        List<DocumentBlock> blocks = new ArrayList();
        this._size = 0;
        this._bigBigBlockSize = bigBlockSize;
        DocumentBlock block;
        do {
            block = new DocumentBlock(stream, bigBlockSize);
            int blockSize = block.size();
            if (blockSize > 0) {
                blocks.add(block);
                this._size += blockSize;
            }
        } while (!block.partiallyRead());
        BlockWritable[] bigBlocks = (DocumentBlock[]) blocks.toArray(new DocumentBlock[blocks.size()]);
        this._big_store = new BigBlockStore(bigBlockSize, bigBlocks);
        this._property = new DocumentProperty(name, this._size);
        this._property.setDocument(this);
        if (this._property.shouldUseSmallBlocks()) {
            this._small_store = new SmallBlockStore(bigBlockSize, SmallDocumentBlock.convert(bigBlockSize, bigBlocks, this._size));
            this._big_store = new BigBlockStore(bigBlockSize, new DocumentBlock[0]);
            return;
        }
        this._small_store = new SmallBlockStore(bigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
    }

    public OPOIFSDocument(String name, InputStream stream) throws IOException {
        this(name, POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, stream);
    }

    public OPOIFSDocument(String name, int size, POIFSBigBlockSize bigBlockSize, POIFSDocumentPath path, POIFSWriterListener writer) {
        this._size = size;
        this._bigBigBlockSize = bigBlockSize;
        this._property = new DocumentProperty(name, this._size);
        this._property.setDocument(this);
        if (this._property.shouldUseSmallBlocks()) {
            this._small_store = new SmallBlockStore(this._bigBigBlockSize, path, name, size, writer);
            this._big_store = new BigBlockStore(this._bigBigBlockSize, EMPTY_BIG_BLOCK_ARRAY);
            return;
        }
        this._small_store = new SmallBlockStore(this._bigBigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
        this._big_store = new BigBlockStore(this._bigBigBlockSize, path, name, size, writer);
    }

    public OPOIFSDocument(String name, int size, POIFSDocumentPath path, POIFSWriterListener writer) {
        this(name, size, POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, path, writer);
    }

    public SmallDocumentBlock[] getSmallBlocks() {
        return this._small_store.getBlocks();
    }

    public int getSize() {
        return this._size;
    }

    void read(byte[] buffer, int offset) {
        int len = buffer.length;
        DataInputBlock currentBlock = getDataInputBlock(offset);
        int blockAvailable = currentBlock.available();
        if (blockAvailable > len) {
            currentBlock.readFully(buffer, 0, len);
            return;
        }
        int remaining = len;
        int writePos = 0;
        int currentOffset = offset;
        while (remaining > 0) {
            boolean blockIsExpiring;
            int reqSize;
            if (remaining >= blockAvailable) {
                blockIsExpiring = true;
            } else {
                blockIsExpiring = false;
            }
            if (blockIsExpiring) {
                reqSize = blockAvailable;
            } else {
                reqSize = remaining;
            }
            currentBlock.readFully(buffer, writePos, reqSize);
            remaining -= reqSize;
            writePos += reqSize;
            currentOffset += reqSize;
            if (blockIsExpiring) {
                if (currentOffset != this._size) {
                    currentBlock = getDataInputBlock(currentOffset);
                    blockAvailable = currentBlock.available();
                } else if (remaining > 0) {
                    throw new IllegalStateException("reached end of document stream unexpectedly");
                } else {
                    return;
                }
            }
        }
    }

    DataInputBlock getDataInputBlock(int offset) {
        if (offset >= this._size) {
            if (offset <= this._size) {
                return null;
            }
            throw new RuntimeException("Request for Offset " + offset + " doc size is " + this._size);
        } else if (this._property.shouldUseSmallBlocks()) {
            return SmallDocumentBlock.getDataInputBlock(this._small_store.getBlocks(), offset);
        } else {
            return DocumentBlock.getDataInputBlock(this._big_store.getBlocks(), offset);
        }
    }

    DocumentProperty getDocumentProperty() {
        return this._property;
    }

    public void writeBlocks(OutputStream stream) throws IOException {
        this._big_store.writeBlocks(stream);
    }

    public int countBlocks() {
        return this._big_store.countBlocks();
    }

    public void setStartBlock(int index) {
        this._property.setStartBlock(index);
    }

    public Object[] getViewableArray() {
        String result = "<NO DATA>";
        BlockWritable[] blocks = null;
        try {
            if (this._big_store.isValid()) {
                blocks = this._big_store.getBlocks();
            } else if (this._small_store.isValid()) {
                blocks = this._small_store.getBlocks();
            }
            if (blocks != null) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                for (BlockWritable bw : blocks) {
                    bw.writeBlocks(output);
                }
                result = HexDump.dump(output.toByteArray(), 0, 0, Math.min(output.size(), this._property.getSize()));
            }
        } catch (IOException e) {
            result = e.getMessage();
        }
        return new String[]{result};
    }

    public Iterator<Object> getViewableIterator() {
        return Collections.emptyList().iterator();
    }

    public boolean preferArray() {
        return true;
    }

    public String getShortDescription() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Document: \"").append(this._property.getName()).append("\"");
        buffer.append(" size = ").append(getSize());
        return buffer.toString();
    }
}
