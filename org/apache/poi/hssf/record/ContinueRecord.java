package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class ContinueRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 60;
    private byte[] _data;

    public ContinueRecord(byte[] data) {
        this._data = data;
    }

    protected int getDataSize() {
        return this._data.length;
    }

    public void serialize(LittleEndianOutput out) {
        out.write(this._data);
    }

    public byte[] getData() {
        return this._data;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CONTINUE RECORD]\n");
        buffer.append("    .data = ").append(HexDump.toHex(this._data)).append("\n");
        buffer.append("[/CONTINUE RECORD]\n");
        return buffer.toString();
    }

    public short getSid() {
        return (short) 60;
    }

    public ContinueRecord(RecordInputStream in) {
        this._data = in.readRemainder();
    }

    public ContinueRecord clone() {
        return new ContinueRecord(this._data);
    }
}
