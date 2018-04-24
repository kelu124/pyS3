package org.apache.poi.ss.usermodel;

public enum FontScheme {
    NONE(1),
    MAJOR(2),
    MINOR(3);
    
    private static FontScheme[] _table;
    private int value;

    static {
        _table = new FontScheme[4];
        for (FontScheme c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private FontScheme(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value;
    }

    public static FontScheme valueOf(int value) {
        return _table[value];
    }
}
