package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public abstract class BooleanFunction implements Function {
    public static final Function AND = new C11401();
    public static final Function FALSE = new C11423();
    public static final Function NOT = new C11445();
    public static final Function OR = new C11412();
    public static final Function TRUE = new C11434();

    static class C11401 extends BooleanFunction {
        C11401() {
        }

        protected boolean getInitialResultValue() {
            return true;
        }

        protected boolean partialEvaluate(boolean cumulativeResult, boolean currentValue) {
            return cumulativeResult && currentValue;
        }
    }

    static class C11412 extends BooleanFunction {
        C11412() {
        }

        protected boolean getInitialResultValue() {
            return false;
        }

        protected boolean partialEvaluate(boolean cumulativeResult, boolean currentValue) {
            return cumulativeResult || currentValue;
        }
    }

    static class C11423 extends Fixed0ArgFunction {
        C11423() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
            return BoolEval.FALSE;
        }
    }

    static class C11434 extends Fixed0ArgFunction {
        C11434() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
            return BoolEval.TRUE;
        }
    }

    static class C11445 extends Fixed1ArgFunction {
        C11445() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            boolean z = false;
            try {
                Boolean b = OperandResolver.coerceValueToBoolean(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex), false);
                if (!(b == null ? false : b.booleanValue())) {
                    z = true;
                }
                return BoolEval.valueOf(z);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }
    }

    protected abstract boolean getInitialResultValue();

    protected abstract boolean partialEvaluate(boolean z, boolean z2);

    public final ValueEval evaluate(ValueEval[] args, int srcRow, int srcCol) {
        if (args.length < 1) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            return BoolEval.valueOf(calculate(args));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private boolean calculate(ValueEval[] args) throws EvaluationException {
        boolean result = getInitialResultValue();
        boolean atleastOneNonBlank = false;
        for (ValueEval arg : args) {
            Boolean tempVe;
            if (arg instanceof TwoDEval) {
                TwoDEval ae = (TwoDEval) arg;
                int height = ae.getHeight();
                int width = ae.getWidth();
                for (int rrIx = 0; rrIx < height; rrIx++) {
                    for (int rcIx = 0; rcIx < width; rcIx++) {
                        tempVe = OperandResolver.coerceValueToBoolean(ae.getValue(rrIx, rcIx), true);
                        if (tempVe != null) {
                            result = partialEvaluate(result, tempVe.booleanValue());
                            atleastOneNonBlank = true;
                        }
                    }
                }
            } else if (arg instanceof RefEval) {
                RefEval re = (RefEval) arg;
                int firstSheetIndex = re.getFirstSheetIndex();
                int lastSheetIndex = re.getLastSheetIndex();
                for (int sIx = firstSheetIndex; sIx <= lastSheetIndex; sIx++) {
                    tempVe = OperandResolver.coerceValueToBoolean(re.getInnerValueEval(sIx), true);
                    if (tempVe != null) {
                        result = partialEvaluate(result, tempVe.booleanValue());
                        atleastOneNonBlank = true;
                    }
                }
            } else {
                if (arg == MissingArgEval.instance) {
                    tempVe = null;
                } else {
                    tempVe = OperandResolver.coerceValueToBoolean(arg, false);
                }
                if (tempVe != null) {
                    result = partialEvaluate(result, tempVe.booleanValue());
                    atleastOneNonBlank = true;
                }
            }
        }
        if (atleastOneNonBlank) {
            return result;
        }
        throw new EvaluationException(ErrorEval.VALUE_INVALID);
    }
}
