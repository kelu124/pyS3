package org.apache.poi.hssf.record.pivottable;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class DataItemRecord extends StandardRecord {
    public static final short sid = (short) 197;
    private int df;
    private int ifmt;
    private int iiftab;
    private int isxvd;
    private int isxvdData;
    private int isxvi;
    private String name;

    public DataItemRecord(RecordInputStream in) {
        this.isxvdData = in.readUShort();
        this.iiftab = in.readUShort();
        this.df = in.readUShort();
        this.isxvd = in.readUShort();
        this.isxvi = in.readUShort();
        this.ifmt = in.readUShort();
        this.name = in.readString();
    }

    protected void serialize(LittleEndianOutput out) {
        out.writeShort(this.isxvdData);
        out.writeShort(this.iiftab);
        out.writeShort(this.df);
        out.writeShort(this.isxvd);
        out.writeShort(this.isxvi);
        out.writeShort(this.ifmt);
        StringUtil.writeUnicodeString(out, this.name);
    }

    protected int getDataSize() {
        return StringUtil.getEncodedSize(this.name) + 12;
    }

    public short getSid() {
        return (short) 197;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SXDI]\n");
        buffer.append("  .isxvdData = ").append(HexDump.shortToHex(this.isxvdData)).append("\n");
        buffer.append("  .iiftab = ").append(HexDump.shortToHex(this.iiftab)).append("\n");
        buffer.append("  .df = ").append(HexDump.shortToHex(this.df)).append("\n");
        buffer.append("  .isxvd = ").append(HexDump.shortToHex(this.isxvd)).append("\n");
        buffer.append("  .isxvi = ").append(HexDump.shortToHex(this.isxvi)).append("\n");
        buffer.append("  .ifmt = ").append(HexDump.shortToHex(this.ifmt)).append("\n");
        buffer.append("[/SXDI]\n");
        return buffer.toString();
    }
}
