package org.apache.poi.ss.usermodel;

public enum FontUnderline {
    SINGLE(1),
    DOUBLE(2),
    SINGLE_ACCOUNTING(3),
    DOUBLE_ACCOUNTING(4),
    NONE(5);
    
    private static FontUnderline[] _table;
    private int value;

    static {
        _table = new FontUnderline[6];
        for (FontUnderline c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private FontUnderline(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value;
    }

    public byte getByteValue() {
        switch (this) {
            case DOUBLE:
                return (byte) 2;
            case DOUBLE_ACCOUNTING:
                return (byte) 34;
            case SINGLE_ACCOUNTING:
                return (byte) 33;
            case NONE:
                return (byte) 0;
            default:
                return (byte) 1;
        }
    }

    public static FontUnderline valueOf(int value) {
        return _table[value];
    }

    public static FontUnderline valueOf(byte value) {
        switch (value) {
            case (byte) 1:
                return SINGLE;
            case (byte) 2:
                return DOUBLE;
            case (byte) 33:
                return SINGLE_ACCOUNTING;
            case (byte) 34:
                return DOUBLE_ACCOUNTING;
            default:
                return NONE;
        }
    }
}
