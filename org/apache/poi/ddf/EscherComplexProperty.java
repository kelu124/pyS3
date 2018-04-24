package org.apache.poi.ddf;

import java.util.Arrays;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public class EscherComplexProperty extends EscherProperty {
    protected byte[] _complexData;

    public EscherComplexProperty(short id, byte[] complexData) {
        super(id);
        if (complexData == null) {
            throw new IllegalArgumentException("complexData can't be null");
        }
        this._complexData = (byte[]) complexData.clone();
    }

    public EscherComplexProperty(short propertyNumber, boolean isBlipId, byte[] complexData) {
        super(propertyNumber, true, isBlipId);
        if (complexData == null) {
            throw new IllegalArgumentException("complexData can't be null");
        }
        this._complexData = (byte[]) complexData.clone();
    }

    public int serializeSimplePart(byte[] data, int pos) {
        LittleEndian.putShort(data, pos, getId());
        LittleEndian.putInt(data, pos + 2, this._complexData.length);
        return 6;
    }

    public int serializeComplexPart(byte[] data, int pos) {
        System.arraycopy(this._complexData, 0, data, pos, this._complexData.length);
        return this._complexData.length;
    }

    public byte[] getComplexData() {
        return this._complexData;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof EscherComplexProperty)) {
            return false;
        }
        return Arrays.equals(this._complexData, ((EscherComplexProperty) o)._complexData);
    }

    public int getPropertySize() {
        return this._complexData.length + 6;
    }

    public int hashCode() {
        return getId() * 11;
    }

    public String toString() {
        return "propNum: " + getPropertyNumber() + ", propName: " + EscherProperties.getPropertyName(getPropertyNumber()) + ", complex: " + isComplex() + ", blipId: " + isBlipId() + ", data: " + System.getProperty("line.separator") + HexDump.toHex(this._complexData, 32);
    }

    public String toXml(String tab) {
        return tab + "<" + getClass().getSimpleName() + " id=\"0x" + HexDump.toHex(getId()) + "\" name=\"" + getName() + "\" blipId=\"" + isBlipId() + "\">\n" + tab + "</" + getClass().getSimpleName() + ">\n";
    }
}
