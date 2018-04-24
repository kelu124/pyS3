package org.apache.poi.sl.draw.binding;

import com.itextpdf.text.html.HtmlTags;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "ST_RectAlignment", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public enum STRectAlignment {
    TL("tl"),
    T("t"),
    TR(HtmlTags.TR),
    L("l"),
    CTR("ctr"),
    R("r"),
    BL("bl"),
    B(HtmlTags.f33B),
    BR(HtmlTags.BR);
    
    private final String value;

    private STRectAlignment(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static STRectAlignment fromValue(String v) {
        for (STRectAlignment c : values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
