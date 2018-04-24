package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Point2D", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public class CTPoint2D {
    @XmlAttribute(required = true)
    protected long f48x;
    @XmlAttribute(required = true)
    protected long f49y;

    public long getX() {
        return this.f48x;
    }

    public void setX(long value) {
        this.f48x = value;
    }

    public boolean isSetX() {
        return true;
    }

    public long getY() {
        return this.f49y;
    }

    public void setY(long value) {
        this.f49y = value;
    }

    public boolean isSetY() {
        return true;
    }
}
