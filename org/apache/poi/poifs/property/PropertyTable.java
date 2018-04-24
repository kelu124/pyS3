package org.apache.poi.poifs.property;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.storage.BlockWritable;
import org.apache.poi.poifs.storage.HeaderBlock;
import org.apache.poi.poifs.storage.PropertyBlock;
import org.apache.poi.poifs.storage.RawDataBlockList;

public final class PropertyTable extends PropertyTableBase implements BlockWritable {
    private POIFSBigBlockSize _bigBigBlockSize;
    private BlockWritable[] _blocks = null;

    public PropertyTable(HeaderBlock headerBlock) {
        super(headerBlock);
        this._bigBigBlockSize = headerBlock.getBigBlockSize();
    }

    public PropertyTable(HeaderBlock headerBlock, RawDataBlockList blockList) throws IOException {
        super(headerBlock, PropertyFactory.convertToProperties(blockList.fetchBlocks(headerBlock.getPropertyStart(), -1)));
        this._bigBigBlockSize = headerBlock.getBigBlockSize();
    }

    public void preWrite() {
        Property[] properties = (Property[]) this._properties.toArray(new Property[this._properties.size()]);
        for (int k = 0; k < properties.length; k++) {
            properties[k].setIndex(k);
        }
        this._blocks = PropertyBlock.createPropertyBlockArray(this._bigBigBlockSize, this._properties);
        for (Property preWrite : properties) {
            preWrite.preWrite();
        }
    }

    public int countBlocks() {
        return this._blocks == null ? 0 : this._blocks.length;
    }

    public void writeBlocks(OutputStream stream) throws IOException {
        if (this._blocks != null) {
            for (BlockWritable writeBlocks : this._blocks) {
                writeBlocks.writeBlocks(stream);
            }
        }
    }
}
