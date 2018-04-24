package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.RefEvalBase;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.ptg.AreaI.OffsetArea;
import org.apache.poi.ss.util.CellReference;

public final class LazyRefEval extends RefEvalBase {
    private final SheetRangeEvaluator _evaluator;

    public LazyRefEval(int rowIndex, int columnIndex, SheetRangeEvaluator sre) {
        super((SheetRange) sre, rowIndex, columnIndex);
        this._evaluator = sre;
    }

    public ValueEval getInnerValueEval(int sheetIndex) {
        return this._evaluator.getEvalForCell(sheetIndex, getRow(), getColumn());
    }

    public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx) {
        return new LazyAreaEval(new OffsetArea(getRow(), getColumn(), relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx), this._evaluator);
    }

    public boolean isSubTotal() {
        return this._evaluator.getSheetEvaluator(getFirstSheetIndex()).isSubTotal(getRow(), getColumn());
    }

    public String toString() {
        return getClass().getName() + "[" + this._evaluator.getSheetNameRange() + '!' + new CellReference(getRow(), getColumn()).formatAsString() + "]";
    }
}
