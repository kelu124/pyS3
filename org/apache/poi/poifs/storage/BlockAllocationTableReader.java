package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.util.IntList;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public final class BlockAllocationTableReader {
    private static final int MAX_BLOCK_COUNT = 65535;
    private static final POILogger _logger = POILogFactory.getLogger(BlockAllocationTableReader.class);
    private final IntList _entries;
    private POIFSBigBlockSize bigBlockSize;

    public BlockAllocationTableReader(POIFSBigBlockSize bigBlockSize, int block_count, int[] block_array, int xbat_count, int xbat_index, BlockList raw_block_list) throws IOException {
        this(bigBlockSize);
        sanityCheckBlockCount(block_count);
        int limit = Math.min(block_count, block_array.length);
        RawDataBlock[] blocks = new RawDataBlock[block_count];
        int i = 0;
        while (i < limit) {
            int nextOffset = block_array[i];
            if (nextOffset > raw_block_list.blockCount()) {
                throw new IOException("Your file contains " + raw_block_list.blockCount() + " sectors, but the initial DIFAT array at index " + i + " referenced block # " + nextOffset + ". This isn't allowed and " + " your file is corrupt");
            }
            blocks[i] = (RawDataBlock) raw_block_list.remove(nextOffset);
            i++;
        }
        if (i < block_count) {
            if (xbat_index < 0) {
                throw new IOException("BAT count exceeds limit, yet XBAT index indicates no valid entries");
            }
            int chain_index = xbat_index;
            int max_entries_per_block = bigBlockSize.getXBATEntriesPerBlock();
            int chain_index_offset = bigBlockSize.getNextXBATChainOffset();
            int j = 0;
            while (j < xbat_count) {
                limit = Math.min(block_count - i, max_entries_per_block);
                byte[] data = raw_block_list.remove(chain_index).getData();
                int offset = 0;
                int k = 0;
                int block_index = i;
                while (k < limit) {
                    i = block_index + 1;
                    blocks[block_index] = (RawDataBlock) raw_block_list.remove(LittleEndian.getInt(data, offset));
                    offset += 4;
                    k++;
                    block_index = i;
                }
                chain_index = LittleEndian.getInt(data, chain_index_offset);
                if (chain_index == -2) {
                    i = block_index;
                    break;
                } else {
                    j++;
                    i = block_index;
                }
            }
        }
        if (i != block_count) {
            throw new IOException("Could not find all blocks");
        }
        setEntries(blocks, raw_block_list);
    }

    BlockAllocationTableReader(POIFSBigBlockSize bigBlockSize, ListManagedBlock[] blocks, BlockList raw_block_list) throws IOException {
        this(bigBlockSize);
        setEntries(blocks, raw_block_list);
    }

    BlockAllocationTableReader(POIFSBigBlockSize bigBlockSize) {
        this.bigBlockSize = bigBlockSize;
        this._entries = new IntList();
    }

    public static void sanityCheckBlockCount(int block_count) throws IOException {
        if (block_count <= 0) {
            throw new IOException("Illegal block count; minimum count is 1, got " + block_count + " instead");
        } else if (block_count > 65535) {
            throw new IOException("Block count " + block_count + " is too high. POI maximum is " + 65535 + ".");
        }
    }

    ListManagedBlock[] fetchBlocks(int startBlock, int headerPropertiesStartBlock, BlockList blockList) throws IOException {
        List<ListManagedBlock> blocks = new ArrayList();
        int currentBlock = startBlock;
        boolean firstPass = true;
        while (currentBlock != -2) {
            try {
                blocks.add(blockList.remove(currentBlock));
                currentBlock = this._entries.get(currentBlock);
                firstPass = false;
            } catch (IOException e) {
                if (currentBlock == headerPropertiesStartBlock) {
                    _logger.log(5, "Warning, header block comes after data blocks in POIFS block listing");
                    currentBlock = -2;
                } else if (currentBlock == 0 && firstPass) {
                    _logger.log(5, "Warning, incorrectly terminated empty data blocks in POIFS block listing (should end at -2, ended at 0)");
                    currentBlock = -2;
                } else {
                    throw e;
                }
            }
        }
        return (ListManagedBlock[]) blocks.toArray(new ListManagedBlock[blocks.size()]);
    }

    boolean isUsed(int index) {
        try {
            return this._entries.get(index) != -1;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    int getNextBlockIndex(int index) throws IOException {
        if (isUsed(index)) {
            return this._entries.get(index);
        }
        throw new IOException("index " + index + " is unused");
    }

    private void setEntries(ListManagedBlock[] blocks, BlockList raw_blocks) throws IOException {
        int limit = this.bigBlockSize.getBATEntriesPerBlock();
        for (int block_index = 0; block_index < blocks.length; block_index++) {
            byte[] data = blocks[block_index].getData();
            int offset = 0;
            for (int k = 0; k < limit; k++) {
                int entry = LittleEndian.getInt(data, offset);
                if (entry == -1) {
                    raw_blocks.zap(this._entries.size());
                }
                this._entries.add(entry);
                offset += 4;
            }
            blocks[block_index] = null;
        }
        raw_blocks.setBAT(this);
    }

    @Internal
    public IntList getEntries() {
        return this._entries;
    }
}
