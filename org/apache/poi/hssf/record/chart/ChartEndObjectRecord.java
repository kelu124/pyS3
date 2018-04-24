package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class ChartEndObjectRecord extends StandardRecord {
    public static final short sid = (short) 2133;
    private short grbitFrt;
    private short iObjectKind;
    private byte[] reserved = new byte[6];
    private short rt;

    public ChartEndObjectRecord(RecordInputStream in) {
        this.rt = in.readShort();
        this.grbitFrt = in.readShort();
        this.iObjectKind = in.readShort();
        if (in.available() != 0) {
            in.readFully(this.reserved);
        }
    }

    protected int getDataSize() {
        return 12;
    }

    public short getSid() {
        return sid;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.rt);
        out.writeShort(this.grbitFrt);
        out.writeShort(this.iObjectKind);
        out.write(this.reserved);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ENDOBJECT]\n");
        buffer.append("    .rt         =").append(HexDump.shortToHex(this.rt)).append('\n');
        buffer.append("    .grbitFrt   =").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
        buffer.append("    .iObjectKind=").append(HexDump.shortToHex(this.iObjectKind)).append('\n');
        buffer.append("    .reserved   =").append(HexDump.toHex(this.reserved)).append('\n');
        buffer.append("[/ENDOBJECT]\n");
        return buffer.toString();
    }
}
