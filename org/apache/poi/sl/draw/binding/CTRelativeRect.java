package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_RelativeRect", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public class CTRelativeRect {
    @XmlAttribute
    protected Integer f55b;
    @XmlAttribute
    protected Integer f56l;
    @XmlAttribute
    protected Integer f57r;
    @XmlAttribute
    protected Integer f58t;

    public int getL() {
        if (this.f56l == null) {
            return 0;
        }
        return this.f56l.intValue();
    }

    public void setL(int value) {
        this.f56l = Integer.valueOf(value);
    }

    public boolean isSetL() {
        return this.f56l != null;
    }

    public void unsetL() {
        this.f56l = null;
    }

    public int getT() {
        if (this.f58t == null) {
            return 0;
        }
        return this.f58t.intValue();
    }

    public void setT(int value) {
        this.f58t = Integer.valueOf(value);
    }

    public boolean isSetT() {
        return this.f58t != null;
    }

    public void unsetT() {
        this.f58t = null;
    }

    public int getR() {
        if (this.f57r == null) {
            return 0;
        }
        return this.f57r.intValue();
    }

    public void setR(int value) {
        this.f57r = Integer.valueOf(value);
    }

    public boolean isSetR() {
        return this.f57r != null;
    }

    public void unsetR() {
        this.f57r = null;
    }

    public int getB() {
        if (this.f55b == null) {
            return 0;
        }
        return this.f55b.intValue();
    }

    public void setB(int value) {
        this.f55b = Integer.valueOf(value);
    }

    public boolean isSetB() {
        return this.f55b != null;
    }

    public void unsetB() {
        this.f55b = null;
    }
}
