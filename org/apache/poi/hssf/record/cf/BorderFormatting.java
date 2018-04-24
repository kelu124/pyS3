package org.apache.poi.hssf.record.cf;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;
import org.bytedeco.javacpp.avcodec;

public final class BorderFormatting implements Cloneable {
    public static final short BORDER_DASHED = (short) 3;
    public static final short BORDER_DASH_DOT = (short) 9;
    public static final short BORDER_DASH_DOT_DOT = (short) 11;
    public static final short BORDER_DOTTED = (short) 7;
    public static final short BORDER_DOUBLE = (short) 6;
    public static final short BORDER_HAIR = (short) 4;
    public static final short BORDER_MEDIUM = (short) 2;
    public static final short BORDER_MEDIUM_DASHED = (short) 8;
    public static final short BORDER_MEDIUM_DASH_DOT = (short) 10;
    public static final short BORDER_MEDIUM_DASH_DOT_DOT = (short) 12;
    public static final short BORDER_NONE = (short) 0;
    public static final short BORDER_SLANTED_DASH_DOT = (short) 13;
    public static final short BORDER_THICK = (short) 5;
    public static final short BORDER_THIN = (short) 1;
    private static final BitField bordBlTrtLineOnOff = BitFieldFactory.getInstance(Integer.MIN_VALUE);
    private static final BitField bordBottomLineColor = BitFieldFactory.getInstance(16256);
    private static final BitField bordBottomLineStyle = BitFieldFactory.getInstance(avcodec.MB_TYPE_L0L1);
    private static final BitField bordDiagLineColor = BitFieldFactory.getInstance(2080768);
    private static final BitField bordDiagLineStyle = BitFieldFactory.getInstance(31457280);
    private static final BitField bordLeftLineColor = BitFieldFactory.getInstance(8323072);
    private static final BitField bordLeftLineStyle = BitFieldFactory.getInstance(15);
    private static final BitField bordRightLineColor = BitFieldFactory.getInstance(1065353216);
    private static final BitField bordRightLineStyle = BitFieldFactory.getInstance(240);
    private static final BitField bordTlBrLineOnOff = BitFieldFactory.getInstance(1073741824);
    private static final BitField bordTopLineColor = BitFieldFactory.getInstance(127);
    private static final BitField bordTopLineStyle = BitFieldFactory.getInstance(3840);
    private int field_13_border_styles1;
    private int field_14_border_styles2;

    public BorderFormatting() {
        this.field_13_border_styles1 = 0;
        this.field_14_border_styles2 = 0;
    }

    public BorderFormatting(LittleEndianInput in) {
        this.field_13_border_styles1 = in.readInt();
        this.field_14_border_styles2 = in.readInt();
    }

    public int getDataLength() {
        return 8;
    }

    public void setBorderLeft(int border) {
        this.field_13_border_styles1 = bordLeftLineStyle.setValue(this.field_13_border_styles1, border);
    }

    public int getBorderLeft() {
        return bordLeftLineStyle.getValue(this.field_13_border_styles1);
    }

    public void setBorderRight(int border) {
        this.field_13_border_styles1 = bordRightLineStyle.setValue(this.field_13_border_styles1, border);
    }

    public int getBorderRight() {
        return bordRightLineStyle.getValue(this.field_13_border_styles1);
    }

    public void setBorderTop(int border) {
        this.field_13_border_styles1 = bordTopLineStyle.setValue(this.field_13_border_styles1, border);
    }

    public int getBorderTop() {
        return bordTopLineStyle.getValue(this.field_13_border_styles1);
    }

    public void setBorderBottom(int border) {
        this.field_13_border_styles1 = bordBottomLineStyle.setValue(this.field_13_border_styles1, border);
    }

    public int getBorderBottom() {
        return bordBottomLineStyle.getValue(this.field_13_border_styles1);
    }

    public void setBorderDiagonal(int border) {
        this.field_14_border_styles2 = bordDiagLineStyle.setValue(this.field_14_border_styles2, border);
    }

    public int getBorderDiagonal() {
        return bordDiagLineStyle.getValue(this.field_14_border_styles2);
    }

    public void setLeftBorderColor(int color) {
        this.field_13_border_styles1 = bordLeftLineColor.setValue(this.field_13_border_styles1, color);
    }

    public int getLeftBorderColor() {
        return bordLeftLineColor.getValue(this.field_13_border_styles1);
    }

    public void setRightBorderColor(int color) {
        this.field_13_border_styles1 = bordRightLineColor.setValue(this.field_13_border_styles1, color);
    }

    public int getRightBorderColor() {
        return bordRightLineColor.getValue(this.field_13_border_styles1);
    }

    public void setTopBorderColor(int color) {
        this.field_14_border_styles2 = bordTopLineColor.setValue(this.field_14_border_styles2, color);
    }

    public int getTopBorderColor() {
        return bordTopLineColor.getValue(this.field_14_border_styles2);
    }

    public void setBottomBorderColor(int color) {
        this.field_14_border_styles2 = bordBottomLineColor.setValue(this.field_14_border_styles2, color);
    }

    public int getBottomBorderColor() {
        return bordBottomLineColor.getValue(this.field_14_border_styles2);
    }

    public void setDiagonalBorderColor(int color) {
        this.field_14_border_styles2 = bordDiagLineColor.setValue(this.field_14_border_styles2, color);
    }

    public int getDiagonalBorderColor() {
        return bordDiagLineColor.getValue(this.field_14_border_styles2);
    }

    public void setForwardDiagonalOn(boolean on) {
        this.field_13_border_styles1 = bordBlTrtLineOnOff.setBoolean(this.field_13_border_styles1, on);
    }

    public void setBackwardDiagonalOn(boolean on) {
        this.field_13_border_styles1 = bordTlBrLineOnOff.setBoolean(this.field_13_border_styles1, on);
    }

    public boolean isForwardDiagonalOn() {
        return bordBlTrtLineOnOff.isSet(this.field_13_border_styles1);
    }

    public boolean isBackwardDiagonalOn() {
        return bordTlBrLineOnOff.isSet(this.field_13_border_styles1);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("    [Border Formatting]\n");
        buffer.append("          .lftln     = ").append(Integer.toHexString(getBorderLeft())).append("\n");
        buffer.append("          .rgtln     = ").append(Integer.toHexString(getBorderRight())).append("\n");
        buffer.append("          .topln     = ").append(Integer.toHexString(getBorderTop())).append("\n");
        buffer.append("          .btmln     = ").append(Integer.toHexString(getBorderBottom())).append("\n");
        buffer.append("          .leftborder= ").append(Integer.toHexString(getLeftBorderColor())).append("\n");
        buffer.append("          .rghtborder= ").append(Integer.toHexString(getRightBorderColor())).append("\n");
        buffer.append("          .topborder= ").append(Integer.toHexString(getTopBorderColor())).append("\n");
        buffer.append("          .bottomborder= ").append(Integer.toHexString(getBottomBorderColor())).append("\n");
        buffer.append("          .fwdiag= ").append(isForwardDiagonalOn()).append("\n");
        buffer.append("          .bwdiag= ").append(isBackwardDiagonalOn()).append("\n");
        buffer.append("    [/Border Formatting]\n");
        return buffer.toString();
    }

    public BorderFormatting clone() {
        BorderFormatting rec = new BorderFormatting();
        rec.field_13_border_styles1 = this.field_13_border_styles1;
        rec.field_14_border_styles2 = this.field_14_border_styles2;
        return rec;
    }

    public int serialize(int offset, byte[] data) {
        LittleEndian.putInt(data, offset + 0, this.field_13_border_styles1);
        LittleEndian.putInt(data, offset + 4, this.field_14_border_styles2);
        return 8;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(this.field_13_border_styles1);
        out.writeInt(this.field_14_border_styles2);
    }
}
