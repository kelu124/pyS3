package org.apache.poi.ss.formula.functions;

import java.math.BigInteger;
import java.util.HashMap;
import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;

public class FactDouble extends Fixed1ArgFunction implements FreeRefFunction {
    static HashMap<Integer, BigInteger> cache = new HashMap();
    public static final FreeRefFunction instance = new FactDouble();

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE) {
        try {
            int number = OperandResolver.coerceValueToInt(numberVE);
            if (number < 0) {
                return ErrorEval.NUM_ERROR;
            }
            return new NumberEval((double) factorial(number).longValue());
        } catch (EvaluationException e) {
            return ErrorEval.VALUE_INVALID;
        }
    }

    public static BigInteger factorial(int n) {
        if (n == 0 || n < 0) {
            return BigInteger.ONE;
        }
        if (cache.containsKey(Integer.valueOf(n))) {
            return (BigInteger) cache.get(Integer.valueOf(n));
        }
        BigInteger result = BigInteger.valueOf((long) n).multiply(factorial(n - 2));
        cache.put(Integer.valueOf(n), result);
        return result;
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length != 1) {
            return ErrorEval.VALUE_INVALID;
        }
        return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
    }
}
