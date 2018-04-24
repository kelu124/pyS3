package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class PasswordRev4Record extends StandardRecord {
    public static final short sid = (short) 444;
    private int field_1_password;

    public PasswordRev4Record(int pw) {
        this.field_1_password = pw;
    }

    public PasswordRev4Record(RecordInputStream in) {
        this.field_1_password = in.readShort();
    }

    public void setPassword(short pw) {
        this.field_1_password = pw;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PROT4REVPASSWORD]\n");
        buffer.append("    .password = ").append(HexDump.shortToHex(this.field_1_password)).append("\n");
        buffer.append("[/PROT4REVPASSWORD]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_password);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 444;
    }
}
