package org.apache.poi.ss.formula;

import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class BaseFormulaEvaluator implements FormulaEvaluator, WorkbookEvaluatorProvider {
    protected final WorkbookEvaluator _bookEvaluator;

    static /* synthetic */ class C11141 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$poi$ss$usermodel$CellType = new int[CellType.values().length];

        static {
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.BOOLEAN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.ERROR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.FORMULA.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.NUMERIC.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.STRING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.BLANK.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    protected abstract RichTextString createRichTextString(String str);

    protected abstract CellValue evaluateFormulaCellValue(Cell cell);

    protected BaseFormulaEvaluator(WorkbookEvaluator bookEvaluator) {
        this._bookEvaluator = bookEvaluator;
    }

    public static void setupEnvironment(String[] workbookNames, BaseFormulaEvaluator[] evaluators) {
        WorkbookEvaluator[] wbEvals = new WorkbookEvaluator[evaluators.length];
        for (int i = 0; i < wbEvals.length; i++) {
            wbEvals[i] = evaluators[i]._bookEvaluator;
        }
        CollaboratingWorkbooksEnvironment.setup(workbookNames, wbEvals);
    }

    public void setupReferencedWorkbooks(Map<String, FormulaEvaluator> evaluators) {
        CollaboratingWorkbooksEnvironment.setupFormulaEvaluator(evaluators);
    }

    public WorkbookEvaluator _getWorkbookEvaluator() {
        return this._bookEvaluator;
    }

    public void clearAllCachedResultValues() {
        this._bookEvaluator.clearAllCachedResultValues();
    }

    public CellValue evaluate(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (C11141.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cell.getCellTypeEnum().ordinal()]) {
            case 1:
                return CellValue.valueOf(cell.getBooleanCellValue());
            case 2:
                return CellValue.getError(cell.getErrorCellValue());
            case 3:
                return evaluateFormulaCellValue(cell);
            case 4:
                return new CellValue(cell.getNumericCellValue());
            case 5:
                return new CellValue(cell.getRichStringCellValue().getString());
            case 6:
                return null;
            default:
                throw new IllegalStateException("Bad cell type (" + cell.getCellTypeEnum() + ")");
        }
    }

    public Cell evaluateInCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        Cell result = cell;
        if (cell.getCellTypeEnum() != CellType.FORMULA) {
            return result;
        }
        CellValue cv = evaluateFormulaCellValue(cell);
        setCellValue(cell, cv);
        setCellType(cell, cv);
        return result;
    }

    public int evaluateFormulaCell(Cell cell) {
        return evaluateFormulaCellEnum(cell).getCode();
    }

    public CellType evaluateFormulaCellEnum(Cell cell) {
        if (cell == null || cell.getCellTypeEnum() != CellType.FORMULA) {
            return CellType._NONE;
        }
        CellValue cv = evaluateFormulaCellValue(cell);
        setCellValue(cell, cv);
        return cv.getCellTypeEnum();
    }

    protected static void setCellType(Cell cell, CellValue cv) {
        CellType cellType = cv.getCellTypeEnum();
        switch (C11141.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()]) {
            case 1:
            case 2:
            case 4:
            case 5:
                cell.setCellType(cellType);
                return;
            case 3:
                throw new IllegalArgumentException("This should never happen. Formulas should have already been evaluated.");
            case 6:
                throw new IllegalArgumentException("This should never happen. Blanks eventually get translated to zero.");
            default:
                throw new IllegalStateException("Unexpected cell value type (" + cellType + ")");
        }
    }

    protected void setCellValue(Cell cell, CellValue cv) {
        CellType cellType = cv.getCellTypeEnum();
        switch (C11141.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()]) {
            case 1:
                cell.setCellValue(cv.getBooleanValue());
                return;
            case 2:
                cell.setCellErrorValue(cv.getErrorValue());
                return;
            case 4:
                cell.setCellValue(cv.getNumberValue());
                return;
            case 5:
                cell.setCellValue(createRichTextString(cv.getStringValue()));
                return;
            default:
                throw new IllegalStateException("Unexpected cell value type (" + cellType + ")");
        }
    }

    public static void evaluateAllFormulaCells(Workbook wb) {
        evaluateAllFormulaCells(wb, wb.getCreationHelper().createFormulaEvaluator());
    }

    protected static void evaluateAllFormulaCells(Workbook wb, FormulaEvaluator evaluator) {
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            for (Row<Cell> r : wb.getSheetAt(i)) {
                for (Cell c : r) {
                    if (c.getCellTypeEnum() == CellType.FORMULA) {
                        evaluator.evaluateFormulaCellEnum(c);
                    }
                }
            }
        }
    }

    public void setIgnoreMissingWorkbooks(boolean ignore) {
        this._bookEvaluator.setIgnoreMissingWorkbooks(ignore);
    }

    public void setDebugEvaluationOutputForNextEval(boolean value) {
        this._bookEvaluator.setDebugEvaluationOutputForNextEval(value);
    }
}
