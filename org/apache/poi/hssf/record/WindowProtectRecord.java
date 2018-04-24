package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class WindowProtectRecord extends StandardRecord {
    private static final BitField settingsProtectedFlag = BitFieldFactory.getInstance(1);
    public static final short sid = (short) 25;
    private int _options;

    public WindowProtectRecord(int options) {
        this._options = options;
    }

    public WindowProtectRecord(RecordInputStream in) {
        this(in.readUShort());
    }

    public WindowProtectRecord(boolean protect) {
        this(0);
        setProtect(protect);
    }

    public void setProtect(boolean protect) {
        this._options = settingsProtectedFlag.setBoolean(this._options, protect);
    }

    public boolean getProtect() {
        return settingsProtectedFlag.isSet(this._options);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[WINDOWPROTECT]\n");
        buffer.append("    .options = ").append(HexDump.shortToHex(this._options)).append("\n");
        buffer.append("[/WINDOWPROTECT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._options);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 25;
    }

    public Object clone() {
        return new WindowProtectRecord(this._options);
    }
}
