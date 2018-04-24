package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.xml.XmlDomWriter;
import com.itextpdf.xmp.XMPConst;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Deprecated
public class XmpReader {
    public static final String EXTRASPACE = "                                                                                                   \n";
    public static final String XPACKET_PI_BEGIN = "<?xpacket begin=\"﻿\" id=\"W5M0MpCehiHzreSzNTczkc9d\"?>\n";
    public static final String XPACKET_PI_END_W = "<?xpacket end=\"w\"?>";
    private Document domDocument;

    public XmpReader(byte[] bytes) throws SAXException, IOException {
        try {
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            fact.setNamespaceAware(true);
            this.domDocument = fact.newDocumentBuilder().parse(new ByteArrayInputStream(bytes));
        } catch (ParserConfigurationException e) {
            throw new ExceptionConverter(e);
        }
    }

    public boolean replaceNode(String namespaceURI, String localName, String value) {
        NodeList nodes = this.domDocument.getElementsByTagNameNS(namespaceURI, localName);
        if (nodes.getLength() == 0) {
            return false;
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            setNodeText(this.domDocument, nodes.item(i), value);
        }
        return true;
    }

    public boolean replaceDescriptionAttribute(String namespaceURI, String localName, String value) {
        NodeList descNodes = this.domDocument.getElementsByTagNameNS(XMPConst.NS_RDF, "Description");
        if (descNodes.getLength() == 0) {
            return false;
        }
        for (int i = 0; i < descNodes.getLength(); i++) {
            Node attr = descNodes.item(i).getAttributes().getNamedItemNS(namespaceURI, localName);
            if (attr != null) {
                attr.setNodeValue(value);
                return true;
            }
        }
        return false;
    }

    public boolean add(String parent, String namespaceURI, String localName, String value) {
        NodeList nodes = this.domDocument.getElementsByTagName(parent);
        if (nodes.getLength() == 0) {
            return false;
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            Node pNode = nodes.item(i);
            NamedNodeMap attrs = pNode.getAttributes();
            for (int j = 0; j < attrs.getLength(); j++) {
                Node node = attrs.item(j);
                if (namespaceURI.equals(node.getNodeValue())) {
                    String prefix = node.getLocalName();
                    node = this.domDocument.createElementNS(namespaceURI, localName);
                    node.setPrefix(prefix);
                    node.appendChild(this.domDocument.createTextNode(value));
                    pNode.appendChild(node);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setNodeText(Document domDocument, Node n, String value) {
        if (n == null) {
            return false;
        }
        while (true) {
            Node nc = n.getFirstChild();
            if (nc != null) {
                n.removeChild(nc);
            } else {
                n.appendChild(domDocument.createTextNode(value));
                return true;
            }
        }
    }

    public byte[] serializeDoc() throws IOException {
        XmlDomWriter xw = new XmlDomWriter();
        ByteArrayOutputStream fout = new ByteArrayOutputStream();
        xw.setOutput(fout, null);
        fout.write(XPACKET_PI_BEGIN.getBytes(XmpWriter.UTF8));
        fout.flush();
        xw.write(this.domDocument.getElementsByTagName("x:xmpmeta").item(0));
        fout.flush();
        for (int i = 0; i < 20; i++) {
            fout.write(EXTRASPACE.getBytes());
        }
        fout.write(XPACKET_PI_END_W.getBytes());
        fout.close();
        return fout.toByteArray();
    }
}
