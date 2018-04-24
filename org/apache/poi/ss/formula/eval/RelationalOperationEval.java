package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.functions.Fixed2ArgFunction;
import org.apache.poi.ss.formula.functions.Function;
import org.apache.poi.ss.util.NumberComparer;

public abstract class RelationalOperationEval extends Fixed2ArgFunction {
    public static final Function EqualEval = new C11191();
    public static final Function GreaterEqualEval = new C11202();
    public static final Function GreaterThanEval = new C11213();
    public static final Function LessEqualEval = new C11224();
    public static final Function LessThanEval = new C11235();
    public static final Function NotEqualEval = new C11246();

    static class C11191 extends RelationalOperationEval {
        C11191() {
        }

        protected boolean convertComparisonResult(int cmpResult) {
            return cmpResult == 0;
        }
    }

    static class C11202 extends RelationalOperationEval {
        C11202() {
        }

        protected boolean convertComparisonResult(int cmpResult) {
            return cmpResult >= 0;
        }
    }

    static class C11213 extends RelationalOperationEval {
        C11213() {
        }

        protected boolean convertComparisonResult(int cmpResult) {
            return cmpResult > 0;
        }
    }

    static class C11224 extends RelationalOperationEval {
        C11224() {
        }

        protected boolean convertComparisonResult(int cmpResult) {
            return cmpResult <= 0;
        }
    }

    static class C11235 extends RelationalOperationEval {
        C11235() {
        }

        protected boolean convertComparisonResult(int cmpResult) {
            return cmpResult < 0;
        }
    }

    static class C11246 extends RelationalOperationEval {
        C11246() {
        }

        protected boolean convertComparisonResult(int cmpResult) {
            return cmpResult != 0;
        }
    }

    protected abstract boolean convertComparisonResult(int i);

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        try {
            return BoolEval.valueOf(convertComparisonResult(doCompare(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex), OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex))));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static int doCompare(ValueEval va, ValueEval vb) {
        if (va == BlankEval.instance) {
            return compareBlank(vb);
        }
        if (vb == BlankEval.instance) {
            return -compareBlank(va);
        }
        if (va instanceof BoolEval) {
            if (!(vb instanceof BoolEval)) {
                return 1;
            }
            BoolEval bA = (BoolEval) va;
            if (bA.getBooleanValue() == ((BoolEval) vb).getBooleanValue()) {
                return 0;
            }
            if (bA.getBooleanValue()) {
                return 1;
            }
            return -1;
        } else if (vb instanceof BoolEval) {
            return -1;
        } else {
            if (va instanceof StringEval) {
                if (!(vb instanceof StringEval)) {
                    return 1;
                }
                return ((StringEval) va).getStringValue().compareToIgnoreCase(((StringEval) vb).getStringValue());
            } else if (vb instanceof StringEval) {
                return -1;
            } else {
                if ((va instanceof NumberEval) && (vb instanceof NumberEval)) {
                    return NumberComparer.compare(((NumberEval) va).getNumberValue(), ((NumberEval) vb).getNumberValue());
                }
                throw new IllegalArgumentException("Bad operand types (" + va.getClass().getName() + "), (" + vb.getClass().getName() + ")");
            }
        }
    }

    private static int compareBlank(ValueEval v) {
        int i = -1;
        if (v == BlankEval.instance) {
            return 0;
        }
        if (v instanceof BoolEval) {
            if (!((BoolEval) v).getBooleanValue()) {
                i = 0;
            }
            return i;
        } else if (v instanceof NumberEval) {
            return NumberComparer.compare(0.0d, ((NumberEval) v).getNumberValue());
        } else {
            if (!(v instanceof StringEval)) {
                throw new IllegalArgumentException("bad value class (" + v.getClass().getName() + ")");
            } else if (((StringEval) v).getStringValue().length() >= 1) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
