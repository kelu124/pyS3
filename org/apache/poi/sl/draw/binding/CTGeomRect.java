package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_GeomRect", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public class CTGeomRect {
    @XmlAttribute(required = true)
    protected String f42b;
    @XmlAttribute(required = true)
    protected String f43l;
    @XmlAttribute(required = true)
    protected String f44r;
    @XmlAttribute(required = true)
    protected String f45t;

    public String getL() {
        return this.f43l;
    }

    public void setL(String value) {
        this.f43l = value;
    }

    public boolean isSetL() {
        return this.f43l != null;
    }

    public String getT() {
        return this.f45t;
    }

    public void setT(String value) {
        this.f45t = value;
    }

    public boolean isSetT() {
        return this.f45t != null;
    }

    public String getR() {
        return this.f44r;
    }

    public void setR(String value) {
        this.f44r = value;
    }

    public boolean isSetR() {
        return this.f44r != null;
    }

    public String getB() {
        return this.f42b;
    }

    public void setB(String value) {
        this.f42b = value;
    }

    public boolean isSetB() {
        return this.f42b != null;
    }
}
