package org.apache.poi.hssf.eventusermodel;

import org.apache.poi.hssf.record.Record;

public interface HSSFListener {
    void processRecord(Record record);
}
