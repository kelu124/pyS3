package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public abstract class AreaPtgBase extends OperandPtg implements AreaI {
    private static final BitField colRelative = BitFieldFactory.getInstance(16384);
    private static final BitField columnMask = BitFieldFactory.getInstance(16383);
    private static final BitField rowRelative = BitFieldFactory.getInstance(32768);
    private int field_1_first_row;
    private int field_2_last_row;
    private int field_3_first_column;
    private int field_4_last_column;

    protected final RuntimeException notImplemented() {
        return new RuntimeException("Coding Error: This method should never be called. This ptg should be converted");
    }

    protected AreaPtgBase() {
    }

    protected AreaPtgBase(AreaReference ar) {
        boolean z;
        boolean z2 = true;
        CellReference firstCell = ar.getFirstCell();
        CellReference lastCell = ar.getLastCell();
        setFirstRow(firstCell.getRow());
        setFirstColumn(firstCell.getCol() == (short) -1 ? 0 : firstCell.getCol());
        setLastRow(lastCell.getRow());
        setLastColumn(lastCell.getCol() == (short) -1 ? 255 : lastCell.getCol());
        if (firstCell.isColAbsolute()) {
            z = false;
        } else {
            z = true;
        }
        setFirstColRelative(z);
        if (lastCell.isColAbsolute()) {
            z = false;
        } else {
            z = true;
        }
        setLastColRelative(z);
        if (firstCell.isRowAbsolute()) {
            z = false;
        } else {
            z = true;
        }
        setFirstRowRelative(z);
        if (lastCell.isRowAbsolute()) {
            z2 = false;
        }
        setLastRowRelative(z2);
    }

    protected AreaPtgBase(int firstRow, int lastRow, int firstColumn, int lastColumn, boolean firstRowRelative, boolean lastRowRelative, boolean firstColRelative, boolean lastColRelative) {
        if (lastRow >= firstRow) {
            setFirstRow(firstRow);
            setLastRow(lastRow);
            setFirstRowRelative(firstRowRelative);
            setLastRowRelative(lastRowRelative);
        } else {
            setFirstRow(lastRow);
            setLastRow(firstRow);
            setFirstRowRelative(lastRowRelative);
            setLastRowRelative(firstRowRelative);
        }
        if (lastColumn >= firstColumn) {
            setFirstColumn(firstColumn);
            setLastColumn(lastColumn);
            setFirstColRelative(firstColRelative);
            setLastColRelative(lastColRelative);
            return;
        }
        setFirstColumn(lastColumn);
        setLastColumn(firstColumn);
        setFirstColRelative(lastColRelative);
        setLastColRelative(firstColRelative);
    }

    public void sortTopLeftToBottomRight() {
        if (getFirstRow() > getLastRow()) {
            int firstRow = getFirstRow();
            boolean firstRowRel = isFirstRowRelative();
            setFirstRow(getLastRow());
            setFirstRowRelative(isLastRowRelative());
            setLastRow(firstRow);
            setLastRowRelative(firstRowRel);
        }
        if (getFirstColumn() > getLastColumn()) {
            int firstCol = getFirstColumn();
            boolean firstColRel = isFirstColRelative();
            setFirstColumn(getLastColumn());
            setFirstColRelative(isLastColRelative());
            setLastColumn(firstCol);
            setLastColRelative(firstColRel);
        }
    }

    protected final void readCoordinates(LittleEndianInput in) {
        this.field_1_first_row = in.readUShort();
        this.field_2_last_row = in.readUShort();
        this.field_3_first_column = in.readUShort();
        this.field_4_last_column = in.readUShort();
    }

    protected final void writeCoordinates(LittleEndianOutput out) {
        out.writeShort(this.field_1_first_row);
        out.writeShort(this.field_2_last_row);
        out.writeShort(this.field_3_first_column);
        out.writeShort(this.field_4_last_column);
    }

    public final int getFirstRow() {
        return this.field_1_first_row;
    }

    public final void setFirstRow(int rowIx) {
        this.field_1_first_row = rowIx;
    }

    public final int getLastRow() {
        return this.field_2_last_row;
    }

    public final void setLastRow(int rowIx) {
        this.field_2_last_row = rowIx;
    }

    public final int getFirstColumn() {
        return columnMask.getValue(this.field_3_first_column);
    }

    public final short getFirstColumnRaw() {
        return (short) this.field_3_first_column;
    }

    public final boolean isFirstRowRelative() {
        return rowRelative.isSet(this.field_3_first_column);
    }

    public final void setFirstRowRelative(boolean rel) {
        this.field_3_first_column = rowRelative.setBoolean(this.field_3_first_column, rel);
    }

    public final boolean isFirstColRelative() {
        return colRelative.isSet(this.field_3_first_column);
    }

    public final void setFirstColRelative(boolean rel) {
        this.field_3_first_column = colRelative.setBoolean(this.field_3_first_column, rel);
    }

    public final void setFirstColumn(int colIx) {
        this.field_3_first_column = columnMask.setValue(this.field_3_first_column, colIx);
    }

    public final void setFirstColumnRaw(int column) {
        this.field_3_first_column = column;
    }

    public final int getLastColumn() {
        return columnMask.getValue(this.field_4_last_column);
    }

    public final short getLastColumnRaw() {
        return (short) this.field_4_last_column;
    }

    public final boolean isLastRowRelative() {
        return rowRelative.isSet(this.field_4_last_column);
    }

    public final void setLastRowRelative(boolean rel) {
        this.field_4_last_column = rowRelative.setBoolean(this.field_4_last_column, rel);
    }

    public final boolean isLastColRelative() {
        return colRelative.isSet(this.field_4_last_column);
    }

    public final void setLastColRelative(boolean rel) {
        this.field_4_last_column = colRelative.setBoolean(this.field_4_last_column, rel);
    }

    public final void setLastColumn(int colIx) {
        this.field_4_last_column = columnMask.setValue(this.field_4_last_column, colIx);
    }

    public final void setLastColumnRaw(short column) {
        this.field_4_last_column = column;
    }

    protected final String formatReferenceAsString() {
        boolean z;
        boolean z2 = true;
        int firstRow = getFirstRow();
        int firstColumn = getFirstColumn();
        if (isFirstRowRelative()) {
            z = false;
        } else {
            z = true;
        }
        CellReference topLeft = new CellReference(firstRow, firstColumn, z, !isFirstColRelative());
        int lastRow = getLastRow();
        firstRow = getLastColumn();
        if (isLastRowRelative()) {
            z = false;
        } else {
            z = true;
        }
        if (isLastColRelative()) {
            z2 = false;
        }
        CellReference botRight = new CellReference(lastRow, firstRow, z, z2);
        if (AreaReference.isWholeColumnReference(SpreadsheetVersion.EXCEL97, topLeft, botRight)) {
            return new AreaReference(topLeft, botRight).formatAsString();
        }
        return topLeft.formatAsString() + ":" + botRight.formatAsString();
    }

    public String toFormulaString() {
        return formatReferenceAsString();
    }

    public byte getDefaultOperandClass() {
        return (byte) 0;
    }
}
