package org.apache.poi.hssf.record.cf;

import org.apache.poi.util.LittleEndianInput;

public final class DataBarThreshold extends Threshold implements Cloneable {
    public DataBarThreshold(LittleEndianInput in) {
        super(in);
    }

    public DataBarThreshold clone() {
        DataBarThreshold rec = new DataBarThreshold();
        super.copyTo(rec);
        return rec;
    }
}
