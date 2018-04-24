package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class MemFuncPtg extends OperandPtg {
    public static final byte sid = (byte) 41;
    private final int field_1_len_ref_subexpression;

    public MemFuncPtg(LittleEndianInput in) {
        this(in.readUShort());
    }

    public MemFuncPtg(int subExprLen) {
        this.field_1_len_ref_subexpression = subExprLen;
    }

    public int getSize() {
        return 3;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 41);
        out.writeShort(this.field_1_len_ref_subexpression);
    }

    public String toFormulaString() {
        return "";
    }

    public byte getDefaultOperandClass() {
        return (byte) 0;
    }

    public int getNumberOfOperands() {
        return this.field_1_len_ref_subexpression;
    }

    public int getLenRefSubexpression() {
        return this.field_1_len_ref_subexpression;
    }

    public final String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [len=");
        sb.append(this.field_1_len_ref_subexpression);
        sb.append("]");
        return sb.toString();
    }
}
