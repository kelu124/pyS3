package org.apache.poi.ss.usermodel;

import java.util.Map;
import org.apache.poi.util.Internal;

public interface FormulaEvaluator {
    void clearAllCachedResultValues();

    CellValue evaluate(Cell cell);

    void evaluateAll();

    int evaluateFormulaCell(Cell cell);

    @Internal(since = "POI 3.15 beta 3")
    CellType evaluateFormulaCellEnum(Cell cell);

    Cell evaluateInCell(Cell cell);

    void notifyDeleteCell(Cell cell);

    void notifySetFormula(Cell cell);

    void notifyUpdateCell(Cell cell);

    void setDebugEvaluationOutputForNextEval(boolean z);

    void setIgnoreMissingWorkbooks(boolean z);

    void setupReferencedWorkbooks(Map<String, FormulaEvaluator> map);
}
