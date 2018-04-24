package org.apache.poi.poifs.property;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.NPOIFSStream;
import org.apache.poi.poifs.storage.HeaderBlock;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public final class NPropertyTable extends PropertyTableBase {
    private static final POILogger _logger = POILogFactory.getLogger(NPropertyTable.class);
    private POIFSBigBlockSize _bigBigBlockSize;

    public NPropertyTable(HeaderBlock headerBlock) {
        super(headerBlock);
        this._bigBigBlockSize = headerBlock.getBigBlockSize();
    }

    public NPropertyTable(HeaderBlock headerBlock, NPOIFSFileSystem filesystem) throws IOException {
        super(headerBlock, buildProperties(new NPOIFSStream(filesystem, headerBlock.getPropertyStart()).iterator(), headerBlock.getBigBlockSize()));
        this._bigBigBlockSize = headerBlock.getBigBlockSize();
    }

    private static List<Property> buildProperties(Iterator<ByteBuffer> dataSource, POIFSBigBlockSize bigBlockSize) throws IOException {
        List<Property> properties = new ArrayList();
        while (dataSource.hasNext()) {
            byte[] data;
            ByteBuffer bb = (ByteBuffer) dataSource.next();
            if (bb.hasArray() && bb.arrayOffset() == 0 && bb.array().length == bigBlockSize.getBigBlockSize()) {
                data = bb.array();
            } else {
                data = new byte[bigBlockSize.getBigBlockSize()];
                int toRead = data.length;
                if (bb.remaining() < bigBlockSize.getBigBlockSize()) {
                    _logger.log(5, new Object[]{"Short Property Block, ", Integer.valueOf(bb.remaining()), " bytes instead of the expected " + bigBlockSize.getBigBlockSize()});
                    toRead = bb.remaining();
                }
                bb.get(data, 0, toRead);
            }
            PropertyFactory.convertToProperties(data, properties);
        }
        return properties;
    }

    public int countBlocks() {
        long rawSize = (long) (this._properties.size() * 128);
        int blkSize = this._bigBigBlockSize.getBigBlockSize();
        int numBlocks = (int) (rawSize / ((long) blkSize));
        if (rawSize % ((long) blkSize) != 0) {
            return numBlocks + 1;
        }
        return numBlocks;
    }

    public void preWrite() {
        List<Property> pList = new ArrayList();
        int i = 0;
        for (Property p : this._properties) {
            if (p != null) {
                int i2 = i + 1;
                p.setIndex(i);
                pList.add(p);
                i = i2;
            }
        }
        for (Property p2 : pList) {
            p2.preWrite();
        }
    }

    public void write(NPOIFSStream stream) throws IOException {
        OutputStream os = stream.getOutputStream();
        for (Property property : this._properties) {
            if (property != null) {
                property.writeData(os);
            }
        }
        os.close();
        if (getStartBlock() != stream.getStartBlock()) {
            setStartBlock(stream.getStartBlock());
        }
    }
}
