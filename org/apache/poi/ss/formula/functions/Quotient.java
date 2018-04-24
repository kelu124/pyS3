package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;

public class Quotient extends Fixed2ArgFunction implements FreeRefFunction {
    public static final FreeRefFunction instance = new Quotient();

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval venumerator, ValueEval vedenominator) {
        try {
            double enumerator = OperandResolver.coerceValueToDouble(venumerator);
            try {
                double denominator = OperandResolver.coerceValueToDouble(vedenominator);
                if (denominator == 0.0d) {
                    return ErrorEval.DIV_ZERO;
                }
                return new NumberEval((double) ((int) (enumerator / denominator)));
            } catch (EvaluationException e) {
                return ErrorEval.VALUE_INVALID;
            }
        } catch (EvaluationException e2) {
            return ErrorEval.VALUE_INVALID;
        }
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length != 2) {
            return ErrorEval.VALUE_INVALID;
        }
        return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1]);
    }
}
