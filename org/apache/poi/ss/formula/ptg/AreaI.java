package org.apache.poi.ss.formula.ptg;

public interface AreaI {

    public static class OffsetArea implements AreaI {
        private final int _firstColumn;
        private final int _firstRow;
        private final int _lastColumn;
        private final int _lastRow;

        public OffsetArea(int baseRow, int baseColumn, int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx) {
            this._firstRow = Math.min(relFirstRowIx, relLastRowIx) + baseRow;
            this._lastRow = Math.max(relFirstRowIx, relLastRowIx) + baseRow;
            this._firstColumn = Math.min(relFirstColIx, relLastColIx) + baseColumn;
            this._lastColumn = Math.max(relFirstColIx, relLastColIx) + baseColumn;
        }

        public int getFirstColumn() {
            return this._firstColumn;
        }

        public int getFirstRow() {
            return this._firstRow;
        }

        public int getLastColumn() {
            return this._lastColumn;
        }

        public int getLastRow() {
            return this._lastRow;
        }
    }

    int getFirstColumn();

    int getFirstRow();

    int getLastColumn();

    int getLastRow();
}
