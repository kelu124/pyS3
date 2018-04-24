package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;

public class PdfProperties {
    public static final String KEYWORDS = "Keywords";
    public static final String PART = "part";
    public static final String PRODUCER = "Producer";
    public static final String VERSION = "PDFVersion";

    public static void setKeywords(XMPMeta xmpMeta, String keywords) throws XMPException {
        xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", KEYWORDS, keywords);
    }

    public static void setProducer(XMPMeta xmpMeta, String producer) throws XMPException {
        xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", PRODUCER, producer);
    }

    public static void setVersion(XMPMeta xmpMeta, String version) throws XMPException {
        xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", VERSION, version);
    }
}
