package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.AreaEvalBase;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.ptg.AreaI;
import org.apache.poi.ss.formula.ptg.AreaI.OffsetArea;
import org.apache.poi.ss.util.CellReference;

final class LazyAreaEval extends AreaEvalBase {
    private final SheetRangeEvaluator _evaluator;

    LazyAreaEval(AreaI ptg, SheetRangeEvaluator evaluator) {
        super(ptg, evaluator);
        this._evaluator = evaluator;
    }

    public LazyAreaEval(int firstRowIndex, int firstColumnIndex, int lastRowIndex, int lastColumnIndex, SheetRangeEvaluator evaluator) {
        super(evaluator, firstRowIndex, firstColumnIndex, lastRowIndex, lastColumnIndex);
        this._evaluator = evaluator;
    }

    public ValueEval getRelativeValue(int relativeRowIndex, int relativeColumnIndex) {
        return getRelativeValue(getFirstSheetIndex(), relativeRowIndex, relativeColumnIndex);
    }

    public ValueEval getRelativeValue(int sheetIndex, int relativeRowIndex, int relativeColumnIndex) {
        return this._evaluator.getEvalForCell(sheetIndex, relativeRowIndex + getFirstRow(), relativeColumnIndex + getFirstColumn());
    }

    public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx) {
        return new LazyAreaEval(new OffsetArea(getFirstRow(), getFirstColumn(), relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx), this._evaluator);
    }

    public LazyAreaEval getRow(int rowIndex) {
        if (rowIndex >= getHeight()) {
            throw new IllegalArgumentException("Invalid rowIndex " + rowIndex + ".  Allowable range is (0.." + getHeight() + ").");
        }
        int absRowIx = getFirstRow() + rowIndex;
        return new LazyAreaEval(absRowIx, getFirstColumn(), absRowIx, getLastColumn(), this._evaluator);
    }

    public LazyAreaEval getColumn(int columnIndex) {
        if (columnIndex >= getWidth()) {
            throw new IllegalArgumentException("Invalid columnIndex " + columnIndex + ".  Allowable range is (0.." + getWidth() + ").");
        }
        int absColIx = getFirstColumn() + columnIndex;
        return new LazyAreaEval(getFirstRow(), absColIx, getLastRow(), absColIx, this._evaluator);
    }

    public String toString() {
        return getClass().getName() + "[" + this._evaluator.getSheetNameRange() + '!' + new CellReference(getFirstRow(), getFirstColumn()).formatAsString() + ':' + new CellReference(getLastRow(), getLastColumn()).formatAsString() + "]";
    }

    public boolean isSubTotal(int rowIndex, int columnIndex) {
        return this._evaluator.getSheetEvaluator(this._evaluator.getFirstSheetIndex()).isSubTotal(getFirstRow() + rowIndex, getFirstColumn() + columnIndex);
    }
}
