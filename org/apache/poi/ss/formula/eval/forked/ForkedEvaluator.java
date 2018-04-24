package org.apache.poi.ss.formula.eval.forked;

import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment;
import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationWorkbook;
import org.apache.poi.ss.formula.IStabilityClassifier;
import org.apache.poi.ss.formula.WorkbookEvaluator;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;

public final class ForkedEvaluator {
    private WorkbookEvaluator _evaluator;
    private ForkedEvaluationWorkbook _sewb;

    static /* synthetic */ class C11301 {
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

    private ForkedEvaluator(EvaluationWorkbook masterWorkbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        this._sewb = new ForkedEvaluationWorkbook(masterWorkbook);
        this._evaluator = new WorkbookEvaluator(this._sewb, stabilityClassifier, udfFinder);
    }

    private static EvaluationWorkbook createEvaluationWorkbook(Workbook wb) {
        if (wb instanceof HSSFWorkbook) {
            return HSSFEvaluationWorkbook.create((HSSFWorkbook) wb);
        }
        try {
            Class<?> evalWB = Class.forName("org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook");
            Class<?> xssfWB = Class.forName("org.apache.poi.xssf.usermodel.XSSFWorkbook");
            return (EvaluationWorkbook) evalWB.getDeclaredMethod("create", new Class[]{xssfWB}).invoke(null, new Object[]{wb});
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected workbook type (" + wb.getClass().getName() + ") - check for poi-ooxml and poi-ooxml schemas jar in the classpath", e);
        }
    }

    public static ForkedEvaluator create(Workbook wb, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        return new ForkedEvaluator(createEvaluationWorkbook(wb), stabilityClassifier, udfFinder);
    }

    public void updateCell(String sheetName, int rowIndex, int columnIndex, ValueEval value) {
        ForkedEvaluationCell cell = this._sewb.getOrCreateUpdatableCell(sheetName, rowIndex, columnIndex);
        cell.setValue(value);
        this._evaluator.notifyUpdateCell(cell);
    }

    public void copyUpdatedCells(Workbook workbook) {
        this._sewb.copyUpdatedCells(workbook);
    }

    public ValueEval evaluate(String sheetName, int rowIndex, int columnIndex) {
        EvaluationCell cell = this._sewb.getEvaluationCell(sheetName, rowIndex, columnIndex);
        switch (C11301.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cell.getCellTypeEnum().ordinal()]) {
            case 1:
                return BoolEval.valueOf(cell.getBooleanCellValue());
            case 2:
                return ErrorEval.valueOf(cell.getErrorCellValue());
            case 3:
                return this._evaluator.evaluate(cell);
            case 4:
                return new NumberEval(cell.getNumericCellValue());
            case 5:
                return new StringEval(cell.getStringCellValue());
            case 6:
                return null;
            default:
                throw new IllegalStateException("Bad cell type (" + cell.getCellTypeEnum() + ")");
        }
    }

    public static void setupEnvironment(String[] workbookNames, ForkedEvaluator[] evaluators) {
        WorkbookEvaluator[] wbEvals = new WorkbookEvaluator[evaluators.length];
        for (int i = 0; i < wbEvals.length; i++) {
            wbEvals[i] = evaluators[i]._evaluator;
        }
        CollaboratingWorkbooksEnvironment.setup(workbookNames, wbEvals);
    }
}
