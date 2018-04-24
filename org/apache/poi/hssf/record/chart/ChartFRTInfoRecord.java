package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class ChartFRTInfoRecord extends StandardRecord {
    public static final short sid = (short) 2128;
    private short grbitFrt;
    private CFRTID[] rgCFRTID;
    private short rt;
    private byte verOriginator;
    private byte verWriter;

    private static final class CFRTID {
        public static final int ENCODED_SIZE = 4;
        private int rtFirst;
        private int rtLast;

        public CFRTID(LittleEndianInput in) {
            this.rtFirst = in.readShort();
            this.rtLast = in.readShort();
        }

        public void serialize(LittleEndianOutput out) {
            out.writeShort(this.rtFirst);
            out.writeShort(this.rtLast);
        }
    }

    public ChartFRTInfoRecord(RecordInputStream in) {
        this.rt = in.readShort();
        this.grbitFrt = in.readShort();
        this.verOriginator = in.readByte();
        this.verWriter = in.readByte();
        int cCFRTID = in.readShort();
        this.rgCFRTID = new CFRTID[cCFRTID];
        for (int i = 0; i < cCFRTID; i++) {
            this.rgCFRTID[i] = new CFRTID(in);
        }
    }

    protected int getDataSize() {
        return (this.rgCFRTID.length * 4) + 8;
    }

    public short getSid() {
        return sid;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.rt);
        out.writeShort(this.grbitFrt);
        out.writeByte(this.verOriginator);
        out.writeByte(this.verWriter);
        out.writeShort(nCFRTIDs);
        for (CFRTID serialize : this.rgCFRTID) {
            serialize.serialize(out);
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CHARTFRTINFO]\n");
        buffer.append("    .rt           =").append(HexDump.shortToHex(this.rt)).append('\n');
        buffer.append("    .grbitFrt     =").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
        buffer.append("    .verOriginator=").append(HexDump.byteToHex(this.verOriginator)).append('\n');
        buffer.append("    .verWriter    =").append(HexDump.byteToHex(this.verOriginator)).append('\n');
        buffer.append("    .nCFRTIDs     =").append(HexDump.shortToHex(this.rgCFRTID.length)).append('\n');
        buffer.append("[/CHARTFRTINFO]\n");
        return buffer.toString();
    }
}
