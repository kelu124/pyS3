package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Scale2D", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"sx", "sy"})
public class CTScale2D {
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", required = true)
    protected CTRatio sx;
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", required = true)
    protected CTRatio sy;

    public CTRatio getSx() {
        return this.sx;
    }

    public void setSx(CTRatio value) {
        this.sx = value;
    }

    public boolean isSetSx() {
        return this.sx != null;
    }

    public CTRatio getSy() {
        return this.sy;
    }

    public void setSy(CTRatio value) {
        this.sy = value;
    }

    public boolean isSetSy() {
        return this.sy != null;
    }
}
