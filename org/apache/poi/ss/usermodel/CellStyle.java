package org.apache.poi.ss.usermodel;

import org.apache.poi.util.Removal;

public interface CellStyle {
    @Removal(version = "3.17")
    public static final short ALIGN_CENTER = (short) 2;
    @Removal(version = "3.17")
    public static final short ALIGN_CENTER_SELECTION = (short) 6;
    @Removal(version = "3.17")
    public static final short ALIGN_FILL = (short) 4;
    @Removal(version = "3.17")
    public static final short ALIGN_GENERAL = (short) 0;
    @Removal(version = "3.17")
    public static final short ALIGN_JUSTIFY = (short) 5;
    @Removal(version = "3.17")
    public static final short ALIGN_LEFT = (short) 1;
    @Removal(version = "3.17")
    public static final short ALIGN_RIGHT = (short) 3;
    @Removal(version = "3.17")
    public static final short ALT_BARS = (short) 3;
    @Removal(version = "3.17")
    public static final short BIG_SPOTS = (short) 9;
    @Removal(version = "3.17")
    public static final short BORDER_DASHED = (short) 3;
    @Removal(version = "3.17")
    public static final short BORDER_DASH_DOT = (short) 9;
    @Removal(version = "3.17")
    public static final short BORDER_DASH_DOT_DOT = (short) 11;
    @Removal(version = "3.17")
    public static final short BORDER_DOTTED = (short) 4;
    @Removal(version = "3.17")
    public static final short BORDER_DOUBLE = (short) 6;
    @Removal(version = "3.17")
    public static final short BORDER_HAIR = (short) 7;
    @Removal(version = "3.17")
    public static final short BORDER_MEDIUM = (short) 2;
    @Removal(version = "3.17")
    public static final short BORDER_MEDIUM_DASHED = (short) 8;
    @Removal(version = "3.17")
    public static final short BORDER_MEDIUM_DASH_DOT = (short) 10;
    @Removal(version = "3.17")
    public static final short BORDER_MEDIUM_DASH_DOT_DOT = (short) 12;
    @Removal(version = "3.17")
    public static final short BORDER_NONE = (short) 0;
    @Removal(version = "3.17")
    public static final short BORDER_SLANTED_DASH_DOT = (short) 13;
    @Removal(version = "3.17")
    public static final short BORDER_THICK = (short) 5;
    @Removal(version = "3.17")
    public static final short BORDER_THIN = (short) 1;
    @Removal(version = "3.17")
    public static final short BRICKS = (short) 10;
    @Removal(version = "3.17")
    public static final short DIAMONDS = (short) 16;
    @Removal(version = "3.17")
    public static final short FINE_DOTS = (short) 2;
    @Removal(version = "3.17")
    public static final short LEAST_DOTS = (short) 18;
    @Removal(version = "3.17")
    public static final short LESS_DOTS = (short) 17;
    @Removal(version = "3.17")
    public static final short NO_FILL = (short) 0;
    @Removal(version = "3.17")
    public static final short SOLID_FOREGROUND = (short) 1;
    @Removal(version = "3.17")
    public static final short SPARSE_DOTS = (short) 4;
    @Removal(version = "3.17")
    public static final short SQUARES = (short) 15;
    @Removal(version = "3.17")
    public static final short THICK_BACKWARD_DIAG = (short) 7;
    @Removal(version = "3.17")
    public static final short THICK_FORWARD_DIAG = (short) 8;
    @Removal(version = "3.17")
    public static final short THICK_HORZ_BANDS = (short) 5;
    @Removal(version = "3.17")
    public static final short THICK_VERT_BANDS = (short) 6;
    @Removal(version = "3.17")
    public static final short THIN_BACKWARD_DIAG = (short) 13;
    @Removal(version = "3.17")
    public static final short THIN_FORWARD_DIAG = (short) 14;
    @Removal(version = "3.17")
    public static final short THIN_HORZ_BANDS = (short) 11;
    @Removal(version = "3.17")
    public static final short THIN_VERT_BANDS = (short) 12;
    @Removal(version = "3.17")
    public static final short VERTICAL_BOTTOM = (short) 2;
    @Removal(version = "3.17")
    public static final short VERTICAL_CENTER = (short) 1;
    @Removal(version = "3.17")
    public static final short VERTICAL_JUSTIFY = (short) 3;
    @Removal(version = "3.17")
    public static final short VERTICAL_TOP = (short) 0;

    void cloneStyleFrom(CellStyle cellStyle);

    short getAlignment();

    HorizontalAlignment getAlignmentEnum();

    short getBorderBottom();

    BorderStyle getBorderBottomEnum();

    short getBorderLeft();

    BorderStyle getBorderLeftEnum();

    short getBorderRight();

    BorderStyle getBorderRightEnum();

    short getBorderTop();

    BorderStyle getBorderTopEnum();

    short getBottomBorderColor();

    short getDataFormat();

    String getDataFormatString();

    short getFillBackgroundColor();

    Color getFillBackgroundColorColor();

    short getFillForegroundColor();

    Color getFillForegroundColorColor();

    short getFillPattern();

    FillPatternType getFillPatternEnum();

    short getFontIndex();

    boolean getHidden();

    short getIndention();

    short getIndex();

    short getLeftBorderColor();

    boolean getLocked();

    short getRightBorderColor();

    short getRotation();

    boolean getShrinkToFit();

    short getTopBorderColor();

    short getVerticalAlignment();

    VerticalAlignment getVerticalAlignmentEnum();

    boolean getWrapText();

    void setAlignment(HorizontalAlignment horizontalAlignment);

    void setAlignment(short s);

    void setBorderBottom(BorderStyle borderStyle);

    @Removal(version = "3.17")
    void setBorderBottom(short s);

    void setBorderLeft(BorderStyle borderStyle);

    @Removal(version = "3.17")
    void setBorderLeft(short s);

    void setBorderRight(BorderStyle borderStyle);

    @Removal(version = "3.17")
    void setBorderRight(short s);

    void setBorderTop(BorderStyle borderStyle);

    @Removal(version = "3.17")
    void setBorderTop(short s);

    void setBottomBorderColor(short s);

    void setDataFormat(short s);

    void setFillBackgroundColor(short s);

    void setFillForegroundColor(short s);

    void setFillPattern(FillPatternType fillPatternType);

    void setFillPattern(short s);

    void setFont(Font font);

    void setHidden(boolean z);

    void setIndention(short s);

    void setLeftBorderColor(short s);

    void setLocked(boolean z);

    void setRightBorderColor(short s);

    void setRotation(short s);

    void setShrinkToFit(boolean z);

    void setTopBorderColor(short s);

    void setVerticalAlignment(VerticalAlignment verticalAlignment);

    void setVerticalAlignment(short s);

    void setWrapText(boolean z);
}
