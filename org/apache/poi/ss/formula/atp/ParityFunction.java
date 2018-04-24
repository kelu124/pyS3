package org.apache.poi.ss.formula.atp;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

final class ParityFunction implements FreeRefFunction {
    public static final FreeRefFunction IS_EVEN = new ParityFunction(0);
    public static final FreeRefFunction IS_ODD = new ParityFunction(1);
    private final int _desiredParity;

    private ParityFunction(int desiredParity) {
        this._desiredParity = desiredParity;
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        boolean z = true;
        if (args.length != 1) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            if (evaluateArgParity(args[0], ec.getRowIndex(), ec.getColumnIndex()) != this._desiredParity) {
                z = false;
            }
            return BoolEval.valueOf(z);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static int evaluateArgParity(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        double d = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(arg, srcCellRow, (short) srcCellCol));
        if (d < 0.0d) {
            d = -d;
        }
        return (int) (1 & ((long) Math.floor(d)));
    }
}
