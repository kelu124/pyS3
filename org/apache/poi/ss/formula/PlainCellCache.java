package org.apache.poi.ss.formula;

import java.util.HashMap;
import java.util.Map;

final class PlainCellCache {
    private Map<Loc, PlainValueCellCacheEntry> _plainValueEntriesByLoc = new HashMap();

    public static final class Loc {
        static final /* synthetic */ boolean $assertionsDisabled = (!PlainCellCache.class.desiredAssertionStatus());
        private final long _bookSheetColumn;
        private final int _rowIndex;

        public Loc(int bookIndex, int sheetIndex, int rowIndex, int columnIndex) {
            this._bookSheetColumn = toBookSheetColumn(bookIndex, sheetIndex, columnIndex);
            this._rowIndex = rowIndex;
        }

        public static long toBookSheetColumn(int bookIndex, int sheetIndex, int columnIndex) {
            return (((((long) bookIndex) & 65535) << 48) + ((((long) sheetIndex) & 65535) << 32)) + ((((long) columnIndex) & 65535) << null);
        }

        public Loc(long bookSheetColumn, int rowIndex) {
            this._bookSheetColumn = bookSheetColumn;
            this._rowIndex = rowIndex;
        }

        public int hashCode() {
            return ((int) (this._bookSheetColumn ^ (this._bookSheetColumn >>> 32))) + (this._rowIndex * 17);
        }

        public boolean equals(Object obj) {
            if ($assertionsDisabled || (obj instanceof Loc)) {
                Loc other = (Loc) obj;
                return this._bookSheetColumn == other._bookSheetColumn && this._rowIndex == other._rowIndex;
            } else {
                throw new AssertionError("these package-private cache key instances are only compared to themselves");
            }
        }

        public int getRowIndex() {
            return this._rowIndex;
        }

        public int getColumnIndex() {
            return (int) (this._bookSheetColumn & 65535);
        }

        public int getSheetIndex() {
            return (int) ((this._bookSheetColumn >> 32) & 65535);
        }

        public int getBookIndex() {
            return (int) ((this._bookSheetColumn >> 48) & 65535);
        }
    }

    public void put(Loc key, PlainValueCellCacheEntry cce) {
        this._plainValueEntriesByLoc.put(key, cce);
    }

    public void clear() {
        this._plainValueEntriesByLoc.clear();
    }

    public PlainValueCellCacheEntry get(Loc key) {
        return (PlainValueCellCacheEntry) this._plainValueEntriesByLoc.get(key);
    }

    public void remove(Loc key) {
        this._plainValueEntriesByLoc.remove(key);
    }
}
