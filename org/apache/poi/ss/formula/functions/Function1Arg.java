package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;

public interface Function1Arg extends Function {
    ValueEval evaluate(int i, int i2, ValueEval valueEval);
}
