package com.itextpdf.text;

public class Header extends Meta {
    private StringBuffer name;

    public Header(String name, String content) {
        super(0, content);
        this.name = new StringBuffer(name);
    }

    public String getName() {
        return this.name.toString();
    }
}
