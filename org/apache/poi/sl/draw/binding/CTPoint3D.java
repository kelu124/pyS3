package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Point3D", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public class CTPoint3D {
    @XmlAttribute(required = true)
    protected long f50x;
    @XmlAttribute(required = true)
    protected long f51y;
    @XmlAttribute(required = true)
    protected long f52z;

    public long getX() {
        return this.f50x;
    }

    public void setX(long value) {
        this.f50x = value;
    }

    public boolean isSetX() {
        return true;
    }

    public long getY() {
        return this.f51y;
    }

    public void setY(long value) {
        this.f51y = value;
    }

    public boolean isSetY() {
        return true;
    }

    public long getZ() {
        return this.f52z;
    }

    public void setZ(long value) {
        this.f52z = value;
    }

    public boolean isSetZ() {
        return true;
    }
}
