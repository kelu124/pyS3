package com.itextpdf.text.pdf;

public interface PdfPTableEvent {
    void tableLayout(PdfPTable pdfPTable, float[][] fArr, float[] fArr2, int i, int i2, PdfContentByte[] pdfContentByteArr);
}
