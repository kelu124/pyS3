package org.apache.poi.hssf.usermodel;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;

final class HSSFWorkbook$SheetRecordCollector implements RecordVisitor {
    private List<Record> _list = new ArrayList(128);
    private int _totalSize = 0;

    public int getTotalSize() {
        return this._totalSize;
    }

    public void visitRecord(Record r) {
        this._list.add(r);
        this._totalSize += r.getRecordSize();
    }

    public int serialize(int offset, byte[] data) {
        int result = 0;
        for (Record rec : this._list) {
            result += rec.serialize(offset + result, data);
        }
        return result;
    }
}
