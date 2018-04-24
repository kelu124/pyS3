package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class UncalcedRecord extends StandardRecord {
    public static final short sid = (short) 94;
    private short _reserved;

    public UncalcedRecord() {
        this._reserved = (short) 0;
    }

    public short getSid() {
        return (short) 94;
    }

    public UncalcedRecord(RecordInputStream in) {
        this._reserved = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[UNCALCED]\n");
        buffer.append("    _reserved: ").append(this._reserved).append('\n');
        buffer.append("[/UNCALCED]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._reserved);
    }

    protected int getDataSize() {
        return 2;
    }

    public static int getStaticRecordSize() {
        return 6;
    }
}
