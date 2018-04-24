package org.apache.poi.hssf.record.pivottable;

import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class PageItemRecord extends StandardRecord {
    public static final short sid = (short) 182;
    private final FieldInfo[] _fieldInfos;

    private static final class FieldInfo {
        public static final int ENCODED_SIZE = 6;
        private int _idObj;
        private int _isxvd;
        private int _isxvi;

        public FieldInfo(RecordInputStream in) {
            this._isxvi = in.readShort();
            this._isxvd = in.readShort();
            this._idObj = in.readShort();
        }

        protected void serialize(LittleEndianOutput out) {
            out.writeShort(this._isxvi);
            out.writeShort(this._isxvd);
            out.writeShort(this._idObj);
        }

        public void appendDebugInfo(StringBuffer sb) {
            sb.append('(');
            sb.append("isxvi=").append(HexDump.shortToHex(this._isxvi));
            sb.append(" isxvd=").append(HexDump.shortToHex(this._isxvd));
            sb.append(" idObj=").append(HexDump.shortToHex(this._idObj));
            sb.append(')');
        }
    }

    public PageItemRecord(RecordInputStream in) {
        int dataSize = in.remaining();
        if (dataSize % 6 != 0) {
            throw new RecordFormatException("Bad data size " + dataSize);
        }
        FieldInfo[] fis = new FieldInfo[(dataSize / 6)];
        for (int i = 0; i < fis.length; i++) {
            fis[i] = new FieldInfo(in);
        }
        this._fieldInfos = fis;
    }

    protected void serialize(LittleEndianOutput out) {
        for (FieldInfo serialize : this._fieldInfos) {
            serialize.serialize(out);
        }
    }

    protected int getDataSize() {
        return this._fieldInfos.length * 6;
    }

    public short getSid() {
        return (short) 182;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[SXPI]\n");
        for (int i = 0; i < this._fieldInfos.length; i++) {
            sb.append("    item[").append(i).append("]=");
            this._fieldInfos[i].appendDebugInfo(sb);
            sb.append('\n');
        }
        sb.append("[/SXPI]\n");
        return sb.toString();
    }
}
