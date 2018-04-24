package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.LookupUtils.ValueVector;

public final class Vlookup extends Var3or4ArgFunction {
    private static final ValueEval DEFAULT_ARG3 = BoolEval.TRUE;

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, DEFAULT_ARG3);
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval lookup_value, ValueEval table_array, ValueEval col_index, ValueEval range_lookup) {
        try {
            ValueEval lookupValue = OperandResolver.getSingleValue(lookup_value, srcRowIndex, srcColumnIndex);
            TwoDEval tableArray = LookupUtils.resolveTableArrayArg(table_array);
            return createResultColumnVector(tableArray, LookupUtils.resolveRowOrColIndexArg(col_index, srcRowIndex, srcColumnIndex)).getItem(LookupUtils.lookupIndexOfValue(lookupValue, LookupUtils.createColumnVector(tableArray, 0), LookupUtils.resolveRangeLookupArg(range_lookup, srcRowIndex, srcColumnIndex)));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private ValueVector createResultColumnVector(TwoDEval tableArray, int colIndex) throws EvaluationException {
        if (colIndex < tableArray.getWidth()) {
            return LookupUtils.createColumnVector(tableArray, colIndex);
        }
        throw EvaluationException.invalidRef();
    }
}
