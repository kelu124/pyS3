package com.itextpdf.text.pdf;

public interface ICachedColorSpace {
    boolean equals(Object obj);

    PdfObject getPdfObject(PdfWriter pdfWriter);

    int hashCode();
}
