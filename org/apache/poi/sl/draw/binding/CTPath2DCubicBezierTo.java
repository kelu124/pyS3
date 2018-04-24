package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Path2DCubicBezierTo", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"pt"})
public class CTPath2DCubicBezierTo {
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", required = true)
    protected List<CTAdjPoint2D> pt;

    public List<CTAdjPoint2D> getPt() {
        if (this.pt == null) {
            this.pt = new ArrayList();
        }
        return this.pt;
    }

    public boolean isSetPt() {
        return (this.pt == null || this.pt.isEmpty()) ? false : true;
    }

    public void unsetPt() {
        this.pt = null;
    }
}
