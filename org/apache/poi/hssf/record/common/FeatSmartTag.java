package org.apache.poi.hssf.record.common;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.util.LittleEndianOutput;

public final class FeatSmartTag implements SharedFeature {
    private byte[] data;

    public FeatSmartTag() {
        this.data = new byte[0];
    }

    public FeatSmartTag(RecordInputStream in) {
        this.data = in.readRemainder();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" [FEATURE SMART TAGS]\n");
        buffer.append(" [/FEATURE SMART TAGS]\n");
        return buffer.toString();
    }

    public int getDataSize() {
        return this.data.length;
    }

    public void serialize(LittleEndianOutput out) {
        out.write(this.data);
    }
}
