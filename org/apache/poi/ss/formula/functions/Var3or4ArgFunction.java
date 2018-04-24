package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.ValueEval;

abstract class Var3or4ArgFunction implements Function3Arg, Function4Arg {
    Var3or4ArgFunction() {
    }

    public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        switch (args.length) {
            case 3:
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2]);
            case 4:
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], args[3]);
            default:
                return ErrorEval.VALUE_INVALID;
        }
    }
}
