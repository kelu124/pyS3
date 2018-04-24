package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.ValueEval;

final class SheetRangeEvaluator implements SheetRange {
    private final int _firstSheetIndex;
    private final int _lastSheetIndex;
    private SheetRefEvaluator[] _sheetEvaluators;

    public SheetRangeEvaluator(int firstSheetIndex, int lastSheetIndex, SheetRefEvaluator[] sheetEvaluators) {
        if (firstSheetIndex < 0) {
            throw new IllegalArgumentException("Invalid firstSheetIndex: " + firstSheetIndex + ".");
        } else if (lastSheetIndex < firstSheetIndex) {
            throw new IllegalArgumentException("Invalid lastSheetIndex: " + lastSheetIndex + " for firstSheetIndex: " + firstSheetIndex + ".");
        } else {
            this._firstSheetIndex = firstSheetIndex;
            this._lastSheetIndex = lastSheetIndex;
            this._sheetEvaluators = (SheetRefEvaluator[]) sheetEvaluators.clone();
        }
    }

    public SheetRangeEvaluator(int onlySheetIndex, SheetRefEvaluator sheetEvaluator) {
        this(onlySheetIndex, onlySheetIndex, new SheetRefEvaluator[]{sheetEvaluator});
    }

    public SheetRefEvaluator getSheetEvaluator(int sheetIndex) {
        if (sheetIndex >= this._firstSheetIndex && sheetIndex <= this._lastSheetIndex) {
            return this._sheetEvaluators[sheetIndex - this._firstSheetIndex];
        }
        throw new IllegalArgumentException("Invalid SheetIndex: " + sheetIndex + " - Outside range " + this._firstSheetIndex + " : " + this._lastSheetIndex);
    }

    public int getFirstSheetIndex() {
        return this._firstSheetIndex;
    }

    public int getLastSheetIndex() {
        return this._lastSheetIndex;
    }

    public String getSheetName(int sheetIndex) {
        return getSheetEvaluator(sheetIndex).getSheetName();
    }

    public String getSheetNameRange() {
        StringBuilder sb = new StringBuilder();
        sb.append(getSheetName(this._firstSheetIndex));
        if (this._firstSheetIndex != this._lastSheetIndex) {
            sb.append(':');
            sb.append(getSheetName(this._lastSheetIndex));
        }
        return sb.toString();
    }

    public ValueEval getEvalForCell(int sheetIndex, int rowIndex, int columnIndex) {
        return getSheetEvaluator(sheetIndex).getEvalForCell(rowIndex, columnIndex);
    }
}
