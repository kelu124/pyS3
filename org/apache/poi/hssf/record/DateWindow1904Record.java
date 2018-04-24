package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class DateWindow1904Record extends StandardRecord {
    public static final short sid = (short) 34;
    private short field_1_window;

    public DateWindow1904Record(RecordInputStream in) {
        this.field_1_window = in.readShort();
    }

    public void setWindowing(short window) {
        this.field_1_window = window;
    }

    public short getWindowing() {
        return this.field_1_window;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[1904]\n");
        buffer.append("    .is1904          = ").append(Integer.toHexString(getWindowing())).append("\n");
        buffer.append("[/1904]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getWindowing());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 34;
    }
}
