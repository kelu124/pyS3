package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;

public class PdfPattern extends PdfStream {
    PdfPattern(PdfPatternPainter painter) {
        this(painter, -1);
    }

    PdfPattern(PdfPatternPainter painter, int compressionLevel) {
        PdfNumber one = new PdfNumber(1);
        PdfArray matrix = painter.getMatrix();
        if (matrix != null) {
            put(PdfName.MATRIX, matrix);
        }
        put(PdfName.TYPE, PdfName.PATTERN);
        put(PdfName.BBOX, new PdfRectangle(painter.getBoundingBox()));
        put(PdfName.RESOURCES, painter.getResources());
        put(PdfName.TILINGTYPE, one);
        put(PdfName.PATTERNTYPE, one);
        if (painter.isStencil()) {
            put(PdfName.PAINTTYPE, new PdfNumber(2));
        } else {
            put(PdfName.PAINTTYPE, one);
        }
        put(PdfName.XSTEP, new PdfNumber(painter.getXStep()));
        put(PdfName.YSTEP, new PdfNumber(painter.getYStep()));
        this.bytes = painter.toPdf(null);
        put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
        try {
            flateCompress(compressionLevel);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
}
