package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.Version;

@Deprecated
public class PdfSchema extends XmpSchema {
    public static final String DEFAULT_XPATH_ID = "pdf";
    public static final String DEFAULT_XPATH_URI = "http://ns.adobe.com/pdf/1.3/";
    public static final String KEYWORDS = "pdf:Keywords";
    public static final String PRODUCER = "pdf:Producer";
    public static final String VERSION = "pdf:PDFVersion";
    private static final long serialVersionUID = -1541148669123992185L;

    public PdfSchema() {
        super("xmlns:pdf=\"http://ns.adobe.com/pdf/1.3/\"");
        addProducer(Version.getInstance().getVersion());
    }

    public void addKeywords(String keywords) {
        setProperty(KEYWORDS, keywords);
    }

    public void addProducer(String producer) {
        setProperty(PRODUCER, producer);
    }

    public void addVersion(String version) {
        setProperty(VERSION, version);
    }
}
