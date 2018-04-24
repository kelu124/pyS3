package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.xml.XMLUtil;
import com.itextpdf.text.xml.xmp.XmpWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class TaggedPdfReaderTool {
    protected PrintWriter out;
    protected PdfReader reader;

    public void convertToXml(PdfReader reader, OutputStream os, String charset) throws IOException {
        this.reader = reader;
        this.out = new PrintWriter(new OutputStreamWriter(os, charset));
        PdfDictionary struct = reader.getCatalog().getAsDict(PdfName.STRUCTTREEROOT);
        if (struct == null) {
            throw new IOException(MessageLocalization.getComposedMessage("no.structtreeroot.found", new Object[0]));
        }
        inspectChild(struct.getDirectObject(PdfName.f125K));
        this.out.flush();
        this.out.close();
    }

    public void convertToXml(PdfReader reader, OutputStream os) throws IOException {
        convertToXml(reader, os, XmpWriter.UTF8);
    }

    public void inspectChild(PdfObject k) throws IOException {
        if (k != null) {
            if (k instanceof PdfArray) {
                inspectChildArray((PdfArray) k);
            } else if (k instanceof PdfDictionary) {
                inspectChildDictionary((PdfDictionary) k);
            }
        }
    }

    public void inspectChildArray(PdfArray k) throws IOException {
        if (k != null) {
            for (int i = 0; i < k.size(); i++) {
                inspectChild(k.getDirectObject(i));
            }
        }
    }

    public void inspectChildDictionary(PdfDictionary k) throws IOException {
        inspectChildDictionary(k, false);
    }

    public void inspectChildDictionary(PdfDictionary k, boolean inspectAttributes) throws IOException {
        if (k != null) {
            PdfName s = k.getAsName(PdfName.f133S);
            if (s != null) {
                String tagN = PdfName.decodeName(s.toString());
                String tag = fixTagName(tagN);
                this.out.print("<");
                this.out.print(tag);
                if (inspectAttributes) {
                    PdfDictionary a = k.getAsDict(PdfName.f117A);
                    if (a != null) {
                        for (PdfName key : a.getKeys()) {
                            this.out.print(' ');
                            PdfObject value = PdfReader.getPdfObject(a.get(key));
                            this.out.print(xmlName(key));
                            this.out.print("=\"");
                            this.out.print(value.toString());
                            this.out.print("\"");
                        }
                    }
                }
                this.out.print(">");
                PdfObject alt = k.get(PdfName.ALT);
                if (!(alt == null || alt.toString() == null)) {
                    this.out.print("<alt><![CDATA[");
                    this.out.print(alt.toString().replaceAll("[\\000]*", ""));
                    this.out.print("]]></alt>");
                }
                PdfDictionary dict = k.getAsDict(PdfName.PG);
                if (dict != null) {
                    parseTag(tagN, k.getDirectObject(PdfName.f125K), dict);
                }
                inspectChild(k.getDirectObject(PdfName.f125K));
                this.out.print("</");
                this.out.print(tag);
                this.out.println(">");
                return;
            }
            inspectChild(k.getDirectObject(PdfName.f125K));
        }
    }

    protected String xmlName(PdfName name) {
        String xmlName = name.toString().replaceFirst("/", "");
        return Character.toLowerCase(xmlName.charAt(0)) + xmlName.substring(1);
    }

    private static String fixTagName(String tag) {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < tag.length(); k++) {
            boolean nameStart;
            boolean nameMiddle;
            char c = tag.charAt(k);
            if (c == ':' || ((c >= 'A' && c <= 'Z') || c == '_' || ((c >= 'a' && c <= 'z') || ((c >= 'À' && c <= 'Ö') || ((c >= 'Ø' && c <= 'ö') || ((c >= 'ø' && c <= '˿') || ((c >= 'Ͱ' && c <= 'ͽ') || ((c >= 'Ϳ' && c <= '῿') || ((c >= '‌' && c <= '‍') || ((c >= '⁰' && c <= '↏') || ((c >= 'Ⰰ' && c <= '⿯') || ((c >= '、' && c <= '퟿') || ((c >= '豈' && c <= '﷏') || (c >= 'ﷰ' && c <= '�')))))))))))))) {
                nameStart = true;
            } else {
                nameStart = false;
            }
            if (c == '-' || c == '.' || ((c >= '0' && c <= '9') || c == '·' || ((c >= '̀' && c <= 'ͯ') || ((c >= '‿' && c <= '⁀') || nameStart)))) {
                nameMiddle = true;
            } else {
                nameMiddle = false;
            }
            if (k == 0) {
                if (!nameStart) {
                    c = '_';
                }
            } else if (!nameMiddle) {
                c = '-';
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public void parseTag(String tag, PdfObject object, PdfDictionary page) throws IOException {
        if (object instanceof PdfNumber) {
            RenderFilter filter = new MarkedContentRenderFilter(((PdfNumber) object).intValue());
            FilteredTextRenderListener listener = new FilteredTextRenderListener(new SimpleTextExtractionStrategy(), filter);
            new PdfContentStreamProcessor(listener).processContent(PdfReader.getPageContent(page), page.getAsDict(PdfName.RESOURCES));
            this.out.print(XMLUtil.escapeXML(listener.getResultantText(), true));
        } else if (object instanceof PdfArray) {
            PdfArray arr = (PdfArray) object;
            int n = arr.size();
            for (int i = 0; i < n; i++) {
                parseTag(tag, arr.getPdfObject(i), page);
                if (i < n - 1) {
                    this.out.println();
                }
            }
        } else if (object instanceof PdfDictionary) {
            PdfDictionary mcr = (PdfDictionary) object;
            parseTag(tag, mcr.getDirectObject(PdfName.MCID), mcr.getAsDict(PdfName.PG));
        }
    }
}
