package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class WriteProtectRecord extends StandardRecord {
    public static final short sid = (short) 134;

    public WriteProtectRecord(RecordInputStream in) {
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[WRITEPROTECT]\n");
        buffer.append("[/WRITEPROTECT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
    }

    protected int getDataSize() {
        return 0;
    }

    public short getSid() {
        return (short) 134;
    }
}
