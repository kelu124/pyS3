package org.apache.poi.ss.formula;

public class EvaluationWorkbook$ExternalSheetRange extends EvaluationWorkbook$ExternalSheet {
    private final String _lastSheetName;

    public EvaluationWorkbook$ExternalSheetRange(String workbookName, String firstSheetName, String lastSheetName) {
        super(workbookName, firstSheetName);
        this._lastSheetName = lastSheetName;
    }

    public String getFirstSheetName() {
        return getSheetName();
    }

    public String getLastSheetName() {
        return this._lastSheetName;
    }
}
