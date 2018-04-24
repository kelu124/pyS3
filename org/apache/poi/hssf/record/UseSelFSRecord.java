package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class UseSelFSRecord extends StandardRecord {
    public static final short sid = (short) 352;
    private static final BitField useNaturalLanguageFormulasFlag = BitFieldFactory.getInstance(1);
    private int _options;

    private UseSelFSRecord(int options) {
        this._options = options;
    }

    public UseSelFSRecord(RecordInputStream in) {
        this(in.readUShort());
    }

    public UseSelFSRecord(boolean b) {
        this(0);
        this._options = useNaturalLanguageFormulasFlag.setBoolean(this._options, b);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[USESELFS]\n");
        buffer.append("    .options = ").append(HexDump.shortToHex(this._options)).append("\n");
        buffer.append("[/USESELFS]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._options);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        return new UseSelFSRecord(this._options);
    }
}
