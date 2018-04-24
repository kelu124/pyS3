package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class AutoFilterInfoRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 157;
    private short _cEntries;

    public AutoFilterInfoRecord(RecordInputStream in) {
        this._cEntries = in.readShort();
    }

    public void setNumEntries(short num) {
        this._cEntries = num;
    }

    public short getNumEntries() {
        return this._cEntries;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[AUTOFILTERINFO]\n");
        buffer.append("    .numEntries          = ").append(this._cEntries).append("\n");
        buffer.append("[/AUTOFILTERINFO]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._cEntries);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 157;
    }

    public AutoFilterInfoRecord clone() {
        return (AutoFilterInfoRecord) cloneViaReserialise();
    }
}
