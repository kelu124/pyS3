package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfStream;

public interface XObjectDoHandler {
    void handleXObject(PdfContentStreamProcessor pdfContentStreamProcessor, PdfStream pdfStream, PdfIndirectReference pdfIndirectReference);
}
