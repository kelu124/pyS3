package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.ThreeDEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.CountUtils.I_MatchPredicate;

public final class Countblank extends Fixed1ArgFunction {
    private static final I_MatchPredicate predicate = new C11491();

    static class C11491 implements I_MatchPredicate {
        C11491() {
        }

        public boolean matches(ValueEval valueEval) {
            return valueEval == BlankEval.instance || ((valueEval instanceof StringEval) && "".equals(((StringEval) valueEval).getStringValue()));
        }
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        double result;
        if (arg0 instanceof RefEval) {
            result = (double) CountUtils.countMatchingCellsInRef((RefEval) arg0, predicate);
        } else if (arg0 instanceof ThreeDEval) {
            result = (double) CountUtils.countMatchingCellsInArea((ThreeDEval) arg0, predicate);
        } else {
            throw new IllegalArgumentException("Bad range arg type (" + arg0.getClass().getName() + ")");
        }
        return new NumberEval(result);
    }
}
