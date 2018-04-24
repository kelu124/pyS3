package org.apache.poi.ss.formula.eval;

public final class MissingArgEval implements ValueEval {
    public static final MissingArgEval instance = new MissingArgEval();

    private MissingArgEval() {
    }
}
