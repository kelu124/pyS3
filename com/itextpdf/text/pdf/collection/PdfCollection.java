package com.itextpdf.text.pdf.collection;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;

public class PdfCollection extends PdfDictionary {
    public static final int CUSTOM = 3;
    public static final int DETAILS = 0;
    public static final int HIDDEN = 2;
    public static final int TILE = 1;

    public PdfCollection(int type) {
        super(PdfName.COLLECTION);
        switch (type) {
            case 1:
                put(PdfName.VIEW, PdfName.f134T);
                return;
            case 2:
                put(PdfName.VIEW, PdfName.f123H);
                return;
            case 3:
                put(PdfName.VIEW, PdfName.f119C);
                return;
            default:
                put(PdfName.VIEW, PdfName.f120D);
                return;
        }
    }

    public void setInitialDocument(String description) {
        put(PdfName.f120D, new PdfString(description, null));
    }

    public void setSchema(PdfCollectionSchema schema) {
        put(PdfName.SCHEMA, schema);
    }

    public PdfCollectionSchema getSchema() {
        return (PdfCollectionSchema) get(PdfName.SCHEMA);
    }

    public void setSort(PdfCollectionSort sort) {
        put(PdfName.SORT, sort);
    }
}
