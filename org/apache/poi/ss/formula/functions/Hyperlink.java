package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;

public final class Hyperlink extends Var1or2ArgFunction {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        return arg0;
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        return arg1;
    }
}
