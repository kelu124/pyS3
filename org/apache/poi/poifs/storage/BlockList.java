package org.apache.poi.poifs.storage;

import java.io.IOException;

public interface BlockList {
    int blockCount();

    ListManagedBlock[] fetchBlocks(int i, int i2) throws IOException;

    ListManagedBlock remove(int i) throws IOException;

    void setBAT(BlockAllocationTableReader blockAllocationTableReader) throws IOException;

    void zap(int i);
}
