package org.apache.poi.ss.formula.ptg;

public final class UnaryMinusPtg extends ValueOperatorPtg {
    private static final String MINUS = "-";
    public static final ValueOperatorPtg instance = new UnaryMinusPtg();
    public static final byte sid = (byte) 19;

    private UnaryMinusPtg() {
    }

    protected byte getSid() {
        return (byte) 19;
    }

    public int getNumberOfOperands() {
        return 1;
    }

    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(MINUS);
        buffer.append(operands[0]);
        return buffer.toString();
    }
}
