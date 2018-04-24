package com.itextpdf.text;

import java.util.List;
import java.util.Properties;

public class MarkedObject implements Element {
    protected Element element;
    protected Properties markupAttributes;

    protected MarkedObject() {
        this.markupAttributes = new Properties();
        this.element = null;
    }

    public MarkedObject(Element element) {
        this.markupAttributes = new Properties();
        this.element = element;
    }

    public List<Chunk> getChunks() {
        return this.element.getChunks();
    }

    public boolean process(ElementListener listener) {
        try {
            return listener.add(this.element);
        } catch (DocumentException e) {
            return false;
        }
    }

    public int type() {
        return 50;
    }

    public boolean isContent() {
        return true;
    }

    public boolean isNestable() {
        return true;
    }

    public Properties getMarkupAttributes() {
        return this.markupAttributes;
    }

    public void setMarkupAttribute(String key, String value) {
        this.markupAttributes.setProperty(key, value);
    }
}
