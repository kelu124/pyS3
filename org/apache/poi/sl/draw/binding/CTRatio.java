package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Ratio", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public class CTRatio {
    @XmlAttribute(required = true)
    protected long f53d;
    @XmlAttribute(required = true)
    protected long f54n;

    public long getN() {
        return this.f54n;
    }

    public void setN(long value) {
        this.f54n = value;
    }

    public boolean isSetN() {
        return true;
    }

    public long getD() {
        return this.f53d;
    }

    public void setD(long value) {
        this.f53d = value;
    }

    public boolean isSetD() {
        return true;
    }
}
