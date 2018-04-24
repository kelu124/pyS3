package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;

public class MarkedContentInfo {
    private final PdfDictionary dictionary;
    private final PdfName tag;

    public MarkedContentInfo(PdfName tag, PdfDictionary dictionary) {
        this.tag = tag;
        if (dictionary == null) {
            dictionary = new PdfDictionary();
        }
        this.dictionary = dictionary;
    }

    public PdfName getTag() {
        return this.tag;
    }

    public boolean hasMcid() {
        return this.dictionary.contains(PdfName.MCID);
    }

    public int getMcid() {
        PdfNumber id = this.dictionary.getAsNumber(PdfName.MCID);
        if (id != null) {
            return id.intValue();
        }
        throw new IllegalStateException("MarkedContentInfo does not contain MCID");
    }
}
