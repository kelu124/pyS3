package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.ValueEval;

interface IEvaluationListener {

    public interface ICacheEntry {
        ValueEval getValue();
    }

    void onCacheHit(int i, int i2, int i3, ValueEval valueEval);

    void onChangeFromBlankValue(int i, int i2, int i3, EvaluationCell evaluationCell, ICacheEntry iCacheEntry);

    void onClearCachedValue(ICacheEntry iCacheEntry);

    void onClearDependentCachedValue(ICacheEntry iCacheEntry, int i);

    void onClearWholeCache();

    void onEndEvaluate(ICacheEntry iCacheEntry, ValueEval valueEval);

    void onReadPlainValue(int i, int i2, int i3, ICacheEntry iCacheEntry);

    void onStartEvaluate(EvaluationCell evaluationCell, ICacheEntry iCacheEntry);

    void sortDependentCachedValues(ICacheEntry[] iCacheEntryArr);
}
