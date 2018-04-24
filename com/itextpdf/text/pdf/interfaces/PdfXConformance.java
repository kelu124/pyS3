package com.itextpdf.text.pdf.interfaces;

public interface PdfXConformance extends PdfIsoConformance {
    int getPDFXConformance();

    boolean isPdfX();

    void setPDFXConformance(int i);
}
