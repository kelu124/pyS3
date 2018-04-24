package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.functions.Fixed2ArgFunction;
import org.apache.poi.ss.formula.functions.Function;

public final class ConcatEval extends Fixed2ArgFunction {
    public static final Function instance = new ConcatEval();

    private ConcatEval() {
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        try {
            ValueEval ve0 = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
            ValueEval ve1 = OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex);
            StringBuilder sb = new StringBuilder();
            sb.append(getText(ve0));
            sb.append(getText(ve1));
            return new StringEval(sb.toString());
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private Object getText(ValueEval ve) {
        if (ve instanceof StringValueEval) {
            return ((StringValueEval) ve).getStringValue();
        }
        if (ve == BlankEval.instance) {
            return "";
        }
        throw new IllegalAccessError("Unexpected value type (" + ve.getClass().getName() + ")");
    }
}
