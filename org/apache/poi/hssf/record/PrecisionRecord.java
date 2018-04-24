package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class PrecisionRecord extends StandardRecord {
    public static final short sid = (short) 14;
    public short field_1_precision;

    public PrecisionRecord(RecordInputStream in) {
        this.field_1_precision = in.readShort();
    }

    public void setFullPrecision(boolean fullprecision) {
        if (fullprecision) {
            this.field_1_precision = (short) 1;
        } else {
            this.field_1_precision = (short) 0;
        }
    }

    public boolean getFullPrecision() {
        return this.field_1_precision == (short) 1;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PRECISION]\n");
        buffer.append("    .precision       = ").append(getFullPrecision()).append("\n");
        buffer.append("[/PRECISION]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_precision);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 14;
    }
}
