package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class IntPtg extends ScalarConstantPtg {
    private static final int MAX_VALUE = 65535;
    private static final int MIN_VALUE = 0;
    public static final int SIZE = 3;
    public static final byte sid = (byte) 30;
    private final int field_1_value;

    public static boolean isInRange(int i) {
        return i >= 0 && i <= 65535;
    }

    public IntPtg(LittleEndianInput in) {
        this(in.readUShort());
    }

    public IntPtg(int value) {
        if (isInRange(value)) {
            this.field_1_value = value;
            return;
        }
        throw new IllegalArgumentException("value is out of range: " + value);
    }

    public int getValue() {
        return this.field_1_value;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 30);
        out.writeShort(getValue());
    }

    public int getSize() {
        return 3;
    }

    public String toFormulaString() {
        return String.valueOf(getValue());
    }
}
