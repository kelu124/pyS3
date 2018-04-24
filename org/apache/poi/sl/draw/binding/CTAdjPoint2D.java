package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_AdjPoint2D", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public class CTAdjPoint2D {
    @XmlAttribute(required = true)
    protected String f40x;
    @XmlAttribute(required = true)
    protected String f41y;

    public String getX() {
        return this.f40x;
    }

    public void setX(String value) {
        this.f40x = value;
    }

    public boolean isSetX() {
        return this.f40x != null;
    }

    public String getY() {
        return this.f41y;
    }

    public void setY(String value) {
        this.f41y = value;
    }

    public boolean isSetY() {
        return this.f41y != null;
    }
}
