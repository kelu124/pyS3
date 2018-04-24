package org.apache.poi.hssf.record.common;

import org.apache.poi.ss.util.CellRangeAddress;

public interface FutureRecord {
    CellRangeAddress getAssociatedRange();

    FtrHeader getFutureHeader();

    short getFutureRecordType();
}
