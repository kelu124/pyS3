package org.apache.poi.hssf.eventmodel;

import org.apache.poi.hssf.record.Record;

public interface ERFListener {
    boolean processRecord(Record record);
}
