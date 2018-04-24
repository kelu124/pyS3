package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class PlotGrowthRecord extends StandardRecord {
    public static final short sid = (short) 4196;
    private int field_1_horizontalScale;
    private int field_2_verticalScale;

    public PlotGrowthRecord(RecordInputStream in) {
        this.field_1_horizontalScale = in.readInt();
        this.field_2_verticalScale = in.readInt();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PLOTGROWTH]\n");
        buffer.append("    .horizontalScale      = ").append("0x").append(HexDump.toHex(getHorizontalScale())).append(" (").append(getHorizontalScale()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .verticalScale        = ").append("0x").append(HexDump.toHex(getVerticalScale())).append(" (").append(getVerticalScale()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/PLOTGROWTH]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(this.field_1_horizontalScale);
        out.writeInt(this.field_2_verticalScale);
    }

    protected int getDataSize() {
        return 8;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        PlotGrowthRecord rec = new PlotGrowthRecord();
        rec.field_1_horizontalScale = this.field_1_horizontalScale;
        rec.field_2_verticalScale = this.field_2_verticalScale;
        return rec;
    }

    public int getHorizontalScale() {
        return this.field_1_horizontalScale;
    }

    public void setHorizontalScale(int field_1_horizontalScale) {
        this.field_1_horizontalScale = field_1_horizontalScale;
    }

    public int getVerticalScale() {
        return this.field_2_verticalScale;
    }

    public void setVerticalScale(int field_2_verticalScale) {
        this.field_2_verticalScale = field_2_verticalScale;
    }
}
