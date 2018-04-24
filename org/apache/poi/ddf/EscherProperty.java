package org.apache.poi.ddf;

public abstract class EscherProperty {
    private short _id;

    public abstract int serializeComplexPart(byte[] bArr, int i);

    public abstract int serializeSimplePart(byte[] bArr, int i);

    public EscherProperty(short id) {
        this._id = id;
    }

    public EscherProperty(short propertyNumber, boolean isComplex, boolean isBlipId) {
        int i;
        int i2 = 0;
        if (isComplex) {
            i = 32768;
        } else {
            i = 0;
        }
        i += propertyNumber;
        if (isBlipId) {
            i2 = 16384;
        }
        this._id = (short) (i2 + i);
    }

    public short getId() {
        return this._id;
    }

    public short getPropertyNumber() {
        return (short) (this._id & 16383);
    }

    public boolean isComplex() {
        return (this._id & -32768) != 0;
    }

    public boolean isBlipId() {
        return (this._id & 16384) != 0;
    }

    public String getName() {
        return EscherProperties.getPropertyName(getPropertyNumber());
    }

    public int getPropertySize() {
        return 6;
    }

    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append("<").append(getClass().getSimpleName()).append(" id=\"").append(getId()).append("\" name=\"").append(getName()).append("\" blipId=\"").append(isBlipId()).append("\"/>\n");
        return builder.toString();
    }
}
