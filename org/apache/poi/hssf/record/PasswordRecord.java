package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class PasswordRecord extends StandardRecord {
    public static final short sid = (short) 19;
    private int field_1_password;

    public PasswordRecord(int password) {
        this.field_1_password = password;
    }

    public PasswordRecord(RecordInputStream in) {
        this.field_1_password = in.readShort();
    }

    public void setPassword(int password) {
        this.field_1_password = password;
    }

    public int getPassword() {
        return this.field_1_password;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PASSWORD]\n");
        buffer.append("    .password = ").append(HexDump.shortToHex(this.field_1_password)).append("\n");
        buffer.append("[/PASSWORD]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_password);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 19;
    }

    public Object clone() {
        return new PasswordRecord(this.field_1_password);
    }
}
