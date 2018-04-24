package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_PolarAdjustHandle", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"pos"})
public class CTPolarAdjustHandle {
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String gdRefAng;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String gdRefR;
    @XmlAttribute
    protected String maxAng;
    @XmlAttribute
    protected String maxR;
    @XmlAttribute
    protected String minAng;
    @XmlAttribute
    protected String minR;
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

    public String getGdRefR() {
        return this.gdRefR;
    }

    public void setGdRefR(String value) {
        this.gdRefR = value;
    }

    public boolean isSetGdRefR() {
        return this.gdRefR != null;
    }

    public String getMinR() {
        return this.minR;
    }

    public void setMinR(String value) {
        this.minR = value;
    }

    public boolean isSetMinR() {
        return this.minR != null;
    }

    public String getMaxR() {
        return this.maxR;
    }

    public void setMaxR(String value) {
        this.maxR = value;
    }

    public boolean isSetMaxR() {
        return this.maxR != null;
    }

    public String getGdRefAng() {
        return this.gdRefAng;
    }

    public void setGdRefAng(String value) {
        this.gdRefAng = value;
    }

    public boolean isSetGdRefAng() {
        return this.gdRefAng != null;
    }

    public String getMinAng() {
        return this.minAng;
    }

    public void setMinAng(String value) {
        this.minAng = value;
    }

    public boolean isSetMinAng() {
        return this.minAng != null;
    }

    public String getMaxAng() {
        return this.maxAng;
    }

    public void setMaxAng(String value) {
        this.maxAng = value;
    }

    public boolean isSetMaxAng() {
        return this.maxAng != null;
    }
}
