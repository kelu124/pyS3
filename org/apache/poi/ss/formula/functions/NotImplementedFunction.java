package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.NotImplementedFunctionException;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class NotImplementedFunction implements Function {
    private final String _functionName;

    protected NotImplementedFunction() {
        this._functionName = getClass().getName();
    }

    public NotImplementedFunction(String name) {
        this._functionName = name;
    }

    public ValueEval evaluate(ValueEval[] operands, int srcRow, int srcCol) {
        throw new NotImplementedFunctionException(this._functionName);
    }

    public String getFunctionName() {
        return this._functionName;
    }
}
