package org.apache.poi.hssf.model;

import org.apache.poi.hssf.record.RecordFormatException;

public class InternalSheet$UnsupportedBOFType extends RecordFormatException {
    private final int type;

    protected InternalSheet$UnsupportedBOFType(int type) {
        super("BOF not of a supported type, found " + type);
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
