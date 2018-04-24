package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class ChartFormatRecord extends StandardRecord {
    public static final short sid = (short) 4116;
    private static final BitField varyDisplayPattern = BitFieldFactory.getInstance(1);
    private int field1_x_position;
    private int field2_y_position;
    private int field3_width;
    private int field4_height;
    private int field5_grbit;
    private int field6_unknown;

    public ChartFormatRecord(RecordInputStream in) {
        this.field1_x_position = in.readInt();
        this.field2_y_position = in.readInt();
        this.field3_width = in.readInt();
        this.field4_height = in.readInt();
        this.field5_grbit = in.readUShort();
        this.field6_unknown = in.readUShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CHARTFORMAT]\n");
        buffer.append("    .xPosition       = ").append(getXPosition()).append("\n");
        buffer.append("    .yPosition       = ").append(getYPosition()).append("\n");
        buffer.append("    .width           = ").append(getWidth()).append("\n");
        buffer.append("    .height          = ").append(getHeight()).append("\n");
        buffer.append("    .grBit           = ").append(HexDump.intToHex(this.field5_grbit)).append("\n");
        buffer.append("[/CHARTFORMAT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(getXPosition());
        out.writeInt(getYPosition());
        out.writeInt(getWidth());
        out.writeInt(getHeight());
        out.writeShort(this.field5_grbit);
        out.writeShort(this.field6_unknown);
    }

    protected int getDataSize() {
        return 20;
    }

    public short getSid() {
        return sid;
    }

    public int getXPosition() {
        return this.field1_x_position;
    }

    public void setXPosition(int xPosition) {
        this.field1_x_position = xPosition;
    }

    public int getYPosition() {
        return this.field2_y_position;
    }

    public void setYPosition(int yPosition) {
        this.field2_y_position = yPosition;
    }

    public int getWidth() {
        return this.field3_width;
    }

    public void setWidth(int width) {
        this.field3_width = width;
    }

    public int getHeight() {
        return this.field4_height;
    }

    public void setHeight(int height) {
        this.field4_height = height;
    }

    public boolean getVaryDisplayPattern() {
        return varyDisplayPattern.isSet(this.field5_grbit);
    }

    public void setVaryDisplayPattern(boolean value) {
        this.field5_grbit = varyDisplayPattern.setBoolean(this.field5_grbit, value);
    }
}
