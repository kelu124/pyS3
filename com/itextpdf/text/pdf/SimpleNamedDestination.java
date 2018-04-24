package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.xml.XMLUtil;
import com.itextpdf.text.xml.simpleparser.IanaEncodings;
import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public final class SimpleNamedDestination implements SimpleXMLDocHandler {
    private HashMap<String, String> xmlLast;
    private HashMap<String, String> xmlNames;

    private SimpleNamedDestination() {
    }

    public static HashMap<String, String> getNamedDestination(PdfReader reader, boolean fromNames) {
        int k;
        IntHashtable pages = new IntHashtable();
        int numPages = reader.getNumberOfPages();
        for (k = 1; k <= numPages; k++) {
            pages.put(reader.getPageOrigRef(k).getNumber(), k);
        }
        HashMap<String, PdfObject> names = fromNames ? reader.getNamedDestinationFromNames() : reader.getNamedDestinationFromStrings();
        HashMap<String, String> n2 = new HashMap(names.size());
        for (Entry<String, PdfObject> entry : names.entrySet()) {
            PdfArray arr = (PdfArray) entry.getValue();
            StringBuffer s = new StringBuffer();
            try {
                s.append(pages.get(arr.getAsIndirectObject(0).getNumber()));
                s.append(' ').append(arr.getPdfObject(1).toString().substring(1));
                for (k = 2; k < arr.size(); k++) {
                    s.append(' ').append(arr.getPdfObject(k).toString());
                }
                n2.put(entry.getKey(), s.toString());
            } catch (Exception e) {
            }
        }
        return n2;
    }

    public static void exportToXML(HashMap<String, String> names, OutputStream out, String encoding, boolean onlyASCII) throws IOException {
        exportToXML((HashMap) names, new BufferedWriter(new OutputStreamWriter(out, IanaEncodings.getJavaEncoding(encoding))), encoding, onlyASCII);
    }

    public static void exportToXML(HashMap<String, String> names, Writer wrt, String encoding, boolean onlyASCII) throws IOException {
        wrt.write("<?xml version=\"1.0\" encoding=\"");
        wrt.write(XMLUtil.escapeXML(encoding, onlyASCII));
        wrt.write("\"?>\n<Destination>\n");
        for (Entry<String, String> entry : names.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            wrt.write("  <Name Page=\"");
            wrt.write(XMLUtil.escapeXML(value, onlyASCII));
            wrt.write("\">");
            wrt.write(XMLUtil.escapeXML(escapeBinaryString(key), onlyASCII));
            wrt.write("</Name>\n");
        }
        wrt.write("</Destination>\n");
        wrt.flush();
    }

    public static HashMap<String, String> importFromXML(InputStream in) throws IOException {
        SimpleNamedDestination names = new SimpleNamedDestination();
        SimpleXMLParser.parse(names, in);
        return names.xmlNames;
    }

    public static HashMap<String, String> importFromXML(Reader in) throws IOException {
        SimpleNamedDestination names = new SimpleNamedDestination();
        SimpleXMLParser.parse(names, in);
        return names.xmlNames;
    }

    static PdfArray createDestinationArray(String value, PdfWriter writer) {
        PdfArray ar = new PdfArray();
        StringTokenizer tk = new StringTokenizer(value);
        ar.add(writer.getPageReference(Integer.parseInt(tk.nextToken())));
        if (tk.hasMoreTokens()) {
            String fn = tk.nextToken();
            if (fn.startsWith("/")) {
                fn = fn.substring(1);
            }
            ar.add(new PdfName(fn));
            for (int k = 0; k < 4 && tk.hasMoreTokens(); k++) {
                fn = tk.nextToken();
                if (fn.equals("null")) {
                    ar.add(PdfNull.PDFNULL);
                } else {
                    ar.add(new PdfNumber(fn));
                }
            }
        } else {
            ar.add(PdfName.XYZ);
            ar.add(new float[]{0.0f, 10000.0f, 0.0f});
        }
        return ar;
    }

    public static PdfDictionary outputNamedDestinationAsNames(HashMap<String, String> names, PdfWriter writer) {
        PdfDictionary dic = new PdfDictionary();
        for (Entry<String, String> entry : names.entrySet()) {
            try {
                String key = (String) entry.getKey();
                dic.put(new PdfName(key), createDestinationArray((String) entry.getValue(), writer));
            } catch (Exception e) {
            }
        }
        return dic;
    }

    public static PdfDictionary outputNamedDestinationAsStrings(HashMap<String, String> names, PdfWriter writer) throws IOException {
        HashMap<String, PdfObject> n2 = new HashMap(names.size());
        for (Entry<String, String> entry : names.entrySet()) {
            try {
                n2.put(entry.getKey(), writer.addToBody(createDestinationArray((String) entry.getValue(), writer)).getIndirectReference());
            } catch (Exception e) {
            }
        }
        return PdfNameTree.writeTree(n2, writer);
    }

    public static String escapeBinaryString(String s) {
        StringBuffer buf = new StringBuffer();
        for (char c : s.toCharArray()) {
            if (c < ' ') {
                buf.append('\\');
                String octal = "00" + Integer.toOctalString(c);
                buf.append(octal.substring(octal.length() - 3));
            } else if (c == '\\') {
                buf.append("\\\\");
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    public static String unEscapeBinaryString(String s) {
        StringBuffer buf = new StringBuffer();
        char[] cc = s.toCharArray();
        int len = cc.length;
        int k = 0;
        while (k < len) {
            char c = cc[k];
            if (c == '\\') {
                k++;
                if (k >= len) {
                    buf.append('\\');
                    break;
                }
                c = cc[k];
                if (c < '0' || c > PdfWriter.VERSION_1_7) {
                    buf.append(c);
                } else {
                    int n = c - 48;
                    k++;
                    for (int j = 0; j < 2 && k < len; j++) {
                        c = cc[k];
                        if (c < '0' || c > PdfWriter.VERSION_1_7) {
                            break;
                        }
                        k++;
                        n = ((n * 8) + c) - 48;
                    }
                    k--;
                    buf.append((char) n);
                }
            } else {
                buf.append(c);
            }
            k++;
        }
        return buf.toString();
    }

    public void endDocument() {
    }

    public void endElement(String tag) {
        if (tag.equals("Destination")) {
            if (this.xmlLast != null || this.xmlNames == null) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("destination.end.tag.out.of.place", new Object[0]));
            }
        } else if (!tag.equals("Name")) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.end.tag.1", tag));
        } else if (this.xmlLast == null || this.xmlNames == null) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("name.end.tag.out.of.place", new Object[0]));
        } else if (this.xmlLast.containsKey("Page")) {
            this.xmlNames.put(unEscapeBinaryString((String) this.xmlLast.get("Name")), this.xmlLast.get("Page"));
            this.xmlLast = null;
        } else {
            throw new RuntimeException(MessageLocalization.getComposedMessage("page.attribute.missing", new Object[0]));
        }
    }

    public void startDocument() {
    }

    public void startElement(String tag, Map<String, String> h) {
        if (this.xmlNames == null) {
            if (tag.equals("Destination")) {
                this.xmlNames = new HashMap();
                return;
            }
            throw new RuntimeException(MessageLocalization.getComposedMessage("root.element.is.not.destination", new Object[0]));
        } else if (!tag.equals("Name")) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("tag.1.not.allowed", tag));
        } else if (this.xmlLast != null) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("nested.tags.are.not.allowed", new Object[0]));
        } else {
            this.xmlLast = new HashMap(h);
            this.xmlLast.put("Name", "");
        }
    }

    public void text(String str) {
        if (this.xmlLast != null) {
            this.xmlLast.put("Name", ((String) this.xmlLast.get("Name")) + str);
        }
    }
}
