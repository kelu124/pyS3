package org.apache.poi.ss.formula.eval.forked;

import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationName;
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.ss.formula.EvaluationWorkbook;
import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalName;
import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheet;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.Internal;

@Internal
final class ForkedEvaluationWorkbook implements EvaluationWorkbook {
    private final EvaluationWorkbook _masterBook;
    private final Map<String, ForkedEvaluationSheet> _sharedSheetsByName = new HashMap();

    public ForkedEvaluationWorkbook(EvaluationWorkbook master) {
        this._masterBook = master;
    }

    public ForkedEvaluationCell getOrCreateUpdatableCell(String sheetName, int rowIndex, int columnIndex) {
        return getSharedSheet(sheetName).getOrCreateUpdatableCell(rowIndex, columnIndex);
    }

    public EvaluationCell getEvaluationCell(String sheetName, int rowIndex, int columnIndex) {
        return getSharedSheet(sheetName).getCell(rowIndex, columnIndex);
    }

    private ForkedEvaluationSheet getSharedSheet(String sheetName) {
        ForkedEvaluationSheet result = (ForkedEvaluationSheet) this._sharedSheetsByName.get(sheetName);
        if (result != null) {
            return result;
        }
        result = new ForkedEvaluationSheet(this._masterBook.getSheet(this._masterBook.getSheetIndex(sheetName)));
        this._sharedSheetsByName.put(sheetName, result);
        return result;
    }

    public void copyUpdatedCells(Workbook workbook) {
        String[] sheetNames = new String[this._sharedSheetsByName.size()];
        this._sharedSheetsByName.keySet().toArray(sheetNames);
        for (String sheetName : sheetNames) {
            ((ForkedEvaluationSheet) this._sharedSheetsByName.get(sheetName)).copyUpdatedCells(workbook.getSheet(sheetName));
        }
    }

    public int convertFromExternSheetIndex(int externSheetIndex) {
        return this._masterBook.convertFromExternSheetIndex(externSheetIndex);
    }

    public ExternalSheet getExternalSheet(int externSheetIndex) {
        return this._masterBook.getExternalSheet(externSheetIndex);
    }

    public ExternalSheet getExternalSheet(String firstSheetName, String lastSheetName, int externalWorkbookNumber) {
        return this._masterBook.getExternalSheet(firstSheetName, lastSheetName, externalWorkbookNumber);
    }

    public Ptg[] getFormulaTokens(EvaluationCell cell) {
        if (!(cell instanceof ForkedEvaluationCell)) {
            return this._masterBook.getFormulaTokens(cell);
        }
        throw new RuntimeException("Updated formulas not supported yet");
    }

    public EvaluationName getName(NamePtg namePtg) {
        return this._masterBook.getName(namePtg);
    }

    public EvaluationName getName(String name, int sheetIndex) {
        return this._masterBook.getName(name, sheetIndex);
    }

    public EvaluationSheet getSheet(int sheetIndex) {
        return getSharedSheet(getSheetName(sheetIndex));
    }

    public ExternalName getExternalName(int externSheetIndex, int externNameIndex) {
        return this._masterBook.getExternalName(externSheetIndex, externNameIndex);
    }

    public ExternalName getExternalName(String nameName, String sheetName, int externalWorkbookNumber) {
        return this._masterBook.getExternalName(nameName, sheetName, externalWorkbookNumber);
    }

    public int getSheetIndex(EvaluationSheet sheet) {
        if (sheet instanceof ForkedEvaluationSheet) {
            return ((ForkedEvaluationSheet) sheet).getSheetIndex(this._masterBook);
        }
        return this._masterBook.getSheetIndex(sheet);
    }

    public int getSheetIndex(String sheetName) {
        return this._masterBook.getSheetIndex(sheetName);
    }

    public String getSheetName(int sheetIndex) {
        return this._masterBook.getSheetName(sheetIndex);
    }

    public String resolveNameXText(NameXPtg ptg) {
        return this._masterBook.resolveNameXText(ptg);
    }

    public UDFFinder getUDFFinder() {
        return this._masterBook.getUDFFinder();
    }

    public void clearAllCachedResultValues() {
        this._masterBook.clearAllCachedResultValues();
    }
}
