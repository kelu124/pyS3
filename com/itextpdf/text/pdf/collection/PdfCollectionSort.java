package com.itextpdf.text.pdf.collection;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;

public class PdfCollectionSort extends PdfDictionary {
    public PdfCollectionSort(String key) {
        super(PdfName.COLLECTIONSORT);
        put(PdfName.f133S, new PdfName(key));
    }

    public PdfCollectionSort(String[] keys) {
        super(PdfName.COLLECTIONSORT);
        PdfArray array = new PdfArray();
        for (String pdfName : keys) {
            array.add(new PdfName(pdfName));
        }
        put(PdfName.f133S, array);
    }

    public void setSortOrder(boolean ascending) {
        if (get(PdfName.f133S) instanceof PdfName) {
            put(PdfName.f117A, new PdfBoolean(ascending));
            return;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.have.to.define.a.boolean.array.for.this.collection.sort.dictionary", new Object[0]));
    }

    public void setSortOrder(boolean[] ascending) {
        PdfObject o = get(PdfName.f133S);
        if (!(o instanceof PdfArray)) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.need.a.single.boolean.for.this.collection.sort.dictionary", new Object[0]));
        } else if (((PdfArray) o).size() != ascending.length) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.number.of.booleans.in.this.array.doesn.t.correspond.with.the.number.of.fields", new Object[0]));
        } else {
            PdfArray array = new PdfArray();
            for (boolean pdfBoolean : ascending) {
                array.add(new PdfBoolean(pdfBoolean));
            }
            put(PdfName.f117A, array);
        }
    }
}
