package org.apache.poi.hssf.model;

import java.util.List;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;

class InternalSheet$1 implements RecordVisitor {
    final /* synthetic */ List val$recs;

    InternalSheet$1(List list) {
        this.val$recs = list;
    }

    public void visitRecord(Record r) {
        this.val$recs.add(r);
    }
}
