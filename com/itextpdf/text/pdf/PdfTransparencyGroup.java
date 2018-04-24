package com.itextpdf.text.pdf;

public class PdfTransparencyGroup extends PdfDictionary {
    public PdfTransparencyGroup() {
        put(PdfName.f133S, PdfName.TRANSPARENCY);
    }

    public void setIsolated(boolean isolated) {
        if (isolated) {
            put(PdfName.f124I, PdfBoolean.PDFTRUE);
        } else {
            remove(PdfName.f124I);
        }
    }

    public void setKnockout(boolean knockout) {
        if (knockout) {
            put(PdfName.f125K, PdfBoolean.PDFTRUE);
        } else {
            remove(PdfName.f125K);
        }
    }
}
