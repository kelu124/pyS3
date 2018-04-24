package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class CalcModeRecord extends StandardRecord implements Cloneable {
    public static final short AUTOMATIC = (short) 1;
    public static final short AUTOMATIC_EXCEPT_TABLES = (short) -1;
    public static final short MANUAL = (short) 0;
    public static final short sid = (short) 13;
    private short field_1_calcmode;

    public CalcModeRecord(RecordInputStream in) {
        this.field_1_calcmode = in.readShort();
    }

    public void setCalcMode(short calcmode) {
        this.field_1_calcmode = calcmode;
    }

    public short getCalcMode() {
        return this.field_1_calcmode;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CALCMODE]\n");
        buffer.append("    .calcmode       = ").append(Integer.toHexString(getCalcMode())).append("\n");
        buffer.append("[/CALCMODE]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(getCalcMode());
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return (short) 13;
    }

    public CalcModeRecord clone() {
        CalcModeRecord rec = new CalcModeRecord();
        rec.field_1_calcmode = this.field_1_calcmode;
        return rec;
    }
}
