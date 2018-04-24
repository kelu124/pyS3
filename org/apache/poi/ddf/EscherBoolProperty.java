package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;

public class EscherBoolProperty extends EscherSimpleProperty {
    public EscherBoolProperty(short propertyNumber, int value) {
        super(propertyNumber, value);
    }

    public boolean isTrue() {
        return this.propertyValue != 0;
    }

    public boolean isFalse() {
        return this.propertyValue == 0;
    }

    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append("<").append(getClass().getSimpleName()).append(" id=\"0x").append(HexDump.toHex(getId())).append("\" name=\"").append(getName()).append("\" simpleValue=\"").append(getPropertyValue()).append("\" blipId=\"").append(isBlipId()).append("\" value=\"").append(isTrue()).append("\"").append("/>\n");
        return builder.toString();
    }
}
