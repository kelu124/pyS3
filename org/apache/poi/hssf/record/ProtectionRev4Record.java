package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class ProtectionRev4Record extends StandardRecord {
    private static final BitField protectedFlag = BitFieldFactory.getInstance(1);
    public static final short sid = (short) 431;
    private int _options;

    private ProtectionRev4Record(int options) {
        this._options = options;
    }

    public ProtectionRev4Record(boolean protect) {
        this(0);
        setProtect(protect);
    }

    public ProtectionRev4Record(RecordInputStream in) {
        this(in.readUShort());
    }

    public void setProtect(boolean protect) {
        this._options = protectedFlag.setBoolean(this._options, protect);
    }

    public boolean getProtect() {
        return protectedFlag.isSet(this._options);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PROT4REV]\n");
        buffer.append("    .options = ").append(HexDump.shortToHex(this._options)).append("\n");
        buffer.append("[/PROT4REV]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._options);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
