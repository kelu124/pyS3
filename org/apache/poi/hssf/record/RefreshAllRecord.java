package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class RefreshAllRecord extends StandardRecord {
    private static final BitField refreshFlag = BitFieldFactory.getInstance(1);
    public static final short sid = (short) 439;
    private int _options;

    private RefreshAllRecord(int options) {
        this._options = options;
    }

    public RefreshAllRecord(RecordInputStream in) {
        this(in.readUShort());
    }

    public RefreshAllRecord(boolean refreshAll) {
        this(0);
        setRefreshAll(refreshAll);
    }

    public void setRefreshAll(boolean refreshAll) {
        this._options = refreshFlag.setBoolean(this._options, refreshAll);
    }

    public boolean getRefreshAll() {
        return refreshFlag.isSet(this._options);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[REFRESHALL]\n");
        buffer.append("    .options      = ").append(HexDump.shortToHex(this._options)).append("\n");
        buffer.append("[/REFRESHALL]\n");
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
        return new RefreshAllRecord(this._options);
    }
}
