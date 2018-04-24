package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public abstract class LogicalFunction extends Fixed1ArgFunction {
    public static final Function ISBLANK = new C11605();
    public static final Function ISERR = new C11627();
    public static final Function ISERROR = new C11616();
    public static final Function ISLOGICAL = new C11561();
    public static final Function ISNA = new C11638();
    public static final Function ISNONTEXT = new C11572();
    public static final Function ISNUMBER = new C11583();
    public static final Function ISREF = new C11649();
    public static final Function ISTEXT = new C11594();

    static class C11561 extends LogicalFunction {
        C11561() {
        }

        protected boolean evaluate(ValueEval arg) {
            return arg instanceof BoolEval;
        }
    }

    static class C11572 extends LogicalFunction {
        C11572() {
        }

        protected boolean evaluate(ValueEval arg) {
            return !(arg instanceof StringEval);
        }
    }

    static class C11583 extends LogicalFunction {
        C11583() {
        }

        protected boolean evaluate(ValueEval arg) {
            return arg instanceof NumberEval;
        }
    }

    static class C11594 extends LogicalFunction {
        C11594() {
        }

        protected boolean evaluate(ValueEval arg) {
            return arg instanceof StringEval;
        }
    }

    static class C11605 extends LogicalFunction {
        C11605() {
        }

        protected boolean evaluate(ValueEval arg) {
            return arg instanceof BlankEval;
        }
    }

    static class C11616 extends LogicalFunction {
        C11616() {
        }

        protected boolean evaluate(ValueEval arg) {
            return arg instanceof ErrorEval;
        }
    }

    static class C11627 extends LogicalFunction {
        C11627() {
        }

        protected boolean evaluate(ValueEval arg) {
            if (!(arg instanceof ErrorEval) || arg == ErrorEval.NA) {
                return false;
            }
            return true;
        }
    }

    static class C11638 extends LogicalFunction {
        C11638() {
        }

        protected boolean evaluate(ValueEval arg) {
            return arg == ErrorEval.NA;
        }
    }

    static class C11649 extends Fixed1ArgFunction {
        C11649() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            if ((arg0 instanceof RefEval) || (arg0 instanceof AreaEval)) {
                return BoolEval.TRUE;
            }
            return BoolEval.FALSE;
        }
    }

    protected abstract boolean evaluate(ValueEval valueEval);

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        ValueEval ve;
        try {
            ve = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
        } catch (EvaluationException e) {
            ve = e.getErrorEval();
        }
        return BoolEval.valueOf(evaluate(ve));
    }
}
