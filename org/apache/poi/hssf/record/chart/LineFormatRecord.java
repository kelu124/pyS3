package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class LineFormatRecord extends StandardRecord implements Cloneable {
    public static final short LINE_PATTERN_DARK_GRAY_PATTERN = (short) 6;
    public static final short LINE_PATTERN_DASH = (short) 1;
    public static final short LINE_PATTERN_DASH_DOT = (short) 3;
    public static final short LINE_PATTERN_DASH_DOT_DOT = (short) 4;
    public static final short LINE_PATTERN_DOT = (short) 2;
    public static final short LINE_PATTERN_LIGHT_GRAY_PATTERN = (short) 8;
    public static final short LINE_PATTERN_MEDIUM_GRAY_PATTERN = (short) 7;
    public static final short LINE_PATTERN_NONE = (short) 5;
    public static final short LINE_PATTERN_SOLID = (short) 0;
    public static final short WEIGHT_HAIRLINE = (short) -1;
    public static final short WEIGHT_MEDIUM = (short) 1;
    public static final short WEIGHT_NARROW = (short) 0;
    public static final short WEIGHT_WIDE = (short) 2;
    private static final BitField auto = BitFieldFactory.getInstance(1);
    private static final BitField drawTicks = BitFieldFactory.getInstance(4);
    public static final short sid = (short) 4103;
    private static final BitField unknown = BitFieldFactory.getInstance(4);
    private int field_1_lineColor;
    private short field_2_linePattern;
    private short field_3_weight;
    private short field_4_format;
    private short field_5_colourPaletteIndex;

    public LineFormatRecord(RecordInputStream in) {
        this.field_1_lineColor = in.readInt();
        this.field_2_linePattern = in.readShort();
        this.field_3_weight = in.readShort();
        this.field_4_format = in.readShort();
        this.field_5_colourPaletteIndex = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[LINEFORMAT]\n");
        buffer.append("    .lineColor            = ").append("0x").append(HexDump.toHex(getLineColor())).append(" (").append(getLineColor()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .linePattern          = ").append("0x").append(HexDump.toHex(getLinePattern())).append(" (").append(getLinePattern()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .weight               = ").append("0x").append(HexDump.toHex(getWeight())).append(" (").append(getWeight()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .format               = ").append("0x").append(HexDump.toHex(getFormat())).append(" (").append(getFormat()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("         .auto                     = ").append(isAuto()).append('\n');
        buffer.append("         .drawTicks                = ").append(isDrawTicks()).append('\n');
        buffer.append("         .unknown                  = ").append(isUnknown()).append('\n');
        buffer.append("    .colourPaletteIndex   = ").append("0x").append(HexDump.toHex(getColourPaletteIndex())).append(" (").append(getColourPaletteIndex()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/LINEFORMAT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(this.field_1_lineColor);
        out.writeShort(this.field_2_linePattern);
        out.writeShort(this.field_3_weight);
        out.writeShort(this.field_4_format);
        out.writeShort(this.field_5_colourPaletteIndex);
    }

    protected int getDataSize() {
        return 12;
    }

    public short getSid() {
        return sid;
    }

    public LineFormatRecord clone() {
        LineFormatRecord rec = new LineFormatRecord();
        rec.field_1_lineColor = this.field_1_lineColor;
        rec.field_2_linePattern = this.field_2_linePattern;
        rec.field_3_weight = this.field_3_weight;
        rec.field_4_format = this.field_4_format;
        rec.field_5_colourPaletteIndex = this.field_5_colourPaletteIndex;
        return rec;
    }

    public int getLineColor() {
        return this.field_1_lineColor;
    }

    public void setLineColor(int field_1_lineColor) {
        this.field_1_lineColor = field_1_lineColor;
    }

    public short getLinePattern() {
        return this.field_2_linePattern;
    }

    public void setLinePattern(short field_2_linePattern) {
        this.field_2_linePattern = field_2_linePattern;
    }

    public short getWeight() {
        return this.field_3_weight;
    }

    public void setWeight(short field_3_weight) {
        this.field_3_weight = field_3_weight;
    }

    public short getFormat() {
        return this.field_4_format;
    }

    public void setFormat(short field_4_format) {
        this.field_4_format = field_4_format;
    }

    public short getColourPaletteIndex() {
        return this.field_5_colourPaletteIndex;
    }

    public void setColourPaletteIndex(short field_5_colourPaletteIndex) {
        this.field_5_colourPaletteIndex = field_5_colourPaletteIndex;
    }

    public void setAuto(boolean value) {
        this.field_4_format = auto.setShortBoolean(this.field_4_format, value);
    }

    public boolean isAuto() {
        return auto.isSet(this.field_4_format);
    }

    public void setDrawTicks(boolean value) {
        this.field_4_format = drawTicks.setShortBoolean(this.field_4_format, value);
    }

    public boolean isDrawTicks() {
        return drawTicks.isSet(this.field_4_format);
    }

    public void setUnknown(boolean value) {
        this.field_4_format = unknown.setShortBoolean(this.field_4_format, value);
    }

    public boolean isUnknown() {
        return unknown.isSet(this.field_4_format);
    }
}
