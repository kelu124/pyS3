package org.apache.poi.ss.formula.ptg;

public abstract class ScalarConstantPtg extends Ptg {
    public final boolean isBaseToken() {
        return true;
    }

    public final byte getDefaultOperandClass() {
        return (byte) 32;
    }

    public final String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(toFormulaString());
        sb.append("]");
        return sb.toString();
    }
}
