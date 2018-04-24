package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.LinearRegressionFunction.FUNCTION;

public final class Slope extends Fixed2ArgFunction {
    private final LinearRegressionFunction func = new LinearRegressionFunction(FUNCTION.SLOPE);

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        return this.func.evaluate(srcRowIndex, srcColumnIndex, arg0, arg1);
    }
}
