package org.apache.poi.hssf.record.cf;

import java.util.Locale;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndian;

public final class FontFormatting implements Cloneable {
    public static final int FONT_CELL_HEIGHT_PRESERVED = -1;
    private static final short FONT_WEIGHT_BOLD = (short) 700;
    private static final short FONT_WEIGHT_NORMAL = (short) 400;
    private static final int OFFSET_ESCAPEMENT_TYPE = 74;
    private static final int OFFSET_ESCAPEMENT_TYPE_MODIFIED = 92;
    private static final int OFFSET_FONT_COLOR_INDEX = 80;
    private static final int OFFSET_FONT_FORMATING_END = 116;
    private static final int OFFSET_FONT_HEIGHT = 64;
    private static final int OFFSET_FONT_NAME = 0;
    private static final int OFFSET_FONT_OPTIONS = 68;
    private static final int OFFSET_FONT_WEIGHT = 72;
    private static final int OFFSET_FONT_WEIGHT_MODIFIED = 100;
    private static final int OFFSET_NOT_USED1 = 104;
    private static final int OFFSET_NOT_USED2 = 108;
    private static final int OFFSET_NOT_USED3 = 112;
    private static final int OFFSET_OPTION_FLAGS = 88;
    private static final int OFFSET_UNDERLINE_TYPE = 76;
    private static final int OFFSET_UNDERLINE_TYPE_MODIFIED = 96;
    private static final int RAW_DATA_SIZE = 118;
    public static final short SS_NONE = (short) 0;
    public static final short SS_SUB = (short) 2;
    public static final short SS_SUPER = (short) 1;
    public static final byte U_DOUBLE = (byte) 2;
    public static final byte U_DOUBLE_ACCOUNTING = (byte) 34;
    public static final byte U_NONE = (byte) 0;
    public static final byte U_SINGLE = (byte) 1;
    public static final byte U_SINGLE_ACCOUNTING = (byte) 33;
    private static final BitField cancellation = BitFieldFactory.getInstance(128);
    private static final BitField cancellationModified = BitFieldFactory.getInstance(128);
    private static final BitField outline = BitFieldFactory.getInstance(8);
    private static final BitField outlineModified = BitFieldFactory.getInstance(8);
    private static final BitField posture = BitFieldFactory.getInstance(2);
    private static final BitField shadow = BitFieldFactory.getInstance(16);
    private static final BitField shadowModified = BitFieldFactory.getInstance(16);
    private static final BitField styleModified = BitFieldFactory.getInstance(2);
    private final byte[] _rawData;

    public FontFormatting() {
        this._rawData = new byte[118];
        setFontHeight(-1);
        setItalic(false);
        setFontWieghtModified(false);
        setOutline(false);
        setShadow(false);
        setStrikeout(false);
        setEscapementType((short) 0);
        setUnderlineType((short) 0);
        setFontColorIndex((short) -1);
        setFontStyleModified(false);
        setFontOutlineModified(false);
        setFontShadowModified(false);
        setFontCancellationModified(false);
        setEscapementTypeModified(false);
        setUnderlineTypeModified(false);
        setShort(0, 0);
        setInt(104, 1);
        setInt(108, 0);
        setInt(112, Integer.MAX_VALUE);
        setShort(116, 1);
    }

    public FontFormatting(RecordInputStream in) {
        this._rawData = new byte[118];
        for (int i = 0; i < this._rawData.length; i++) {
            this._rawData[i] = in.readByte();
        }
    }

    private short getShort(int offset) {
        return LittleEndian.getShort(this._rawData, offset);
    }

    private void setShort(int offset, int value) {
        LittleEndian.putShort(this._rawData, offset, (short) value);
    }

    private int getInt(int offset) {
        return LittleEndian.getInt(this._rawData, offset);
    }

    private void setInt(int offset, int value) {
        LittleEndian.putInt(this._rawData, offset, value);
    }

    public byte[] getRawRecord() {
        return this._rawData;
    }

    public int getDataLength() {
        return 118;
    }

    public void setFontHeight(int height) {
        setInt(64, height);
    }

    public int getFontHeight() {
        return getInt(64);
    }

    private void setFontOption(boolean option, BitField field) {
        setInt(68, field.setBoolean(getInt(68), option));
    }

    private boolean getFontOption(BitField field) {
        return field.isSet(getInt(68));
    }

    public void setItalic(boolean italic) {
        setFontOption(italic, posture);
    }

    public boolean isItalic() {
        return getFontOption(posture);
    }

    public void setOutline(boolean on) {
        setFontOption(on, outline);
    }

    public boolean isOutlineOn() {
        return getFontOption(outline);
    }

    public void setShadow(boolean on) {
        setFontOption(on, shadow);
    }

    public boolean isShadowOn() {
        return getFontOption(shadow);
    }

    public void setStrikeout(boolean strike) {
        setFontOption(strike, cancellation);
    }

    public boolean isStruckout() {
        return getFontOption(cancellation);
    }

    private void setFontWeight(short pbw) {
        short bw = pbw;
        if (bw < (short) 100) {
            bw = (short) 100;
        }
        if (bw > (short) 1000) {
            bw = (short) 1000;
        }
        setShort(72, bw);
    }

    public void setBold(boolean bold) {
        setFontWeight(bold ? (short) 700 : (short) 400);
    }

    public short getFontWeight() {
        return getShort(72);
    }

    public boolean isBold() {
        return getFontWeight() == (short) 700;
    }

    public short getEscapementType() {
        return getShort(74);
    }

    public void setEscapementType(short escapementType) {
        setShort(74, escapementType);
    }

    public short getUnderlineType() {
        return getShort(76);
    }

    public void setUnderlineType(short underlineType) {
        setShort(76, underlineType);
    }

    public short getFontColorIndex() {
        return (short) getInt(80);
    }

    public void setFontColorIndex(short fci) {
        setInt(80, fci);
    }

    private boolean getOptionFlag(BitField field) {
        return field.getValue(getInt(88)) == 0;
    }

    private void setOptionFlag(boolean modified, BitField field) {
        setInt(88, field.setValue(getInt(88), modified ? 0 : 1));
    }

    public boolean isFontStyleModified() {
        return getOptionFlag(styleModified);
    }

    public void setFontStyleModified(boolean modified) {
        setOptionFlag(modified, styleModified);
    }

    public boolean isFontOutlineModified() {
        return getOptionFlag(outlineModified);
    }

    public void setFontOutlineModified(boolean modified) {
        setOptionFlag(modified, outlineModified);
    }

    public boolean isFontShadowModified() {
        return getOptionFlag(shadowModified);
    }

    public void setFontShadowModified(boolean modified) {
        setOptionFlag(modified, shadowModified);
    }

    public void setFontCancellationModified(boolean modified) {
        setOptionFlag(modified, cancellationModified);
    }

    public boolean isFontCancellationModified() {
        return getOptionFlag(cancellationModified);
    }

    public void setEscapementTypeModified(boolean modified) {
        setInt(92, modified ? 0 : 1);
    }

    public boolean isEscapementTypeModified() {
        return getInt(92) == 0;
    }

    public void setUnderlineTypeModified(boolean modified) {
        setInt(96, modified ? 0 : 1);
    }

    public boolean isUnderlineTypeModified() {
        return getInt(96) == 0;
    }

    public void setFontWieghtModified(boolean modified) {
        setInt(100, modified ? 0 : 1);
    }

    public boolean isFontWeightModified() {
        return getInt(100) == 0;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("    [Font Formatting]\n");
        buffer.append("\t.font height = ").append(getFontHeight()).append(" twips\n");
        if (isFontStyleModified()) {
            buffer.append("\t.font posture = ").append(isItalic() ? "Italic" : "Normal").append("\n");
        } else {
            buffer.append("\t.font posture = ]not modified]").append("\n");
        }
        if (isFontOutlineModified()) {
            buffer.append("\t.font outline = ").append(isOutlineOn()).append("\n");
        } else {
            buffer.append("\t.font outline is not modified\n");
        }
        if (isFontShadowModified()) {
            buffer.append("\t.font shadow = ").append(isShadowOn()).append("\n");
        } else {
            buffer.append("\t.font shadow is not modified\n");
        }
        if (isFontCancellationModified()) {
            buffer.append("\t.font strikeout = ").append(isStruckout()).append("\n");
        } else {
            buffer.append("\t.font strikeout is not modified\n");
        }
        if (isFontStyleModified()) {
            StringBuffer append = buffer.append("\t.font weight = ").append(getFontWeight());
            String str = getFontWeight() == (short) 400 ? "(Normal)" : getFontWeight() == (short) 700 ? "(Bold)" : "0x" + Integer.toHexString(getFontWeight());
            append.append(str).append("\n");
        } else {
            buffer.append("\t.font weight = ]not modified]").append("\n");
        }
        if (isEscapementTypeModified()) {
            buffer.append("\t.escapement type = ").append(getEscapementType()).append("\n");
        } else {
            buffer.append("\t.escapement type is not modified\n");
        }
        if (isUnderlineTypeModified()) {
            buffer.append("\t.underline type = ").append(getUnderlineType()).append("\n");
        } else {
            buffer.append("\t.underline type is not modified\n");
        }
        buffer.append("\t.color index = ").append("0x" + Integer.toHexString(getFontColorIndex()).toUpperCase(Locale.ROOT)).append("\n");
        buffer.append("    [/Font Formatting]\n");
        return buffer.toString();
    }

    public FontFormatting clone() {
        FontFormatting other = new FontFormatting();
        System.arraycopy(this._rawData, 0, other._rawData, 0, this._rawData.length);
        return other;
    }
}
