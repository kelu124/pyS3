package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.util.List;
import org.apache.poi.util.Internal;

public class SmallDocumentBlockList extends BlockListImpl {
    public /* bridge */ /* synthetic */ int blockCount() {
        return super.blockCount();
    }

    public /* bridge */ /* synthetic */ ListManagedBlock[] fetchBlocks(int i, int i2) throws IOException {
        return super.fetchBlocks(i, i2);
    }

    @Internal
    public /* bridge */ /* synthetic */ ListManagedBlock get(int i) {
        return super.get(i);
    }

    public /* bridge */ /* synthetic */ ListManagedBlock remove(int i) throws IOException {
        return super.remove(i);
    }

    public /* bridge */ /* synthetic */ void setBAT(BlockAllocationTableReader blockAllocationTableReader) throws IOException {
        super.setBAT(blockAllocationTableReader);
    }

    public /* bridge */ /* synthetic */ void zap(int i) {
        super.zap(i);
    }

    public SmallDocumentBlockList(List<SmallDocumentBlock> blocks) {
        setBlocks((ListManagedBlock[]) blocks.toArray(new SmallDocumentBlock[blocks.size()]));
    }
}
