package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.functions.Fixed1ArgFunction;
import org.apache.poi.ss.formula.functions.Function;

public final class UnaryMinusEval extends Fixed1ArgFunction {
    public static final Function instance = new UnaryMinusEval();

    private UnaryMinusEval() {
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        try {
            double d = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex));
            if (d == 0.0d) {
                return NumberEval.ZERO;
            }
            return new NumberEval(-d);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }
}
