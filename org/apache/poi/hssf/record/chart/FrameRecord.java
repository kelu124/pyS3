package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class FrameRecord extends StandardRecord implements Cloneable {
    public static final short BORDER_TYPE_REGULAR = (short) 0;
    public static final short BORDER_TYPE_SHADOW = (short) 1;
    private static final BitField autoPosition = BitFieldFactory.getInstance(2);
    private static final BitField autoSize = BitFieldFactory.getInstance(1);
    public static final short sid = (short) 4146;
    private short field_1_borderType;
    private short field_2_options;

    public FrameRecord(RecordInputStream in) {
        this.field_1_borderType = in.readShort();
        this.field_2_options = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FRAME]\n");
        buffer.append("    .borderType           = ").append("0x").append(HexDump.toHex(getBorderType())).append(" (").append(getBorderType()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .options              = ").append("0x").append(HexDump.toHex(getOptions())).append(" (").append(getOptions()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("         .autoSize                 = ").append(isAutoSize()).append('\n');
        buffer.append("         .autoPosition             = ").append(isAutoPosition()).append('\n');
        buffer.append("[/FRAME]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_borderType);
        out.writeShort(this.field_2_options);
    }

    protected int getDataSize() {
        return 4;
    }

    public short getSid() {
        return sid;
    }

    public FrameRecord clone() {
        FrameRecord rec = new FrameRecord();
        rec.field_1_borderType = this.field_1_borderType;
        rec.field_2_options = this.field_2_options;
        return rec;
    }

    public short getBorderType() {
        return this.field_1_borderType;
    }

    public void setBorderType(short field_1_borderType) {
        this.field_1_borderType = field_1_borderType;
    }

    public short getOptions() {
        return this.field_2_options;
    }

    public void setOptions(short field_2_options) {
        this.field_2_options = field_2_options;
    }

    public void setAutoSize(boolean value) {
        this.field_2_options = autoSize.setShortBoolean(this.field_2_options, value);
    }

    public boolean isAutoSize() {
        return autoSize.isSet(this.field_2_options);
    }

    public void setAutoPosition(boolean value) {
        this.field_2_options = autoPosition.setShortBoolean(this.field_2_options, value);
    }

    public boolean isAutoPosition() {
        return autoPosition.isSet(this.field_2_options);
    }
}
