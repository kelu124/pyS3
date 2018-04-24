package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Path2DList", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"path"})
public class CTPath2DList {
    @XmlElement(namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
    protected List<CTPath2D> path;

    public List<CTPath2D> getPath() {
        if (this.path == null) {
            this.path = new ArrayList();
        }
        return this.path;
    }

    public boolean isSetPath() {
        return (this.path == null || this.path.isEmpty()) ? false : true;
    }

    public void unsetPath() {
        this.path = null;
    }
}
