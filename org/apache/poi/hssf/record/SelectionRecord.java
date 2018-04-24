package org.apache.poi.hssf.record;

import org.apache.poi.hssf.util.CellRangeAddress8Bit;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class SelectionRecord extends StandardRecord {
    public static final short sid = (short) 29;
    private byte field_1_pane;
    private int field_2_row_active_cell;
    private int field_3_col_active_cell;
    private int field_4_active_cell_ref_index;
    private CellRangeAddress8Bit[] field_6_refs;

    public SelectionRecord(int activeCellRow, int activeCellCol) {
        this.field_1_pane = (byte) 3;
        this.field_2_row_active_cell = activeCellRow;
        this.field_3_col_active_cell = activeCellCol;
        this.field_4_active_cell_ref_index = 0;
        this.field_6_refs = new CellRangeAddress8Bit[]{new CellRangeAddress8Bit(activeCellRow, activeCellRow, activeCellCol, activeCellCol)};
    }

    public SelectionRecord(RecordInputStream in) {
        this.field_1_pane = in.readByte();
        this.field_2_row_active_cell = in.readUShort();
        this.field_3_col_active_cell = in.readShort();
        this.field_4_active_cell_ref_index = in.readShort();
        this.field_6_refs = new CellRangeAddress8Bit[in.readUShort()];
        for (int i = 0; i < this.field_6_refs.length; i++) {
            this.field_6_refs[i] = new CellRangeAddress8Bit(in);
        }
    }

    public void setPane(byte pane) {
        this.field_1_pane = pane;
    }

    public void setActiveCellRow(int row) {
        this.field_2_row_active_cell = row;
    }

    public void setActiveCellCol(short col) {
        this.field_3_col_active_cell = col;
    }

    public void setActiveCellRef(short ref) {
        this.field_4_active_cell_ref_index = ref;
    }

    public byte getPane() {
        return this.field_1_pane;
    }

    public int getActiveCellRow() {
        return this.field_2_row_active_cell;
    }

    public int getActiveCellCol() {
        return this.field_3_col_active_cell;
    }

    public int getActiveCellRef() {
        return this.field_4_active_cell_ref_index;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[SELECTION]\n");
        sb.append("    .pane            = ").append(HexDump.byteToHex(getPane())).append("\n");
        sb.append("    .activecellrow   = ").append(HexDump.shortToHex(getActiveCellRow())).append("\n");
        sb.append("    .activecellcol   = ").append(HexDump.shortToHex(getActiveCellCol())).append("\n");
        sb.append("    .activecellref   = ").append(HexDump.shortToHex(getActiveCellRef())).append("\n");
        sb.append("    .numrefs         = ").append(HexDump.shortToHex(this.field_6_refs.length)).append("\n");
        sb.append("[/SELECTION]\n");
        return sb.toString();
    }

    protected int getDataSize() {
        return CellRangeAddress8Bit.getEncodedSize(this.field_6_refs.length) + 9;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeByte(getPane());
        out.writeShort(getActiveCellRow());
        out.writeShort(getActiveCellCol());
        out.writeShort(getActiveCellRef());
        out.writeShort(this.field_6_refs.length);
        for (CellRangeAddress8Bit field_6_ref : this.field_6_refs) {
            field_6_ref.serialize(out);
        }
    }

    public short getSid() {
        return (short) 29;
    }

    public Object clone() {
        SelectionRecord rec = new SelectionRecord(this.field_2_row_active_cell, this.field_3_col_active_cell);
        rec.field_1_pane = this.field_1_pane;
        rec.field_4_active_cell_ref_index = this.field_4_active_cell_ref_index;
        rec.field_6_refs = this.field_6_refs;
        return rec;
    }
}
