package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class IterationRecord extends StandardRecord implements Cloneable {
    private static final BitField iterationOn = BitFieldFactory.getInstance(1);
    public static final short sid = (short) 17;
    private int _flags;

    public IterationRecord(boolean iterateOn) {
        this._flags = iterationOn.setBoolean(0, iterateOn);
    }

    public IterationRecord(RecordInputStream in) {
        this._flags = in.readShort();
    }

    public void setIteration(boolean iterate) {
        this._flags = iterationOn.setBoolean(this._flags, iterate);
    }

    public boolean getIteration() {
        return iterationOn.isSet(this._flags);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ITERATION]\n");
        buffer.append("    .flags      = ").append(HexDump.shortToHex(this._flags)).append("\n");
        buffer.append("[/ITERATION]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._flags);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 17;
    }

    public IterationRecord clone() {
        return new IterationRecord(getIteration());
    }
}
