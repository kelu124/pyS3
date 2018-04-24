package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.LookupUtils.ValueVector;

public final class Hlookup extends Var3or4ArgFunction {
    private static final ValueEval DEFAULT_ARG3 = BoolEval.TRUE;

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, DEFAULT_ARG3);
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3) {
        try {
            ValueEval lookupValue = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
            TwoDEval tableArray = LookupUtils.resolveTableArrayArg(arg1);
            return createResultColumnVector(tableArray, LookupUtils.resolveRowOrColIndexArg(arg2, srcRowIndex, srcColumnIndex)).getItem(LookupUtils.lookupIndexOfValue(lookupValue, LookupUtils.createRowVector(tableArray, 0), LookupUtils.resolveRangeLookupArg(arg3, srcRowIndex, srcColumnIndex)));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private ValueVector createResultColumnVector(TwoDEval tableArray, int rowIndex) throws EvaluationException {
        if (rowIndex < tableArray.getHeight()) {
            return LookupUtils.createRowVector(tableArray, rowIndex);
        }
        throw EvaluationException.invalidRef();
    }
}
