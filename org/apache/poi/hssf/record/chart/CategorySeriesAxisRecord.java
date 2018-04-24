package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class CategorySeriesAxisRecord extends StandardRecord implements Cloneable {
    private static final BitField crossesFarRight = BitFieldFactory.getInstance(2);
    private static final BitField reversed = BitFieldFactory.getInstance(4);
    public static final short sid = (short) 4128;
    private static final BitField valueAxisCrossing = BitFieldFactory.getInstance(1);
    private short field_1_crossingPoint;
    private short field_2_labelFrequency;
    private short field_3_tickMarkFrequency;
    private short field_4_options;

    public CategorySeriesAxisRecord(RecordInputStream in) {
        this.field_1_crossingPoint = in.readShort();
        this.field_2_labelFrequency = in.readShort();
        this.field_3_tickMarkFrequency = in.readShort();
        this.field_4_options = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CATSERRANGE]\n");
        buffer.append("    .crossingPoint        = ").append("0x").append(HexDump.toHex(getCrossingPoint())).append(" (").append(getCrossingPoint()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .labelFrequency       = ").append("0x").append(HexDump.toHex(getLabelFrequency())).append(" (").append(getLabelFrequency()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .tickMarkFrequency    = ").append("0x").append(HexDump.toHex(getTickMarkFrequency())).append(" (").append(getTickMarkFrequency()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .options              = ").append("0x").append(HexDump.toHex(getOptions())).append(" (").append(getOptions()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("         .valueAxisCrossing        = ").append(isValueAxisCrossing()).append('\n');
        buffer.append("         .crossesFarRight          = ").append(isCrossesFarRight()).append('\n');
        buffer.append("         .reversed                 = ").append(isReversed()).append('\n');
        buffer.append("[/CATSERRANGE]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_crossingPoint);
        out.writeShort(this.field_2_labelFrequency);
        out.writeShort(this.field_3_tickMarkFrequency);
        out.writeShort(this.field_4_options);
    }

    protected int getDataSize() {
        return 8;
    }

    public short getSid() {
        return sid;
    }

    public CategorySeriesAxisRecord clone() {
        CategorySeriesAxisRecord rec = new CategorySeriesAxisRecord();
        rec.field_1_crossingPoint = this.field_1_crossingPoint;
        rec.field_2_labelFrequency = this.field_2_labelFrequency;
        rec.field_3_tickMarkFrequency = this.field_3_tickMarkFrequency;
        rec.field_4_options = this.field_4_options;
        return rec;
    }

    public short getCrossingPoint() {
        return this.field_1_crossingPoint;
    }

    public void setCrossingPoint(short field_1_crossingPoint) {
        this.field_1_crossingPoint = field_1_crossingPoint;
    }

    public short getLabelFrequency() {
        return this.field_2_labelFrequency;
    }

    public void setLabelFrequency(short field_2_labelFrequency) {
        this.field_2_labelFrequency = field_2_labelFrequency;
    }

    public short getTickMarkFrequency() {
        return this.field_3_tickMarkFrequency;
    }

    public void setTickMarkFrequency(short field_3_tickMarkFrequency) {
        this.field_3_tickMarkFrequency = field_3_tickMarkFrequency;
    }

    public short getOptions() {
        return this.field_4_options;
    }

    public void setOptions(short field_4_options) {
        this.field_4_options = field_4_options;
    }

    public void setValueAxisCrossing(boolean value) {
        this.field_4_options = valueAxisCrossing.setShortBoolean(this.field_4_options, value);
    }

    public boolean isValueAxisCrossing() {
        return valueAxisCrossing.isSet(this.field_4_options);
    }

    public void setCrossesFarRight(boolean value) {
        this.field_4_options = crossesFarRight.setShortBoolean(this.field_4_options, value);
    }

    public boolean isCrossesFarRight() {
        return crossesFarRight.isSet(this.field_4_options);
    }

    public void setReversed(boolean value) {
        this.field_4_options = reversed.setShortBoolean(this.field_4_options, value);
    }

    public boolean isReversed() {
        return reversed.isSet(this.field_4_options);
    }
}
