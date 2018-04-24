package com.itextpdf.text.pdf;

interface PdfPageElement {
    boolean isParent();

    void setParent(PdfIndirectReference pdfIndirectReference);
}
