package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class TabIdRecord extends StandardRecord {
    private static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final short sid = (short) 317;
    public short[] _tabids;

    public TabIdRecord() {
        this._tabids = EMPTY_SHORT_ARRAY;
    }

    public TabIdRecord(RecordInputStream in) {
        this._tabids = new short[(in.remaining() / 2)];
        for (int i = 0; i < this._tabids.length; i++) {
            this._tabids[i] = in.readShort();
        }
    }

    public void setTabIdArray(short[] array) {
        this._tabids = (short[]) array.clone();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[TABID]\n");
        buffer.append("    .elements        = ").append(this._tabids.length).append("\n");
        for (int i = 0; i < this._tabids.length; i++) {
            buffer.append("    .element_").append(i).append(" = ").append(this._tabids[i]).append("\n");
        }
        buffer.append("[/TABID]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        short[] tabids = this._tabids;
        for (short writeShort : tabids) {
            out.writeShort(writeShort);
        }
    }

    protected int getDataSize() {
        return this._tabids.length * 2;
    }

    public short getSid() {
        return (short) 317;
    }
}
