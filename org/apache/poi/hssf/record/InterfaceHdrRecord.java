package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class InterfaceHdrRecord extends StandardRecord {
    public static final int CODEPAGE = 1200;
    public static final short sid = (short) 225;
    private final int _codepage;

    public InterfaceHdrRecord(int codePage) {
        this._codepage = codePage;
    }

    public InterfaceHdrRecord(RecordInputStream in) {
        this._codepage = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[INTERFACEHDR]\n");
        buffer.append("    .codepage = ").append(HexDump.shortToHex(this._codepage)).append("\n");
        buffer.append("[/INTERFACEHDR]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._codepage);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }
}
