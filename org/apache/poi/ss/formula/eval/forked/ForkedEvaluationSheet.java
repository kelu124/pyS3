package org.apache.poi.ss.formula.eval.forked;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.ss.formula.EvaluationWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Internal;

@Internal
final class ForkedEvaluationSheet implements EvaluationSheet {
    private final EvaluationSheet _masterSheet;
    private final Map<RowColKey, ForkedEvaluationCell> _sharedCellsByRowCol = new HashMap();

    public ForkedEvaluationSheet(EvaluationSheet masterSheet) {
        this._masterSheet = masterSheet;
    }

    public EvaluationCell getCell(int rowIndex, int columnIndex) {
        ForkedEvaluationCell result = (ForkedEvaluationCell) this._sharedCellsByRowCol.get(new RowColKey(rowIndex, columnIndex));
        if (result == null) {
            return this._masterSheet.getCell(rowIndex, columnIndex);
        }
        return result;
    }

    public ForkedEvaluationCell getOrCreateUpdatableCell(int rowIndex, int columnIndex) {
        RowColKey key = new RowColKey(rowIndex, columnIndex);
        ForkedEvaluationCell result = (ForkedEvaluationCell) this._sharedCellsByRowCol.get(key);
        if (result != null) {
            return result;
        }
        EvaluationCell mcell = this._masterSheet.getCell(rowIndex, columnIndex);
        if (mcell == null) {
            throw new UnsupportedOperationException("Underlying cell '" + new CellReference(rowIndex, columnIndex).formatAsString() + "' is missing in master sheet.");
        }
        result = new ForkedEvaluationCell(this, mcell);
        this._sharedCellsByRowCol.put(key, result);
        return result;
    }

    public void copyUpdatedCells(Sheet sheet) {
        RowColKey[] keys = new RowColKey[this._sharedCellsByRowCol.size()];
        this._sharedCellsByRowCol.keySet().toArray(keys);
        Arrays.sort(keys);
        for (RowColKey key : keys) {
            Row row = sheet.getRow(key.getRowIndex());
            if (row == null) {
                row = sheet.createRow(key.getRowIndex());
            }
            Cell destCell = row.getCell(key.getColumnIndex());
            if (destCell == null) {
                destCell = row.createCell(key.getColumnIndex());
            }
            ((ForkedEvaluationCell) this._sharedCellsByRowCol.get(key)).copyValue(destCell);
        }
    }

    public int getSheetIndex(EvaluationWorkbook mewb) {
        return mewb.getSheetIndex(this._masterSheet);
    }

    public void clearAllCachedResultValues() {
        this._masterSheet.clearAllCachedResultValues();
    }
}
