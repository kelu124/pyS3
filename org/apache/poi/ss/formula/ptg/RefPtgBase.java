package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public abstract class RefPtgBase extends OperandPtg {
    private static final BitField colRelative = BitFieldFactory.getInstance(16384);
    private static final BitField column = BitFieldFactory.getInstance(16383);
    private static final BitField rowRelative = BitFieldFactory.getInstance(32768);
    private int field_1_row;
    private int field_2_col;

    protected RefPtgBase() {
    }

    protected RefPtgBase(CellReference c) {
        boolean z;
        boolean z2 = true;
        setRow(c.getRow());
        setColumn(c.getCol());
        if (c.isColAbsolute()) {
            z = false;
        } else {
            z = true;
        }
        setColRelative(z);
        if (c.isRowAbsolute()) {
            z2 = false;
        }
        setRowRelative(z2);
    }

    protected final void readCoordinates(LittleEndianInput in) {
        this.field_1_row = in.readUShort();
        this.field_2_col = in.readUShort();
    }

    protected final void writeCoordinates(LittleEndianOutput out) {
        out.writeShort(this.field_1_row);
        out.writeShort(this.field_2_col);
    }

    public final void setRow(int rowIndex) {
        this.field_1_row = rowIndex;
    }

    public final int getRow() {
        return this.field_1_row;
    }

    public final boolean isRowRelative() {
        return rowRelative.isSet(this.field_2_col);
    }

    public final void setRowRelative(boolean rel) {
        this.field_2_col = rowRelative.setBoolean(this.field_2_col, rel);
    }

    public final boolean isColRelative() {
        return colRelative.isSet(this.field_2_col);
    }

    public final void setColRelative(boolean rel) {
        this.field_2_col = colRelative.setBoolean(this.field_2_col, rel);
    }

    public final void setColumn(int col) {
        this.field_2_col = column.setValue(this.field_2_col, col);
    }

    public final int getColumn() {
        return column.getValue(this.field_2_col);
    }

    protected final String formatReferenceAsString() {
        boolean z;
        boolean z2 = true;
        int row = getRow();
        int column = getColumn();
        if (isRowRelative()) {
            z = false;
        } else {
            z = true;
        }
        if (isColRelative()) {
            z2 = false;
        }
        return new CellReference(row, column, z, z2).formatAsString();
    }

    public final byte getDefaultOperandClass() {
        return (byte) 0;
    }
}
