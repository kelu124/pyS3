package org.apache.poi.ss.formula.ptg;

public final class GreaterEqualPtg extends ValueOperatorPtg {
    public static final int SIZE = 1;
    public static final ValueOperatorPtg instance = new GreaterEqualPtg();
    public static final byte sid = (byte) 12;

    private GreaterEqualPtg() {
    }

    protected byte getSid() {
        return (byte) 12;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append(">=");
        buffer.append(operands[1]);
        return buffer.toString();
    }
}
