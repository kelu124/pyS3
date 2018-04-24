package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class RefModeRecord extends StandardRecord {
    public static final short USE_A1_MODE = (short) 1;
    public static final short USE_R1C1_MODE = (short) 0;
    public static final short sid = (short) 15;
    private short field_1_mode;

    public RefModeRecord(RecordInputStream in) {
        this.field_1_mode = in.readShort();
    }

    public void setMode(short mode) {
        this.field_1_mode = mode;
    }

    public short getMode() {
        return this.field_1_mode;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[REFMODE]\n");
        buffer.append("    .mode           = ").append(Integer.toHexString(getMode())).append("\n");
        buffer.append("[/REFMODE]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getMode());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 15;
    }

    public Object clone() {
        RefModeRecord rec = new RefModeRecord();
        rec.field_1_mode = this.field_1_mode;
        return rec;
    }
}
