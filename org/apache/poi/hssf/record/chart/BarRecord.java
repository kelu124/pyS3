package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class BarRecord extends StandardRecord implements Cloneable {
    private static final BitField displayAsPercentage = BitFieldFactory.getInstance(4);
    private static final BitField horizontal = BitFieldFactory.getInstance(1);
    private static final BitField shadow = BitFieldFactory.getInstance(8);
    public static final short sid = (short) 4119;
    private static final BitField stacked = BitFieldFactory.getInstance(2);
    private short field_1_barSpace;
    private short field_2_categorySpace;
    private short field_3_formatFlags;

    public BarRecord(RecordInputStream in) {
        this.field_1_barSpace = in.readShort();
        this.field_2_categorySpace = in.readShort();
        this.field_3_formatFlags = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[BAR]\n");
        buffer.append("    .barSpace             = ").append("0x").append(HexDump.toHex(getBarSpace())).append(" (").append(getBarSpace()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .categorySpace        = ").append("0x").append(HexDump.toHex(getCategorySpace())).append(" (").append(getCategorySpace()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .formatFlags          = ").append("0x").append(HexDump.toHex(getFormatFlags())).append(" (").append(getFormatFlags()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("         .horizontal               = ").append(isHorizontal()).append('\n');
        buffer.append("         .stacked                  = ").append(isStacked()).append('\n');
        buffer.append("         .displayAsPercentage      = ").append(isDisplayAsPercentage()).append('\n');
        buffer.append("         .shadow                   = ").append(isShadow()).append('\n');
        buffer.append("[/BAR]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_barSpace);
        out.writeShort(this.field_2_categorySpace);
        out.writeShort(this.field_3_formatFlags);
    }

    protected int getDataSize() {
        return 6;
    }

    public short getSid() {
        return sid;
    }

    public BarRecord clone() {
        BarRecord rec = new BarRecord();
        rec.field_1_barSpace = this.field_1_barSpace;
        rec.field_2_categorySpace = this.field_2_categorySpace;
        rec.field_3_formatFlags = this.field_3_formatFlags;
        return rec;
    }

    public short getBarSpace() {
        return this.field_1_barSpace;
    }

    public void setBarSpace(short field_1_barSpace) {
        this.field_1_barSpace = field_1_barSpace;
    }

    public short getCategorySpace() {
        return this.field_2_categorySpace;
    }

    public void setCategorySpace(short field_2_categorySpace) {
        this.field_2_categorySpace = field_2_categorySpace;
    }

    public short getFormatFlags() {
        return this.field_3_formatFlags;
    }

    public void setFormatFlags(short field_3_formatFlags) {
        this.field_3_formatFlags = field_3_formatFlags;
    }

    public void setHorizontal(boolean value) {
        this.field_3_formatFlags = horizontal.setShortBoolean(this.field_3_formatFlags, value);
    }

    public boolean isHorizontal() {
        return horizontal.isSet(this.field_3_formatFlags);
    }

    public void setStacked(boolean value) {
        this.field_3_formatFlags = stacked.setShortBoolean(this.field_3_formatFlags, value);
    }

    public boolean isStacked() {
        return stacked.isSet(this.field_3_formatFlags);
    }

    public void setDisplayAsPercentage(boolean value) {
        this.field_3_formatFlags = displayAsPercentage.setShortBoolean(this.field_3_formatFlags, value);
    }

    public boolean isDisplayAsPercentage() {
        return displayAsPercentage.isSet(this.field_3_formatFlags);
    }

    public void setShadow(boolean value) {
        this.field_3_formatFlags = shadow.setShortBoolean(this.field_3_formatFlags, value);
    }

    public boolean isShadow() {
        return shadow.isSet(this.field_3_formatFlags);
    }
}
