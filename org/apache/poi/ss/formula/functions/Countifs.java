package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public class Countifs implements FreeRefFunction {
    public static final FreeRefFunction instance = new Countifs();

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length < 2 || args.length % 2 != 0) {
            return ErrorEval.VALUE_INVALID;
        }
        Double result = null;
        for (int i = 0; i < args.length; i += 2) {
            ValueEval firstArg = args[i];
            ValueEval secondArg = args[i + 1];
            NumberEval evaluate = (NumberEval) new Countif().evaluate(new ValueEval[]{firstArg, secondArg}, ec.getRowIndex(), ec.getColumnIndex());
            if (result == null) {
                result = Double.valueOf(evaluate.getNumberValue());
            } else if (evaluate.getNumberValue() < result.doubleValue()) {
                result = Double.valueOf(evaluate.getNumberValue());
            }
        }
        return new NumberEval(result == null ? 0.0d : result.doubleValue());
    }
}
