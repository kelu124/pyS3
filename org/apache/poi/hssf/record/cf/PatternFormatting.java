package org.apache.poi.hssf.record.cf;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class PatternFormatting implements Cloneable {
    public static final short ALT_BARS = (short) 3;
    public static final short BIG_SPOTS = (short) 9;
    public static final short BRICKS = (short) 10;
    public static final short DIAMONDS = (short) 16;
    public static final short FINE_DOTS = (short) 2;
    public static final short LEAST_DOTS = (short) 18;
    public static final short LESS_DOTS = (short) 17;
    public static final short NO_FILL = (short) 0;
    public static final short SOLID_FOREGROUND = (short) 1;
    public static final short SPARSE_DOTS = (short) 4;
    public static final short SQUARES = (short) 15;
    public static final short THICK_BACKWARD_DIAG = (short) 7;
    public static final short THICK_FORWARD_DIAG = (short) 8;
    public static final short THICK_HORZ_BANDS = (short) 5;
    public static final short THICK_VERT_BANDS = (short) 6;
    public static final short THIN_BACKWARD_DIAG = (short) 13;
    public static final short THIN_FORWARD_DIAG = (short) 14;
    public static final short THIN_HORZ_BANDS = (short) 11;
    public static final short THIN_VERT_BANDS = (short) 12;
    private static final BitField fillPatternStyle = BitFieldFactory.getInstance(64512);
    private static final BitField patternBackgroundColorIndex = BitFieldFactory.getInstance(16256);
    private static final BitField patternColorIndex = BitFieldFactory.getInstance(127);
    private int field_15_pattern_style;
    private int field_16_pattern_color_indexes;

    public PatternFormatting() {
        this.field_15_pattern_style = 0;
        this.field_16_pattern_color_indexes = 0;
    }

    public PatternFormatting(LittleEndianInput in) {
        this.field_15_pattern_style = in.readUShort();
        this.field_16_pattern_color_indexes = in.readUShort();
    }

    public int getDataLength() {
        return 4;
    }

    public void setFillPattern(int fp) {
        this.field_15_pattern_style = fillPatternStyle.setValue(this.field_15_pattern_style, fp);
    }

    public int getFillPattern() {
        return fillPatternStyle.getValue(this.field_15_pattern_style);
    }

    public void setFillBackgroundColor(int bg) {
        this.field_16_pattern_color_indexes = patternBackgroundColorIndex.setValue(this.field_16_pattern_color_indexes, bg);
    }

    public int getFillBackgroundColor() {
        return patternBackgroundColorIndex.getValue(this.field_16_pattern_color_indexes);
    }

    public void setFillForegroundColor(int fg) {
        this.field_16_pattern_color_indexes = patternColorIndex.setValue(this.field_16_pattern_color_indexes, fg);
    }

    public int getFillForegroundColor() {
        return patternColorIndex.getValue(this.field_16_pattern_color_indexes);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("    [Pattern Formatting]\n");
        buffer.append("          .fillpattern= ").append(Integer.toHexString(getFillPattern())).append("\n");
        buffer.append("          .fgcoloridx= ").append(Integer.toHexString(getFillForegroundColor())).append("\n");
        buffer.append("          .bgcoloridx= ").append(Integer.toHexString(getFillBackgroundColor())).append("\n");
        buffer.append("    [/Pattern Formatting]\n");
        return buffer.toString();
    }

    public Object clone() {
        PatternFormatting rec = new PatternFormatting();
        rec.field_15_pattern_style = this.field_15_pattern_style;
        rec.field_16_pattern_color_indexes = this.field_16_pattern_color_indexes;
        return rec;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_15_pattern_style);
        out.writeShort(this.field_16_pattern_color_indexes);
    }
}
