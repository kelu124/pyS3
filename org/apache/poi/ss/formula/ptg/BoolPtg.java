package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class BoolPtg extends ScalarConstantPtg {
    private static final BoolPtg FALSE = new BoolPtg(false);
    public static final int SIZE = 2;
    private static final BoolPtg TRUE = new BoolPtg(true);
    public static final byte sid = (byte) 29;
    private final boolean _value;

    private BoolPtg(boolean b) {
        this._value = b;
    }

    public static BoolPtg valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }

    public static BoolPtg read(LittleEndianInput in) {
        boolean z = true;
        if (in.readByte() != (byte) 1) {
            z = false;
        }
        return valueOf(z);
    }

    public boolean getValue() {
        return this._value;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 29);
        out.writeByte(this._value ? 1 : 0);
    }

    public int getSize() {
        return 2;
    }

    public String toFormulaString() {
        return this._value ? "TRUE" : "FALSE";
    }
}
