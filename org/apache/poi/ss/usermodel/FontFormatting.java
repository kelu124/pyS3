package org.apache.poi.ss.usermodel;

public interface FontFormatting {
    public static final short SS_NONE = (short) 0;
    public static final short SS_SUB = (short) 2;
    public static final short SS_SUPER = (short) 1;
    public static final byte U_DOUBLE = (byte) 2;
    public static final byte U_DOUBLE_ACCOUNTING = (byte) 34;
    public static final byte U_NONE = (byte) 0;
    public static final byte U_SINGLE = (byte) 1;
    public static final byte U_SINGLE_ACCOUNTING = (byte) 33;

    short getEscapementType();

    Color getFontColor();

    short getFontColorIndex();

    int getFontHeight();

    short getUnderlineType();

    boolean isBold();

    boolean isItalic();

    void resetFontStyle();

    void setEscapementType(short s);

    void setFontColor(Color color);

    void setFontColorIndex(short s);

    void setFontHeight(int i);

    void setFontStyle(boolean z, boolean z2);

    void setUnderlineType(short s);
}
