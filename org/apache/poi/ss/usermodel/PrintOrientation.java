package org.apache.poi.ss.usermodel;

public enum PrintOrientation {
    DEFAULT(1),
    PORTRAIT(2),
    LANDSCAPE(3);
    
    private static PrintOrientation[] _table;
    private int orientation;

    static {
        _table = new PrintOrientation[4];
        for (PrintOrientation c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private PrintOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getValue() {
        return this.orientation;
    }

    public static PrintOrientation valueOf(int value) {
        return _table[value];
    }
}
