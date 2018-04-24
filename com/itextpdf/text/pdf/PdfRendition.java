package com.itextpdf.text.pdf;

import java.io.IOException;

public class PdfRendition extends PdfDictionary {
    PdfRendition(String file, PdfFileSpecification fs, String mimeType) throws IOException {
        put(PdfName.f133S, new PdfName("MR"));
        put(PdfName.f128N, new PdfString("Rendition for " + file));
        put(PdfName.f119C, new PdfMediaClipData(file, fs, mimeType));
    }
}
