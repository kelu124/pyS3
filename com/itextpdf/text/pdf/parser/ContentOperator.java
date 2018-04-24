package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfObject;
import java.util.ArrayList;

public interface ContentOperator {
    void invoke(PdfContentStreamProcessor pdfContentStreamProcessor, PdfLiteral pdfLiteral, ArrayList<PdfObject> arrayList) throws Exception;
}
