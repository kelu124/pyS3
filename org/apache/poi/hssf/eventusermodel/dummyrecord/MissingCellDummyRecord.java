package org.apache.poi.hssf.eventusermodel.dummyrecord;

public final class MissingCellDummyRecord extends DummyRecordBase {
    private int column;
    private int row;

    public /* bridge */ /* synthetic */ int serialize(int i, byte[] bArr) {
        return super.serialize(i, bArr);
    }

    public MissingCellDummyRecord(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }
}
