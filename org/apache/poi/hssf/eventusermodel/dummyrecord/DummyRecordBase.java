package org.apache.poi.hssf.eventusermodel.dummyrecord;

import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFormatException;

abstract class DummyRecordBase extends Record {
    protected DummyRecordBase() {
    }

    public final short getSid() {
        return (short) -1;
    }

    public int serialize(int offset, byte[] data) {
        throw new RecordFormatException("Cannot serialize a dummy record");
    }

    public final int getRecordSize() {
        throw new RecordFormatException("Cannot serialize a dummy record");
    }
}
