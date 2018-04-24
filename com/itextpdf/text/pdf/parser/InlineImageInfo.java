package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfDictionary;

public class InlineImageInfo {
    private final PdfDictionary imageDictionary;
    private final byte[] samples;

    public InlineImageInfo(byte[] samples, PdfDictionary imageDictionary) {
        this.samples = samples;
        this.imageDictionary = imageDictionary;
    }

    public PdfDictionary getImageDictionary() {
        return this.imageDictionary;
    }

    public byte[] getSamples() {
        return this.samples;
    }
}
