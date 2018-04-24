package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Na extends Fixed0ArgFunction {
    public ValueEval evaluate(int srcCellRow, int srcCellCol) {
        return ErrorEval.NA;
    }
}
