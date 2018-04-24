package org.apache.poi.ss.formula.eval;

import org.apache.poi.ss.formula.SheetRange;

public interface RefEval extends ValueEval, SheetRange {
    int getColumn();

    int getFirstSheetIndex();

    ValueEval getInnerValueEval(int i);

    int getLastSheetIndex();

    int getNumberOfSheets();

    int getRow();

    AreaEval offset(int i, int i2, int i3, int i4);
}
