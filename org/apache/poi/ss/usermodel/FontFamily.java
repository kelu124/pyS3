package org.apache.poi.ss.usermodel;

public enum FontFamily {
    NOT_APPLICABLE(0),
    ROMAN(1),
    SWISS(2),
    MODERN(3),
    SCRIPT(4),
    DECORATIVE(5);
    
    private static FontFamily[] _table;
    private int family;

    static {
        _table = new FontFamily[6];
        for (FontFamily c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private FontFamily(int value) {
        this.family = value;
    }

    public int getValue() {
        return this.family;
    }

    public static FontFamily valueOf(int family) {
        return _table[family];
    }
}
