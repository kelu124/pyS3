package org.apache.poi.hssf.record;

public abstract class RecordBase {
    public abstract int getRecordSize();

    public abstract int serialize(int i, byte[] bArr);
}
