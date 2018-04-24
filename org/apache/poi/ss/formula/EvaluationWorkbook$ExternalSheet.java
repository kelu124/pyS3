package org.apache.poi.ss.formula;

public class EvaluationWorkbook$ExternalSheet {
    private final String _sheetName;
    private final String _workbookName;

    public EvaluationWorkbook$ExternalSheet(String workbookName, String sheetName) {
        this._workbookName = workbookName;
        this._sheetName = sheetName;
    }

    public String getWorkbookName() {
        return this._workbookName;
    }

    public String getSheetName() {
        return this._sheetName;
    }
}
