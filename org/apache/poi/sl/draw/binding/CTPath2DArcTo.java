package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Path2DArcTo", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public class CTPath2DArcTo {
    @XmlAttribute(name = "hR", required = true)
    protected String hr;
    @XmlAttribute(required = true)
    protected String stAng;
    @XmlAttribute(required = true)
    protected String swAng;
    @XmlAttribute(name = "wR", required = true)
    protected String wr;

    public String getWR() {
        return this.wr;
    }

    public void setWR(String value) {
        this.wr = value;
    }

    public boolean isSetWR() {
        return this.wr != null;
    }

    public String getHR() {
        return this.hr;
    }

    public void setHR(String value) {
        this.hr = value;
    }

    public boolean isSetHR() {
        return this.hr != null;
    }

    public String getStAng() {
        return this.stAng;
    }

    public void setStAng(String value) {
        this.stAng = value;
    }

    public boolean isSetStAng() {
        return this.stAng != null;
    }

    public String getSwAng() {
        return this.swAng;
    }

    public void setSwAng(String value) {
        this.swAng = value;
    }

    public boolean isSetSwAng() {
        return this.swAng != null;
    }
}
