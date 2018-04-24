package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class RightMarginRecord extends StandardRecord implements Margin {
    public static final short sid = (short) 39;
    private double field_1_margin;

    public RightMarginRecord(RecordInputStream in) {
        this.field_1_margin = in.readDouble();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[RightMargin]\n");
        buffer.append("    .margin               = ").append(" (").append(getMargin()).append(" )\n");
        buffer.append("[/RightMargin]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeDouble(this.field_1_margin);
    }

    protected int getDataSize() {
        return 8;
    }

    public short getSid() {
        return (short) 39;
    }

    public double getMargin() {
        return this.field_1_margin;
    }

    public void setMargin(double field_1_margin) {
        this.field_1_margin = field_1_margin;
    }

    public Object clone() {
        RightMarginRecord rec = new RightMarginRecord();
        rec.field_1_margin = this.field_1_margin;
        return rec;
    }
}
