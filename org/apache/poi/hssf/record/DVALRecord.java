package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class DVALRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 434;
    private short field_1_options;
    private int field_2_horiz_pos;
    private int field_3_vert_pos;
    private int field_5_dv_no;
    private int field_cbo_id;

    public DVALRecord() {
        this.field_cbo_id = -1;
        this.field_5_dv_no = 0;
    }

    public DVALRecord(RecordInputStream in) {
        this.field_1_options = in.readShort();
        this.field_2_horiz_pos = in.readInt();
        this.field_3_vert_pos = in.readInt();
        this.field_cbo_id = in.readInt();
        this.field_5_dv_no = in.readInt();
    }

    public void setOptions(short options) {
        this.field_1_options = options;
    }

    public void setHorizontalPos(int horiz_pos) {
        this.field_2_horiz_pos = horiz_pos;
    }

    public void setVerticalPos(int vert_pos) {
        this.field_3_vert_pos = vert_pos;
    }

    public void setObjectID(int cboID) {
        this.field_cbo_id = cboID;
    }

    public void setDVRecNo(int dvNo) {
        this.field_5_dv_no = dvNo;
    }

    public short getOptions() {
        return this.field_1_options;
    }

    public int getHorizontalPos() {
        return this.field_2_horiz_pos;
    }

    public int getVerticalPos() {
        return this.field_3_vert_pos;
    }

    public int getObjectID() {
        return this.field_cbo_id;
    }

    public int getDVRecNo() {
        return this.field_5_dv_no;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DVAL]\n");
        buffer.append("    .options      = ").append(getOptions()).append('\n');
        buffer.append("    .horizPos     = ").append(getHorizontalPos()).append('\n');
        buffer.append("    .vertPos      = ").append(getVerticalPos()).append('\n');
        buffer.append("    .comboObjectID   = ").append(Integer.toHexString(getObjectID())).append("\n");
        buffer.append("    .DVRecordsNumber = ").append(Integer.toHexString(getDVRecNo())).append("\n");
        buffer.append("[/DVAL]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getOptions());
        out.writeInt(getHorizontalPos());
        out.writeInt(getVerticalPos());
        out.writeInt(getObjectID());
        out.writeInt(getDVRecNo());
    }

    protected int getDataSize() {
        return 18;
    }

    public short getSid() {
        return sid;
    }

    public DVALRecord clone() {
        DVALRecord rec = new DVALRecord();
        rec.field_1_options = this.field_1_options;
        rec.field_2_horiz_pos = this.field_2_horiz_pos;
        rec.field_3_vert_pos = this.field_3_vert_pos;
        rec.field_cbo_id = this.field_cbo_id;
        rec.field_5_dv_no = this.field_5_dv_no;
        return rec;
    }
}
