package com.itextpdf.text.pdf.collection;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import java.util.Calendar;

public class PdfCollectionItem extends PdfDictionary {
    PdfCollectionSchema schema;

    public PdfCollectionItem(PdfCollectionSchema schema) {
        super(PdfName.COLLECTIONITEM);
        this.schema = schema;
    }

    public void addItem(String key, String value) {
        PdfName fieldname = new PdfName(key);
        put(fieldname, ((PdfCollectionField) this.schema.get(fieldname)).getValue(value));
    }

    public void addItem(String key, PdfString value) {
        PdfName fieldname = new PdfName(key);
        if (((PdfCollectionField) this.schema.get(fieldname)).fieldType == 0) {
            put(fieldname, value);
        }
    }

    public void addItem(String key, PdfDate d) {
        PdfName fieldname = new PdfName(key);
        if (((PdfCollectionField) this.schema.get(fieldname)).fieldType == 1) {
            put(fieldname, d);
        }
    }

    public void addItem(String key, PdfNumber n) {
        PdfName fieldname = new PdfName(key);
        if (((PdfCollectionField) this.schema.get(fieldname)).fieldType == 2) {
            put(fieldname, n);
        }
    }

    public void addItem(String key, Calendar c) {
        addItem(key, new PdfDate(c));
    }

    public void addItem(String key, int i) {
        addItem(key, new PdfNumber(i));
    }

    public void addItem(String key, float f) {
        addItem(key, new PdfNumber(f));
    }

    public void addItem(String key, double d) {
        addItem(key, new PdfNumber(d));
    }

    public void setPrefix(String key, String prefix) {
        PdfName fieldname = new PdfName(key);
        PdfObject o = get(fieldname);
        if (o == null) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.must.set.a.value.before.adding.a.prefix", new Object[0]));
        }
        PdfDictionary dict = new PdfDictionary(PdfName.COLLECTIONSUBITEM);
        dict.put(PdfName.f120D, o);
        dict.put(PdfName.f130P, new PdfString(prefix, PdfObject.TEXT_UNICODE));
        put(fieldname, dict);
    }
}
