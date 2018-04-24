package com.itextpdf.text.pdf.codec.wmf;

import com.itextpdf.text.Document;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MetaFont extends MetaObject {
    static final int BOLDTHRESHOLD = 600;
    static final int DEFAULT_PITCH = 0;
    static final int ETO_CLIPPED = 4;
    static final int ETO_OPAQUE = 2;
    static final int FF_DECORATIVE = 5;
    static final int FF_DONTCARE = 0;
    static final int FF_MODERN = 3;
    static final int FF_ROMAN = 1;
    static final int FF_SCRIPT = 4;
    static final int FF_SWISS = 2;
    static final int FIXED_PITCH = 1;
    static final int MARKER_BOLD = 1;
    static final int MARKER_COURIER = 0;
    static final int MARKER_HELVETICA = 4;
    static final int MARKER_ITALIC = 2;
    static final int MARKER_SYMBOL = 12;
    static final int MARKER_TIMES = 8;
    static final int VARIABLE_PITCH = 2;
    static final String[] fontNames = new String[]{"Courier", "Courier-Bold", "Courier-Oblique", "Courier-BoldOblique", "Helvetica", "Helvetica-Bold", "Helvetica-Oblique", "Helvetica-BoldOblique", "Times-Roman", "Times-Bold", "Times-Italic", "Times-BoldItalic", "Symbol", "ZapfDingbats"};
    static final int nameSize = 32;
    float angle;
    int bold;
    int charset;
    String faceName;
    BaseFont font;
    int height;
    int italic;
    int pitchAndFamily;
    boolean strikeout;
    boolean underline;

    public MetaFont() {
        this.faceName = "arial";
        this.font = null;
        this.type = 3;
    }

    public void init(InputMeta in) throws IOException {
        int i;
        boolean z;
        boolean z2 = true;
        this.height = Math.abs(in.readShort());
        in.skip(2);
        this.angle = (float) ((((double) in.readShort()) / 1800.0d) * 3.141592653589793d);
        in.skip(2);
        if (in.readShort() >= 600) {
            i = 1;
        } else {
            i = 0;
        }
        this.bold = i;
        if (in.readByte() != 0) {
            i = 2;
        } else {
            i = 0;
        }
        this.italic = i;
        if (in.readByte() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.underline = z;
        if (in.readByte() == 0) {
            z2 = false;
        }
        this.strikeout = z2;
        this.charset = in.readByte();
        in.skip(3);
        this.pitchAndFamily = in.readByte();
        byte[] name = new byte[32];
        int k = 0;
        while (k < 32) {
            int c = in.readByte();
            if (c != 0) {
                name[k] = (byte) c;
                k++;
            }
        }
        try {
            this.faceName = new String(name, 0, k, "Cp1252");
        } catch (UnsupportedEncodingException e) {
            this.faceName = new String(name, 0, k);
        }
        this.faceName = this.faceName.toLowerCase();
    }

    public BaseFont getFont() {
        int i = 0;
        if (this.font != null) {
            return this.font;
        }
        int i2;
        String str = this.faceName;
        String str2 = "Cp1252";
        if (this.italic != 0) {
            i2 = 2;
        } else {
            i2 = 0;
        }
        if (this.bold != 0) {
            i = 1;
        }
        this.font = FontFactory.getFont(str, str2, true, 10.0f, i2 | i).getBaseFont();
        if (this.font != null) {
            return this.font;
        }
        String fontName;
        if (this.faceName.indexOf("courier") == -1 && this.faceName.indexOf("terminal") == -1 && this.faceName.indexOf("fixedsys") == -1) {
            if (this.faceName.indexOf("ms sans serif") == -1 && this.faceName.indexOf("arial") == -1 && this.faceName.indexOf("system") == -1) {
                if (this.faceName.indexOf("arial black") == -1) {
                    if (this.faceName.indexOf("times") == -1 && this.faceName.indexOf("ms serif") == -1 && this.faceName.indexOf("roman") == -1) {
                        if (this.faceName.indexOf("symbol") == -1) {
                            int pitch = this.pitchAndFamily & 3;
                            switch ((this.pitchAndFamily >> 4) & 7) {
                                case 1:
                                    fontName = fontNames[(this.italic + 8) + this.bold];
                                    break;
                                case 2:
                                case 4:
                                case 5:
                                    fontName = fontNames[(this.italic + 4) + this.bold];
                                    break;
                                case 3:
                                    fontName = fontNames[(this.italic + 0) + this.bold];
                                    break;
                                default:
                                    switch (pitch) {
                                        case 1:
                                            fontName = fontNames[(this.italic + 0) + this.bold];
                                            break;
                                        default:
                                            fontName = fontNames[(this.italic + 4) + this.bold];
                                            break;
                                    }
                            }
                        }
                        fontName = fontNames[12];
                    } else {
                        fontName = fontNames[(this.italic + 8) + this.bold];
                    }
                } else {
                    fontName = fontNames[(this.italic + 4) + 1];
                }
            } else {
                fontName = fontNames[(this.italic + 4) + this.bold];
            }
        } else {
            fontName = fontNames[(this.italic + 0) + this.bold];
        }
        try {
            this.font = BaseFont.createFont(fontName, "Cp1252", false);
            return this.font;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public float getAngle() {
        return this.angle;
    }

    public boolean isUnderline() {
        return this.underline;
    }

    public boolean isStrikeout() {
        return this.strikeout;
    }

    public float getFontSize(MetaState state) {
        return Math.abs(state.transformY(this.height) - state.transformY(0)) * Document.wmfFontCorrection;
    }
}
