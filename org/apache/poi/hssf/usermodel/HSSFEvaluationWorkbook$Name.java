package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.ss.formula.EvaluationName;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.Ptg;

final class HSSFEvaluationWorkbook$Name implements EvaluationName {
    private final int _index;
    private final NameRecord _nameRecord;

    public HSSFEvaluationWorkbook$Name(NameRecord nameRecord, int index) {
        this._nameRecord = nameRecord;
        this._index = index;
    }

    public Ptg[] getNameDefinition() {
        return this._nameRecord.getNameDefinition();
    }

    public String getNameText() {
        return this._nameRecord.getNameText();
    }

    public boolean hasFormula() {
        return this._nameRecord.hasFormula();
    }

    public boolean isFunctionName() {
        return this._nameRecord.isFunctionName();
    }

    public boolean isRange() {
        return this._nameRecord.hasFormula();
    }

    public NamePtg createPtg() {
        return new NamePtg(this._index);
    }
}
