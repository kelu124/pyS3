package org.apache.poi.sl.draw.binding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Path2D", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"closeOrMoveToOrLnTo"})
public class CTPath2D {
    @XmlElements({@XmlElement(name = "lnTo", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = CTPath2DLineTo.class), @XmlElement(name = "close", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = CTPath2DClose.class), @XmlElement(name = "cubicBezTo", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = CTPath2DCubicBezierTo.class), @XmlElement(name = "quadBezTo", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = CTPath2DQuadBezierTo.class), @XmlElement(name = "arcTo", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = CTPath2DArcTo.class), @XmlElement(name = "moveTo", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", type = CTPath2DMoveTo.class)})
    protected List<Object> closeOrMoveToOrLnTo;
    @XmlAttribute
    protected Boolean extrusionOk;
    @XmlAttribute
    protected STPathFillMode fill;
    @XmlAttribute
    protected Long f46h;
    @XmlAttribute
    protected Boolean stroke;
    @XmlAttribute
    protected Long f47w;

    public List<Object> getCloseOrMoveToOrLnTo() {
        if (this.closeOrMoveToOrLnTo == null) {
            this.closeOrMoveToOrLnTo = new ArrayList();
        }
        return this.closeOrMoveToOrLnTo;
    }

    public boolean isSetCloseOrMoveToOrLnTo() {
        return (this.closeOrMoveToOrLnTo == null || this.closeOrMoveToOrLnTo.isEmpty()) ? false : true;
    }

    public void unsetCloseOrMoveToOrLnTo() {
        this.closeOrMoveToOrLnTo = null;
    }

    public long getW() {
        if (this.f47w == null) {
            return 0;
        }
        return this.f47w.longValue();
    }

    public void setW(long value) {
        this.f47w = Long.valueOf(value);
    }

    public boolean isSetW() {
        return this.f47w != null;
    }

    public void unsetW() {
        this.f47w = null;
    }

    public long getH() {
        if (this.f46h == null) {
            return 0;
        }
        return this.f46h.longValue();
    }

    public void setH(long value) {
        this.f46h = Long.valueOf(value);
    }

    public boolean isSetH() {
        return this.f46h != null;
    }

    public void unsetH() {
        this.f46h = null;
    }

    public STPathFillMode getFill() {
        if (this.fill == null) {
            return STPathFillMode.NORM;
        }
        return this.fill;
    }

    public void setFill(STPathFillMode value) {
        this.fill = value;
    }

    public boolean isSetFill() {
        return this.fill != null;
    }

    public boolean isStroke() {
        if (this.stroke == null) {
            return true;
        }
        return this.stroke.booleanValue();
    }

    public void setStroke(boolean value) {
        this.stroke = Boolean.valueOf(value);
    }

    public boolean isSetStroke() {
        return this.stroke != null;
    }

    public void unsetStroke() {
        this.stroke = null;
    }

    public boolean isExtrusionOk() {
        if (this.extrusionOk == null) {
            return true;
        }
        return this.extrusionOk.booleanValue();
    }

    public void setExtrusionOk(boolean value) {
        this.extrusionOk = Boolean.valueOf(value);
    }

    public boolean isSetExtrusionOk() {
        return this.extrusionOk != null;
    }

    public void unsetExtrusionOk() {
        this.extrusionOk = null;
    }
}
