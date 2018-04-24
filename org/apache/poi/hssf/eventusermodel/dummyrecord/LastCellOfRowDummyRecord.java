package org.apache.poi.hssf.eventusermodel.dummyrecord;

public final class LastCellOfRowDummyRecord extends DummyRecordBase {
    private final int lastColumnNumber;
    private final int row;

    public /* bridge */ /* synthetic */ int serialize(int i, byte[] bArr) {
        return super.serialize(i, bArr);
    }

    public LastCellOfRowDummyRecord(int row, int lastColumnNumber) {
        this.row = row;
        this.lastColumnNumber = lastColumnNumber;
    }

    public int getRow() {
        return this.row;
    }

    public int getLastColumnNumber() {
        return this.lastColumnNumber;
    }

    public String toString() {
        return "End-of-Row for Row=" + this.row + " at Column=" + this.lastColumnNumber;
    }
}
