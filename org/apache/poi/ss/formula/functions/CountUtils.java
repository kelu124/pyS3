package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.ThreeDEval;
import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;

final class CountUtils {

    public interface I_MatchPredicate {
        boolean matches(ValueEval valueEval);
    }

    public interface I_MatchAreaPredicate extends I_MatchPredicate {
        boolean matches(TwoDEval twoDEval, int i, int i2);
    }

    private CountUtils() {
    }

    public static int countMatchingCellsInArea(ThreeDEval areaEval, I_MatchPredicate criteriaPredicate) {
        int result = 0;
        int firstSheetIndex = areaEval.getFirstSheetIndex();
        int lastSheetIndex = areaEval.getLastSheetIndex();
        for (int sIx = firstSheetIndex; sIx <= lastSheetIndex; sIx++) {
            int height = areaEval.getHeight();
            int width = areaEval.getWidth();
            int rrIx = 0;
            while (rrIx < height) {
                int rcIx = 0;
                while (rcIx < width) {
                    ValueEval ve = areaEval.getValue(sIx, rrIx, rcIx);
                    if ((!(criteriaPredicate instanceof I_MatchAreaPredicate) || ((I_MatchAreaPredicate) criteriaPredicate).matches(areaEval, rrIx, rcIx)) && criteriaPredicate.matches(ve)) {
                        result++;
                    }
                    rcIx++;
                }
                rrIx++;
            }
        }
        return result;
    }

    public static int countMatchingCellsInRef(RefEval refEval, I_MatchPredicate criteriaPredicate) {
        int result = 0;
        int firstSheetIndex = refEval.getFirstSheetIndex();
        int lastSheetIndex = refEval.getLastSheetIndex();
        for (int sIx = firstSheetIndex; sIx <= lastSheetIndex; sIx++) {
            if (criteriaPredicate.matches(refEval.getInnerValueEval(sIx))) {
                result++;
            }
        }
        return result;
    }

    public static int countArg(ValueEval eval, I_MatchPredicate criteriaPredicate) {
        if (eval == null) {
            throw new IllegalArgumentException("eval must not be null");
        } else if (eval instanceof ThreeDEval) {
            return countMatchingCellsInArea((ThreeDEval) eval, criteriaPredicate);
        } else {
            if (eval instanceof TwoDEval) {
                throw new IllegalArgumentException("Count requires 3D Evals, 2D ones aren't supported");
            } else if (eval instanceof RefEval) {
                return countMatchingCellsInRef((RefEval) eval, criteriaPredicate);
            } else {
                return criteriaPredicate.matches(eval) ? 1 : 0;
            }
        }
    }
}
