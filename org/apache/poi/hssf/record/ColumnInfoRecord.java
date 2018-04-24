package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class ColumnInfoRecord extends StandardRecord implements Cloneable {
    private static final BitField collapsed = BitFieldFactory.getInstance(4096);
    private static final BitField hidden = BitFieldFactory.getInstance(1);
    private static final BitField outlevel = BitFieldFactory.getInstance(1792);
    public static final short sid = (short) 125;
    private int _colWidth;
    private int _firstCol;
    private int _lastCol;
    private int _options;
    private int _xfIndex;
    private int field_6_reserved;

    public ColumnInfoRecord() {
        setColumnWidth(2275);
        this._options = 2;
        this._xfIndex = 15;
        this.field_6_reserved = 2;
    }

    public ColumnInfoRecord(RecordInputStream in) {
        this._firstCol = in.readUShort();
        this._lastCol = in.readUShort();
        this._colWidth = in.readUShort();
        this._xfIndex = in.readUShort();
        this._options = in.readUShort();
        switch (in.remaining()) {
            case 0:
                this.field_6_reserved = 0;
                return;
            case 1:
                this.field_6_reserved = in.readByte();
                return;
            case 2:
                this.field_6_reserved = in.readUShort();
                return;
            default:
                throw new RuntimeException("Unusual record size remaining=(" + in.remaining() + ")");
        }
    }

    public void setFirstColumn(int fc) {
        this._firstCol = fc;
    }

    public void setLastColumn(int lc) {
        this._lastCol = lc;
    }

    public void setColumnWidth(int cw) {
        this._colWidth = cw;
    }

    public void setXFIndex(int xfi) {
        this._xfIndex = xfi;
    }

    public void setHidden(boolean ishidden) {
        this._options = hidden.setBoolean(this._options, ishidden);
    }

    public void setOutlineLevel(int olevel) {
        this._options = outlevel.setValue(this._options, olevel);
    }

    public void setCollapsed(boolean isCollapsed) {
        this._options = collapsed.setBoolean(this._options, isCollapsed);
    }

    public int getFirstColumn() {
        return this._firstCol;
    }

    public int getLastColumn() {
        return this._lastCol;
    }

    public int getColumnWidth() {
        return this._colWidth;
    }

    public int getXFIndex() {
        return this._xfIndex;
    }

    public boolean getHidden() {
        return hidden.isSet(this._options);
    }

    public int getOutlineLevel() {
        return outlevel.getValue(this._options);
    }

    public boolean getCollapsed() {
        return collapsed.isSet(this._options);
    }

    public boolean containsColumn(int columnIndex) {
        return this._firstCol <= columnIndex && columnIndex <= this._lastCol;
    }

    public boolean isAdjacentBefore(ColumnInfoRecord other) {
        return this._lastCol == other._firstCol + -1;
    }

    public boolean formatMatches(ColumnInfoRecord other) {
        if (this._xfIndex == other._xfIndex && this._options == other._options && this._colWidth == other._colWidth) {
            return true;
        }
        return false;
    }

    public short getSid() {
        return (short) 125;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getFirstColumn());
        out.writeShort(getLastColumn());
        out.writeShort(getColumnWidth());
        out.writeShort(getXFIndex());
        out.writeShort(this._options);
        out.writeShort(this.field_6_reserved);
    }

    protected int getDataSize() {
        return 12;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[COLINFO]\n");
        sb.append("  colfirst = ").append(getFirstColumn()).append("\n");
        sb.append("  collast  = ").append(getLastColumn()).append("\n");
        sb.append("  colwidth = ").append(getColumnWidth()).append("\n");
        sb.append("  xfindex  = ").append(getXFIndex()).append("\n");
        sb.append("  options  = ").append(HexDump.shortToHex(this._options)).append("\n");
        sb.append("    hidden   = ").append(getHidden()).append("\n");
        sb.append("    olevel   = ").append(getOutlineLevel()).append("\n");
        sb.append("    collapsed= ").append(getCollapsed()).append("\n");
        sb.append("[/COLINFO]\n");
        return sb.toString();
    }

    public ColumnInfoRecord clone() {
        ColumnInfoRecord rec = new ColumnInfoRecord();
        rec._firstCol = this._firstCol;
        rec._lastCol = this._lastCol;
        rec._colWidth = this._colWidth;
        rec._xfIndex = this._xfIndex;
        rec._options = this._options;
        rec.field_6_reserved = this.field_6_reserved;
        return rec;
    }
}
