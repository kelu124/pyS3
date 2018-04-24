package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.AreaEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Column implements Function0Arg, Function1Arg {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
        return new NumberEval((double) (srcColumnIndex + 1));
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        int rnum;
        if (arg0 instanceof AreaEval) {
            rnum = ((AreaEval) arg0).getFirstColumn();
        } else if (!(arg0 instanceof RefEval)) {
            return ErrorEval.VALUE_INVALID;
        } else {
            rnum = ((RefEval) arg0).getColumn();
        }
        return new NumberEval((double) (rnum + 1));
    }

    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        switch (args.length) {
            case 0:
                return new NumberEval((double) (srcColumnIndex + 1));
            case 1:
                return evaluate(srcRowIndex, srcColumnIndex, args[0]);
            default:
                return ErrorEval.VALUE_INVALID;
        }
    }
}
