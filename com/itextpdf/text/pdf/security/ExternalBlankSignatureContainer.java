package com.itextpdf.text.pdf.security;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public class ExternalBlankSignatureContainer implements ExternalSignatureContainer {
    private PdfDictionary sigDic;

    public ExternalBlankSignatureContainer(PdfDictionary sigDic) {
        this.sigDic = sigDic;
    }

    public ExternalBlankSignatureContainer(PdfName filter, PdfName subFilter) {
        this.sigDic = new PdfDictionary();
        this.sigDic.put(PdfName.FILTER, filter);
        this.sigDic.put(PdfName.SUBFILTER, subFilter);
    }

    public byte[] sign(InputStream data) throws GeneralSecurityException {
        return new byte[0];
    }

    public void modifySigningDictionary(PdfDictionary signDic) {
        signDic.putAll(this.sigDic);
    }
}
