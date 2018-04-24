package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.EvaluationName;

public final class ExternalNameEval implements ValueEval {
    private final EvaluationName _name;

    public ExternalNameEval(EvaluationName name) {
        this._name = name;
    }

    public EvaluationName getName() {
        return this._name;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(this._name.getNameText());
        sb.append("]");
        return sb.toString();
    }
}
