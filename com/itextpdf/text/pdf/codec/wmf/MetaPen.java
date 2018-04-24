package com.itextpdf.text.pdf.codec.wmf;

import com.itextpdf.text.BaseColor;
import java.io.IOException;

public class MetaPen extends MetaObject {
    public static final int PS_DASH = 1;
    public static final int PS_DASHDOT = 3;
    public static final int PS_DASHDOTDOT = 4;
    public static final int PS_DOT = 2;
    public static final int PS_INSIDEFRAME = 6;
    public static final int PS_NULL = 5;
    public static final int PS_SOLID = 0;
    BaseColor color;
    int penWidth;
    int style;

    public MetaPen() {
        this.style = 0;
        this.penWidth = 1;
        this.color = BaseColor.BLACK;
        this.type = 1;
    }

    public void init(InputMeta in) throws IOException {
        this.style = in.readWord();
        this.penWidth = in.readShort();
        in.readWord();
        this.color = in.readColor();
    }

    public int getStyle() {
        return this.style;
    }

    public int getPenWidth() {
        return this.penWidth;
    }

    public BaseColor getColor() {
        return this.color;
    }
}
