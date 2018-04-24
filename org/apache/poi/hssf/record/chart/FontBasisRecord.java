package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class FontBasisRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 4192;
    private short field_1_xBasis;
    private short field_2_yBasis;
    private short field_3_heightBasis;
    private short field_4_scale;
    private short field_5_indexToFontTable;

    public FontBasisRecord(RecordInputStream in) {
        this.field_1_xBasis = in.readShort();
        this.field_2_yBasis = in.readShort();
        this.field_3_heightBasis = in.readShort();
        this.field_4_scale = in.readShort();
        this.field_5_indexToFontTable = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FBI]\n");
        buffer.append("    .xBasis               = ").append("0x").append(HexDump.toHex(getXBasis())).append(" (").append(getXBasis()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .yBasis               = ").append("0x").append(HexDump.toHex(getYBasis())).append(" (").append(getYBasis()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .heightBasis          = ").append("0x").append(HexDump.toHex(getHeightBasis())).append(" (").append(getHeightBasis()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .scale                = ").append("0x").append(HexDump.toHex(getScale())).append(" (").append(getScale()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .indexToFontTable     = ").append("0x").append(HexDump.toHex(getIndexToFontTable())).append(" (").append(getIndexToFontTable()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/FBI]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_xBasis);
        out.writeShort(this.field_2_yBasis);
        out.writeShort(this.field_3_heightBasis);
        out.writeShort(this.field_4_scale);
        out.writeShort(this.field_5_indexToFontTable);
    }

    protected int getDataSize() {
        return 10;
    }

    public short getSid() {
        return sid;
    }

    public FontBasisRecord clone() {
        FontBasisRecord rec = new FontBasisRecord();
        rec.field_1_xBasis = this.field_1_xBasis;
        rec.field_2_yBasis = this.field_2_yBasis;
        rec.field_3_heightBasis = this.field_3_heightBasis;
        rec.field_4_scale = this.field_4_scale;
        rec.field_5_indexToFontTable = this.field_5_indexToFontTable;
        return rec;
    }

    public short getXBasis() {
        return this.field_1_xBasis;
    }

    public void setXBasis(short field_1_xBasis) {
        this.field_1_xBasis = field_1_xBasis;
    }

    public short getYBasis() {
        return this.field_2_yBasis;
    }

    public void setYBasis(short field_2_yBasis) {
        this.field_2_yBasis = field_2_yBasis;
    }

    public short getHeightBasis() {
        return this.field_3_heightBasis;
    }

    public void setHeightBasis(short field_3_heightBasis) {
        this.field_3_heightBasis = field_3_heightBasis;
    }

    public short getScale() {
        return this.field_4_scale;
    }

    public void setScale(short field_4_scale) {
        this.field_4_scale = field_4_scale;
    }

    public short getIndexToFontTable() {
        return this.field_5_indexToFontTable;
    }

    public void setIndexToFontTable(short field_5_indexToFontTable) {
        this.field_5_indexToFontTable = field_5_indexToFontTable;
    }
}
