package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class AxisLineFormatRecord extends StandardRecord implements Cloneable {
    public static final short AXIS_TYPE_AXIS_LINE = (short) 0;
    public static final short AXIS_TYPE_MAJOR_GRID_LINE = (short) 1;
    public static final short AXIS_TYPE_MINOR_GRID_LINE = (short) 2;
    public static final short AXIS_TYPE_WALLS_OR_FLOOR = (short) 3;
    public static final short sid = (short) 4129;
    private short field_1_axisType;

    public AxisLineFormatRecord(RecordInputStream in) {
        this.field_1_axisType = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[AXISLINEFORMAT]\n");
        buffer.append("    .axisType             = ").append("0x").append(HexDump.toHex(getAxisType())).append(" (").append(getAxisType()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/AXISLINEFORMAT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_axisType);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public AxisLineFormatRecord clone() {
        AxisLineFormatRecord rec = new AxisLineFormatRecord();
        rec.field_1_axisType = this.field_1_axisType;
        return rec;
    }

    public short getAxisType() {
        return this.field_1_axisType;
    }

    public void setAxisType(short field_1_axisType) {
        this.field_1_axisType = field_1_axisType;
    }
}
