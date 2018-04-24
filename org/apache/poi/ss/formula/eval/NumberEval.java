package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.ptg.IntPtg;
import org.apache.poi.ss.formula.ptg.NumberPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.util.NumberToTextConverter;

public final class NumberEval implements NumericValueEval, StringValueEval {
    public static final NumberEval ZERO = new NumberEval(0.0d);
    private String _stringValue;
    private final double _value;

    public NumberEval(Ptg ptg) {
        if (ptg == null) {
            throw new IllegalArgumentException("ptg must not be null");
        } else if (ptg instanceof IntPtg) {
            this._value = (double) ((IntPtg) ptg).getValue();
        } else if (ptg instanceof NumberPtg) {
            this._value = ((NumberPtg) ptg).getValue();
        } else {
            throw new IllegalArgumentException("bad argument type (" + ptg.getClass().getName() + ")");
        }
    }

    public NumberEval(double value) {
        this._value = value;
    }

    public double getNumberValue() {
        return this._value;
    }

    public String getStringValue() {
        if (this._stringValue == null) {
            this._stringValue = NumberToTextConverter.toText(this._value);
        }
        return this._stringValue;
    }

    public final String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(getStringValue());
        sb.append("]");
        return sb.toString();
    }
}
