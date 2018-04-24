package com.itextpdf.text.pdf;

public interface PdfPTableEventAfterSplit extends PdfPTableEventSplit {
    void afterSplitTable(PdfPTable pdfPTable, PdfPRow pdfPRow, int i);
}
