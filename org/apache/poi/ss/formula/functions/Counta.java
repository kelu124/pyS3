package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.CountUtils.I_MatchAreaPredicate;
import org.apache.poi.ss.formula.functions.CountUtils.I_MatchPredicate;

public final class Counta implements Function {
    private static final I_MatchPredicate defaultPredicate = new C11471();
    private static final I_MatchPredicate subtotalPredicate = new C11482();
    private final I_MatchPredicate _predicate;

    static class C11471 implements I_MatchPredicate {
        C11471() {
        }

        public boolean matches(ValueEval valueEval) {
            if (valueEval == BlankEval.instance) {
                return false;
            }
            return true;
        }
    }

    static class C11482 implements I_MatchAreaPredicate {
        C11482() {
        }

        public boolean matches(ValueEval valueEval) {
            return Counta.defaultPredicate.matches(valueEval);
        }

        public boolean matches(TwoDEval areEval, int rowIndex, int columnIndex) {
            return !areEval.isSubTotal(rowIndex, columnIndex);
        }
    }

    public Counta() {
        this._predicate = defaultPredicate;
    }

    private Counta(I_MatchPredicate criteriaPredicate) {
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

    public static Counta subtotalInstance() {
        return new Counta(subtotalPredicate);
    }
}
