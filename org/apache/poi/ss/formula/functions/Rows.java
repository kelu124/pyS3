package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Rows extends Fixed1ArgFunction {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        int result;
        if (arg0 instanceof TwoDEval) {
            result = ((TwoDEval) arg0).getHeight();
        } else if (!(arg0 instanceof RefEval)) {
            return ErrorEval.VALUE_INVALID;
        } else {
            result = 1;
        }
        return new NumberEval((double) result);
    }
}
