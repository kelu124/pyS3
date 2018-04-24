package org.apache.poi.ss.formula.ptg;

public final class NotEqualPtg extends ValueOperatorPtg {
    public static final ValueOperatorPtg instance = new NotEqualPtg();
    public static final byte sid = (byte) 14;

    private NotEqualPtg() {
    }

    protected byte getSid() {
        return (byte) 14;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append("<>");
        buffer.append(operands[1]);
        return buffer.toString();
    }
}
