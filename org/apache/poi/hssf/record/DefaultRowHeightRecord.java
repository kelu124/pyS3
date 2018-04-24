package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class DefaultRowHeightRecord extends StandardRecord implements Cloneable {
    public static final short DEFAULT_ROW_HEIGHT = (short) 255;
    public static final short sid = (short) 549;
    private short field_1_option_flags;
    private short field_2_row_height;

    public DefaultRowHeightRecord() {
        this.field_1_option_flags = (short) 0;
        this.field_2_row_height = (short) 255;
    }

    public DefaultRowHeightRecord(RecordInputStream in) {
        this.field_1_option_flags = in.readShort();
        this.field_2_row_height = in.readShort();
    }

    public void setOptionFlags(short flags) {
        this.field_1_option_flags = flags;
    }

    public void setRowHeight(short height) {
        this.field_2_row_height = height;
    }

    public short getOptionFlags() {
        return this.field_1_option_flags;
    }

    public short getRowHeight() {
        return this.field_2_row_height;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DEFAULTROWHEIGHT]\n");
        buffer.append("    .optionflags    = ").append(Integer.toHexString(getOptionFlags())).append("\n");
        buffer.append("    .rowheight      = ").append(Integer.toHexString(getRowHeight())).append("\n");
        buffer.append("[/DEFAULTROWHEIGHT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getOptionFlags());
        out.writeShort(getRowHeight());
    }

    protected int getDataSize() {
        return 4;
    }

    public short getSid() {
        return sid;
    }

    public DefaultRowHeightRecord clone() {
        DefaultRowHeightRecord rec = new DefaultRowHeightRecord();
        rec.field_1_option_flags = this.field_1_option_flags;
        rec.field_2_row_height = this.field_2_row_height;
        return rec;
    }
}
