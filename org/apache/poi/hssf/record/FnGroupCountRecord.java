package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class FnGroupCountRecord extends StandardRecord {
    public static final short COUNT = (short) 14;
    public static final short sid = (short) 156;
    private short field_1_count;

    public FnGroupCountRecord(RecordInputStream in) {
        this.field_1_count = in.readShort();
    }

    public void setCount(short count) {
        this.field_1_count = count;
    }

    public short getCount() {
        return this.field_1_count;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FNGROUPCOUNT]\n");
        buffer.append("    .count            = ").append(getCount()).append("\n");
        buffer.append("[/FNGROUPCOUNT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getCount());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 156;
    }
}
