package org.apache.poi.ss.formula;

import java.util.HashMap;
import java.util.Map;

final class FormulaCellCache {
    private final Map<Object, FormulaCellCacheEntry> _formulaEntriesByCell = new HashMap();

    interface IEntryOperation {
        void processEntry(FormulaCellCacheEntry formulaCellCacheEntry);
    }

    public CellCacheEntry[] getCacheEntries() {
        FormulaCellCacheEntry[] result = new FormulaCellCacheEntry[this._formulaEntriesByCell.size()];
        this._formulaEntriesByCell.values().toArray(result);
        return result;
    }

    public void clear() {
        this._formulaEntriesByCell.clear();
    }

    public FormulaCellCacheEntry get(EvaluationCell cell) {
        return (FormulaCellCacheEntry) this._formulaEntriesByCell.get(cell.getIdentityKey());
    }

    public void put(EvaluationCell cell, FormulaCellCacheEntry entry) {
        this._formulaEntriesByCell.put(cell.getIdentityKey(), entry);
    }

    public FormulaCellCacheEntry remove(EvaluationCell cell) {
        return (FormulaCellCacheEntry) this._formulaEntriesByCell.remove(cell.getIdentityKey());
    }

    public void applyOperation(IEntryOperation operation) {
        for (FormulaCellCacheEntry processEntry : this._formulaEntriesByCell.values()) {
            operation.processEntry(processEntry);
        }
    }
}
