package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.ptg.FuncVarPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.CellType;

final class SheetRefEvaluator {
    private final WorkbookEvaluator _bookEvaluator;
    private EvaluationSheet _sheet;
    private final int _sheetIndex;
    private final EvaluationTracker _tracker;

    public SheetRefEvaluator(WorkbookEvaluator bookEvaluator, EvaluationTracker tracker, int sheetIndex) {
        if (sheetIndex < 0) {
            throw new IllegalArgumentException("Invalid sheetIndex: " + sheetIndex + ".");
        }
        this._bookEvaluator = bookEvaluator;
        this._tracker = tracker;
        this._sheetIndex = sheetIndex;
    }

    public String getSheetName() {
        return this._bookEvaluator.getSheetName(this._sheetIndex);
    }

    public ValueEval getEvalForCell(int rowIndex, int columnIndex) {
        return this._bookEvaluator.evaluateReference(getSheet(), this._sheetIndex, rowIndex, columnIndex, this._tracker);
    }

    private EvaluationSheet getSheet() {
        if (this._sheet == null) {
            this._sheet = this._bookEvaluator.getSheet(this._sheetIndex);
        }
        return this._sheet;
    }

    public boolean isSubTotal(int rowIndex, int columnIndex) {
        EvaluationCell cell = getSheet().getCell(rowIndex, columnIndex);
        if (cell == null || cell.getCellTypeEnum() != CellType.FORMULA) {
            return false;
        }
        for (Ptg ptg : this._bookEvaluator.getWorkbook().getFormulaTokens(cell)) {
            if ((ptg instanceof FuncVarPtg) && "SUBTOTAL".equals(((FuncVarPtg) ptg).getName())) {
                return true;
            }
        }
        return false;
    }
}
