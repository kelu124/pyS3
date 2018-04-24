package org.apache.poi.ss.formula;

public class EvaluationWorkbook$ExternalName {
    private final int _ix;
    private final String _nameName;
    private final int _nameNumber;

    public EvaluationWorkbook$ExternalName(String nameName, int nameNumber, int ix) {
        this._nameName = nameName;
        this._nameNumber = nameNumber;
        this._ix = ix;
    }

    public String getName() {
        return this._nameName;
    }

    public int getNumber() {
        return this._nameNumber;
    }

    public int getIx() {
        return this._ix;
    }
}
