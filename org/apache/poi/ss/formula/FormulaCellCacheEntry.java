package org.apache.poi.ss.formula;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.poi.ss.formula.FormulaUsedBlankCellSet.BookSheetKey;
import org.apache.poi.ss.formula.eval.ValueEval;

final class FormulaCellCacheEntry extends CellCacheEntry {
    private CellCacheEntry[] _sensitiveInputCells;
    private FormulaUsedBlankCellSet _usedBlankCellGroup;

    public boolean isInputSensitive() {
        boolean z = false;
        if (this._sensitiveInputCells != null && this._sensitiveInputCells.length > 0) {
            return true;
        }
        if (!(this._usedBlankCellGroup == null || this._usedBlankCellGroup.isEmpty())) {
            z = true;
        }
        return z;
    }

    public void setSensitiveInputCells(CellCacheEntry[] sensitiveInputCells) {
        if (sensitiveInputCells == null) {
            this._sensitiveInputCells = null;
            changeConsumingCells(CellCacheEntry.EMPTY_ARRAY);
            return;
        }
        this._sensitiveInputCells = (CellCacheEntry[]) sensitiveInputCells.clone();
        changeConsumingCells(this._sensitiveInputCells);
    }

    public void clearFormulaEntry() {
        CellCacheEntry[] usedCells = this._sensitiveInputCells;
        if (usedCells != null) {
            for (int i = usedCells.length - 1; i >= 0; i--) {
                usedCells[i].clearConsumingCell(this);
            }
        }
        this._sensitiveInputCells = null;
        clearValue();
    }

    private void changeConsumingCells(CellCacheEntry[] usedCells) {
        CellCacheEntry[] prevUsedCells = this._sensitiveInputCells;
        for (CellCacheEntry addConsumingCell : usedCells) {
            addConsumingCell.addConsumingCell(this);
        }
        if (prevUsedCells != null) {
            if (nPrevUsed >= 1) {
                Set<CellCacheEntry> usedSet;
                if (nUsed < 1) {
                    usedSet = Collections.emptySet();
                } else {
                    usedSet = new HashSet((nUsed * 3) / 2);
                    for (Object add : usedCells) {
                        usedSet.add(add);
                    }
                }
                for (CellCacheEntry prevUsed : prevUsedCells) {
                    if (!usedSet.contains(prevUsed)) {
                        prevUsed.clearConsumingCell(this);
                    }
                }
            }
        }
    }

    public void updateFormulaResult(ValueEval result, CellCacheEntry[] sensitiveInputCells, FormulaUsedBlankCellSet usedBlankAreas) {
        updateValue(result);
        setSensitiveInputCells(sensitiveInputCells);
        this._usedBlankCellGroup = usedBlankAreas;
    }

    public void notifyUpdatedBlankCell(BookSheetKey bsk, int rowIndex, int columnIndex, IEvaluationListener evaluationListener) {
        if (this._usedBlankCellGroup != null && this._usedBlankCellGroup.containsCell(bsk, rowIndex, columnIndex)) {
            clearFormulaEntry();
            recurseClearCachedFormulaResults(evaluationListener);
        }
    }
}
