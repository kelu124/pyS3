package org.apache.poi.hssf.model;

import java.util.List;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;

final class InternalSheet$RecordCloner implements RecordVisitor {
    private final List<Record> _destList;

    public InternalSheet$RecordCloner(List<Record> destList) {
        this._destList = destList;
    }

    public void visitRecord(Record r) {
        try {
            this._destList.add((Record) r.clone());
        } catch (Throwable e) {
            throw new RecordFormatException(e);
        }
    }
}
