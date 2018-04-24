package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.CountUtils.I_MatchAreaPredicate;
import org.apache.poi.ss.formula.functions.CountUtils.I_MatchPredicate;

public final class Count implements Function {
    private static final I_MatchPredicate defaultPredicate = new C11451();
    private static final I_MatchPredicate subtotalPredicate = new C11462();
    private final I_MatchPredicate _predicate;

    static class C11451 implements I_MatchPredicate {
        C11451() {
        }

        public boolean matches(ValueEval valueEval) {
            if ((valueEval instanceof NumberEval) || valueEval == MissingArgEval.instance) {
                return true;
            }
            return false;
        }
    }

    static class C11462 implements I_MatchAreaPredicate {
        C11462() {
        }

        public boolean matches(ValueEval valueEval) {
            return Count.defaultPredicate.matches(valueEval);
        }

        public boolean matches(TwoDEval areEval, int rowIndex, int columnIndex) {
            return !areEval.isSubTotal(rowIndex, columnIndex);
        }
    }

    public Count() {
        this._predicate = defaultPredicate;
    }

    private Count(I_MatchPredicate criteriaPredicate) {
        this._predicate = criteriaPredicate;
    }

    public ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol) {
        if (nArgs < 1) {
            return ErrorEval.VALUE_INVALID;
        }
        if (nArgs > 30) {
            return ErrorEval.VALUE_INVALID;
        }
        int temp = 0;
        for (ValueEval countArg : args) {
            temp += CountUtils.countArg(countArg, this._predicate);
        }
        return new NumberEval((double) temp);
    }

    public static Count subtotalInstance() {
        return new Count(subtotalPredicate);
    }
}
