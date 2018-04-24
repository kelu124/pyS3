package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ValueEval;

public interface IDStarAlgorithm {
    ValueEval getResult();

    boolean processMatch(ValueEval valueEval);
}
