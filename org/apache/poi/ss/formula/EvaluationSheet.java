package org.apache.poi.ss.formula;

import org.apache.poi.util.Internal;

@Internal
public interface EvaluationSheet {
    void clearAllCachedResultValues();

    EvaluationCell getCell(int i, int i2);
}
