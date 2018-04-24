package com.itextpdf.text.pdf.security;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;

public class PdfSignatureBuildProperties extends PdfDictionary {
    public void setSignatureCreator(String name) {
        getPdfSignatureAppProperty().setSignatureCreator(name);
    }

    PdfSignatureAppDictionary getPdfSignatureAppProperty() {
        PdfSignatureAppDictionary appPropDic = (PdfSignatureAppDictionary) getAsDict(PdfName.APP);
        if (appPropDic != null) {
            return appPropDic;
        }
        appPropDic = new PdfSignatureAppDictionary();
        put(PdfName.APP, appPropDic);
        return appPropDic;
    }
}
