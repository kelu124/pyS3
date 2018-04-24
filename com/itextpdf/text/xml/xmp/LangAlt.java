package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.xml.XMLUtil;
import java.util.Enumeration;
import java.util.Properties;

@Deprecated
public class LangAlt extends Properties {
    public static final String DEFAULT = "x-default";
    private static final long serialVersionUID = 4396971487200843099L;

    public LangAlt(String defaultValue) {
        addLanguage("x-default", defaultValue);
    }

    public void addLanguage(String language, String value) {
        setProperty(language, XMLUtil.escapeXML(value, false));
    }

    protected void process(StringBuffer buf, Object lang) {
        buf.append("<rdf:li xml:lang=\"");
        buf.append(lang);
        buf.append("\" >");
        buf.append(get(lang));
        buf.append("</rdf:li>");
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<rdf:Alt>");
        Enumeration<?> e = propertyNames();
        while (e.hasMoreElements()) {
            process(sb, e.nextElement());
        }
        sb.append("</rdf:Alt>");
        return sb.toString();
    }
}
