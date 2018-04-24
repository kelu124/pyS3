package org.apache.poi.hssf.record;

import java.io.ByteArrayInputStream;

public final class DrawingRecordForBiffViewer extends AbstractEscherHolderRecord {
    public static final short sid = (short) 236;

    public DrawingRecordForBiffViewer(RecordInputStream in) {
        super(in);
    }

    public DrawingRecordForBiffViewer(DrawingRecord r) {
        super(convertToInputStream(r));
        convertRawBytesToEscherRecords();
    }

    private static RecordInputStream convertToInputStream(DrawingRecord r) {
        RecordInputStream rinp = new RecordInputStream(new ByteArrayInputStream(r.serialize()));
        rinp.nextRecord();
        return rinp;
    }

    protected String getRecordName() {
        return "MSODRAWING";
    }

    public short getSid() {
        return (short) 236;
    }
}
