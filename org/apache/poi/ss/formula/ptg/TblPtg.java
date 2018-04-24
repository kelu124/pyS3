package org.apache.poi.ss.formula.ptg;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class TblPtg extends ControlPtg {
    private static final int SIZE = 5;
    public static final short sid = (short) 2;
    private final int field_1_first_row;
    private final int field_2_first_col;

    public TblPtg(LittleEndianInput in) {
        this.field_1_first_row = in.readUShort();
        this.field_2_first_col = in.readUShort();
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 2);
        out.writeShort(this.field_1_first_row);
        out.writeShort(this.field_2_first_col);
    }

    public int getSize() {
        return 5;
    }

    public int getRow() {
        return this.field_1_first_row;
    }

    public int getColumn() {
        return this.field_2_first_col;
    }

    public String toFormulaString() {
        throw new RuntimeException("Table and Arrays are not yet supported");
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("[Data Table - Parent cell is an interior cell in a data table]\n");
        buffer.append("top left row = ").append(getRow()).append("\n");
        buffer.append("top left col = ").append(getColumn()).append("\n");
        return buffer.toString();
    }
}
