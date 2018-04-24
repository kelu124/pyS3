package org.apache.poi.hssf.record;

import java.util.Locale;

public final class RecordInputStream$LeftoverDataException extends RuntimeException {
    public RecordInputStream$LeftoverDataException(int sid, int remainingByteCount) {
        super("Initialisation of record 0x" + Integer.toHexString(sid).toUpperCase(Locale.ROOT) + "(" + getRecordName(sid) + ") left " + remainingByteCount + " bytes remaining still to be read.");
    }

    private static String getRecordName(int sid) {
        Class<? extends Record> recordClass = RecordFactory.getRecordClass(sid);
        if (recordClass == null) {
            return null;
        }
        return recordClass.getSimpleName();
    }
}
