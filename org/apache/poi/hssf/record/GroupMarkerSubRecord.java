package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class GroupMarkerSubRecord extends SubRecord implements Cloneable {
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final short sid = (short) 6;
    private byte[] reserved;

    public GroupMarkerSubRecord() {
        this.reserved = EMPTY_BYTE_ARRAY;
    }

    public GroupMarkerSubRecord(LittleEndianInput in, int size) {
        byte[] buf = new byte[size];
        in.readFully(buf);
        this.reserved = buf;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        String nl = System.getProperty("line.separator");
        buffer.append("[ftGmo]" + nl);
        buffer.append("  reserved = ").append(HexDump.toHex(this.reserved)).append(nl);
        buffer.append("[/ftGmo]" + nl);
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(6);
        out.writeShort(this.reserved.length);
        out.write(this.reserved);
    }

    protected int getDataSize() {
        return this.reserved.length;
    }

    public short getSid() {
        return (short) 6;
    }

    public GroupMarkerSubRecord clone() {
        GroupMarkerSubRecord rec = new GroupMarkerSubRecord();
        rec.reserved = new byte[this.reserved.length];
        for (int i = 0; i < this.reserved.length; i++) {
            rec.reserved[i] = this.reserved[i];
        }
        return rec;
    }
}
