package com.itextpdf.text.pdf.security;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import java.util.ArrayList;
import java.util.List;

public class SignaturePermissions {
    boolean annotationsAllowed = true;
    boolean certification = false;
    List<FieldLock> fieldLocks = new ArrayList();
    boolean fillInAllowed = true;

    public class FieldLock {
        PdfName action;
        PdfArray fields;

        public FieldLock(PdfName action, PdfArray fields) {
            this.action = action;
            this.fields = fields;
        }

        public PdfName getAction() {
            return this.action;
        }

        public PdfArray getFields() {
            return this.fields;
        }

        public String toString() {
            return this.action.toString() + (this.fields == null ? "" : this.fields.toString());
        }
    }

    public SignaturePermissions(PdfDictionary sigDict, SignaturePermissions previous) {
        if (previous != null) {
            this.annotationsAllowed &= previous.isAnnotationsAllowed();
            this.fillInAllowed &= previous.isFillInAllowed();
            this.fieldLocks.addAll(previous.getFieldLocks());
        }
        PdfArray ref = sigDict.getAsArray(PdfName.REFERENCE);
        if (ref != null) {
            for (int i = 0; i < ref.size(); i++) {
                PdfDictionary dict = ref.getAsDict(i);
                PdfDictionary params = dict.getAsDict(PdfName.TRANSFORMPARAMS);
                if (PdfName.DOCMDP.equals(dict.getAsName(PdfName.TRANSFORMMETHOD))) {
                    this.certification = true;
                }
                PdfName action = params.getAsName(PdfName.ACTION);
                if (action != null) {
                    this.fieldLocks.add(new FieldLock(action, params.getAsArray(PdfName.FIELDS)));
                }
                PdfNumber p = params.getAsNumber(PdfName.f130P);
                if (p != null) {
                    switch (p.intValue()) {
                        case 1:
                            this.fillInAllowed &= 0;
                            break;
                        case 2:
                            break;
                        default:
                            continue;
                    }
                    this.annotationsAllowed &= 0;
                }
            }
        }
    }

    public boolean isCertification() {
        return this.certification;
    }

    public boolean isFillInAllowed() {
        return this.fillInAllowed;
    }

    public boolean isAnnotationsAllowed() {
        return this.annotationsAllowed;
    }

    public List<FieldLock> getFieldLocks() {
        return this.fieldLocks;
    }
}
