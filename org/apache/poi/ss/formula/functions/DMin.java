package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.NumericValueEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class DMin implements IDStarAlgorithm {
    private ValueEval minimumValue;

    public boolean processMatch(ValueEval eval) {
        if (eval instanceof NumericValueEval) {
            if (this.minimumValue == null) {
                this.minimumValue = eval;
            } else if (((NumericValueEval) eval).getNumberValue() < ((NumericValueEval) this.minimumValue).getNumberValue()) {
                this.minimumValue = eval;
            }
        }
        return true;
    }

    public ValueEval getResult() {
        if (this.minimumValue == null) {
            return NumberEval.ZERO;
        }
        return this.minimumValue;
    }
}
