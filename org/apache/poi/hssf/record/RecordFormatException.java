package org.apache.poi.hssf.record;

public class RecordFormatException extends org.apache.poi.util.RecordFormatException {
    public RecordFormatException(String exception) {
        super(exception);
    }

    public RecordFormatException(String exception, Throwable thr) {
        super(exception, thr);
    }

    public RecordFormatException(Throwable thr) {
        super(thr);
    }
}
