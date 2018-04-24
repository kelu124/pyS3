package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianOutput;

public final class ParenthesisPtg extends ControlPtg {
    private static final int SIZE = 1;
    public static final ControlPtg instance = new ParenthesisPtg();
    public static final byte sid = (byte) 21;

    private ParenthesisPtg() {
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 21);
    }

    public int getSize() {
        return 1;
    }

    public String toFormulaString() {
        return "()";
    }

    public String toFormulaString(String[] operands) {
        return "(" + operands[0] + ")";
    }
}
