package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.filesystem.BATManaged;
import org.apache.poi.poifs.filesystem.OPOIFSDocument;
import org.apache.poi.poifs.property.RootProperty;

public class SmallBlockTableWriter implements BlockWritable, BATManaged {
    private int _big_block_count;
    private RootProperty _root;
    private BlockAllocationTableWriter _sbat;
    private List<SmallDocumentBlock> _small_blocks = new ArrayList();

    public SmallBlockTableWriter(POIFSBigBlockSize bigBlockSize, List<OPOIFSDocument> documents, RootProperty root) {
        this._sbat = new BlockAllocationTableWriter(bigBlockSize);
        this._root = root;
        for (OPOIFSDocument doc : documents) {
            SmallDocumentBlock[] blocks = doc.getSmallBlocks();
            if (blocks.length != 0) {
                doc.setStartBlock(this._sbat.allocateSpace(blocks.length));
                for (Object add : blocks) {
                    this._small_blocks.add(add);
                }
            } else {
                doc.setStartBlock(-2);
            }
        }
        this._sbat.simpleCreateBlocks();
        this._root.setSize(this._small_blocks.size());
        this._big_block_count = SmallDocumentBlock.fill(bigBlockSize, this._small_blocks);
    }

    public int getSBATBlockCount() {
        return (this._big_block_count + 15) / 16;
    }

    public BlockAllocationTableWriter getSBAT() {
        return this._sbat;
    }

    public int countBlocks() {
        return this._big_block_count;
    }

    public void setStartBlock(int start_block) {
        this._root.setStartBlock(start_block);
    }

    public void writeBlocks(OutputStream stream) throws IOException {
        for (BlockWritable block : this._small_blocks) {
            block.writeBlocks(stream);
        }
    }
}
