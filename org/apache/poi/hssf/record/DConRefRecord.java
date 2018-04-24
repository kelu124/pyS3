package org.apache.poi.hssf.record;

import java.util.Arrays;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public class DConRefRecord extends StandardRecord {
    public static final short sid = (short) 81;
    private byte[] _unused;
    private int charCount;
    private int charType;
    private int firstCol;
    private int firstRow;
    private int lastCol;
    private int lastRow;
    private byte[] path;

    public DConRefRecord(byte[] data) {
        if (LittleEndian.getShort(data, 0) != (short) 81) {
            throw new RecordFormatException("incompatible sid.");
        }
        int offset = (0 + 2) + 2;
        this.firstRow = LittleEndian.getUShort(data, offset);
        offset += 2;
        this.lastRow = LittleEndian.getUShort(data, offset);
        offset += 2;
        this.firstCol = LittleEndian.getUByte(data, offset);
        offset++;
        this.lastCol = LittleEndian.getUByte(data, offset);
        offset++;
        this.charCount = LittleEndian.getUShort(data, offset);
        offset += 2;
        if (this.charCount < 2) {
            throw new RecordFormatException("Character count must be >= 2");
        }
        this.charType = LittleEndian.getUByte(data, offset);
        int byteLength = this.charCount * ((this.charType & 1) + 1);
        this.path = LittleEndian.getByteArray(data, offset + 1, byteLength);
        offset = byteLength + 13;
        if (this.path[0] == (byte) 2) {
            this._unused = LittleEndian.getByteArray(data, offset, this.charType + 1);
        }
    }

    public DConRefRecord(RecordInputStream inStream) {
        if (inStream.getSid() != (short) 81) {
            throw new RecordFormatException("Wrong sid: " + inStream.getSid());
        }
        this.firstRow = inStream.readUShort();
        this.lastRow = inStream.readUShort();
        this.firstCol = inStream.readUByte();
        this.lastCol = inStream.readUByte();
        this.charCount = inStream.readUShort();
        this.charType = inStream.readUByte() & 1;
        this.path = new byte[(this.charCount * (this.charType + 1))];
        inStream.readFully(this.path);
        if (this.path[0] == (byte) 2) {
            this._unused = inStream.readRemainder();
        }
    }

    protected int getDataSize() {
        int sz = this.path.length + 9;
        if (this.path[0] == (byte) 2) {
            return sz + this._unused.length;
        }
        return sz;
    }

    protected void serialize(LittleEndianOutput out) {
        out.writeShort(this.firstRow);
        out.writeShort(this.lastRow);
        out.writeByte(this.firstCol);
        out.writeByte(this.lastCol);
        out.writeShort(this.charCount);
        out.writeByte(this.charType);
        out.write(this.path);
        if (this.path[0] == (byte) 2) {
            out.write(this._unused);
        }
    }

    public short getSid() {
        return (short) 81;
    }

    public int getFirstColumn() {
        return this.firstCol;
    }

    public int getFirstRow() {
        return this.firstRow;
    }

    public int getLastColumn() {
        return this.lastCol;
    }

    public int getLastRow() {
        return this.lastRow;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[DCONREF]\n");
        b.append("    .ref\n");
        b.append("        .firstrow   = ").append(this.firstRow).append("\n");
        b.append("        .lastrow    = ").append(this.lastRow).append("\n");
        b.append("        .firstcol   = ").append(this.firstCol).append("\n");
        b.append("        .lastcol    = ").append(this.lastCol).append("\n");
        b.append("    .cch            = ").append(this.charCount).append("\n");
        b.append("    .stFile\n");
        b.append("        .h          = ").append(this.charType).append("\n");
        b.append("        .rgb        = ").append(getReadablePath()).append("\n");
        b.append("[/DCONREF]\n");
        return b.toString();
    }

    public byte[] getPath() {
        return Arrays.copyOf(this.path, this.path.length);
    }

    public String getReadablePath() {
        if (this.path == null) {
            return null;
        }
        int offset = 1;
        while (this.path[offset] < (byte) 32 && offset < this.path.length) {
            offset++;
        }
        return new String(Arrays.copyOfRange(this.path, offset, this.path.length), StringUtil.UTF8).replaceAll("\u0003", "/");
    }

    public boolean isExternalRef() {
        if (this.path[0] == (byte) 1) {
            return true;
        }
        return false;
    }
}
