package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public abstract class Fixed4ArgFunction implements Function4Arg {
    public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        if (args.length != 4) {
            return ErrorEval.VALUE_INVALID;
        }
        return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], args[3]);
    }
}
