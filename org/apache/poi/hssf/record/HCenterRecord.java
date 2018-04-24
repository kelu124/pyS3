package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class HCenterRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 131;
    private short field_1_hcenter;

    public HCenterRecord(RecordInputStream in) {
        this.field_1_hcenter = in.readShort();
    }

    public void setHCenter(boolean hc) {
        if (hc) {
            this.field_1_hcenter = (short) 1;
        } else {
            this.field_1_hcenter = (short) 0;
        }
    }

    public boolean getHCenter() {
        return this.field_1_hcenter == (short) 1;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[HCENTER]\n");
        buffer.append("    .hcenter        = ").append(getHCenter()).append("\n");
        buffer.append("[/HCENTER]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_hcenter);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 131;
    }

    public HCenterRecord clone() {
        HCenterRecord rec = new HCenterRecord();
        rec.field_1_hcenter = this.field_1_hcenter;
        return rec;
    }
}
