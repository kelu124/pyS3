package org.apache.poi.ss.formula;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.ValueEval;

final class EvaluationTracker {
    private final EvaluationCache _cache;
    private final Set<FormulaCellCacheEntry> _currentlyEvaluatingCells = new HashSet();
    private final List<CellEvaluationFrame> _evaluationFrames = new ArrayList();

    public EvaluationTracker(EvaluationCache cache) {
        this._cache = cache;
    }

    public boolean startEvaluate(FormulaCellCacheEntry cce) {
        if (cce == null) {
            throw new IllegalArgumentException("cellLoc must not be null");
        } else if (this._currentlyEvaluatingCells.contains(cce)) {
            return false;
        } else {
            this._currentlyEvaluatingCells.add(cce);
            this._evaluationFrames.add(new CellEvaluationFrame(cce));
            return true;
        }
    }

    public void updateCacheResult(ValueEval result) {
        int nFrames = this._evaluationFrames.size();
        if (nFrames < 1) {
            throw new IllegalStateException("Call to endEvaluate without matching call to startEvaluate");
        }
        CellEvaluationFrame frame = (CellEvaluationFrame) this._evaluationFrames.get(nFrames - 1);
        if (result != ErrorEval.CIRCULAR_REF_ERROR || nFrames <= 1) {
            frame.updateFormulaResult(result);
        }
    }

    public void endEvaluate(CellCacheEntry cce) {
        int nFrames = this._evaluationFrames.size();
        if (nFrames < 1) {
            throw new IllegalStateException("Call to endEvaluate without matching call to startEvaluate");
        }
        nFrames--;
        if (cce != ((CellEvaluationFrame) this._evaluationFrames.get(nFrames)).getCCE()) {
            throw new IllegalStateException("Wrong cell specified. ");
        }
        this._evaluationFrames.remove(nFrames);
        this._currentlyEvaluatingCells.remove(cce);
    }

    public void acceptFormulaDependency(CellCacheEntry cce) {
        int prevFrameIndex = this._evaluationFrames.size() - 1;
        if (prevFrameIndex >= 0) {
            ((CellEvaluationFrame) this._evaluationFrames.get(prevFrameIndex)).addSensitiveInputCell(cce);
        }
    }

    public void acceptPlainValueDependency(int bookIndex, int sheetIndex, int rowIndex, int columnIndex, ValueEval value) {
        int prevFrameIndex = this._evaluationFrames.size() - 1;
        if (prevFrameIndex >= 0) {
            CellEvaluationFrame consumingFrame = (CellEvaluationFrame) this._evaluationFrames.get(prevFrameIndex);
            if (value == BlankEval.instance) {
                consumingFrame.addUsedBlankCell(bookIndex, sheetIndex, rowIndex, columnIndex);
            } else {
                consumingFrame.addSensitiveInputCell(this._cache.getPlainValueEntry(bookIndex, sheetIndex, rowIndex, columnIndex, value));
            }
        }
    }
}
