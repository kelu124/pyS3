package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class BookBoolRecord extends StandardRecord {
    public static final short sid = (short) 218;
    private short field_1_save_link_values;

    public BookBoolRecord(RecordInputStream in) {
        this.field_1_save_link_values = in.readShort();
    }

    public void setSaveLinkValues(short flag) {
        this.field_1_save_link_values = flag;
    }

    public short getSaveLinkValues() {
        return this.field_1_save_link_values;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[BOOKBOOL]\n");
        buffer.append("    .savelinkvalues  = ").append(Integer.toHexString(getSaveLinkValues())).append("\n");
        buffer.append("[/BOOKBOOL]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_save_link_values);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
