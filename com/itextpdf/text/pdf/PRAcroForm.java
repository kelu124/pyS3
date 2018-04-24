package com.itextpdf.text.pdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PRAcroForm extends PdfDictionary {
    HashMap<String, FieldInformation> fieldByName = new HashMap();
    ArrayList<FieldInformation> fields = new ArrayList();
    PdfReader reader;
    ArrayList<PdfDictionary> stack = new ArrayList();

    public static class FieldInformation {
        String fieldName;
        PdfDictionary info;
        PRIndirectReference ref;

        FieldInformation(String fieldName, PdfDictionary info, PRIndirectReference ref) {
            this.fieldName = fieldName;
            this.info = info;
            this.ref = ref;
        }

        public String getWidgetName() {
            PdfObject name = this.info.get(PdfName.NM);
            if (name != null) {
                return name.toString();
            }
            return null;
        }

        public String getName() {
            return this.fieldName;
        }

        public PdfDictionary getInfo() {
            return this.info;
        }

        public PRIndirectReference getRef() {
            return this.ref;
        }
    }

    public PRAcroForm(PdfReader reader) {
        this.reader = reader;
    }

    public int size() {
        return this.fields.size();
    }

    public ArrayList<FieldInformation> getFields() {
        return this.fields;
    }

    public FieldInformation getField(String name) {
        return (FieldInformation) this.fieldByName.get(name);
    }

    public PRIndirectReference getRefByName(String name) {
        FieldInformation fi = (FieldInformation) this.fieldByName.get(name);
        if (fi == null) {
            return null;
        }
        return fi.getRef();
    }

    public void readAcroForm(PdfDictionary root) {
        if (root != null) {
            this.hashMap = root.hashMap;
            pushAttrib(root);
            PdfArray fieldlist = (PdfArray) PdfReader.getPdfObjectRelease(root.get(PdfName.FIELDS));
            if (fieldlist != null) {
                iterateFields(fieldlist, null, null);
            }
        }
    }

    protected void iterateFields(PdfArray fieldlist, PRIndirectReference fieldDict, String parentPath) {
        Iterator<PdfObject> it = fieldlist.listIterator();
        while (it.hasNext()) {
            PdfObject ref = (PRIndirectReference) it.next();
            PdfDictionary dict = (PdfDictionary) PdfReader.getPdfObjectRelease(ref);
            PRIndirectReference myFieldDict = fieldDict;
            String fullPath = parentPath;
            PdfString tField = (PdfString) dict.get(PdfName.f134T);
            boolean isFieldDict = tField != null;
            if (isFieldDict) {
                myFieldDict = ref;
                if (parentPath == null) {
                    fullPath = tField.toString();
                } else {
                    fullPath = parentPath + '.' + tField.toString();
                }
            }
            PdfArray kids = (PdfArray) dict.get(PdfName.KIDS);
            if (kids != null) {
                pushAttrib(dict);
                iterateFields(kids, myFieldDict, fullPath);
                this.stack.remove(this.stack.size() - 1);
            } else if (myFieldDict != null) {
                PdfDictionary mergedDict = (PdfDictionary) this.stack.get(this.stack.size() - 1);
                if (isFieldDict) {
                    mergedDict = mergeAttrib(mergedDict, dict);
                }
                mergedDict.put(PdfName.f134T, new PdfString(fullPath));
                FieldInformation fi = new FieldInformation(fullPath, mergedDict, myFieldDict);
                this.fields.add(fi);
                this.fieldByName.put(fullPath, fi);
            }
        }
    }

    protected PdfDictionary mergeAttrib(PdfDictionary parent, PdfDictionary child) {
        PdfDictionary targ = new PdfDictionary();
        if (parent != null) {
            targ.putAll(parent);
        }
        for (PdfName key : child.getKeys()) {
            if (key.equals(PdfName.DR) || key.equals(PdfName.DA) || key.equals(PdfName.f131Q) || key.equals(PdfName.FF) || key.equals(PdfName.DV) || key.equals(PdfName.f136V) || key.equals(PdfName.FT) || key.equals(PdfName.NM) || key.equals(PdfName.f122F)) {
                targ.put(key, child.get(key));
            }
        }
        return targ;
    }

    protected void pushAttrib(PdfDictionary dict) {
        PdfDictionary dic = null;
        if (!this.stack.isEmpty()) {
            dic = (PdfDictionary) this.stack.get(this.stack.size() - 1);
        }
        this.stack.add(mergeAttrib(dic, dict));
    }
}
