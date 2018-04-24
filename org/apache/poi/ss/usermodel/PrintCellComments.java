package org.apache.poi.ss.usermodel;

public enum PrintCellComments {
    NONE(1),
    AS_DISPLAYED(2),
    AT_END(3);
    
    private static PrintCellComments[] _table;
    private int comments;

    static {
        _table = new PrintCellComments[4];
        for (PrintCellComments c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private PrintCellComments(int comments) {
        this.comments = comments;
    }

    public int getValue() {
        return this.comments;
    }

    public static PrintCellComments valueOf(int value) {
        return _table[value];
    }
}
