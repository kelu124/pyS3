package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.functions.Fixed2ArgFunction;
import org.apache.poi.ss.formula.functions.Function;

public abstract class TwoOperandNumericOperation extends Fixed2ArgFunction {
    public static final Function AddEval = new C11251();
    public static final Function DivideEval = new C11262();
    public static final Function MultiplyEval = new C11273();
    public static final Function PowerEval = new C11284();
    public static final Function SubtractEval = new SubtractEvalClass();

    static class C11251 extends TwoOperandNumericOperation {
        C11251() {
        }

        protected double evaluate(double d0, double d1) {
            return d0 + d1;
        }
    }

    static class C11262 extends TwoOperandNumericOperation {
        C11262() {
        }

        protected double evaluate(double d0, double d1) throws EvaluationException {
            if (d1 != 0.0d) {
                return d0 / d1;
            }
            throw new EvaluationException(ErrorEval.DIV_ZERO);
        }
    }

    static class C11273 extends TwoOperandNumericOperation {
        C11273() {
        }

        protected double evaluate(double d0, double d1) {
            return d0 * d1;
        }
    }

    static class C11284 extends TwoOperandNumericOperation {
        C11284() {
        }

        protected double evaluate(double d0, double d1) {
            return Math.pow(d0, d1);
        }
    }

    private static final class SubtractEvalClass extends TwoOperandNumericOperation {
        protected double evaluate(double d0, double d1) {
            return d0 - d1;
        }
    }

    protected abstract double evaluate(double d, double d2) throws EvaluationException;

    protected final double singleOperandEvaluate(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        return OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol));
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        try {
            double result = evaluate(singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex), singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex));
            if (result == 0.0d && !(this instanceof SubtractEvalClass)) {
                return NumberEval.ZERO;
            }
            if (Double.isNaN(result) || Double.isInfinite(result)) {
                return ErrorEval.NUM_ERROR;
            }
            return new NumberEval(result);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }
}
