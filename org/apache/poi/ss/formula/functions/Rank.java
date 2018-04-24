package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public class Rank extends Var2or3ArgFunction {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        try {
            double result = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex));
            if (!Double.isNaN(result) && !Double.isInfinite(result)) {
                return eval(srcRowIndex, srcColumnIndex, result, convertRangeArg(arg1), true);
            }
            throw new EvaluationException(ErrorEval.NUM_ERROR);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        try {
            double result = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex));
            if (Double.isNaN(result) || Double.isInfinite(result)) {
                throw new EvaluationException(ErrorEval.NUM_ERROR);
            }
            boolean order;
            AreaEval aeRange = convertRangeArg(arg1);
            int order_value = OperandResolver.coerceValueToInt(OperandResolver.getSingleValue(arg2, srcRowIndex, srcColumnIndex));
            if (order_value == 0) {
                order = true;
            } else if (order_value == 1) {
                order = false;
            } else {
                throw new EvaluationException(ErrorEval.NUM_ERROR);
            }
            return eval(srcRowIndex, srcColumnIndex, result, aeRange, order);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static ValueEval eval(int srcRowIndex, int srcColumnIndex, double arg0, AreaEval aeRange, boolean descending_order) {
        int rank = 1;
        int height = aeRange.getHeight();
        int width = aeRange.getWidth();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                Double value = getValue(aeRange, r, c);
                if (value != null && ((descending_order && value.doubleValue() > arg0) || (!descending_order && value.doubleValue() < arg0))) {
                    rank++;
                }
            }
        }
        return new NumberEval((double) rank);
    }

    private static Double getValue(AreaEval aeRange, int relRowIndex, int relColIndex) {
        ValueEval addend = aeRange.getRelativeValue(relRowIndex, relColIndex);
        if (addend instanceof NumberEval) {
            return Double.valueOf(((NumberEval) addend).getNumberValue());
        }
        return null;
    }

    private static AreaEval convertRangeArg(ValueEval eval) throws EvaluationException {
        if (eval instanceof AreaEval) {
            return (AreaEval) eval;
        }
        if (eval instanceof RefEval) {
            return ((RefEval) eval).offset(0, 0, 0, 0);
        }
        throw new EvaluationException(ErrorEval.VALUE_INVALID);
    }
}
