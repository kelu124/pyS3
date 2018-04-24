package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.xml.XMLUtil;
import java.util.ArrayList;
import java.util.Iterator;

@Deprecated
public class XmpArray extends ArrayList<String> {
    public static final String ALTERNATIVE = "rdf:Alt";
    public static final String ORDERED = "rdf:Seq";
    public static final String UNORDERED = "rdf:Bag";
    private static final long serialVersionUID = 5722854116328732742L;
    protected String type;

    public XmpArray(String type) {
        this.type = type;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("<");
        buf.append(this.type);
        buf.append('>');
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            String s = (String) i$.next();
            buf.append("<rdf:li>");
            buf.append(XMLUtil.escapeXML(s, false));
            buf.append("</rdf:li>");
        }
        buf.append("</");
        buf.append(this.type);
        buf.append('>');
        return buf.toString();
    }
}
