package org.apache.poi.sl.usermodel;

import com.itextpdf.text.html.HtmlTags;

public enum RectAlign {
    TOP_LEFT("tl"),
    TOP("t"),
    TOP_RIGHT(HtmlTags.TR),
    LEFT("l"),
    CENTER("ctr"),
    RIGHT("r"),
    BOTTOM_LEFT("bl"),
    BOTTOM(HtmlTags.f33B),
    BOTTOM_RIGHT(HtmlTags.BR);
    
    private final String dir;

    private RectAlign(String dir) {
        this.dir = dir;
    }

    public String toString() {
        return this.dir;
    }
}
