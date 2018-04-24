package org.apache.poi.ss.usermodel;

public enum VerticalAlignment {
    TOP,
    CENTER,
    BOTTOM,
    JUSTIFY,
    DISTRIBUTED;

    public short getCode() {
        return (short) ordinal();
    }

    public static VerticalAlignment forInt(int code) {
        if (code >= 0 && code < values().length) {
            return values()[code];
        }
        throw new IllegalArgumentException("Invalid VerticalAlignment code: " + code);
    }
}
