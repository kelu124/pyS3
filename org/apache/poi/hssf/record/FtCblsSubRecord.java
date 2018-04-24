package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class FtCblsSubRecord extends SubRecord implements Cloneable {
    private static final int ENCODED_SIZE = 20;
    public static final short sid = (short) 12;
    private byte[] reserved;

    public FtCblsSubRecord() {
        this.reserved = new byte[20];
    }

    public FtCblsSubRecord(LittleEndianInput in, int size) {
        if (size != 20) {
            throw new RecordFormatException("Unexpected size (" + size + ")");
        }
        byte[] buf = new byte[size];
        in.readFully(buf);
        this.reserved = buf;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FtCbls ]").append("\n");
        buffer.append("  size     = ").append(getDataSize()).append("\n");
        buffer.append("  reserved = ").append(HexDump.toHex(this.reserved)).append("\n");
        buffer.append("[/FtCbls ]").append("\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(12);
        out.writeShort(this.reserved.length);
        out.write(this.reserved);
    }

    protected int getDataSize() {
        return this.reserved.length;
    }

    public short getSid() {
        return (short) 12;
    }

    public FtCblsSubRecord clone() {
        FtCblsSubRecord rec = new FtCblsSubRecord();
        byte[] recdata = new byte[this.reserved.length];
        System.arraycopy(this.reserved, 0, recdata, 0, recdata.length);
        rec.reserved = recdata;
        return rec;
    }
}
