package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class PaneRecord extends StandardRecord {
    public static final short ACTIVE_PANE_LOWER_LEFT = (short) 2;
    public static final short ACTIVE_PANE_LOWER_RIGHT = (short) 0;
    public static final short ACTIVE_PANE_UPPER_LEFT = (short) 3;
    public static final short ACTIVE_PANE_UPPER_RIGHT = (short) 1;
    public static final short sid = (short) 65;
    private short field_1_x;
    private short field_2_y;
    private short field_3_topRow;
    private short field_4_leftColumn;
    private short field_5_activePane;

    public PaneRecord(RecordInputStream in) {
        this.field_1_x = in.readShort();
        this.field_2_y = in.readShort();
        this.field_3_topRow = in.readShort();
        this.field_4_leftColumn = in.readShort();
        this.field_5_activePane = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PANE]\n");
        buffer.append("    .x                    = ").append("0x").append(HexDump.toHex(getX())).append(" (").append(getX()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .y                    = ").append("0x").append(HexDump.toHex(getY())).append(" (").append(getY()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .topRow               = ").append("0x").append(HexDump.toHex(getTopRow())).append(" (").append(getTopRow()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .leftColumn           = ").append("0x").append(HexDump.toHex(getLeftColumn())).append(" (").append(getLeftColumn()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .activePane           = ").append("0x").append(HexDump.toHex(getActivePane())).append(" (").append(getActivePane()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/PANE]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_x);
        out.writeShort(this.field_2_y);
        out.writeShort(this.field_3_topRow);
        out.writeShort(this.field_4_leftColumn);
        out.writeShort(this.field_5_activePane);
    }

    protected int getDataSize() {
        return 10;
    }

    public short getSid() {
        return (short) 65;
    }

    public Object clone() {
        PaneRecord rec = new PaneRecord();
        rec.field_1_x = this.field_1_x;
        rec.field_2_y = this.field_2_y;
        rec.field_3_topRow = this.field_3_topRow;
        rec.field_4_leftColumn = this.field_4_leftColumn;
        rec.field_5_activePane = this.field_5_activePane;
        return rec;
    }

    public short getX() {
        return this.field_1_x;
    }

    public void setX(short field_1_x) {
        this.field_1_x = field_1_x;
    }

    public short getY() {
        return this.field_2_y;
    }

    public void setY(short field_2_y) {
        this.field_2_y = field_2_y;
    }

    public short getTopRow() {
        return this.field_3_topRow;
    }

    public void setTopRow(short field_3_topRow) {
        this.field_3_topRow = field_3_topRow;
    }

    public short getLeftColumn() {
        return this.field_4_leftColumn;
    }

    public void setLeftColumn(short field_4_leftColumn) {
        this.field_4_leftColumn = field_4_leftColumn;
    }

    public short getActivePane() {
        return this.field_5_activePane;
    }

    public void setActivePane(short field_5_activePane) {
        this.field_5_activePane = field_5_activePane;
    }
}
