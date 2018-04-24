package com.itextpdf.text.pdf;

import java.io.IOException;

public class PdfMediaClipData extends PdfDictionary {
    PdfMediaClipData(String file, PdfFileSpecification fs, String mimeType) throws IOException {
        put(PdfName.TYPE, new PdfName("MediaClip"));
        put(PdfName.f133S, new PdfName("MCD"));
        put(PdfName.f128N, new PdfString("Media clip for " + file));
        put(new PdfName("CT"), new PdfString(mimeType));
        PdfDictionary dic = new PdfDictionary();
        dic.put(new PdfName("TF"), new PdfString("TEMPACCESS"));
        put(new PdfName("P"), dic);
        put(PdfName.f120D, fs.getReference());
    }
}
