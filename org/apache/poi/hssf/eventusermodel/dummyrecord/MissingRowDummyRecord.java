package org.apache.poi.hssf.eventusermodel.dummyrecord;

public final class MissingRowDummyRecord extends DummyRecordBase {
    private int rowNumber;

    public /* bridge */ /* synthetic */ int serialize(int i, byte[] bArr) {
        return super.serialize(i, bArr);
    }

    public MissingRowDummyRecord(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return this.rowNumber;
    }
}
