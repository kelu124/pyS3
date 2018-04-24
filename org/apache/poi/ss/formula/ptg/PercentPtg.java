package org.apache.poi.ss.formula.ptg;

public final class PercentPtg extends ValueOperatorPtg {
    private static final String PERCENT = "%";
    public static final int SIZE = 1;
    public static final ValueOperatorPtg instance = new PercentPtg();
    public static final byte sid = (byte) 20;

    private PercentPtg() {
    }

    protected byte getSid() {
        return (byte) 20;
    }

    public int getNumberOfOperands() {
        return 1;
    }

    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append(PERCENT);
        return buffer.toString();
    }
}
