package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class DrawingSelectionRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 237;
    private int _cpsp;
    private int _dgslk;
    private OfficeArtRecordHeader _header;
    private int[] _shapeIds;
    private int _spidFocus;

    private static final class OfficeArtRecordHeader {
        public static final int ENCODED_SIZE = 8;
        private final int _length;
        private final int _type;
        private final int _verAndInstance;

        public OfficeArtRecordHeader(LittleEndianInput in) {
            this._verAndInstance = in.readUShort();
            this._type = in.readUShort();
            this._length = in.readInt();
        }

        public void serialize(LittleEndianOutput out) {
            out.writeShort(this._verAndInstance);
            out.writeShort(this._type);
            out.writeInt(this._length);
        }

        public String debugFormatAsString() {
            StringBuffer sb = new StringBuffer(32);
            sb.append("ver+inst=").append(HexDump.shortToHex(this._verAndInstance));
            sb.append(" type=").append(HexDump.shortToHex(this._type));
            sb.append(" len=").append(HexDump.intToHex(this._length));
            return sb.toString();
        }
    }

    public DrawingSelectionRecord(RecordInputStream in) {
        this._header = new OfficeArtRecordHeader(in);
        this._cpsp = in.readInt();
        this._dgslk = in.readInt();
        this._spidFocus = in.readInt();
        int nShapes = in.available() / 4;
        int[] shapeIds = new int[nShapes];
        for (int i = 0; i < nShapes; i++) {
            shapeIds[i] = in.readInt();
        }
        this._shapeIds = shapeIds;
    }

    public short getSid() {
        return sid;
    }

    protected int getDataSize() {
        return (this._shapeIds.length * 4) + 20;
    }

    public void serialize(LittleEndianOutput out) {
        this._header.serialize(out);
        out.writeInt(this._cpsp);
        out.writeInt(this._dgslk);
        out.writeInt(this._spidFocus);
        for (int writeInt : this._shapeIds) {
            out.writeInt(writeInt);
        }
    }

    public DrawingSelectionRecord clone() {
        return this;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[MSODRAWINGSELECTION]\n");
        sb.append("    .rh       =(").append(this._header.debugFormatAsString()).append(")\n");
        sb.append("    .cpsp     =").append(HexDump.intToHex(this._cpsp)).append('\n');
        sb.append("    .dgslk    =").append(HexDump.intToHex(this._dgslk)).append('\n');
        sb.append("    .spidFocus=").append(HexDump.intToHex(this._spidFocus)).append('\n');
        sb.append("    .shapeIds =(");
        for (int i = 0; i < this._shapeIds.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(HexDump.intToHex(this._shapeIds[i]));
        }
        sb.append(")\n");
        sb.append("[/MSODRAWINGSELECTION]\n");
        return sb.toString();
    }
}
