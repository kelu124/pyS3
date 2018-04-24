package org.apache.poi.ss.formula.functions;

import java.util.Locale;
import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Dec2Hex extends Var1or2ArgFunction implements FreeRefFunction {
    private static final int DEFAULT_PLACES_VALUE = 10;
    private static final long MAX_VALUE = Long.parseLong("549755813887");
    private static final long MIN_VALUE = Long.parseLong("-549755813888");
    public static final FreeRefFunction instance = new Dec2Hex();

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval number, ValueEval places) {
        try {
            Double number1 = OperandResolver.parseDouble(OperandResolver.coerceValueToString(OperandResolver.getSingleValue(number, srcRowIndex, srcColumnIndex)));
            if (number1 == null) {
                return ErrorEval.VALUE_INVALID;
            }
            if (number1.longValue() < MIN_VALUE || number1.longValue() > MAX_VALUE) {
                return ErrorEval.NUM_ERROR;
            }
            String hex;
            int placesNumber = 0;
            if (number1.doubleValue() < 0.0d) {
                placesNumber = 10;
            } else if (places != null) {
                try {
                    Double placesNumberDouble = OperandResolver.parseDouble(OperandResolver.coerceValueToString(OperandResolver.getSingleValue(places, srcRowIndex, srcColumnIndex)));
                    if (placesNumberDouble == null) {
                        return ErrorEval.VALUE_INVALID;
                    }
                    placesNumber = placesNumberDouble.intValue();
                    if (placesNumber < 0) {
                        return ErrorEval.NUM_ERROR;
                    }
                } catch (EvaluationException e) {
                    return e.getErrorEval();
                }
            }
            if (placesNumber != 0) {
                hex = String.format(Locale.ROOT, "%0" + placesNumber + "X", new Object[]{Integer.valueOf(number1.intValue())});
            } else {
                hex = Long.toHexString(number1.longValue());
            }
            if (number1.doubleValue() < 0.0d) {
                hex = "FF" + hex.substring(2);
            }
            return new StringEval(hex.toUpperCase(Locale.ROOT));
        } catch (EvaluationException e2) {
            return e2.getErrorEval();
        }
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        return evaluate(srcRowIndex, srcColumnIndex, arg0, null);
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length == 1) {
            return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
        }
        if (args.length == 2) {
            return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1]);
        }
        return ErrorEval.VALUE_INVALID;
    }
}
