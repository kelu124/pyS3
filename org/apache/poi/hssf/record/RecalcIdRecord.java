package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.bytedeco.javacpp.opencv_videoio;

public final class RecalcIdRecord extends StandardRecord {
    public static final short sid = (short) 449;
    private int _engineId;
    private final int _reserved0;

    public RecalcIdRecord() {
        this._reserved0 = 0;
        this._engineId = 0;
    }

    public RecalcIdRecord(RecordInputStream in) {
        in.readUShort();
        this._reserved0 = in.readUShort();
        this._engineId = in.readInt();
    }

    public boolean isNeeded() {
        return true;
    }

    public void setEngineId(int val) {
        this._engineId = val;
    }

    public int getEngineId() {
        return this._engineId;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[RECALCID]\n");
        buffer.append("    .reserved = ").append(HexDump.shortToHex(this._reserved0)).append("\n");
        buffer.append("    .engineId = ").append(HexDump.intToHex(this._engineId)).append("\n");
        buffer.append("[/RECALCID]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(opencv_videoio.CV_CAP_PROP_XI_WB_KG);
        out.writeShort(this._reserved0);
        out.writeInt(this._engineId);
    }

    protected int getDataSize() {
        return 8;
    }

    public short getSid() {
        return (short) 449;
    }
}
