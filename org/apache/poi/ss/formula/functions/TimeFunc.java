package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.bytedeco.javacpp.avutil;

public final class TimeFunc extends Fixed3ArgFunction {
    private static final int HOURS_PER_DAY = 24;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        try {
            return new NumberEval(evaluate(evalArg(arg0, srcRowIndex, srcColumnIndex), evalArg(arg1, srcRowIndex, srcColumnIndex), evalArg(arg2, srcRowIndex, srcColumnIndex)));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static int evalArg(ValueEval arg, int srcRowIndex, int srcColumnIndex) throws EvaluationException {
        if (arg == MissingArgEval.instance) {
            return 0;
        }
        return OperandResolver.coerceValueToInt(OperandResolver.getSingleValue(arg, srcRowIndex, srcColumnIndex));
    }

    private static double evaluate(int hours, int minutes, int seconds) throws EvaluationException {
        if (hours > avutil.FF_LAMBDA_MAX || minutes > avutil.FF_LAMBDA_MAX || seconds > avutil.FF_LAMBDA_MAX) {
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        }
        int totalSeconds = ((hours * SECONDS_PER_HOUR) + (minutes * 60)) + seconds;
        if (totalSeconds >= 0) {
            return ((double) (totalSeconds % 86400)) / 86400.0d;
        }
        throw new EvaluationException(ErrorEval.VALUE_INVALID);
    }
}
