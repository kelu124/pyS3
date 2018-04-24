package org.apache.poi.hssf.usermodel;

import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.ss.usermodel.CellType;

final class HSSFEvaluationCell implements EvaluationCell {
    private final HSSFCell _cell;
    private final EvaluationSheet _evalSheet;

    public HSSFEvaluationCell(HSSFCell cell, EvaluationSheet evalSheet) {
        this._cell = cell;
        this._evalSheet = evalSheet;
    }

    public HSSFEvaluationCell(HSSFCell cell) {
        this(cell, new HSSFEvaluationSheet(cell.getSheet()));
    }

    public Object getIdentityKey() {
        return this._cell;
    }

    public HSSFCell getHSSFCell() {
        return this._cell;
    }

    public boolean getBooleanCellValue() {
        return this._cell.getBooleanCellValue();
    }

    public int getCellType() {
        return this._cell.getCellType();
    }

    public CellType getCellTypeEnum() {
        return this._cell.getCellTypeEnum();
    }

    public int getColumnIndex() {
        return this._cell.getColumnIndex();
    }

    public int getErrorCellValue() {
        return this._cell.getErrorCellValue();
    }

    public double getNumericCellValue() {
        return this._cell.getNumericCellValue();
    }

    public int getRowIndex() {
        return this._cell.getRowIndex();
    }

    public EvaluationSheet getSheet() {
        return this._evalSheet;
    }

    public String getStringCellValue() {
        return this._cell.getRichStringCellValue().getString();
    }

    public int getCachedFormulaResultType() {
        return this._cell.getCachedFormulaResultType();
    }

    public CellType getCachedFormulaResultTypeEnum() {
        return this._cell.getCachedFormulaResultTypeEnum();
    }
}
