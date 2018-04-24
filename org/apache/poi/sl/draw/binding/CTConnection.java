package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_Connection", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main")
public class CTConnection {
    @XmlAttribute(required = true)
    protected long id;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "unsignedInt")
    protected long idx;

    public long getId() {
        return this.id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public boolean isSetId() {
        return true;
    }

    public long getIdx() {
        return this.idx;
    }

    public void setIdx(long value) {
        this.idx = value;
    }

    public boolean isSetIdx() {
        return true;
    }
}
