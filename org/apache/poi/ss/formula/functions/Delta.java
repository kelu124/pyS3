package org.apache.poi.ss.formula.functions;

import java.math.BigDecimal;
import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Delta extends Fixed2ArgFunction implements FreeRefFunction {
    private static final NumberEval ONE = new NumberEval(1.0d);
    private static final NumberEval ZERO = new NumberEval(0.0d);
    public static final FreeRefFunction instance = new Delta();

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg1, ValueEval arg2) {
        try {
            Double number1 = OperandResolver.parseDouble(OperandResolver.coerceValueToString(OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex)));
            if (number1 == null) {
                return ErrorEval.VALUE_INVALID;
            }
            try {
                Double number2 = OperandResolver.parseDouble(OperandResolver.coerceValueToString(OperandResolver.getSingleValue(arg2, srcRowIndex, srcColumnIndex)));
                if (number2 == null) {
                    return ErrorEval.VALUE_INVALID;
                }
                return new BigDecimal(number1.doubleValue()).compareTo(new BigDecimal(number2.doubleValue())) == 0 ? ONE : ZERO;
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        } catch (EvaluationException e2) {
            return e2.getErrorEval();
        }
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length == 2) {
            return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1]);
        }
        return ErrorEval.VALUE_INVALID;
    }
}
