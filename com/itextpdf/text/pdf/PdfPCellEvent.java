package com.itextpdf.text.pdf;

import com.itextpdf.text.Rectangle;

public interface PdfPCellEvent {
    void cellLayout(PdfPCell pdfPCell, Rectangle rectangle, PdfContentByte[] pdfContentByteArr);
}
