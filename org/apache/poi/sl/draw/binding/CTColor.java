package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Color", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"scrgbClr", "srgbClr", "hslClr", "sysClr", "schemeClr", "prstClr"})
public class CTColor {
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
    protected CTHslColor hslClr;
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
    protected CTPresetColor prstClr;
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
    protected CTSchemeColor schemeClr;
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
    protected CTScRgbColor scrgbClr;
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
    protected CTSRgbColor srgbClr;
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
    protected CTSystemColor sysClr;

    public CTScRgbColor getScrgbClr() {
        return this.scrgbClr;
    }

    public void setScrgbClr(CTScRgbColor value) {
        this.scrgbClr = value;
    }

    public boolean isSetScrgbClr() {
        return this.scrgbClr != null;
    }

    public CTSRgbColor getSrgbClr() {
        return this.srgbClr;
    }

    public void setSrgbClr(CTSRgbColor value) {
        this.srgbClr = value;
    }

    public boolean isSetSrgbClr() {
        return this.srgbClr != null;
    }

    public CTHslColor getHslClr() {
        return this.hslClr;
    }

    public void setHslClr(CTHslColor value) {
        this.hslClr = value;
    }

    public boolean isSetHslClr() {
        return this.hslClr != null;
    }

    public CTSystemColor getSysClr() {
        return this.sysClr;
    }

    public void setSysClr(CTSystemColor value) {
        this.sysClr = value;
    }

    public boolean isSetSysClr() {
        return this.sysClr != null;
    }

    public CTSchemeColor getSchemeClr() {
        return this.schemeClr;
    }

    public void setSchemeClr(CTSchemeColor value) {
        this.schemeClr = value;
    }

    public boolean isSetSchemeClr() {
        return this.schemeClr != null;
    }

    public CTPresetColor getPrstClr() {
        return this.prstClr;
    }

    public void setPrstClr(CTPresetColor value) {
        this.prstClr = value;
    }

    public boolean isSetPrstClr() {
        return this.prstClr != null;
    }
}
