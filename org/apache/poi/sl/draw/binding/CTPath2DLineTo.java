package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Path2DLineTo", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"pt"})
public class CTPath2DLineTo {
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", required = true)
    protected CTAdjPoint2D pt;

    public CTAdjPoint2D getPt() {
        return this.pt;
    }

    public void setPt(CTAdjPoint2D value) {
        this.pt = value;
    }

    public boolean isSetPt() {
        return this.pt != null;
    }
}
