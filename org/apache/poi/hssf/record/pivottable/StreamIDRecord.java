package org.apache.poi.hssf.record.pivottable;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class StreamIDRecord extends StandardRecord {
    public static final short sid = (short) 213;
    private int idstm;

    public StreamIDRecord(RecordInputStream in) {
        this.idstm = in.readShort();
    }

    protected void serialize(LittleEndianOutput out) {
        out.writeShort(this.idstm);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SXIDSTM]\n");
        buffer.append("    .idstm      =").append(HexDump.shortToHex(this.idstm)).append('\n');
        buffer.append("[/SXIDSTM]\n");
        return buffer.toString();
    }
}
