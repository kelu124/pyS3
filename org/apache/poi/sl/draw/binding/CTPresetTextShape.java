package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_PresetTextShape", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"avLst"})
public class CTPresetTextShape {
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
    protected CTGeomGuideList avLst;
    @XmlAttribute(required = true)
    protected STTextShapeType prst;

    public CTGeomGuideList getAvLst() {
        return this.avLst;
    }

    public void setAvLst(CTGeomGuideList value) {
        this.avLst = value;
    }

    public boolean isSetAvLst() {
        return this.avLst != null;
    }

    public STTextShapeType getPrst() {
        return this.prst;
    }

    public void setPrst(STTextShapeType value) {
        this.prst = value;
    }

    public boolean isSetPrst() {
        return this.prst != null;
    }
}
