package org.apache.poi.ss.formula.eval;

public final class BlankEval implements ValueEval {
    public static final BlankEval instance = new BlankEval();

    private BlankEval() {
    }
}
