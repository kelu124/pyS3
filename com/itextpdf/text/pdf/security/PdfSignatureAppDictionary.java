package com.itextpdf.text.pdf.security;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;

public class PdfSignatureAppDictionary extends PdfDictionary {
    public void setSignatureCreator(String name) {
        put(PdfName.NAME, new PdfName(name));
    }
}
