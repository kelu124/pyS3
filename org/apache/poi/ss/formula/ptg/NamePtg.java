package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
import org.apache.poi.ss.formula.WorkbookDependentFormula;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class NamePtg extends OperandPtg implements WorkbookDependentFormula {
    private static final int SIZE = 5;
    public static final short sid = (short) 35;
    private int field_1_label_index;
    private short field_2_zero;

    public NamePtg(int nameIndex) {
        this.field_1_label_index = nameIndex + 1;
    }

    public NamePtg(LittleEndianInput in) {
        this.field_1_label_index = in.readShort();
        this.field_2_zero = in.readShort();
    }

    public int getIndex() {
        return this.field_1_label_index - 1;
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 35);
        out.writeShort(this.field_1_label_index);
        out.writeShort(this.field_2_zero);
    }

    public int getSize() {
        return 5;
    }

    public String toFormulaString(FormulaRenderingWorkbook book) {
        return book.getNameText(this);
    }

    public String toFormulaString() {
        throw new RuntimeException("3D references need a workbook to determine formula text");
    }

    public byte getDefaultOperandClass() {
        return (byte) 0;
    }
}
