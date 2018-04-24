package org.apache.poi.ss.formula.ptg;

public final class LessThanPtg extends ValueOperatorPtg {
    private static final String LESSTHAN = "<";
    public static final ValueOperatorPtg instance = new LessThanPtg();
    public static final byte sid = (byte) 9;

    private LessThanPtg() {
    }

    protected byte getSid() {
        return (byte) 9;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append(LESSTHAN);
        buffer.append(operands[1]);
        return buffer.toString();
    }
}
