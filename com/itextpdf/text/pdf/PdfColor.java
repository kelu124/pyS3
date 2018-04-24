package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;

class PdfColor extends PdfArray {
    PdfColor(int red, int green, int blue) {
        super(new PdfNumber(((double) (red & 255)) / 255.0d));
        add(new PdfNumber(((double) (green & 255)) / 255.0d));
        add(new PdfNumber(((double) (blue & 255)) / 255.0d));
    }

    PdfColor(BaseColor color) {
        this(color.getRed(), color.getGreen(), color.getBlue());
    }
}
