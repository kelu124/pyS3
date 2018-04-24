package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_GeomGuide", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public class CTGeomGuide {
    @XmlAttribute(required = true)
    protected String fmla;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return this.name != null;
    }

    public String getFmla() {
        return this.fmla;
    }

    public void setFmla(String value) {
        this.fmla = value;
    }

    public boolean isSetFmla() {
        return this.fmla != null;
    }
}
