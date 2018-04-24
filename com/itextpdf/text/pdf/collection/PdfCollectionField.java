package com.itextpdf.text.pdf.collection;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;

public class PdfCollectionField extends PdfDictionary {
    public static final int CREATIONDATE = 6;
    public static final int DATE = 1;
    public static final int DESC = 4;
    public static final int FILENAME = 3;
    public static final int MODDATE = 5;
    public static final int NUMBER = 2;
    public static final int SIZE = 7;
    public static final int TEXT = 0;
    protected int fieldType;

    public PdfCollectionField(String name, int type) {
        super(PdfName.COLLECTIONFIELD);
        put(PdfName.f128N, new PdfString(name, PdfObject.TEXT_UNICODE));
        this.fieldType = type;
        switch (type) {
            case 1:
                put(PdfName.SUBTYPE, PdfName.f120D);
                return;
            case 2:
                put(PdfName.SUBTYPE, PdfName.f128N);
                return;
            case 3:
                put(PdfName.SUBTYPE, PdfName.f122F);
                return;
            case 4:
                put(PdfName.SUBTYPE, PdfName.DESC);
                return;
            case 5:
                put(PdfName.SUBTYPE, PdfName.MODDATE);
                return;
            case 6:
                put(PdfName.SUBTYPE, PdfName.CREATIONDATE);
                return;
            case 7:
                put(PdfName.SUBTYPE, PdfName.SIZE);
                return;
            default:
                put(PdfName.SUBTYPE, PdfName.f133S);
                return;
        }
    }

    public void setOrder(int i) {
        put(PdfName.f129O, new PdfNumber(i));
    }

    public void setVisible(boolean visible) {
        put(PdfName.f136V, new PdfBoolean(visible));
    }

    public void setEditable(boolean editable) {
        put(PdfName.f121E, new PdfBoolean(editable));
    }

    public boolean isCollectionItem() {
        switch (this.fieldType) {
            case 0:
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    public PdfObject getValue(String v) {
        switch (this.fieldType) {
            case 0:
                return new PdfString(v, PdfObject.TEXT_UNICODE);
            case 1:
                return new PdfDate(PdfDate.decode(v));
            case 2:
                return new PdfNumber(v);
            default:
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.is.not.an.acceptable.value.for.the.field.2", v, get(PdfName.f128N).toString()));
        }
    }
}
