package org.apache.poi.ddf;

public class EscherPropertyMetaData {
    public static final byte TYPE_ARRAY = (byte) 5;
    public static final byte TYPE_BOOLEAN = (byte) 1;
    public static final byte TYPE_RGB = (byte) 2;
    public static final byte TYPE_SHAPEPATH = (byte) 3;
    public static final byte TYPE_SIMPLE = (byte) 4;
    public static final byte TYPE_UNKNOWN = (byte) 0;
    private String description;
    private byte type;

    public EscherPropertyMetaData(String description) {
        this.description = description;
    }

    public EscherPropertyMetaData(String description, byte type) {
        this.description = description;
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public byte getType() {
        return this.type;
    }
}
