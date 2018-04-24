package org.apache.poi.hssf.eventusermodel;

import org.apache.poi.hssf.record.Record;

public abstract class AbortableHSSFListener implements HSSFListener {
    public abstract short abortableProcessRecord(Record record) throws HSSFUserException;

    public void processRecord(Record record) {
    }
}
