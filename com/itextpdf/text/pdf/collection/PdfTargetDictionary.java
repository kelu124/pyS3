package com.itextpdf.text.pdf.collection;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;

public class PdfTargetDictionary extends PdfDictionary {
    public PdfTargetDictionary(PdfTargetDictionary nested) {
        put(PdfName.f132R, PdfName.f130P);
        if (nested != null) {
            setAdditionalPath(nested);
        }
    }

    public PdfTargetDictionary(boolean child) {
        if (child) {
            put(PdfName.f132R, PdfName.f119C);
        } else {
            put(PdfName.f132R, PdfName.f130P);
        }
    }

    public void setEmbeddedFileName(String target) {
        put(PdfName.f128N, new PdfString(target, null));
    }

    public void setFileAttachmentPagename(String name) {
        put(PdfName.f130P, new PdfString(name, null));
    }

    public void setFileAttachmentPage(int page) {
        put(PdfName.f130P, new PdfNumber(page));
    }

    public void setFileAttachmentName(String name) {
        put(PdfName.f117A, new PdfString(name, PdfObject.TEXT_UNICODE));
    }

    public void setFileAttachmentIndex(int annotation) {
        put(PdfName.f117A, new PdfNumber(annotation));
    }

    public void setAdditionalPath(PdfTargetDictionary nested) {
        put(PdfName.f134T, nested);
    }
}
