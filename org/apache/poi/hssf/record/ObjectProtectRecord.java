package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class ObjectProtectRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 99;
    private short field_1_protect;

    public ObjectProtectRecord(RecordInputStream in) {
        this.field_1_protect = in.readShort();
    }

    public void setProtect(boolean protect) {
        if (protect) {
            this.field_1_protect = (short) 1;
        } else {
            this.field_1_protect = (short) 0;
        }
    }

    public boolean getProtect() {
        return this.field_1_protect == (short) 1;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SCENARIOPROTECT]\n");
        buffer.append("    .protect         = ").append(getProtect()).append("\n");
        buffer.append("[/SCENARIOPROTECT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_protect);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 99;
    }

    public ObjectProtectRecord clone() {
        ObjectProtectRecord rec = new ObjectProtectRecord();
        rec.field_1_protect = this.field_1_protect;
        return rec;
    }
}
