package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class UnitsRecord extends StandardRecord {
    public static final short sid = (short) 4097;
    private short field_1_units;

    public UnitsRecord(RecordInputStream in) {
        this.field_1_units = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[UNITS]\n");
        buffer.append("    .units                = ").append("0x").append(HexDump.toHex(getUnits())).append(" (").append(getUnits()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/UNITS]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_units);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        UnitsRecord rec = new UnitsRecord();
        rec.field_1_units = this.field_1_units;
        return rec;
    }

    public short getUnits() {
        return this.field_1_units;
    }

    public void setUnits(short field_1_units) {
        this.field_1_units = field_1_units;
    }
}
