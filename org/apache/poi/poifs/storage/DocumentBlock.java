package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.util.IOUtils;

public final class DocumentBlock extends BigBlock {
    private static final byte _default_value = (byte) -1;
    private int _bytes_read;
    private byte[] _data;

    public /* bridge */ /* synthetic */ void writeBlocks(OutputStream outputStream) throws IOException {
        super.writeBlocks(outputStream);
    }

    public DocumentBlock(RawDataBlock block) throws IOException {
        super(block.getBigBlockSize() == 512 ? POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS : POIFSConstants.LARGER_BIG_BLOCK_SIZE_DETAILS);
        this._data = block.getData();
        this._bytes_read = this._data.length;
    }

    public DocumentBlock(InputStream stream, POIFSBigBlockSize bigBlockSize) throws IOException {
        this(bigBlockSize);
        int count = IOUtils.readFully(stream, this._data);
        if (count == -1) {
            count = 0;
        }
        this._bytes_read = count;
    }

    private DocumentBlock(POIFSBigBlockSize bigBlockSize) {
        super(bigBlockSize);
        this._data = new byte[bigBlockSize.getBigBlockSize()];
        Arrays.fill(this._data, (byte) -1);
    }

    public int size() {
        return this._bytes_read;
    }

    public boolean partiallyRead() {
        return this._bytes_read != this.bigBlockSize.getBigBlockSize();
    }

    public static byte getFillByte() {
        return (byte) -1;
    }

    public static DocumentBlock[] convert(POIFSBigBlockSize bigBlockSize, byte[] array, int size) {
        DocumentBlock[] rval = new DocumentBlock[(((bigBlockSize.getBigBlockSize() + size) - 1) / bigBlockSize.getBigBlockSize())];
        int offset = 0;
        for (int k = 0; k < rval.length; k++) {
            rval[k] = new DocumentBlock(bigBlockSize);
            if (offset < array.length) {
                int length = Math.min(bigBlockSize.getBigBlockSize(), array.length - offset);
                System.arraycopy(array, offset, rval[k]._data, 0, length);
                if (length != bigBlockSize.getBigBlockSize()) {
                    Arrays.fill(rval[k]._data, length, bigBlockSize.getBigBlockSize(), (byte) -1);
                }
            } else {
                Arrays.fill(rval[k]._data, (byte) -1);
            }
            offset += bigBlockSize.getBigBlockSize();
        }
        return rval;
    }

    public static DataInputBlock getDataInputBlock(DocumentBlock[] blocks, int offset) {
        if (blocks == null || blocks.length == 0) {
            return null;
        }
        POIFSBigBlockSize bigBlockSize = blocks[0].bigBlockSize;
        return new DataInputBlock(blocks[offset >> bigBlockSize.getHeaderValue()]._data, offset & (bigBlockSize.getBigBlockSize() - 1));
    }

    void writeData(OutputStream stream) throws IOException {
        doWriteData(stream, this._data);
    }
}
