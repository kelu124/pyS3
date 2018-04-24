package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_ConnectionSiteList", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"cxn"})
public class CTConnectionSiteList {
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
    protected List<CTConnectionSite> cxn;

    public List<CTConnectionSite> getCxn() {
        if (this.cxn == null) {
            this.cxn = new ArrayList();
        }
        return this.cxn;
    }

    public boolean isSetCxn() {
        return (this.cxn == null || this.cxn.isEmpty()) ? false : true;
    }

    public void unsetCxn() {
        this.cxn = null;
    }
}
