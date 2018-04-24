package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class DataLabelExtensionRecord extends StandardRecord {
    public static final short sid = (short) 2154;
    private int grbitFrt;
    private int rt;
    private byte[] unused = new byte[8];

    public DataLabelExtensionRecord(RecordInputStream in) {
        this.rt = in.readShort();
        this.grbitFrt = in.readShort();
        in.readFully(this.unused);
    }

    protected int getDataSize() {
        return 12;
    }

    public short getSid() {
        return sid;
    }

    protected void serialize(LittleEndianOutput out) {
        out.writeShort(this.rt);
        out.writeShort(this.grbitFrt);
        out.write(this.unused);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DATALABEXT]\n");
        buffer.append("    .rt      =").append(HexDump.shortToHex(this.rt)).append('\n');
        buffer.append("    .grbitFrt=").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
        buffer.append("    .unused  =").append(HexDump.toHex(this.unused)).append('\n');
        buffer.append("[/DATALABEXT]\n");
        return buffer.toString();
    }
}
