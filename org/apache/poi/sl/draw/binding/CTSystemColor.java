package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_SystemColor", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"egColorTransform"})
public class CTSystemColor {
    @XmlElementRefs({@XmlElementRef(name = "gray", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "green", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "shade", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "blueOff", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "lumMod", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "alphaMod", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "redMod", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "blue", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "sat", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "alphaOff", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "red", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "greenOff", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "hueMod", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "satOff", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "gamma", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "redOff", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "hue", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "satMod", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "lum", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "comp", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "invGamma", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "hueOff", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "alpha", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "greenMod", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "inv", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "blueMod", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "tint", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class), @XmlElementRef(name = "lumOff", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = JAXBElement.class)})
    protected List<JAXBElement<?>> egColorTransform;
    @XmlAttribute
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    protected byte[] lastClr;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String val;

    public List<JAXBElement<?>> getEGColorTransform() {
        if (this.egColorTransform == null) {
            this.egColorTransform = new ArrayList();
        }
        return this.egColorTransform;
    }

    public boolean isSetEGColorTransform() {
        return (this.egColorTransform == null || this.egColorTransform.isEmpty()) ? false : true;
    }

    public void unsetEGColorTransform() {
        this.egColorTransform = null;
    }

    public String getVal() {
        return this.val;
    }

    public void setVal(String value) {
        this.val = value;
    }

    public boolean isSetVal() {
        return this.val != null;
    }

    public byte[] getLastClr() {
        return this.lastClr;
    }

    public void setLastClr(byte[] value) {
        this.lastClr = value != null ? (byte[]) value.clone() : null;
    }

    public boolean isSetLastClr() {
        return this.lastClr != null;
    }
}
