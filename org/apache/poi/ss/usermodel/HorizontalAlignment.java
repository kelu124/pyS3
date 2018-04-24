package org.apache.poi.ss.usermodel;

public enum HorizontalAlignment {
    GENERAL,
    LEFT,
    CENTER,
    RIGHT,
    FILL,
    JUSTIFY,
    CENTER_SELECTION,
    DISTRIBUTED;

    public short getCode() {
        return (short) ordinal();
    }

    public static HorizontalAlignment forInt(int code) {
        if (code >= 0 && code < values().length) {
            return values()[code];
        }
        throw new IllegalArgumentException("Invalid HorizontalAlignment code: " + code);
    }
}
