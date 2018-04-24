package org.apache.poi.ss.formula.eval.forked;

final class ForkedEvaluationSheet$RowColKey implements Comparable<ForkedEvaluationSheet$RowColKey> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ForkedEvaluationSheet.class.desiredAssertionStatus());
    private final int _columnIndex;
    private final int _rowIndex;

    public ForkedEvaluationSheet$RowColKey(int rowIndex, int columnIndex) {
        this._rowIndex = rowIndex;
        this._columnIndex = columnIndex;
    }

    public boolean equals(Object obj) {
        if ($assertionsDisabled || (obj instanceof ForkedEvaluationSheet$RowColKey)) {
            ForkedEvaluationSheet$RowColKey other = (ForkedEvaluationSheet$RowColKey) obj;
            return this._rowIndex == other._rowIndex && this._columnIndex == other._columnIndex;
        } else {
            throw new AssertionError("these private cache key instances are only compared to themselves");
        }
    }

    public int hashCode() {
        return this._rowIndex ^ this._columnIndex;
    }

    public int compareTo(ForkedEvaluationSheet$RowColKey o) {
        int cmp = this._rowIndex - o._rowIndex;
        return cmp != 0 ? cmp : this._columnIndex - o._columnIndex;
    }

    public int getRowIndex() {
        return this._rowIndex;
    }

    public int getColumnIndex() {
        return this._columnIndex;
    }
}
