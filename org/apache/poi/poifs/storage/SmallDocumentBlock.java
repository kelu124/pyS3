package org.apache.poi.poifs.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.poifs.common.POIFSBigBlockSize;

public final class SmallDocumentBlock implements BlockWritable, ListManagedBlock {
    private static final int BLOCK_MASK = 63;
    private static final int BLOCK_SHIFT = 6;
    private static final int _block_size = 64;
    private static final byte _default_fill = (byte) -1;
    private final POIFSBigBlockSize _bigBlockSize;
    private final int _blocks_per_big_block;
    private byte[] _data;

    private SmallDocumentBlock(POIFSBigBlockSize bigBlockSize, byte[] data, int index) {
        this(bigBlockSize);
        System.arraycopy(data, index * 64, this._data, 0, 64);
    }

    protected SmallDocumentBlock(POIFSBigBlockSize bigBlockSize) {
        this._bigBlockSize = bigBlockSize;
        this._blocks_per_big_block = getBlocksPerBigBlock(bigBlockSize);
        this._data = new byte[64];
    }

    private static int getBlocksPerBigBlock(POIFSBigBlockSize bigBlockSize) {
        return bigBlockSize.getBigBlockSize() / 64;
    }

    public static SmallDocumentBlock[] convert(POIFSBigBlockSize bigBlockSize, byte[] array, int size) {
        SmallDocumentBlock[] rval = new SmallDocumentBlock[(((size + 64) - 1) / 64)];
        int offset = 0;
        for (int k = 0; k < rval.length; k++) {
            rval[k] = new SmallDocumentBlock(bigBlockSize);
            if (offset < array.length) {
                int length = Math.min(64, array.length - offset);
                System.arraycopy(array, offset, rval[k]._data, 0, length);
                if (length != 64) {
                    Arrays.fill(rval[k]._data, length, 64, (byte) -1);
                }
            } else {
                Arrays.fill(rval[k]._data, (byte) -1);
            }
            offset += 64;
        }
        return rval;
    }

    public static int fill(POIFSBigBlockSize bigBlockSize, List<SmallDocumentBlock> blocks) {
        int _blocks_per_big_block = getBlocksPerBigBlock(bigBlockSize);
        int count = blocks.size();
        int big_block_count = ((count + _blocks_per_big_block) - 1) / _blocks_per_big_block;
        int full_count = big_block_count * _blocks_per_big_block;
        while (count < full_count) {
            blocks.add(makeEmptySmallDocumentBlock(bigBlockSize));
            count++;
        }
        return big_block_count;
    }

    public static SmallDocumentBlock[] convert(POIFSBigBlockSize bigBlockSize, BlockWritable[] store, int size) throws IOException, ArrayIndexOutOfBoundsException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for (BlockWritable writeBlocks : store) {
            writeBlocks.writeBlocks(stream);
        }
        byte[] data = stream.toByteArray();
        SmallDocumentBlock[] rval = new SmallDocumentBlock[convertToBlockCount(size)];
        for (int index = 0; index < rval.length; index++) {
            rval[index] = new SmallDocumentBlock(bigBlockSize, data, index);
        }
        return rval;
    }

    public static List<SmallDocumentBlock> extract(POIFSBigBlockSize bigBlockSize, ListManagedBlock[] blocks) throws IOException {
        int _blocks_per_big_block = getBlocksPerBigBlock(bigBlockSize);
        List<SmallDocumentBlock> sdbs = new ArrayList();
        for (ListManagedBlock data : blocks) {
            byte[] data2 = data.getData();
            for (int k = 0; k < _blocks_per_big_block; k++) {
                sdbs.add(new SmallDocumentBlock(bigBlockSize, data2, k));
            }
        }
        return sdbs;
    }

    public static DataInputBlock getDataInputBlock(SmallDocumentBlock[] blocks, int offset) {
        return new DataInputBlock(blocks[offset >> 6]._data, offset & 63);
    }

    public static int calcSize(int size) {
        return size * 64;
    }

    protected int getSmallBlocksPerBigBlock() {
        return this._blocks_per_big_block;
    }

    private static SmallDocumentBlock makeEmptySmallDocumentBlock(POIFSBigBlockSize bigBlockSize) {
        SmallDocumentBlock block = new SmallDocumentBlock(bigBlockSize);
        Arrays.fill(block._data, (byte) -1);
        return block;
    }

    private static int convertToBlockCount(int size) {
        return ((size + 64) - 1) / 64;
    }

    public void writeBlocks(OutputStream stream) throws IOException {
        stream.write(this._data);
    }

    public byte[] getData() {
        return this._data;
    }

    public POIFSBigBlockSize getBigBlockSize() {
        return this._bigBlockSize;
    }
}
