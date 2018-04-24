package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.security.XmlLocator;
import com.itextpdf.text.pdf.security.XpathConstructor;
import java.io.IOException;
import java.security.cert.Certificate;
import java.util.Calendar;

public class XmlSignatureAppearance {
    private String description;
    private String mimeType = "text/xml";
    private Certificate signCertificate;
    private Calendar signDate;
    private PdfStamper stamper;
    private PdfStamperImp writer;
    private XmlLocator xmlLocator;
    private XpathConstructor xpathConstructor;

    XmlSignatureAppearance(PdfStamperImp writer) {
        this.writer = writer;
    }

    public PdfStamperImp getWriter() {
        return this.writer;
    }

    public PdfStamper getStamper() {
        return this.stamper;
    }

    public void setStamper(PdfStamper stamper) {
        this.stamper = stamper;
    }

    public void setCertificate(Certificate signCertificate) {
        this.signCertificate = signCertificate;
    }

    public Certificate getCertificate() {
        return this.signCertificate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Calendar getSignDate() {
        if (this.signDate == null) {
            this.signDate = Calendar.getInstance();
        }
        return this.signDate;
    }

    public void setSignDate(Calendar signDate) {
        this.signDate = signDate;
    }

    public XmlLocator getXmlLocator() {
        return this.xmlLocator;
    }

    public void setXmlLocator(XmlLocator xmlLocator) {
        this.xmlLocator = xmlLocator;
    }

    public XpathConstructor getXpathConstructor() {
        return this.xpathConstructor;
    }

    public void setXpathConstructor(XpathConstructor xpathConstructor) {
        this.xpathConstructor = xpathConstructor;
    }

    public void close() throws IOException, DocumentException {
        this.writer.close(this.stamper.getMoreInfo());
    }
}
