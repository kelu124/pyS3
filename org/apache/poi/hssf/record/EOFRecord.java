package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class EOFRecord extends StandardRecord implements Cloneable {
    public static final int ENCODED_SIZE = 4;
    public static final EOFRecord instance = new EOFRecord();
    public static final short sid = (short) 10;

    private EOFRecord() {
    }

    public EOFRecord(RecordInputStream in) {
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[EOF]\n");
        buffer.append("[/EOF]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
    }

    protected int getDataSize() {
        return 0;
    }

    public short getSid() {
        return (short) 10;
    }

    public EOFRecord clone() {
        return instance;
    }
}
