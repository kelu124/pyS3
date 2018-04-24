package com.itextpdf.text.xml.simpleparser;

import java.util.Map;

public interface SimpleXMLDocHandler {
    void endDocument();

    void endElement(String str);

    void startDocument();

    void startElement(String str, Map<String, String> map);

    void text(String str);
}
