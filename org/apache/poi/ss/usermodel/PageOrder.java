package org.apache.poi.ss.usermodel;

public enum PageOrder {
    DOWN_THEN_OVER(1),
    OVER_THEN_DOWN(2);
    
    private static PageOrder[] _table;
    private int order;

    static {
        _table = new PageOrder[3];
        for (PageOrder c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private PageOrder(int order) {
        this.order = order;
    }

    public int getValue() {
        return this.order;
    }

    public static PageOrder valueOf(int value) {
        return _table[value];
    }
}
