package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianOutput;

public final class UnionPtg extends OperationPtg {
    public static final OperationPtg instance = new UnionPtg();
    public static final byte sid = (byte) 16;

    private UnionPtg() {
    }

    public final boolean isBaseToken() {
        return true;
    }

    public int getSize() {
        return 1;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 16);
    }

    public String toFormulaString() {
        return ",";
    }

    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append(",");
        buffer.append(operands[1]);
        return buffer.toString();
    }

    public int getNumberOfOperands() {
        return 2;
    }
}
