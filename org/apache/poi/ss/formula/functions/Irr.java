package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Irr implements Function {
    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        if (args.length == 0 || args.length > 2) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            double guess;
            double[] values = ValueCollector.collectValues(args[0]);
            if (args.length == 2) {
                guess = NumericFunction.singleOperandEvaluate(args[1], srcRowIndex, srcColumnIndex);
            } else {
                guess = 0.1d;
            }
            double result = irr(values, guess);
            NumericFunction.checkValue(result);
            return new NumberEval(result);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    public static double irr(double[] income) {
        return irr(income, 0.1d);
    }

    public static double irr(double[] values, double guess) {
        double x0 = guess;
        for (int i = 0; i < 20; i++) {
            double fValue = 0.0d;
            double fDerivative = 0.0d;
            for (int k = 0; k < values.length; k++) {
                fValue += values[k] / Math.pow(1.0d + x0, (double) k);
                fDerivative += (((double) (-k)) * values[k]) / Math.pow(1.0d + x0, (double) (k + 1));
            }
            double x1 = x0 - (fValue / fDerivative);
            if (Math.abs(x1 - x0) <= 1.0E-7d) {
                return x1;
            }
            x0 = x1;
        }
        return Double.NaN;
    }
}
