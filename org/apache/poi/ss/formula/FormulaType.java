package org.apache.poi.ss.formula;

import org.apache.poi.util.Internal;

@Internal
public enum FormulaType {
    CELL(0),
    SHARED(1),
    ARRAY(2),
    CONDFORMAT(3),
    NAMEDRANGE(4),
    DATAVALIDATION_LIST(5);
    
    private final int code;

    private FormulaType(int code) {
        this.code = code;
    }

    public static FormulaType forInt(int code) {
        for (FormulaType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid FormulaType code: " + code);
    }
}
