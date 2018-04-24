package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class ChartStartBlockRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 2130;
    private short grbitFrt;
    private short iObjectContext;
    private short iObjectInstance1;
    private short iObjectInstance2;
    private short iObjectKind;
    private short rt;

    public ChartStartBlockRecord(RecordInputStream in) {
        this.rt = in.readShort();
        this.grbitFrt = in.readShort();
        this.iObjectKind = in.readShort();
        this.iObjectContext = in.readShort();
        this.iObjectInstance1 = in.readShort();
        this.iObjectInstance2 = in.readShort();
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
        out.writeShort(this.iObjectContext);
        out.writeShort(this.iObjectInstance1);
        out.writeShort(this.iObjectInstance2);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[STARTBLOCK]\n");
        buffer.append("    .rt              =").append(HexDump.shortToHex(this.rt)).append('\n');
        buffer.append("    .grbitFrt        =").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
        buffer.append("    .iObjectKind     =").append(HexDump.shortToHex(this.iObjectKind)).append('\n');
        buffer.append("    .iObjectContext  =").append(HexDump.shortToHex(this.iObjectContext)).append('\n');
        buffer.append("    .iObjectInstance1=").append(HexDump.shortToHex(this.iObjectInstance1)).append('\n');
        buffer.append("    .iObjectInstance2=").append(HexDump.shortToHex(this.iObjectInstance2)).append('\n');
        buffer.append("[/STARTBLOCK]\n");
        return buffer.toString();
    }

    public ChartStartBlockRecord clone() {
        ChartStartBlockRecord record = new ChartStartBlockRecord();
        record.rt = this.rt;
        record.grbitFrt = this.grbitFrt;
        record.iObjectKind = this.iObjectKind;
        record.iObjectContext = this.iObjectContext;
        record.iObjectInstance1 = this.iObjectInstance1;
        record.iObjectInstance2 = this.iObjectInstance2;
        return record;
    }
}
