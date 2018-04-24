package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.NumericValueEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.LookupUtils.LookupValueComparer;
import org.apache.poi.ss.formula.functions.LookupUtils.ValueVector;

public final class Match extends Var2or3ArgFunction {

    private static final class SingleValueVector implements ValueVector {
        private final ValueEval _value;

        public SingleValueVector(ValueEval value) {
            this._value = value;
        }

        public ValueEval getItem(int index) {
            if (index == 0) {
                return this._value;
            }
            throw new RuntimeException("Invalid index (" + index + ") only zero is allowed");
        }

        public int getSize() {
            return 1;
        }
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        return eval(srcRowIndex, srcColumnIndex, arg0, arg1, 1.0d);
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        try {
            return eval(srcRowIndex, srcColumnIndex, arg0, arg1, evaluateMatchTypeArg(arg2, srcRowIndex, srcColumnIndex));
        } catch (EvaluationException e) {
            return ErrorEval.REF_INVALID;
        }
    }

    private static ValueEval eval(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, double match_type) {
        boolean matchExact;
        boolean findLargestLessThanOrEqual = true;
        if (match_type == 0.0d) {
            matchExact = true;
        } else {
            matchExact = false;
        }
        if (match_type <= 0.0d) {
            findLargestLessThanOrEqual = false;
        }
        try {
            return new NumberEval((double) (findIndexOfValue(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex), evaluateLookupRange(arg1), matchExact, findLargestLessThanOrEqual) + 1));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static ValueVector evaluateLookupRange(ValueEval eval) throws EvaluationException {
        if (eval instanceof RefEval) {
            RefEval re = (RefEval) eval;
            if (re.getNumberOfSheets() == 1) {
                return new SingleValueVector(re.getInnerValueEval(re.getFirstSheetIndex()));
            }
            return LookupUtils.createVector(re);
        } else if (eval instanceof TwoDEval) {
            ValueVector result = LookupUtils.createVector((TwoDEval) eval);
            if (result != null) {
                return result;
            }
            throw new EvaluationException(ErrorEval.NA);
        } else if (eval instanceof NumericValueEval) {
            throw new EvaluationException(ErrorEval.NA);
        } else if (!(eval instanceof StringEval)) {
            throw new RuntimeException("Unexpected eval type (" + eval.getClass().getName() + ")");
        } else if (OperandResolver.parseDouble(((StringEval) eval).getStringValue()) == null) {
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        } else {
            throw new EvaluationException(ErrorEval.NA);
        }
    }

    private static double evaluateMatchTypeArg(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        ValueEval match_type = OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol);
        if (match_type instanceof ErrorEval) {
            throw new EvaluationException((ErrorEval) match_type);
        } else if (match_type instanceof NumericValueEval) {
            return ((NumericValueEval) match_type).getNumberValue();
        } else {
            if (match_type instanceof StringEval) {
                Double d = OperandResolver.parseDouble(((StringEval) match_type).getStringValue());
                if (d != null) {
                    return d.doubleValue();
                }
                throw new EvaluationException(ErrorEval.VALUE_INVALID);
            }
            throw new RuntimeException("Unexpected match_type type (" + match_type.getClass().getName() + ")");
        }
    }

    private static int findIndexOfValue(ValueEval lookupValue, ValueVector lookupRange, boolean matchExact, boolean findLargestLessThanOrEqual) throws EvaluationException {
        LookupValueComparer lookupComparer = createLookupComparer(lookupValue, matchExact);
        int size = lookupRange.getSize();
        int i;
        if (matchExact) {
            for (i = 0; i < size; i++) {
                if (lookupComparer.compareTo(lookupRange.getItem(i)).isEqual()) {
                    return i;
                }
            }
            throw new EvaluationException(ErrorEval.NA);
        } else if (findLargestLessThanOrEqual) {
            for (i = size - 1; i >= 0; i--) {
                cmp = lookupComparer.compareTo(lookupRange.getItem(i));
                if (!cmp.isTypeMismatch() && !cmp.isLessThan()) {
                    return i;
                }
            }
            throw new EvaluationException(ErrorEval.NA);
        } else {
            i = 0;
            while (i < size) {
                cmp = lookupComparer.compareTo(lookupRange.getItem(i));
                if (cmp.isEqual()) {
                    return i;
                }
                if (!cmp.isGreaterThan()) {
                    i++;
                } else if (i >= 1) {
                    return i - 1;
                } else {
                    throw new EvaluationException(ErrorEval.NA);
                }
            }
            return size - 1;
        }
    }

    private static LookupValueComparer createLookupComparer(ValueEval lookupValue, boolean matchExact) {
        return LookupUtils.createLookupComparer(lookupValue, matchExact, true);
    }
}
