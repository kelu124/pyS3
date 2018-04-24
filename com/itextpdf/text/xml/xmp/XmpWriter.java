package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.Version;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.XMPMetaFactory;
import com.itextpdf.xmp.XMPUtils;
import com.itextpdf.xmp.options.PropertyOptions;
import com.itextpdf.xmp.options.SerializeOptions;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

public class XmpWriter {
    public static final String UTF16 = "UTF-16";
    public static final String UTF16BE = "UTF-16BE";
    public static final String UTF16LE = "UTF-16LE";
    public static final String UTF8 = "UTF-8";
    protected OutputStream outputStream;
    protected SerializeOptions serializeOptions;
    protected XMPMeta xmpMeta;

    public XmpWriter(OutputStream os, String utfEncoding, int extraSpace) throws IOException {
        this.outputStream = os;
        this.serializeOptions = new SerializeOptions();
        if (UTF16BE.equals(utfEncoding) || UTF16.equals(utfEncoding)) {
            this.serializeOptions.setEncodeUTF16BE(true);
        } else if (UTF16LE.equals(utfEncoding)) {
            this.serializeOptions.setEncodeUTF16LE(true);
        }
        this.serializeOptions.setPadding(extraSpace);
        this.xmpMeta = XMPMetaFactory.create();
        this.xmpMeta.setObjectName(XMPConst.TAG_XMPMETA);
        this.xmpMeta.setObjectName("");
        try {
            this.xmpMeta.setProperty("http://purl.org/dc/elements/1.1/", DublinCoreProperties.FORMAT, "application/pdf");
            this.xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", PdfProperties.PRODUCER, Version.getInstance().getVersion());
        } catch (XMPException e) {
        }
    }

    public XmpWriter(OutputStream os) throws IOException {
        this(os, UTF8, 2000);
    }

    public XmpWriter(OutputStream os, PdfDictionary info) throws IOException {
        this(os);
        if (info != null) {
            for (PdfName key : info.getKeys()) {
                PdfObject obj = info.get(key);
                if (obj != null && obj.isString()) {
                    try {
                        addDocInfoProperty(key, ((PdfString) obj).toUnicodeString());
                    } catch (XMPException xmpExc) {
                        throw new IOException(xmpExc.getMessage());
                    }
                }
            }
        }
    }

    public XmpWriter(OutputStream os, Map<String, String> info) throws IOException {
        this(os);
        if (info != null) {
            for (Entry<String, String> entry : info.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (value != null) {
                    try {
                        addDocInfoProperty(key, value);
                    } catch (XMPException xmpExc) {
                        throw new IOException(xmpExc.getMessage());
                    }
                }
            }
        }
    }

    public XMPMeta getXmpMeta() {
        return this.xmpMeta;
    }

    public void setReadOnly() {
        this.serializeOptions.setReadOnlyPacket(true);
    }

    public void setAbout(String about) {
        this.xmpMeta.setObjectName(about);
    }

    @Deprecated
    public void addRdfDescription(String xmlns, String content) throws IOException {
        try {
            XMPUtils.appendProperties(XMPMetaFactory.parseFromString("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"><rdf:Description rdf:about=\"" + this.xmpMeta.getObjectName() + "\" " + xmlns + ">" + content + "</rdf:Description></rdf:RDF>\n"), this.xmpMeta, true, true);
        } catch (XMPException xmpExc) {
            throw new IOException(xmpExc.getMessage());
        }
    }

    @Deprecated
    public void addRdfDescription(XmpSchema s) throws IOException {
        try {
            XMPUtils.appendProperties(XMPMetaFactory.parseFromString("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"><rdf:Description rdf:about=\"" + this.xmpMeta.getObjectName() + "\" " + s.getXmlns() + ">" + s.toString() + "</rdf:Description></rdf:RDF>\n"), this.xmpMeta, true, true);
        } catch (XMPException xmpExc) {
            throw new IOException(xmpExc.getMessage());
        }
    }

    public void setProperty(String schemaNS, String propName, Object value) throws XMPException {
        this.xmpMeta.setProperty(schemaNS, propName, value);
    }

    public void appendArrayItem(String schemaNS, String arrayName, String value) throws XMPException {
        this.xmpMeta.appendArrayItem(schemaNS, arrayName, new PropertyOptions(512), value, null);
    }

    public void appendOrderedArrayItem(String schemaNS, String arrayName, String value) throws XMPException {
        this.xmpMeta.appendArrayItem(schemaNS, arrayName, new PropertyOptions(1024), value, null);
    }

    public void appendAlternateArrayItem(String schemaNS, String arrayName, String value) throws XMPException {
        this.xmpMeta.appendArrayItem(schemaNS, arrayName, new PropertyOptions(2048), value, null);
    }

    public void serialize(OutputStream externalOutputStream) throws XMPException {
        XMPMetaFactory.serialize(this.xmpMeta, externalOutputStream, this.serializeOptions);
    }

    public void close() throws IOException {
        if (this.outputStream != null) {
            try {
                XMPMetaFactory.serialize(this.xmpMeta, this.outputStream, this.serializeOptions);
                this.outputStream = null;
            } catch (XMPException xmpExc) {
                throw new IOException(xmpExc.getMessage());
            }
        }
    }

    public void addDocInfoProperty(Object key, String value) throws XMPException {
        if (key instanceof String) {
            key = new PdfName((String) key);
        }
        if (PdfName.TITLE.equals(key)) {
            this.xmpMeta.setLocalizedText("http://purl.org/dc/elements/1.1/", "title", "x-default", "x-default", value);
        } else if (PdfName.AUTHOR.equals(key)) {
            this.xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", DublinCoreProperties.CREATOR, new PropertyOptions(1024), value, null);
        } else if (PdfName.SUBJECT.equals(key)) {
            this.xmpMeta.setLocalizedText("http://purl.org/dc/elements/1.1/", DublinCoreProperties.DESCRIPTION, "x-default", "x-default", value);
        } else if (PdfName.KEYWORDS.equals(key)) {
            for (String v : value.split(",|;")) {
                if (v.trim().length() > 0) {
                    this.xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "subject", new PropertyOptions(512), v.trim(), null);
                }
            }
            this.xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", PdfProperties.KEYWORDS, value);
        } else if (PdfName.PRODUCER.equals(key)) {
            this.xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", PdfProperties.PRODUCER, value);
        } else if (PdfName.CREATOR.equals(key)) {
            this.xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", XmpBasicProperties.CREATORTOOL, value);
        } else if (PdfName.CREATIONDATE.equals(key)) {
            this.xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", XmpBasicProperties.CREATEDATE, PdfDate.getW3CDate(value));
        } else if (PdfName.MODDATE.equals(key)) {
            this.xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", XmpBasicProperties.MODIFYDATE, PdfDate.getW3CDate(value));
        }
    }
}
