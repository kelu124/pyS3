package org.apache.poi.ss.formula.ptg;

public final class GreaterThanPtg extends ValueOperatorPtg {
    private static final String GREATERTHAN = ">";
    public static final ValueOperatorPtg instance = new GreaterThanPtg();
    public static final byte sid = (byte) 13;

    private GreaterThanPtg() {
    }

    protected byte getSid() {
        return (byte) 13;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append(GREATERTHAN);
        buffer.append(operands[1]);
        return buffer.toString();
    }
}
