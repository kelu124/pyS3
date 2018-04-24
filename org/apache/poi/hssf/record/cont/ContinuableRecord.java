package org.apache.poi.hssf.record.cont;

import org.apache.poi.hssf.record.Record;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;

public abstract class ContinuableRecord extends Record {
    protected abstract void serialize(ContinuableRecordOutput continuableRecordOutput);

    protected ContinuableRecord() {
    }

    public final int getRecordSize() {
        ContinuableRecordOutput out = ContinuableRecordOutput.createForCountingOnly();
        serialize(out);
        out.terminate();
        return out.getTotalSize();
    }

    public final int serialize(int offset, byte[] data) {
        ContinuableRecordOutput out = new ContinuableRecordOutput(new LittleEndianByteArrayOutputStream(data, offset), getSid());
        serialize(out);
        out.terminate();
        return out.getTotalSize();
    }
}
