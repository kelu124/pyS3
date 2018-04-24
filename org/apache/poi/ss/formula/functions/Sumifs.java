package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.CountUtils.I_MatchPredicate;
import org.apache.poi.ss.formula.functions.Countif.ErrorMatcher;

public final class Sumifs implements FreeRefFunction {
    public static final FreeRefFunction instance = new Sumifs();

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length < 3 || args.length % 2 == 0) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            AreaEval sumRange = convertRangeArg(args[0]);
            AreaEval[] ae = new AreaEval[((args.length - 1) / 2)];
            I_MatchPredicate[] mp = new I_MatchPredicate[ae.length];
            int i = 1;
            int k = 0;
            while (i < args.length) {
                ae[k] = convertRangeArg(args[i]);
                mp[k] = Countif.createCriteriaPredicate(args[i + 1], ec.getRowIndex(), ec.getColumnIndex());
                i += 2;
                k++;
            }
            validateCriteriaRanges(ae, sumRange);
            validateCriteria(mp);
            return new NumberEval(sumMatchingCells(ae, mp, sumRange));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private void validateCriteriaRanges(AreaEval[] criteriaRanges, AreaEval sumRange) throws EvaluationException {
        AreaEval[] arr$ = criteriaRanges;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            AreaEval r = arr$[i$];
            if (r.getHeight() == sumRange.getHeight() && r.getWidth() == sumRange.getWidth()) {
                i$++;
            } else {
                throw EvaluationException.invalidValue();
            }
        }
    }

    private void validateCriteria(I_MatchPredicate[] criteria) throws EvaluationException {
        for (I_MatchPredicate predicate : criteria) {
            if (predicate instanceof ErrorMatcher) {
                throw new EvaluationException(ErrorEval.valueOf(((ErrorMatcher) predicate).getValue()));
            }
        }
    }

    private static double sumMatchingCells(AreaEval[] ranges, I_MatchPredicate[] predicates, AreaEval aeSum) {
        int height = aeSum.getHeight();
        int width = aeSum.getWidth();
        double result = 0.0d;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                boolean matches = true;
                for (int i = 0; i < ranges.length; i++) {
                    if (!predicates[i].matches(ranges[i].getRelativeValue(r, c))) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    result += accumulate(aeSum, r, c);
                }
            }
        }
        return result;
    }

    private static double accumulate(AreaEval aeSum, int relRowIndex, int relColIndex) {
        ValueEval addend = aeSum.getRelativeValue(relRowIndex, relColIndex);
        if (addend instanceof NumberEval) {
            return ((NumberEval) addend).getNumberValue();
        }
        return 0.0d;
    }

    private static AreaEval convertRangeArg(ValueEval eval) throws EvaluationException {
        if (eval instanceof AreaEval) {
            return (AreaEval) eval;
        }
        if (eval instanceof RefEval) {
            return ((RefEval) eval).offset(0, 0, 0, 0);
        }
        throw new EvaluationException(ErrorEval.VALUE_INVALID);
    }
}
