package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class InterfaceEndRecord extends StandardRecord {
    public static final InterfaceEndRecord instance = new InterfaceEndRecord();
    public static final short sid = (short) 226;

    private InterfaceEndRecord() {
    }

    public static Record create(RecordInputStream in) {
        switch (in.remaining()) {
            case 0:
                return instance;
            case 2:
                return new InterfaceHdrRecord(in);
            default:
                throw new RecordFormatException("Invalid record data size: " + in.remaining());
        }
    }

    public String toString() {
        return "[INTERFACEEND/]\n";
    }

    public void serialize(LittleEndianOutput out) {
    }

    protected int getDataSize() {
        return 0;
    }

    public short getSid() {
        return sid;
    }
}
