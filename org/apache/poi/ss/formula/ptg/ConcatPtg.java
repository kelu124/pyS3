package org.apache.poi.ss.formula.ptg;

public final class ConcatPtg extends ValueOperatorPtg {
    private static final String CONCAT = "&";
    public static final ValueOperatorPtg instance = new ConcatPtg();
    public static final byte sid = (byte) 8;

    private ConcatPtg() {
    }

    protected byte getSid() {
        return (byte) 8;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append(CONCAT);
        buffer.append(operands[1]);
        return buffer.toString();
    }
}
