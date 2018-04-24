package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.security.PdfSignatureBuildProperties;

public class PdfSignature extends PdfDictionary {
    public PdfSignature(PdfName filter, PdfName subFilter) {
        super(PdfName.SIG);
        put(PdfName.FILTER, filter);
        put(PdfName.SUBFILTER, subFilter);
    }

    public void setByteRange(int[] range) {
        PdfArray array = new PdfArray();
        for (int pdfNumber : range) {
            array.add(new PdfNumber(pdfNumber));
        }
        put(PdfName.BYTERANGE, array);
    }

    public void setContents(byte[] contents) {
        put(PdfName.CONTENTS, new PdfString(contents).setHexWriting(true));
    }

    public void setCert(byte[] cert) {
        put(PdfName.CERT, new PdfString(cert));
    }

    public void setName(String name) {
        put(PdfName.NAME, new PdfString(name, PdfObject.TEXT_UNICODE));
    }

    public void setDate(PdfDate date) {
        put(PdfName.f127M, date);
    }

    public void setLocation(String name) {
        put(PdfName.LOCATION, new PdfString(name, PdfObject.TEXT_UNICODE));
    }

    public void setReason(String name) {
        put(PdfName.REASON, new PdfString(name, PdfObject.TEXT_UNICODE));
    }

    public void setSignatureCreator(String name) {
        if (name != null) {
            getPdfSignatureBuildProperties().setSignatureCreator(name);
        }
    }

    PdfSignatureBuildProperties getPdfSignatureBuildProperties() {
        PdfSignatureBuildProperties buildPropDic = (PdfSignatureBuildProperties) getAsDict(PdfName.PROP_BUILD);
        if (buildPropDic != null) {
            return buildPropDic;
        }
        buildPropDic = new PdfSignatureBuildProperties();
        put(PdfName.PROP_BUILD, buildPropDic);
        return buildPropDic;
    }

    public void setContact(String name) {
        put(PdfName.CONTACTINFO, new PdfString(name, PdfObject.TEXT_UNICODE));
    }
}
