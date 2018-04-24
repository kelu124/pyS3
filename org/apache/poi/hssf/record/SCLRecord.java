package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class SCLRecord extends StandardRecord {
    public static final short sid = (short) 160;
    private short field_1_numerator;
    private short field_2_denominator;

    public SCLRecord(RecordInputStream in) {
        this.field_1_numerator = in.readShort();
        this.field_2_denominator = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SCL]\n");
        buffer.append("    .numerator            = ").append("0x").append(HexDump.toHex(getNumerator())).append(" (").append(getNumerator()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .denominator          = ").append("0x").append(HexDump.toHex(getDenominator())).append(" (").append(getDenominator()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/SCL]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_numerator);
        out.writeShort(this.field_2_denominator);
    }

    protected int getDataSize() {
        return 4;
    }

    public short getSid() {
        return (short) 160;
    }

    public Object clone() {
        SCLRecord rec = new SCLRecord();
        rec.field_1_numerator = this.field_1_numerator;
        rec.field_2_denominator = this.field_2_denominator;
        return rec;
    }

    public short getNumerator() {
        return this.field_1_numerator;
    }

    public void setNumerator(short field_1_numerator) {
        this.field_1_numerator = field_1_numerator;
    }

    public short getDenominator() {
        return this.field_2_denominator;
    }

    public void setDenominator(short field_2_denominator) {
        this.field_2_denominator = field_2_denominator;
    }
}
