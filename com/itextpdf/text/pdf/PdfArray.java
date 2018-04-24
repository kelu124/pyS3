package com.itextpdf.text.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PdfArray extends PdfObject implements Iterable<PdfObject> {
    protected ArrayList<PdfObject> arrayList;

    public PdfArray() {
        super(5);
        this.arrayList = new ArrayList();
    }

    public PdfArray(PdfObject object) {
        super(5);
        this.arrayList = new ArrayList();
        this.arrayList.add(object);
    }

    public PdfArray(float[] values) {
        super(5);
        this.arrayList = new ArrayList();
        add(values);
    }

    public PdfArray(int[] values) {
        super(5);
        this.arrayList = new ArrayList();
        add(values);
    }

    public PdfArray(List<PdfObject> l) {
        this();
        for (PdfObject element : l) {
            add(element);
        }
    }

    public PdfArray(PdfArray array) {
        super(5);
        this.arrayList = new ArrayList(array.arrayList);
    }

    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        PdfObject object;
        PdfWriter.checkPdfIsoConformance(writer, 11, this);
        os.write(91);
        Iterator<PdfObject> i = this.arrayList.iterator();
        if (i.hasNext()) {
            object = (PdfObject) i.next();
            if (object == null) {
                object = PdfNull.PDFNULL;
            }
            object.toPdf(writer, os);
        }
        while (i.hasNext()) {
            object = (PdfObject) i.next();
            if (object == null) {
                object = PdfNull.PDFNULL;
            }
            int type = object.type();
            if (!(type == 5 || type == 6 || type == 4 || type == 3)) {
                os.write(32);
            }
            object.toPdf(writer, os);
        }
        os.write(93);
    }

    public String toString() {
        return this.arrayList.toString();
    }

    public PdfObject set(int idx, PdfObject obj) {
        return (PdfObject) this.arrayList.set(idx, obj);
    }

    public PdfObject remove(int idx) {
        return (PdfObject) this.arrayList.remove(idx);
    }

    @Deprecated
    public ArrayList<PdfObject> getArrayList() {
        return this.arrayList;
    }

    public int size() {
        return this.arrayList.size();
    }

    public boolean isEmpty() {
        return this.arrayList.isEmpty();
    }

    public boolean add(PdfObject object) {
        return this.arrayList.add(object);
    }

    public boolean add(float[] values) {
        for (float pdfNumber : values) {
            this.arrayList.add(new PdfNumber(pdfNumber));
        }
        return true;
    }

    public boolean add(int[] values) {
        for (int pdfNumber : values) {
            this.arrayList.add(new PdfNumber(pdfNumber));
        }
        return true;
    }

    public void add(int index, PdfObject element) {
        this.arrayList.add(index, element);
    }

    public void addFirst(PdfObject object) {
        this.arrayList.add(0, object);
    }

    public boolean contains(PdfObject object) {
        return this.arrayList.contains(object);
    }

    public ListIterator<PdfObject> listIterator() {
        return this.arrayList.listIterator();
    }

    public PdfObject getPdfObject(int idx) {
        return (PdfObject) this.arrayList.get(idx);
    }

    public PdfObject getDirectObject(int idx) {
        return PdfReader.getPdfObject(getPdfObject(idx));
    }

    public PdfDictionary getAsDict(int idx) {
        PdfObject orig = getDirectObject(idx);
        if (orig == null || !orig.isDictionary()) {
            return null;
        }
        return (PdfDictionary) orig;
    }

    public PdfArray getAsArray(int idx) {
        PdfObject orig = getDirectObject(idx);
        if (orig == null || !orig.isArray()) {
            return null;
        }
        return (PdfArray) orig;
    }

    public PdfStream getAsStream(int idx) {
        PdfObject orig = getDirectObject(idx);
        if (orig == null || !orig.isStream()) {
            return null;
        }
        return (PdfStream) orig;
    }

    public PdfString getAsString(int idx) {
        PdfObject orig = getDirectObject(idx);
        if (orig == null || !orig.isString()) {
            return null;
        }
        return (PdfString) orig;
    }

    public PdfNumber getAsNumber(int idx) {
        PdfObject orig = getDirectObject(idx);
        if (orig == null || !orig.isNumber()) {
            return null;
        }
        return (PdfNumber) orig;
    }

    public PdfName getAsName(int idx) {
        PdfObject orig = getDirectObject(idx);
        if (orig == null || !orig.isName()) {
            return null;
        }
        return (PdfName) orig;
    }

    public PdfBoolean getAsBoolean(int idx) {
        PdfObject orig = getDirectObject(idx);
        if (orig == null || !orig.isBoolean()) {
            return null;
        }
        return (PdfBoolean) orig;
    }

    public PdfIndirectReference getAsIndirectObject(int idx) {
        PdfObject orig = getPdfObject(idx);
        if (orig instanceof PdfIndirectReference) {
            return (PdfIndirectReference) orig;
        }
        return null;
    }

    public Iterator<PdfObject> iterator() {
        return this.arrayList.iterator();
    }

    public long[] asLongArray() {
        long[] rslt = new long[size()];
        for (int k = 0; k < rslt.length; k++) {
            rslt[k] = getAsNumber(k).longValue();
        }
        return rslt;
    }
}
