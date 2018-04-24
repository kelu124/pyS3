package org.apache.poi.ss.usermodel;

public interface PatternFormatting {
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

    short getFillBackgroundColor();

    Color getFillBackgroundColorColor();

    short getFillForegroundColor();

    Color getFillForegroundColorColor();

    short getFillPattern();

    void setFillBackgroundColor(Color color);

    void setFillBackgroundColor(short s);

    void setFillForegroundColor(Color color);

    void setFillForegroundColor(short s);

    void setFillPattern(short s);
}
