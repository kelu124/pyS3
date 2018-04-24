package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.html.HtmlUtilities;

class PdfFont implements Comparable<PdfFont> {
    private BaseFont font;
    protected float hScale = BaseField.BORDER_WIDTH_THIN;
    private float size;

    PdfFont(BaseFont bf, float size) {
        this.size = size;
        this.font = bf;
    }

    public int compareTo(PdfFont pdfFont) {
        if (pdfFont == null) {
            return -1;
        }
        try {
            if (this.font != pdfFont.font) {
                return 1;
            }
            if (size() != pdfFont.size()) {
                return 2;
            }
            return 0;
        } catch (ClassCastException e) {
            return -2;
        }
    }

    float size() {
        return this.size;
    }

    float width() {
        return width(32);
    }

    float width(int character) {
        return this.font.getWidthPoint(character, this.size) * this.hScale;
    }

    float width(String s) {
        return this.font.getWidthPoint(s, this.size) * this.hScale;
    }

    BaseFont getFont() {
        return this.font;
    }

    static PdfFont getDefaultFont() {
        try {
            return new PdfFont(BaseFont.createFont("Helvetica", "Cp1252", false), HtmlUtilities.DEFAULT_FONT_SIZE);
        } catch (Exception ee) {
            throw new ExceptionConverter(ee);
        }
    }

    void setHorizontalScaling(float hScale) {
        this.hScale = hScale;
    }

    float getHorizontalScaling() {
        return this.hScale;
    }
}
