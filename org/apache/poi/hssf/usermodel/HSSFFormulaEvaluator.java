package org.apache.poi.hssf.usermodel;

import java.util.Map;
import org.apache.poi.ss.formula.BaseFormulaEvaluator;
import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment;
import org.apache.poi.ss.formula.IStabilityClassifier;
import org.apache.poi.ss.formula.WorkbookEvaluator;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumericValueEval;
import org.apache.poi.ss.formula.eval.StringValueEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Workbook;

public class HSSFFormulaEvaluator extends BaseFormulaEvaluator {
    private final HSSFWorkbook _book;

    public HSSFFormulaEvaluator(HSSFWorkbook workbook) {
        this(workbook, null);
    }

    public HSSFFormulaEvaluator(HSSFWorkbook workbook, IStabilityClassifier stabilityClassifier) {
        this(workbook, stabilityClassifier, null);
    }

    private HSSFFormulaEvaluator(HSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        super(new WorkbookEvaluator(HSSFEvaluationWorkbook.create(workbook), stabilityClassifier, udfFinder));
        this._book = workbook;
    }

    public static HSSFFormulaEvaluator create(HSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        return new HSSFFormulaEvaluator(workbook, stabilityClassifier, udfFinder);
    }

    protected RichTextString createRichTextString(String str) {
        return new HSSFRichTextString(str);
    }

    public static void setupEnvironment(String[] workbookNames, HSSFFormulaEvaluator[] evaluators) {
        BaseFormulaEvaluator.setupEnvironment(workbookNames, evaluators);
    }

    public void setupReferencedWorkbooks(Map<String, FormulaEvaluator> evaluators) {
        CollaboratingWorkbooksEnvironment.setupFormulaEvaluator(evaluators);
    }

    public void notifyUpdateCell(HSSFCell cell) {
        this._bookEvaluator.notifyUpdateCell(new HSSFEvaluationCell(cell));
    }

    public void notifyUpdateCell(Cell cell) {
        this._bookEvaluator.notifyUpdateCell(new HSSFEvaluationCell((HSSFCell) cell));
    }

    public void notifyDeleteCell(HSSFCell cell) {
        this._bookEvaluator.notifyDeleteCell(new HSSFEvaluationCell(cell));
    }

    public void notifyDeleteCell(Cell cell) {
        this._bookEvaluator.notifyDeleteCell(new HSSFEvaluationCell((HSSFCell) cell));
    }

    public void notifySetFormula(Cell cell) {
        this._bookEvaluator.notifyUpdateCell(new HSSFEvaluationCell((HSSFCell) cell));
    }

    public HSSFCell evaluateInCell(Cell cell) {
        return (HSSFCell) super.evaluateInCell(cell);
    }

    public static void evaluateAllFormulaCells(HSSFWorkbook wb) {
        BaseFormulaEvaluator.evaluateAllFormulaCells(wb, new HSSFFormulaEvaluator(wb));
    }

    public static void evaluateAllFormulaCells(Workbook wb) {
        BaseFormulaEvaluator.evaluateAllFormulaCells(wb);
    }

    public void evaluateAll() {
        BaseFormulaEvaluator.evaluateAllFormulaCells(this._book, this);
    }

    protected CellValue evaluateFormulaCellValue(Cell cell) {
        ValueEval eval = this._bookEvaluator.evaluate(new HSSFEvaluationCell((HSSFCell) cell));
        if (eval instanceof BoolEval) {
            return CellValue.valueOf(((BoolEval) eval).getBooleanValue());
        }
        if (eval instanceof NumericValueEval) {
            return new CellValue(((NumericValueEval) eval).getNumberValue());
        }
        if (eval instanceof StringValueEval) {
            return new CellValue(((StringValueEval) eval).getStringValue());
        }
        if (eval instanceof ErrorEval) {
            return CellValue.getError(((ErrorEval) eval).getErrorCode());
        }
        throw new RuntimeException("Unexpected eval class (" + eval.getClass().getName() + ")");
    }

    public void setIgnoreMissingWorkbooks(boolean ignore) {
        this._bookEvaluator.setIgnoreMissingWorkbooks(ignore);
    }

    public void setDebugEvaluationOutputForNextEval(boolean value) {
        this._bookEvaluator.setDebugEvaluationOutputForNextEval(value);
    }
}
