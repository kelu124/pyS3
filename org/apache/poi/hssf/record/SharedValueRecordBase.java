package org.apache.poi.hssf.record;

import org.apache.poi.hssf.util.CellRangeAddress8Bit;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public abstract class SharedValueRecordBase extends StandardRecord {
    private CellRangeAddress8Bit _range;

    protected abstract int getExtraDataSize();

    protected abstract void serializeExtraData(LittleEndianOutput littleEndianOutput);

    protected SharedValueRecordBase(CellRangeAddress8Bit range) {
        if (range == null) {
            throw new IllegalArgumentException("range must be supplied.");
        }
        this._range = range;
    }

    protected SharedValueRecordBase() {
        this(new CellRangeAddress8Bit(0, 0, 0, 0));
    }

    public SharedValueRecordBase(LittleEndianInput in) {
        this._range = new CellRangeAddress8Bit(in);
    }

    public final CellRangeAddress8Bit getRange() {
        return this._range;
    }

    public final int getFirstRow() {
        return this._range.getFirstRow();
    }

    public final int getLastRow() {
        return this._range.getLastRow();
    }

    public final int getFirstColumn() {
        return (short) this._range.getFirstColumn();
    }

    public final int getLastColumn() {
        return (short) this._range.getLastColumn();
    }

    protected int getDataSize() {
        return getExtraDataSize() + 6;
    }

    public void serialize(LittleEndianOutput out) {
        this._range.serialize(out);
        serializeExtraData(out);
    }

    public final boolean isInRange(int rowIx, int colIx) {
        CellRangeAddress8Bit r = this._range;
        return r.getFirstRow() <= rowIx && r.getLastRow() >= rowIx && r.getFirstColumn() <= colIx && r.getLastColumn() >= colIx;
    }

    public final boolean isFirstCell(int rowIx, int colIx) {
        CellRangeAddress8Bit r = getRange();
        return r.getFirstRow() == rowIx && r.getFirstColumn() == colIx;
    }
}
