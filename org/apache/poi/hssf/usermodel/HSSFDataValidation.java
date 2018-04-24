package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.DVRecord;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.util.CellRangeAddressList;

public final class HSSFDataValidation implements DataValidation {
    private DVConstraint _constraint;
    private boolean _emptyCellAllowed = true;
    private int _errorStyle = 0;
    private String _error_text;
    private String _error_title;
    private String _prompt_text;
    private String _prompt_title;
    private CellRangeAddressList _regions;
    private boolean _showErrorBox = true;
    private boolean _showPromptBox = true;
    private boolean _suppress_dropdown_arrow = false;

    public HSSFDataValidation(CellRangeAddressList regions, DataValidationConstraint constraint) {
        this._regions = regions;
        this._constraint = (DVConstraint) constraint;
    }

    public DataValidationConstraint getValidationConstraint() {
        return this._constraint;
    }

    public DVConstraint getConstraint() {
        return this._constraint;
    }

    public CellRangeAddressList getRegions() {
        return this._regions;
    }

    public void setErrorStyle(int error_style) {
        this._errorStyle = error_style;
    }

    public int getErrorStyle() {
        return this._errorStyle;
    }

    public void setEmptyCellAllowed(boolean allowed) {
        this._emptyCellAllowed = allowed;
    }

    public boolean getEmptyCellAllowed() {
        return this._emptyCellAllowed;
    }

    public void setSuppressDropDownArrow(boolean suppress) {
        this._suppress_dropdown_arrow = suppress;
    }

    public boolean getSuppressDropDownArrow() {
        if (this._constraint.getValidationType() == 3) {
            return this._suppress_dropdown_arrow;
        }
        return false;
    }

    public void setShowPromptBox(boolean show) {
        this._showPromptBox = show;
    }

    public boolean getShowPromptBox() {
        return this._showPromptBox;
    }

    public void setShowErrorBox(boolean show) {
        this._showErrorBox = show;
    }

    public boolean getShowErrorBox() {
        return this._showErrorBox;
    }

    public void createPromptBox(String title, String text) {
        this._prompt_title = title;
        this._prompt_text = text;
        setShowPromptBox(true);
    }

    public String getPromptBoxTitle() {
        return this._prompt_title;
    }

    public String getPromptBoxText() {
        return this._prompt_text;
    }

    public void createErrorBox(String title, String text) {
        this._error_title = title;
        this._error_text = text;
        setShowErrorBox(true);
    }

    public String getErrorBoxTitle() {
        return this._error_title;
    }

    public String getErrorBoxText() {
        return this._error_text;
    }

    public DVRecord createDVRecord(HSSFSheet sheet) {
        FormulaPair fp = this._constraint.createFormulas(sheet);
        int validationType = this._constraint.getValidationType();
        int operator = this._constraint.getOperator();
        int i = this._errorStyle;
        boolean z = this._emptyCellAllowed;
        boolean suppressDropDownArrow = getSuppressDropDownArrow();
        boolean z2 = this._constraint.getValidationType() == 3 && this._constraint.getExplicitListValues() != null;
        return new DVRecord(validationType, operator, i, z, suppressDropDownArrow, z2, this._showPromptBox, this._prompt_title, this._prompt_text, this._showErrorBox, this._error_title, this._error_text, fp.getFormula1(), fp.getFormula2(), this._regions);
    }
}
