package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class DimensionsRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 512;
    private int field_1_first_row;
    private int field_2_last_row;
    private short field_3_first_col;
    private short field_4_last_col;
    private short field_5_zero;

    public DimensionsRecord(RecordInputStream in) {
        this.field_1_first_row = in.readInt();
        this.field_2_last_row = in.readInt();
        this.field_3_first_col = in.readShort();
        this.field_4_last_col = in.readShort();
        this.field_5_zero = in.readShort();
    }

    public void setFirstRow(int row) {
        this.field_1_first_row = row;
    }

    public void setLastRow(int row) {
        this.field_2_last_row = row;
    }

    public void setFirstCol(short col) {
        this.field_3_first_col = col;
    }

    public void setLastCol(short col) {
        this.field_4_last_col = col;
    }

    public int getFirstRow() {
        return this.field_1_first_row;
    }

    public int getLastRow() {
        return this.field_2_last_row;
    }

    public short getFirstCol() {
        return this.field_3_first_col;
    }

    public short getLastCol() {
        return this.field_4_last_col;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DIMENSIONS]\n");
        buffer.append("    .firstrow       = ").append(Integer.toHexString(getFirstRow())).append("\n");
        buffer.append("    .lastrow        = ").append(Integer.toHexString(getLastRow())).append("\n");
        buffer.append("    .firstcol       = ").append(Integer.toHexString(getFirstCol())).append("\n");
        buffer.append("    .lastcol        = ").append(Integer.toHexString(getLastCol())).append("\n");
        buffer.append("    .zero           = ").append(Integer.toHexString(this.field_5_zero)).append("\n");
        buffer.append("[/DIMENSIONS]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(getFirstRow());
        out.writeInt(getLastRow());
        out.writeShort(getFirstCol());
        out.writeShort(getLastCol());
        out.writeShort(0);
    }

    protected int getDataSize() {
        return 14;
    }

    public short getSid() {
        return (short) 512;
    }

    public DimensionsRecord clone() {
        DimensionsRecord rec = new DimensionsRecord();
        rec.field_1_first_row = this.field_1_first_row;
        rec.field_2_last_row = this.field_2_last_row;
        rec.field_3_first_col = this.field_3_first_col;
        rec.field_4_last_col = this.field_4_last_col;
        rec.field_5_zero = this.field_5_zero;
        return rec;
    }
}
