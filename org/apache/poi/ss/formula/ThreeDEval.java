package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.ValueEval;

public interface ThreeDEval extends TwoDEval, SheetRange {
    ValueEval getValue(int i, int i2, int i3);
}
