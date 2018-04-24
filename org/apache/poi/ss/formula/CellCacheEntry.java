package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.IEvaluationListener.ICacheEntry;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;

abstract class CellCacheEntry implements ICacheEntry {
    public static final CellCacheEntry[] EMPTY_ARRAY = new CellCacheEntry[0];
    private final FormulaCellCacheEntrySet _consumingCells = new FormulaCellCacheEntrySet();
    private ValueEval _value;

    protected CellCacheEntry() {
    }

    protected final void clearValue() {
        this._value = null;
    }

    public final boolean updateValue(ValueEval value) {
        if (value == null) {
            throw new IllegalArgumentException("Did not expect to update to null");
        }
        boolean result = !areValuesEqual(this._value, value);
        this._value = value;
        return result;
    }

    public final ValueEval getValue() {
        return this._value;
    }

    private static boolean areValuesEqual(ValueEval a, ValueEval b) {
        if (a == null) {
            return false;
        }
        Class<? extends ValueEval> cls = a.getClass();
        if (cls != b.getClass()) {
            return false;
        }
        if (a == BlankEval.instance) {
            if (b != a) {
                return false;
            }
            return true;
        } else if (cls == NumberEval.class) {
            if (((NumberEval) a).getNumberValue() != ((NumberEval) b).getNumberValue()) {
                return false;
            }
            return true;
        } else if (cls == StringEval.class) {
            return ((StringEval) a).getStringValue().equals(((StringEval) b).getStringValue());
        } else {
            if (cls == BoolEval.class) {
                if (((BoolEval) a).getBooleanValue() != ((BoolEval) b).getBooleanValue()) {
                    return false;
                }
                return true;
            } else if (cls != ErrorEval.class) {
                throw new IllegalStateException("Unexpected value class (" + cls.getName() + ")");
            } else if (((ErrorEval) a).getErrorCode() != ((ErrorEval) b).getErrorCode()) {
                return false;
            } else {
                return true;
            }
        }
    }

    public final void addConsumingCell(FormulaCellCacheEntry cellLoc) {
        this._consumingCells.add(cellLoc);
    }

    public final FormulaCellCacheEntry[] getConsumingCells() {
        return this._consumingCells.toArray();
    }

    public final void clearConsumingCell(FormulaCellCacheEntry cce) {
        if (!this._consumingCells.remove(cce)) {
            throw new IllegalStateException("Specified formula cell is not consumed by this cell");
        }
    }

    public final void recurseClearCachedFormulaResults(IEvaluationListener listener) {
        if (listener == null) {
            recurseClearCachedFormulaResults();
            return;
        }
        listener.onClearCachedValue(this);
        recurseClearCachedFormulaResults(listener, 1);
    }

    protected final void recurseClearCachedFormulaResults() {
        FormulaCellCacheEntry[] formulaCells = getConsumingCells();
        for (FormulaCellCacheEntry fc : formulaCells) {
            fc.clearFormulaEntry();
            fc.recurseClearCachedFormulaResults();
        }
    }

    protected final void recurseClearCachedFormulaResults(IEvaluationListener listener, int depth) {
        FormulaCellCacheEntry[] formulaCells = getConsumingCells();
        listener.sortDependentCachedValues(formulaCells);
        for (FormulaCellCacheEntry fc : formulaCells) {
            listener.onClearDependentCachedValue(fc, depth);
            fc.clearFormulaEntry();
            fc.recurseClearCachedFormulaResults(listener, depth + 1);
        }
    }
}
