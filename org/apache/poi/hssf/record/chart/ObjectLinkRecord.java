package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class ObjectLinkRecord extends StandardRecord implements Cloneable {
    public static final short ANCHOR_ID_CHART_TITLE = (short) 1;
    public static final short ANCHOR_ID_SERIES_OR_POINT = (short) 4;
    public static final short ANCHOR_ID_X_AXIS = (short) 3;
    public static final short ANCHOR_ID_Y_AXIS = (short) 2;
    public static final short ANCHOR_ID_Z_AXIS = (short) 7;
    public static final short sid = (short) 4135;
    private short field_1_anchorId;
    private short field_2_link1;
    private short field_3_link2;

    public ObjectLinkRecord(RecordInputStream in) {
        this.field_1_anchorId = in.readShort();
        this.field_2_link1 = in.readShort();
        this.field_3_link2 = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[OBJECTLINK]\n");
        buffer.append("    .anchorId             = ").append("0x").append(HexDump.toHex(getAnchorId())).append(" (").append(getAnchorId()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .link1                = ").append("0x").append(HexDump.toHex(getLink1())).append(" (").append(getLink1()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .link2                = ").append("0x").append(HexDump.toHex(getLink2())).append(" (").append(getLink2()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/OBJECTLINK]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_anchorId);
        out.writeShort(this.field_2_link1);
        out.writeShort(this.field_3_link2);
    }

    protected int getDataSize() {
        return 6;
    }

    public short getSid() {
        return sid;
    }

    public ObjectLinkRecord clone() {
        ObjectLinkRecord rec = new ObjectLinkRecord();
        rec.field_1_anchorId = this.field_1_anchorId;
        rec.field_2_link1 = this.field_2_link1;
        rec.field_3_link2 = this.field_3_link2;
        return rec;
    }

    public short getAnchorId() {
        return this.field_1_anchorId;
    }

    public void setAnchorId(short field_1_anchorId) {
        this.field_1_anchorId = field_1_anchorId;
    }

    public short getLink1() {
        return this.field_2_link1;
    }

    public void setLink1(short field_2_link1) {
        this.field_2_link1 = field_2_link1;
    }

    public short getLink2() {
        return this.field_3_link2;
    }

    public void setLink2(short field_3_link2) {
        this.field_3_link2 = field_3_link2;
    }
}
