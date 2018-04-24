package org.apache.poi.hssf.record.aggregates;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.model.RecordStream;
import org.apache.poi.hssf.record.DVALRecord;
import org.apache.poi.hssf.record.DVRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;

public final class DataValidityTable extends RecordAggregate {
    private final DVALRecord _headerRec;
    private final List<DVRecord> _validationList;

    public DataValidityTable(RecordStream rs) {
        this._headerRec = (DVALRecord) rs.getNext();
        List<DVRecord> temp = new ArrayList();
        while (rs.peekNextClass() == DVRecord.class) {
            temp.add((DVRecord) rs.getNext());
        }
        this._validationList = temp;
    }

    public DataValidityTable() {
        this._headerRec = new DVALRecord();
        this._validationList = new ArrayList();
    }

    public void visitContainedRecords(RecordVisitor rv) {
        if (!this._validationList.isEmpty()) {
            rv.visitRecord(this._headerRec);
            for (int i = 0; i < this._validationList.size(); i++) {
                rv.visitRecord((Record) this._validationList.get(i));
            }
        }
    }

    public void addDataValidation(DVRecord dvRecord) {
        this._validationList.add(dvRecord);
        this._headerRec.setDVRecNo(this._validationList.size());
    }
}
