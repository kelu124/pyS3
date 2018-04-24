package com.itextpdf.text.xml;

import com.google.common.base.Ascii;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XmlDomWriter {
    protected boolean fCanonical;
    protected PrintWriter fOut;
    protected boolean fXML11;

    public XmlDomWriter(boolean canonical) {
        this.fCanonical = canonical;
    }

    public void setCanonical(boolean canonical) {
        this.fCanonical = canonical;
    }

    public void setOutput(OutputStream stream, String encoding) throws UnsupportedEncodingException {
        if (encoding == null) {
            encoding = "UTF8";
        }
        this.fOut = new PrintWriter(new OutputStreamWriter(stream, encoding));
    }

    public void setOutput(Writer writer) {
        this.fOut = writer instanceof PrintWriter ? (PrintWriter) writer : new PrintWriter(writer);
    }

    public void write(Node node) {
        if (node != null) {
            short type = node.getNodeType();
            Node child;
            switch (type) {
                case (short) 1:
                    this.fOut.print('<');
                    this.fOut.print(node.getNodeName());
                    Attr[] attrs = sortAttributes(node.getAttributes());
                    for (Attr attr : attrs) {
                        this.fOut.print(' ');
                        this.fOut.print(attr.getNodeName());
                        this.fOut.print("=\"");
                        normalizeAndPrint(attr.getNodeValue(), true);
                        this.fOut.print('\"');
                    }
                    this.fOut.print('>');
                    this.fOut.flush();
                    for (child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                        write(child);
                    }
                    break;
                case (short) 3:
                    normalizeAndPrint(node.getNodeValue(), false);
                    this.fOut.flush();
                    break;
                case (short) 4:
                    if (this.fCanonical) {
                        normalizeAndPrint(node.getNodeValue(), false);
                    } else {
                        this.fOut.print("<![CDATA[");
                        this.fOut.print(node.getNodeValue());
                        this.fOut.print("]]>");
                    }
                    this.fOut.flush();
                    break;
                case (short) 5:
                    if (!this.fCanonical) {
                        this.fOut.print('&');
                        this.fOut.print(node.getNodeName());
                        this.fOut.print(';');
                        this.fOut.flush();
                        break;
                    }
                    for (child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                        write(child);
                    }
                    break;
                case (short) 7:
                    this.fOut.print("<?");
                    this.fOut.print(node.getNodeName());
                    String data = node.getNodeValue();
                    if (data != null && data.length() > 0) {
                        this.fOut.print(' ');
                        this.fOut.print(data);
                    }
                    this.fOut.print("?>");
                    this.fOut.flush();
                    break;
                case (short) 8:
                    if (!this.fCanonical) {
                        this.fOut.print("<!--");
                        String comment = node.getNodeValue();
                        if (comment != null && comment.length() > 0) {
                            this.fOut.print(comment);
                        }
                        this.fOut.print("-->");
                        this.fOut.flush();
                        break;
                    }
                    break;
                case (short) 9:
                    Document document = (Document) node;
                    this.fXML11 = false;
                    if (!this.fCanonical) {
                        if (this.fXML11) {
                            this.fOut.println("<?xml version=\"1.1\" encoding=\"UTF-8\"?>");
                        } else {
                            this.fOut.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        }
                        this.fOut.flush();
                        write(document.getDoctype());
                    }
                    write(document.getDocumentElement());
                    break;
                case (short) 10:
                    DocumentType doctype = (DocumentType) node;
                    this.fOut.print("<!DOCTYPE ");
                    this.fOut.print(doctype.getName());
                    String publicId = doctype.getPublicId();
                    String systemId = doctype.getSystemId();
                    if (publicId != null) {
                        this.fOut.print(" PUBLIC '");
                        this.fOut.print(publicId);
                        this.fOut.print("' '");
                        this.fOut.print(systemId);
                        this.fOut.print('\'');
                    } else if (systemId != null) {
                        this.fOut.print(" SYSTEM '");
                        this.fOut.print(systemId);
                        this.fOut.print('\'');
                    }
                    String internalSubset = doctype.getInternalSubset();
                    if (internalSubset != null) {
                        this.fOut.println(" [");
                        this.fOut.print(internalSubset);
                        this.fOut.print(']');
                    }
                    this.fOut.println('>');
                    break;
            }
            if (type == (short) 1) {
                this.fOut.print("</");
                this.fOut.print(node.getNodeName());
                this.fOut.print('>');
                this.fOut.flush();
            }
        }
    }

    protected Attr[] sortAttributes(NamedNodeMap attrs) {
        int i;
        int len = attrs != null ? attrs.getLength() : 0;
        Attr[] array = new Attr[len];
        for (i = 0; i < len; i++) {
            array[i] = (Attr) attrs.item(i);
        }
        for (i = 0; i < len - 1; i++) {
            String name = array[i].getNodeName();
            int index = i;
            for (int j = i + 1; j < len; j++) {
                String curName = array[j].getNodeName();
                if (curName.compareTo(name) < 0) {
                    name = curName;
                    index = j;
                }
            }
            if (index != i) {
                Attr temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }
        return array;
    }

    protected void normalizeAndPrint(String s, boolean isAttValue) {
        int len = s != null ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            normalizeAndPrint(s.charAt(i), isAttValue);
        }
    }

    protected void normalizeAndPrint(char c, boolean isAttValue) {
        switch (c) {
            case '\n':
                if (this.fCanonical) {
                    this.fOut.print("&#xA;");
                    return;
                }
                break;
            case '\r':
                this.fOut.print("&#xD;");
                return;
            case '\"':
                if (isAttValue) {
                    this.fOut.print("&quot;");
                    return;
                } else {
                    this.fOut.print("\"");
                    return;
                }
            case '&':
                this.fOut.print("&amp;");
                return;
            case '<':
                this.fOut.print("&lt;");
                return;
            case '>':
                this.fOut.print("&gt;");
                return;
        }
        if ((!this.fXML11 || ((c < '\u0001' || c > '\u001f' || c == '\t' || c == '\n') && ((c < Ascii.MAX || c > '') && c != ' '))) && !(isAttValue && (c == '\t' || c == '\n'))) {
            this.fOut.print(c);
            return;
        }
        this.fOut.print("&#x");
        this.fOut.print(Integer.toHexString(c).toUpperCase());
        this.fOut.print(";");
    }
}
