package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_PositiveSize2D", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public class CTPositiveSize2D {
    @XmlAttribute(required = true)
    protected long cx;
    @XmlAttribute(required = true)
    protected long cy;

    public long getCx() {
        return this.cx;
    }

    public void setCx(long value) {
        this.cx = value;
    }

    public boolean isSetCx() {
        return true;
    }

    public long getCy() {
        return this.cy;
    }

    public void setCy(long value) {
        this.cy = value;
    }

    public boolean isSetCy() {
        return true;
    }
}
