package org.apache.poi.ss.usermodel;

public interface Font {
    public static final byte ANSI_CHARSET = (byte) 0;
    public static final short BOLDWEIGHT_BOLD = (short) 700;
    public static final short BOLDWEIGHT_NORMAL = (short) 400;
    public static final short COLOR_NORMAL = Short.MAX_VALUE;
    public static final short COLOR_RED = (short) 10;
    public static final byte DEFAULT_CHARSET = (byte) 1;
    public static final short SS_NONE = (short) 0;
    public static final short SS_SUB = (short) 2;
    public static final short SS_SUPER = (short) 1;
    public static final byte SYMBOL_CHARSET = (byte) 2;
    public static final byte U_DOUBLE = (byte) 2;
    public static final byte U_DOUBLE_ACCOUNTING = (byte) 34;
    public static final byte U_NONE = (byte) 0;
    public static final byte U_SINGLE = (byte) 1;
    public static final byte U_SINGLE_ACCOUNTING = (byte) 33;

    boolean getBold();

    short getBoldweight();

    int getCharSet();

    short getColor();

    short getFontHeight();

    short getFontHeightInPoints();

    String getFontName();

    short getIndex();

    boolean getItalic();

    boolean getStrikeout();

    short getTypeOffset();

    byte getUnderline();

    void setBold(boolean z);

    void setBoldweight(short s);

    void setCharSet(byte b);

    void setCharSet(int i);

    void setColor(short s);

    void setFontHeight(short s);

    void setFontHeightInPoints(short s);

    void setFontName(String str);

    void setItalic(boolean z);

    void setStrikeout(boolean z);

    void setTypeOffset(short s);

    void setUnderline(byte b);
}
