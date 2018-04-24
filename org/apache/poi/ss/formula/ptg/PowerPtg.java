package org.apache.poi.ss.formula.ptg;

public final class PowerPtg extends ValueOperatorPtg {
    public static final ValueOperatorPtg instance = new PowerPtg();
    public static final byte sid = (byte) 7;

    private PowerPtg() {
    }

    protected byte getSid() {
        return (byte) 7;
    }

    public int getNumberOfOperands() {
        return 2;
    }

    public String toFormulaString(String[] operands) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(operands[0]);
        buffer.append("^");
        buffer.append(operands[1]);
        return buffer.toString();
    }
}
