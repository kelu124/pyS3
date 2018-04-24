package org.apache.poi.hssf.record;

import java.io.ByteArrayOutputStream;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.LittleEndianOutputStream;

public abstract class SubRecord {

    private static final class UnknownSubRecord extends SubRecord {
        private final byte[] _data;
        private final int _sid;

        public UnknownSubRecord(LittleEndianInput in, int sid, int size) {
            this._sid = sid;
            byte[] buf = new byte[size];
            in.readFully(buf);
            this._data = buf;
        }

        protected int getDataSize() {
            return this._data.length;
        }

        public void serialize(LittleEndianOutput out) {
            out.writeShort(this._sid);
            out.writeShort(this._data.length);
            out.write(this._data);
        }

        public Object clone() {
            return this;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName()).append(" [");
            sb.append("sid=").append(HexDump.shortToHex(this._sid));
            sb.append(" size=").append(this._data.length);
            sb.append(" : ").append(HexDump.toHex(this._data));
            sb.append("]\n");
            return sb.toString();
        }
    }

    public abstract Object clone();

    protected abstract int getDataSize();

    public abstract void serialize(LittleEndianOutput littleEndianOutput);

    protected SubRecord() {
    }

    public static SubRecord createSubRecord(LittleEndianInput in, int cmoOt) {
        int sid = in.readUShort();
        int secondUShort = in.readUShort();
        switch (sid) {
            case 0:
                return new EndSubRecord(in, secondUShort);
            case 6:
                return new GroupMarkerSubRecord(in, secondUShort);
            case 7:
                return new FtCfSubRecord(in, secondUShort);
            case 8:
                return new FtPioGrbitSubRecord(in, secondUShort);
            case 9:
                return new EmbeddedObjectRefSubRecord(in, secondUShort);
            case 12:
                return new FtCblsSubRecord(in, secondUShort);
            case 13:
                return new NoteStructureSubRecord(in, secondUShort);
            case 19:
                return new LbsDataSubRecord(in, secondUShort, cmoOt);
            case 21:
                return new CommonObjectDataSubRecord(in, secondUShort);
            default:
                return new UnknownSubRecord(in, sid, secondUShort);
        }
    }

    public byte[] serialize() {
        int size = getDataSize() + 4;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        serialize(new LittleEndianOutputStream(baos));
        if (baos.size() == size) {
            return baos.toByteArray();
        }
        throw new RuntimeException("write size mismatch");
    }

    public boolean isTerminating() {
        return false;
    }
}
