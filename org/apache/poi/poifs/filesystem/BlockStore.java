package org.apache.poi.poifs.filesystem;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.poi.poifs.storage.BATBlock.BATBlockAndIndex;

public abstract class BlockStore {

    protected class ChainLoopDetector {
        private boolean[] used_blocks;

        protected ChainLoopDetector(long rawSize) {
            int blkSize = BlockStore.this.getBlockStoreBlockSize();
            int numBlocks = (int) (rawSize / ((long) blkSize));
            if (rawSize % ((long) blkSize) != 0) {
                numBlocks++;
            }
            this.used_blocks = new boolean[numBlocks];
        }

        protected void claim(int offset) {
            if (offset < this.used_blocks.length) {
                if (this.used_blocks[offset]) {
                    throw new IllegalStateException("Potential loop detected - Block " + offset + " was already claimed but was just requested again");
                }
                this.used_blocks[offset] = true;
            }
        }
    }

    protected abstract ByteBuffer createBlockIfNeeded(int i) throws IOException;

    protected abstract BATBlockAndIndex getBATBlockAndIndex(int i);

    protected abstract ByteBuffer getBlockAt(int i) throws IOException;

    protected abstract int getBlockStoreBlockSize();

    protected abstract ChainLoopDetector getChainLoopDetector() throws IOException;

    protected abstract int getFreeBlock() throws IOException;

    protected abstract int getNextBlock(int i);

    protected abstract void setNextBlock(int i, int i2);
}
