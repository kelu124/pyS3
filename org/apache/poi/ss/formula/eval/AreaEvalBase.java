package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.SheetRange;
import org.apache.poi.ss.formula.ptg.AreaI;

public abstract class AreaEvalBase implements AreaEval {
    private final int _firstColumn;
    private final int _firstRow;
    private final int _firstSheet;
    private final int _lastColumn;
    private final int _lastRow;
    private final int _lastSheet;
    private final int _nColumns;
    private final int _nRows;

    public abstract ValueEval getRelativeValue(int i, int i2);

    public abstract ValueEval getRelativeValue(int i, int i2, int i3);

    protected AreaEvalBase(SheetRange sheets, int firstRow, int firstColumn, int lastRow, int lastColumn) {
        this._firstColumn = firstColumn;
        this._firstRow = firstRow;
        this._lastColumn = lastColumn;
        this._lastRow = lastRow;
        this._nColumns = (this._lastColumn - this._firstColumn) + 1;
        this._nRows = (this._lastRow - this._firstRow) + 1;
        if (sheets != null) {
            this._firstSheet = sheets.getFirstSheetIndex();
            this._lastSheet = sheets.getLastSheetIndex();
            return;
        }
        this._firstSheet = -1;
        this._lastSheet = -1;
    }

    protected AreaEvalBase(int firstRow, int firstColumn, int lastRow, int lastColumn) {
        this(null, firstRow, firstColumn, lastRow, lastColumn);
    }

    protected AreaEvalBase(AreaI ptg) {
        this(ptg, null);
    }

    protected AreaEvalBase(AreaI ptg, SheetRange sheets) {
        this(sheets, ptg.getFirstRow(), ptg.getFirstColumn(), ptg.getLastRow(), ptg.getLastColumn());
    }

    public final int getFirstColumn() {
        return this._firstColumn;
    }

    public final int getFirstRow() {
        return this._firstRow;
    }

    public final int getLastColumn() {
        return this._lastColumn;
    }

    public final int getLastRow() {
        return this._lastRow;
    }

    public int getFirstSheetIndex() {
        return this._firstSheet;
    }

    public int getLastSheetIndex() {
        return this._lastSheet;
    }

    public final ValueEval getAbsoluteValue(int row, int col) {
        int rowOffsetIx = row - this._firstRow;
        int colOffsetIx = col - this._firstColumn;
        if (rowOffsetIx < 0 || rowOffsetIx >= this._nRows) {
            throw new IllegalArgumentException("Specified row index (" + row + ") is outside the allowed range (" + this._firstRow + ".." + this._lastRow + ")");
        } else if (colOffsetIx >= 0 && colOffsetIx < this._nColumns) {
            return getRelativeValue(rowOffsetIx, colOffsetIx);
        } else {
            throw new IllegalArgumentException("Specified column index (" + col + ") is outside the allowed range (" + this._firstColumn + ".." + col + ")");
        }
    }

    public final boolean contains(int row, int col) {
        return this._firstRow <= row && this._lastRow >= row && this._firstColumn <= col && this._lastColumn >= col;
    }

    public final boolean containsRow(int row) {
        return this._firstRow <= row && this._lastRow >= row;
    }

    public final boolean containsColumn(int col) {
        return this._firstColumn <= col && this._lastColumn >= col;
    }

    public final boolean isColumn() {
        return this._firstColumn == this._lastColumn;
    }

    public final boolean isRow() {
        return this._firstRow == this._lastRow;
    }

    public int getHeight() {
        return (this._lastRow - this._firstRow) + 1;
    }

    public final ValueEval getValue(int row, int col) {
        return getRelativeValue(row, col);
    }

    public final ValueEval getValue(int sheetIndex, int row, int col) {
        return getRelativeValue(sheetIndex, row, col);
    }

    public int getWidth() {
        return (this._lastColumn - this._firstColumn) + 1;
    }

    public boolean isSubTotal(int rowIndex, int columnIndex) {
        return false;
    }
}
