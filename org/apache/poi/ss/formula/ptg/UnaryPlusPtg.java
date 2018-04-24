package org.apache.poi.ss.formula.ptg;

public final class UnaryPlusPtg extends ValueOperatorPtg {
    private static final String ADD = "+";
    public static final ValueOperatorPtg instance = new UnaryPlusPtg();
    public static final byte sid = (byte) 18;

    private UnaryPlusPtg() {
    }

    protected byte getSid() {
        return (byte) 18;
    }

    public int getNumberOfOperands() {
        return 1;
    }

    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(ADD);
        buffer.append(operands[0]);
        return buffer.toString();
    }
}
