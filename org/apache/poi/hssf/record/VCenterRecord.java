package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class VCenterRecord extends StandardRecord {
    public static final short sid = (short) 132;
    private int field_1_vcenter;

    public VCenterRecord(RecordInputStream in) {
        this.field_1_vcenter = in.readShort();
    }

    public void setVCenter(boolean hc) {
        this.field_1_vcenter = hc ? 1 : 0;
    }

    public boolean getVCenter() {
        return this.field_1_vcenter == 1;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[VCENTER]\n");
        buffer.append("    .vcenter        = ").append(getVCenter()).append("\n");
        buffer.append("[/VCENTER]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_vcenter);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 132;
    }

    public Object clone() {
        VCenterRecord rec = new VCenterRecord();
        rec.field_1_vcenter = this.field_1_vcenter;
        return rec;
    }
}
