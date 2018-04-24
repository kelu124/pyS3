package com.itextpdf.text.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class PdfDictionary extends PdfObject {
    public static final PdfName CATALOG = PdfName.CATALOG;
    public static final PdfName FONT = PdfName.FONT;
    public static final PdfName OUTLINES = PdfName.OUTLINES;
    public static final PdfName PAGE = PdfName.PAGE;
    public static final PdfName PAGES = PdfName.PAGES;
    private PdfName dictionaryType;
    protected HashMap<PdfName, PdfObject> hashMap;

    public PdfDictionary() {
        super(6);
        this.dictionaryType = null;
        this.hashMap = new HashMap();
    }

    public PdfDictionary(PdfName type) {
        this();
        this.dictionaryType = type;
        put(PdfName.TYPE, this.dictionaryType);
    }

    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        PdfWriter.checkPdfIsoConformance(writer, 11, this);
        os.write(60);
        os.write(60);
        for (Entry<PdfName, PdfObject> e : this.hashMap.entrySet()) {
            ((PdfName) e.getKey()).toPdf(writer, os);
            PdfObject value = (PdfObject) e.getValue();
            int type = value.type();
            if (!(type == 5 || type == 6 || type == 4 || type == 3)) {
                os.write(32);
            }
            value.toPdf(writer, os);
        }
        os.write(62);
        os.write(62);
    }

    public String toString() {
        if (get(PdfName.TYPE) == null) {
            return "Dictionary";
        }
        return "Dictionary of type: " + get(PdfName.TYPE);
    }

    public void put(PdfName key, PdfObject object) {
        if (object == null || object.isNull()) {
            this.hashMap.remove(key);
        } else {
            this.hashMap.put(key, object);
        }
    }

    public void putEx(PdfName key, PdfObject value) {
        if (value != null) {
            put(key, value);
        }
    }

    public void putAll(PdfDictionary dic) {
        this.hashMap.putAll(dic.hashMap);
    }

    public void remove(PdfName key) {
        this.hashMap.remove(key);
    }

    public void clear() {
        this.hashMap.clear();
    }

    public PdfObject get(PdfName key) {
        return (PdfObject) this.hashMap.get(key);
    }

    public PdfObject getDirectObject(PdfName key) {
        return PdfReader.getPdfObject(get(key));
    }

    public Set<PdfName> getKeys() {
        return this.hashMap.keySet();
    }

    public int size() {
        return this.hashMap.size();
    }

    public boolean contains(PdfName key) {
        return this.hashMap.containsKey(key);
    }

    public boolean isFont() {
        return checkType(FONT);
    }

    public boolean isPage() {
        return checkType(PAGE);
    }

    public boolean isPages() {
        return checkType(PAGES);
    }

    public boolean isCatalog() {
        return checkType(CATALOG);
    }

    public boolean isOutlineTree() {
        return checkType(OUTLINES);
    }

    public boolean checkType(PdfName type) {
        if (type == null) {
            return false;
        }
        if (this.dictionaryType == null) {
            this.dictionaryType = getAsName(PdfName.TYPE);
        }
        return type.equals(this.dictionaryType);
    }

    public void merge(PdfDictionary other) {
        this.hashMap.putAll(other.hashMap);
    }

    public void mergeDifferent(PdfDictionary other) {
        for (PdfName key : other.hashMap.keySet()) {
            if (!this.hashMap.containsKey(key)) {
                this.hashMap.put(key, other.hashMap.get(key));
            }
        }
    }

    public PdfDictionary getAsDict(PdfName key) {
        PdfObject orig = getDirectObject(key);
        if (orig == null || !orig.isDictionary()) {
            return null;
        }
        return (PdfDictionary) orig;
    }

    public PdfArray getAsArray(PdfName key) {
        PdfObject orig = getDirectObject(key);
        if (orig == null || !orig.isArray()) {
            return null;
        }
        return (PdfArray) orig;
    }

    public PdfStream getAsStream(PdfName key) {
        PdfObject orig = getDirectObject(key);
        if (orig == null || !orig.isStream()) {
            return null;
        }
        return (PdfStream) orig;
    }

    public PdfString getAsString(PdfName key) {
        PdfObject orig = getDirectObject(key);
        if (orig == null || !orig.isString()) {
            return null;
        }
        return (PdfString) orig;
    }

    public PdfNumber getAsNumber(PdfName key) {
        PdfObject orig = getDirectObject(key);
        if (orig == null || !orig.isNumber()) {
            return null;
        }
        return (PdfNumber) orig;
    }

    public PdfName getAsName(PdfName key) {
        PdfObject orig = getDirectObject(key);
        if (orig == null || !orig.isName()) {
            return null;
        }
        return (PdfName) orig;
    }

    public PdfBoolean getAsBoolean(PdfName key) {
        PdfObject orig = getDirectObject(key);
        if (orig == null || !orig.isBoolean()) {
            return null;
        }
        return (PdfBoolean) orig;
    }

    public PdfIndirectReference getAsIndirectObject(PdfName key) {
        PdfObject orig = get(key);
        if (orig == null || !orig.isIndirect()) {
            return null;
        }
        return (PdfIndirectReference) orig;
    }
}
