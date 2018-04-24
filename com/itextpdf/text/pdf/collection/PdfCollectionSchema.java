package com.itextpdf.text.pdf.collection;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;

public class PdfCollectionSchema extends PdfDictionary {
    public PdfCollectionSchema() {
        super(PdfName.COLLECTIONSCHEMA);
    }

    public void addField(String name, PdfCollectionField field) {
        put(new PdfName(name), field);
    }
}
