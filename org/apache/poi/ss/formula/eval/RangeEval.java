package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.functions.Fixed2ArgFunction;
import org.apache.poi.ss.formula.functions.Function;

public final class RangeEval extends Fixed2ArgFunction {
    public static final Function instance = new RangeEval();

    private RangeEval() {
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        try {
            return resolveRange(evaluateRef(arg0), evaluateRef(arg1));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static AreaEval resolveRange(AreaEval aeA, AreaEval aeB) {
        int aeAfr = aeA.getFirstRow();
        int aeAfc = aeA.getFirstColumn();
        return aeA.offset(Math.min(aeAfr, aeB.getFirstRow()) - aeAfr, Math.max(aeA.getLastRow(), aeB.getLastRow()) - aeAfr, Math.min(aeAfc, aeB.getFirstColumn()) - aeAfc, Math.max(aeA.getLastColumn(), aeB.getLastColumn()) - aeAfc);
    }

    private static AreaEval evaluateRef(ValueEval arg) throws EvaluationException {
        if (arg instanceof AreaEval) {
            return (AreaEval) arg;
        }
        if (arg instanceof RefEval) {
            return ((RefEval) arg).offset(0, 0, 0, 0);
        }
        if (arg instanceof ErrorEval) {
            throw new EvaluationException((ErrorEval) arg);
        }
        throw new IllegalArgumentException("Unexpected ref arg class (" + arg.getClass().getName() + ")");
    }
}
