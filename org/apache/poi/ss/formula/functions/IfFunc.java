package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class IfFunc extends Var2or3ArgFunction {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        try {
            if (!evaluateFirstArg(arg0, srcRowIndex, srcColumnIndex)) {
                return BoolEval.FALSE;
            }
            if (arg1 == MissingArgEval.instance) {
                return BlankEval.instance;
            }
            return arg1;
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        try {
            if (!evaluateFirstArg(arg0, srcRowIndex, srcColumnIndex)) {
                return arg2 == MissingArgEval.instance ? BlankEval.instance : arg2;
            } else {
                if (arg1 == MissingArgEval.instance) {
                    return BlankEval.instance;
                }
                return arg1;
            }
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    public static boolean evaluateFirstArg(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        Boolean b = OperandResolver.coerceValueToBoolean(OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol), false);
        if (b == null) {
            return false;
        }
        return b.booleanValue();
    }
}
