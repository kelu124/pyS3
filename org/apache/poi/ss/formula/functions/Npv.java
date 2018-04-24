package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Npv implements Function {
    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        if (args.length < 2) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            double rate = NumericFunction.singleOperandEvaluate(args[0], srcRowIndex, srcColumnIndex);
            ValueEval[] vargs = new ValueEval[(args.length - 1)];
            System.arraycopy(args, 1, vargs, 0, vargs.length);
            double result = FinanceLib.npv(rate, ValueCollector.collectValues(vargs));
            NumericFunction.checkValue(result);
            return new NumberEval(result);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }
}
