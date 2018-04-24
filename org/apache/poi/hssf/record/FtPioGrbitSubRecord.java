package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class FtPioGrbitSubRecord extends SubRecord implements Cloneable {
    public static final int AUTO_LOAD_BIT = 512;
    public static final int AUTO_PICT_BIT = 1;
    public static final int CAMERA_BIT = 128;
    public static final int CTL_BIT = 16;
    public static final int DDE_BIT = 2;
    public static final int DEFAULT_SIZE_BIT = 256;
    public static final int ICON_BIT = 8;
    public static final int PRINT_CALC_BIT = 4;
    public static final int PRSTM_BIT = 32;
    public static final short length = (short) 2;
    public static final short sid = (short) 8;
    private short flags = (short) 0;

    public FtPioGrbitSubRecord(LittleEndianInput in, int size) {
        if (size != 2) {
            throw new RecordFormatException("Unexpected size (" + size + ")");
        }
        this.flags = in.readShort();
    }

    public void setFlagByBit(int bitmask, boolean enabled) {
        if (enabled) {
            this.flags = (short) (this.flags | bitmask);
        } else {
            this.flags = (short) (this.flags & (65535 ^ bitmask));
        }
    }

    public boolean getFlagByBit(int bitmask) {
        return (this.flags & bitmask) != 0;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FtPioGrbit ]\n");
        buffer.append("  size     = ").append(2).append("\n");
        buffer.append("  flags    = ").append(HexDump.toHex(this.flags)).append("\n");
        buffer.append("[/FtPioGrbit ]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(8);
        out.writeShort(2);
        out.writeShort(this.flags);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 8;
    }

    public FtPioGrbitSubRecord clone() {
        FtPioGrbitSubRecord rec = new FtPioGrbitSubRecord();
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
