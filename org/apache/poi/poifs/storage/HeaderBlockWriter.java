package org.apache.poi.poifs.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.apache.poi.poifs.common.POIFSBigBlockSize;

public class HeaderBlockWriter implements HeaderBlockConstants, BlockWritable {
    private final HeaderBlock _header_block;

    public HeaderBlockWriter(POIFSBigBlockSize bigBlockSize) {
        this._header_block = new HeaderBlock(bigBlockSize);
    }

    public HeaderBlockWriter(HeaderBlock headerBlock) {
        this._header_block = headerBlock;
    }

    public BATBlock[] setBATBlocks(int blockCount, int startBlock) {
        int j;
        BATBlock[] rvalue;
        POIFSBigBlockSize bigBlockSize = this._header_block.getBigBlockSize();
        this._header_block.setBATCount(blockCount);
        int limit = Math.min(blockCount, 109);
        int[] bat_blocks = new int[limit];
        for (j = 0; j < limit; j++) {
            bat_blocks[j] = startBlock + j;
        }
        this._header_block.setBATArray(bat_blocks);
        if (blockCount > 109) {
            int excess_blocks = blockCount - 109;
            int[] excess_block_array = new int[excess_blocks];
            for (j = 0; j < excess_blocks; j++) {
                excess_block_array[j] = (startBlock + j) + 109;
            }
            rvalue = BATBlock.createXBATBlocks(bigBlockSize, excess_block_array, startBlock + blockCount);
            this._header_block.setXBATStart(startBlock + blockCount);
        } else {
            rvalue = BATBlock.createXBATBlocks(bigBlockSize, new int[0], 0);
            this._header_block.setXBATStart(-2);
        }
        this._header_block.setXBATCount(rvalue.length);
        return rvalue;
    }

    public void setPropertyStart(int startBlock) {
        this._header_block.setPropertyStart(startBlock);
    }

    public void setSBATStart(int startBlock) {
        this._header_block.setSBATStart(startBlock);
    }

    public void setSBATBlockCount(int count) {
        this._header_block.setSBATBlockCount(count);
    }

    static int calculateXBATStorageRequirements(POIFSBigBlockSize bigBlockSize, int blockCount) {
        return blockCount > 109 ? BATBlock.calculateXBATStorageRequirements(bigBlockSize, blockCount - 109) : 0;
    }

    public void writeBlocks(OutputStream stream) throws IOException {
        this._header_block.writeData(stream);
    }

    public void writeBlock(ByteBuffer block) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(this._header_block.getBigBlockSize().getBigBlockSize());
        this._header_block.writeData(baos);
        block.put(baos.toByteArray());
    }
}
