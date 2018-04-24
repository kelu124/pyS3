package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public abstract class Fixed1ArgFunction implements Function1Arg {
    public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        if (args.length != 1) {
            return ErrorEval.VALUE_INVALID;
        }
        return evaluate(srcRowIndex, srcColumnIndex, args[0]);
    }
}
