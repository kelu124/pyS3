package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class GutsRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 128;
    private short field_1_left_row_gutter;
    private short field_2_top_col_gutter;
    private short field_3_row_level_max;
    private short field_4_col_level_max;

    public GutsRecord(RecordInputStream in) {
        this.field_1_left_row_gutter = in.readShort();
        this.field_2_top_col_gutter = in.readShort();
        this.field_3_row_level_max = in.readShort();
        this.field_4_col_level_max = in.readShort();
    }

    public void setLeftRowGutter(short gut) {
        this.field_1_left_row_gutter = gut;
    }

    public void setTopColGutter(short gut) {
        this.field_2_top_col_gutter = gut;
    }

    public void setRowLevelMax(short max) {
        this.field_3_row_level_max = max;
    }

    public void setColLevelMax(short max) {
        this.field_4_col_level_max = max;
    }

    public short getLeftRowGutter() {
        return this.field_1_left_row_gutter;
    }

    public short getTopColGutter() {
        return this.field_2_top_col_gutter;
    }

    public short getRowLevelMax() {
        return this.field_3_row_level_max;
    }

    public short getColLevelMax() {
        return this.field_4_col_level_max;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[GUTS]\n");
        buffer.append("    .leftgutter     = ").append(Integer.toHexString(getLeftRowGutter())).append("\n");
        buffer.append("    .topgutter      = ").append(Integer.toHexString(getTopColGutter())).append("\n");
        buffer.append("    .rowlevelmax    = ").append(Integer.toHexString(getRowLevelMax())).append("\n");
        buffer.append("    .collevelmax    = ").append(Integer.toHexString(getColLevelMax())).append("\n");
        buffer.append("[/GUTS]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getLeftRowGutter());
        out.writeShort(getTopColGutter());
        out.writeShort(getRowLevelMax());
        out.writeShort(getColLevelMax());
    }

    protected int getDataSize() {
        return 8;
    }

    public short getSid() {
        return (short) 128;
    }

    public GutsRecord clone() {
        GutsRecord rec = new GutsRecord();
        rec.field_1_left_row_gutter = this.field_1_left_row_gutter;
        rec.field_2_top_col_gutter = this.field_2_top_col_gutter;
        rec.field_3_row_level_max = this.field_3_row_level_max;
        rec.field_4_col_level_max = this.field_4_col_level_max;
        return rec;
    }
}
