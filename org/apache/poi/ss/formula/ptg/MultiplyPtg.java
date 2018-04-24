package org.apache.poi.ss.formula.ptg;

public final class MultiplyPtg extends ValueOperatorPtg {
    public static final ValueOperatorPtg instance = new MultiplyPtg();
    public static final byte sid = (byte) 5;

    private MultiplyPtg() {
    }

    protected byte getSid() {
        return (byte) 5;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append("*");
        buffer.append(operands[1]);
        return buffer.toString();
    }
}
