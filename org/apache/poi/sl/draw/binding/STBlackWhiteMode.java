package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import org.apache.poi.ss.util.CellUtil;

@XmlEnum
@XmlType(name = "ST_BlackWhiteMode", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public enum STBlackWhiteMode {
    CLR("clr"),
    AUTO("auto"),
    GRAY("gray"),
    LT_GRAY("ltGray"),
    INV_GRAY("invGray"),
    GRAY_WHITE("grayWhite"),
    BLACK_GRAY("blackGray"),
    BLACK_WHITE("blackWhite"),
    BLACK("black"),
    WHITE("white"),
    HIDDEN(CellUtil.HIDDEN);
    
    private final String value;

    private STBlackWhiteMode(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static STBlackWhiteMode fromValue(String v) {
        for (STBlackWhiteMode c : values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
