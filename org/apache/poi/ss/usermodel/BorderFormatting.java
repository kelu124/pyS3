package org.apache.poi.ss.usermodel;

import org.apache.poi.util.Removal;

public interface BorderFormatting {
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

    short getBorderBottom();

    BorderStyle getBorderBottomEnum();

    short getBorderDiagonal();

    BorderStyle getBorderDiagonalEnum();

    short getBorderLeft();

    BorderStyle getBorderLeftEnum();

    short getBorderRight();

    BorderStyle getBorderRightEnum();

    short getBorderTop();

    BorderStyle getBorderTopEnum();

    short getBottomBorderColor();

    Color getBottomBorderColorColor();

    short getDiagonalBorderColor();

    Color getDiagonalBorderColorColor();

    short getLeftBorderColor();

    Color getLeftBorderColorColor();

    short getRightBorderColor();

    Color getRightBorderColorColor();

    short getTopBorderColor();

    Color getTopBorderColorColor();

    void setBorderBottom(BorderStyle borderStyle);

    void setBorderBottom(short s);

    void setBorderDiagonal(BorderStyle borderStyle);

    void setBorderDiagonal(short s);

    void setBorderLeft(BorderStyle borderStyle);

    void setBorderLeft(short s);

    void setBorderRight(BorderStyle borderStyle);

    void setBorderRight(short s);

    void setBorderTop(BorderStyle borderStyle);

    void setBorderTop(short s);

    void setBottomBorderColor(Color color);

    void setBottomBorderColor(short s);

    void setDiagonalBorderColor(Color color);

    void setDiagonalBorderColor(short s);

    void setLeftBorderColor(Color color);

    void setLeftBorderColor(short s);

    void setRightBorderColor(Color color);

    void setRightBorderColor(short s);

    void setTopBorderColor(Color color);

    void setTopBorderColor(short s);
}
