package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class C1179T extends Fixed1ArgFunction {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        ValueEval arg = arg0;
        if (arg instanceof RefEval) {
            RefEval re = (RefEval) arg;
            arg = re.getInnerValueEval(re.getFirstSheetIndex());
        } else if (arg instanceof AreaEval) {
            arg = ((AreaEval) arg).getRelativeValue(0, 0);
        }
        return ((arg instanceof StringEval) || (arg instanceof ErrorEval)) ? arg : StringEval.EMPTY_INSTANCE;
    }
}
