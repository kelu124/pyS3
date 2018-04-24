package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class FtCfSubRecord extends SubRecord implements Cloneable {
    public static final short BITMAP_BIT = (short) 9;
    public static final short METAFILE_BIT = (short) 2;
    public static final short UNSPECIFIED_BIT = (short) -1;
    public static final short length = (short) 2;
    public static final short sid = (short) 7;
    private short flags = (short) 0;

    public FtCfSubRecord(LittleEndianInput in, int size) {
        if (size != 2) {
            throw new RecordFormatException("Unexpected size (" + size + ")");
        }
        this.flags = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FtCf ]\n");
        buffer.append("  size     = ").append(2).append("\n");
        buffer.append("  flags    = ").append(HexDump.toHex(this.flags)).append("\n");
        buffer.append("[/FtCf ]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(7);
        out.writeShort(2);
        out.writeShort(this.flags);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 7;
    }

    public FtCfSubRecord clone() {
        FtCfSubRecord rec = new FtCfSubRecord();
        rec.flags = this.flags;
        return rec;
    }

    public short getFlags() {
        return this.flags;
    }

    public void setFlags(short flags) {
        this.flags = flags;
    }
}
