package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class DefaultColWidthRecord extends StandardRecord implements Cloneable {
    public static final int DEFAULT_COLUMN_WIDTH = 8;
    public static final short sid = (short) 85;
    private int field_1_col_width;

    public DefaultColWidthRecord() {
        this.field_1_col_width = 8;
    }

    public DefaultColWidthRecord(RecordInputStream in) {
        this.field_1_col_width = in.readUShort();
    }

    public void setColWidth(int width) {
        this.field_1_col_width = width;
    }

    public int getColWidth() {
        return this.field_1_col_width;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DEFAULTCOLWIDTH]\n");
        buffer.append("    .colwidth      = ").append(Integer.toHexString(getColWidth())).append("\n");
        buffer.append("[/DEFAULTCOLWIDTH]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getColWidth());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 85;
    }

    public DefaultColWidthRecord clone() {
        DefaultColWidthRecord rec = new DefaultColWidthRecord();
        rec.field_1_col_width = this.field_1_col_width;
        return rec;
    }
}
