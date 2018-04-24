package org.apache.poi.sl.draw.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CT_OfficeArtExtension", namespace = "http://schemas.openxmlformats.org/drawingml/2006/main", propOrder = {"any"})
public class CTOfficeArtExtension {
    @XmlAnyElement(lax = true)
    protected Object any;
    @XmlAttribute
    @XmlSchemaType(name = "token")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String uri;

    public Object getAny() {
        return this.any;
    }

    public void setAny(Object value) {
        this.any = value;
    }

    public boolean isSetAny() {
        return this.any != null;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String value) {
        this.uri = value;
    }

    public boolean isSetUri() {
        return this.uri != null;
    }
}
