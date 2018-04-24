package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class AxisRecord extends StandardRecord implements Cloneable {
    public static final short AXIS_TYPE_CATEGORY_OR_X_AXIS = (short) 0;
    public static final short AXIS_TYPE_SERIES_AXIS = (short) 2;
    public static final short AXIS_TYPE_VALUE_AXIS = (short) 1;
    public static final short sid = (short) 4125;
    private short field_1_axisType;
    private int field_2_reserved1;
    private int field_3_reserved2;
    private int field_4_reserved3;
    private int field_5_reserved4;

    public AxisRecord(RecordInputStream in) {
        this.field_1_axisType = in.readShort();
        this.field_2_reserved1 = in.readInt();
        this.field_3_reserved2 = in.readInt();
        this.field_4_reserved3 = in.readInt();
        this.field_5_reserved4 = in.readInt();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[AXIS]\n");
        buffer.append("    .axisType             = ").append("0x").append(HexDump.toHex(getAxisType())).append(" (").append(getAxisType()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .reserved1            = ").append("0x").append(HexDump.toHex(getReserved1())).append(" (").append(getReserved1()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .reserved2            = ").append("0x").append(HexDump.toHex(getReserved2())).append(" (").append(getReserved2()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .reserved3            = ").append("0x").append(HexDump.toHex(getReserved3())).append(" (").append(getReserved3()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .reserved4            = ").append("0x").append(HexDump.toHex(getReserved4())).append(" (").append(getReserved4()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/AXIS]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_axisType);
        out.writeInt(this.field_2_reserved1);
        out.writeInt(this.field_3_reserved2);
        out.writeInt(this.field_4_reserved3);
        out.writeInt(this.field_5_reserved4);
    }

    protected int getDataSize() {
        return 18;
    }

    public short getSid() {
        return sid;
    }

    public AxisRecord clone() {
        AxisRecord rec = new AxisRecord();
        rec.field_1_axisType = this.field_1_axisType;
        rec.field_2_reserved1 = this.field_2_reserved1;
        rec.field_3_reserved2 = this.field_3_reserved2;
        rec.field_4_reserved3 = this.field_4_reserved3;
        rec.field_5_reserved4 = this.field_5_reserved4;
        return rec;
    }

    public short getAxisType() {
        return this.field_1_axisType;
    }

    public void setAxisType(short field_1_axisType) {
        this.field_1_axisType = field_1_axisType;
    }

    public int getReserved1() {
        return this.field_2_reserved1;
    }

    public void setReserved1(int field_2_reserved1) {
        this.field_2_reserved1 = field_2_reserved1;
    }

    public int getReserved2() {
        return this.field_3_reserved2;
    }

    public void setReserved2(int field_3_reserved2) {
        this.field_3_reserved2 = field_3_reserved2;
    }

    public int getReserved3() {
        return this.field_4_reserved3;
    }

    public void setReserved3(int field_4_reserved3) {
        this.field_4_reserved3 = field_4_reserved3;
    }

    public int getReserved4() {
        return this.field_5_reserved4;
    }

    public void setReserved4(int field_5_reserved4) {
        this.field_5_reserved4 = field_5_reserved4;
    }
}
