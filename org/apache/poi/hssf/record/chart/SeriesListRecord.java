package org.apache.poi.hssf.record.chart;

import java.util.Arrays;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.LittleEndianOutput;

public final class SeriesListRecord extends StandardRecord {
    public static final short sid = (short) 4118;
    private short[] field_1_seriesNumbers;

    public SeriesListRecord(short[] seriesNumbers) {
        this.field_1_seriesNumbers = seriesNumbers == null ? null : (short[]) seriesNumbers.clone();
    }

    public SeriesListRecord(RecordInputStream in) {
        int nItems = in.readUShort();
        short[] ss = new short[nItems];
        for (int i = 0; i < nItems; i++) {
            ss[i] = in.readShort();
        }
        this.field_1_seriesNumbers = ss;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SERIESLIST]\n");
        buffer.append("    .seriesNumbers= ").append(" (").append(Arrays.toString(getSeriesNumbers())).append(" )");
        buffer.append("\n");
        buffer.append("[/SERIESLIST]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(nItems);
        for (short writeShort : this.field_1_seriesNumbers) {
            out.writeShort(writeShort);
        }
    }

    protected int getDataSize() {
        return (this.field_1_seriesNumbers.length * 2) + 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        return new SeriesListRecord(this.field_1_seriesNumbers);
    }

    public short[] getSeriesNumbers() {
        return this.field_1_seriesNumbers;
    }
}
