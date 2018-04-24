package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class HideObjRecord extends StandardRecord {
    public static final short HIDE_ALL = (short) 2;
    public static final short SHOW_ALL = (short) 0;
    public static final short SHOW_PLACEHOLDERS = (short) 1;
    public static final short sid = (short) 141;
    private short field_1_hide_obj;

    public HideObjRecord(RecordInputStream in) {
        this.field_1_hide_obj = in.readShort();
    }

    public void setHideObj(short hide) {
        this.field_1_hide_obj = hide;
    }

    public short getHideObj() {
        return this.field_1_hide_obj;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[HIDEOBJ]\n");
        buffer.append("    .hideobj         = ").append(Integer.toHexString(getHideObj())).append("\n");
        buffer.append("[/HIDEOBJ]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getHideObj());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 141;
    }
}
