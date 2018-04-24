package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;

public class PdfBorderDictionary extends PdfDictionary {
    public static final int STYLE_BEVELED = 2;
    public static final int STYLE_DASHED = 1;
    public static final int STYLE_INSET = 3;
    public static final int STYLE_SOLID = 0;
    public static final int STYLE_UNDERLINE = 4;

    public PdfBorderDictionary(float borderWidth, int borderStyle, PdfDashPattern dashes) {
        put(PdfName.f137W, new PdfNumber(borderWidth));
        switch (borderStyle) {
            case 0:
                put(PdfName.f133S, PdfName.f133S);
                return;
            case 1:
                if (dashes != null) {
                    put(PdfName.f120D, dashes);
                }
                put(PdfName.f133S, PdfName.f120D);
                return;
            case 2:
                put(PdfName.f133S, PdfName.f118B);
                return;
            case 3:
                put(PdfName.f133S, PdfName.f124I);
                return;
            case 4:
                put(PdfName.f133S, PdfName.f135U);
                return;
            default:
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.border.style", new Object[0]));
        }
    }

    public PdfBorderDictionary(float borderWidth, int borderStyle) {
        this(borderWidth, borderStyle, null);
    }
}
