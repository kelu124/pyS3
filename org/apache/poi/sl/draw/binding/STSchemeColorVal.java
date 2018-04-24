package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "ST_SchemeColorVal", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public enum STSchemeColorVal {
    BG_1("bg1"),
    TX_1("tx1"),
    BG_2("bg2"),
    TX_2("tx2"),
    ACCENT_1("accent1"),
    ACCENT_2("accent2"),
    ACCENT_3("accent3"),
    ACCENT_4("accent4"),
    ACCENT_5("accent5"),
    ACCENT_6("accent6"),
    HLINK("hlink"),
    FOL_HLINK("folHlink"),
    PH_CLR("phClr"),
    DK_1("dk1"),
    LT_1("lt1"),
    DK_2("dk2"),
    LT_2("lt2");
    
    private final String value;

    private STSchemeColorVal(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static STSchemeColorVal fromValue(String v) {
        for (STSchemeColorVal c : values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
