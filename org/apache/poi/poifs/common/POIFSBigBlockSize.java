package org.apache.poi.poifs.common;

public final class POIFSBigBlockSize {
    private int bigBlockSize;
    private short headerValue;

    protected POIFSBigBlockSize(int bigBlockSize, short headerValue) {
        this.bigBlockSize = bigBlockSize;
        this.headerValue = headerValue;
    }

    public int getBigBlockSize() {
        return this.bigBlockSize;
    }

    public short getHeaderValue() {
        return this.headerValue;
    }

    public int getPropertiesPerBlock() {
        return this.bigBlockSize / 128;
    }

    public int getBATEntriesPerBlock() {
        return this.bigBlockSize / 4;
    }

    public int getXBATEntriesPerBlock() {
        return getBATEntriesPerBlock() - 1;
    }

    public int getNextXBATChainOffset() {
        return getXBATEntriesPerBlock() * 4;
    }
}
