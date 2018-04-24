package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "ST_PathFillMode", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public enum STPathFillMode {
    NONE("none"),
    NORM("norm"),
    LIGHTEN("lighten"),
    LIGHTEN_LESS("lightenLess"),
    DARKEN("darken"),
    DARKEN_LESS("darkenLess");
    
    private final String value;

    private STPathFillMode(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static STPathFillMode fromValue(String v) {
        for (STPathFillMode c : values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
