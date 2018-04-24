package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_ConnectionSite", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"pos"})
public class CTConnectionSite {
    @XmlAttribute(required = true)
    protected String ang;
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", required = true)
    protected CTAdjPoint2D pos;

    public CTAdjPoint2D getPos() {
        return this.pos;
    }

    public void setPos(CTAdjPoint2D value) {
        this.pos = value;
    }

    public boolean isSetPos() {
        return this.pos != null;
    }

    public String getAng() {
        return this.ang;
    }

    public void setAng(String value) {
        this.ang = value;
    }

    public boolean isSetAng() {
        return this.ang != null;
    }
}
