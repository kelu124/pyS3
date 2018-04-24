package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.SheetRange;

public abstract class RefEvalBase implements RefEval {
    private final int _columnIndex;
    private final int _firstSheetIndex;
    private final int _lastSheetIndex;
    private final int _rowIndex;

    protected RefEvalBase(SheetRange sheetRange, int rowIndex, int columnIndex) {
        if (sheetRange == null) {
            throw new IllegalArgumentException("sheetRange must not be null");
        }
        this._firstSheetIndex = sheetRange.getFirstSheetIndex();
        this._lastSheetIndex = sheetRange.getLastSheetIndex();
        this._rowIndex = rowIndex;
        this._columnIndex = columnIndex;
    }

    protected RefEvalBase(int firstSheetIndex, int lastSheetIndex, int rowIndex, int columnIndex) {
        this._firstSheetIndex = firstSheetIndex;
        this._lastSheetIndex = lastSheetIndex;
        this._rowIndex = rowIndex;
        this._columnIndex = columnIndex;
    }

    protected RefEvalBase(int onlySheetIndex, int rowIndex, int columnIndex) {
        this(onlySheetIndex, onlySheetIndex, rowIndex, columnIndex);
    }

    public int getNumberOfSheets() {
        return (this._lastSheetIndex - this._firstSheetIndex) + 1;
    }

    public int getFirstSheetIndex() {
        return this._firstSheetIndex;
    }

    public int getLastSheetIndex() {
        return this._lastSheetIndex;
    }

    public final int getRow() {
        return this._rowIndex;
    }

    public final int getColumn() {
        return this._columnIndex;
    }
}
