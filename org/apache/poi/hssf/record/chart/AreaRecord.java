package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class AreaRecord extends StandardRecord implements Cloneable {
    private static final BitField displayAsPercentage = BitFieldFactory.getInstance(2);
    private static final BitField shadow = BitFieldFactory.getInstance(4);
    public static final short sid = (short) 4122;
    private static final BitField stacked = BitFieldFactory.getInstance(1);
    private short field_1_formatFlags;

    public AreaRecord(RecordInputStream in) {
        this.field_1_formatFlags = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[AREA]\n");
        buffer.append("    .formatFlags          = ").append("0x").append(HexDump.toHex(getFormatFlags())).append(" (").append(getFormatFlags()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("         .stacked                  = ").append(isStacked()).append('\n');
        buffer.append("         .displayAsPercentage      = ").append(isDisplayAsPercentage()).append('\n');
        buffer.append("         .shadow                   = ").append(isShadow()).append('\n');
        buffer.append("[/AREA]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_formatFlags);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public AreaRecord clone() {
        AreaRecord rec = new AreaRecord();
        rec.field_1_formatFlags = this.field_1_formatFlags;
        return rec;
    }

    public short getFormatFlags() {
        return this.field_1_formatFlags;
    }

    public void setFormatFlags(short field_1_formatFlags) {
        this.field_1_formatFlags = field_1_formatFlags;
    }

    public void setStacked(boolean value) {
        this.field_1_formatFlags = stacked.setShortBoolean(this.field_1_formatFlags, value);
    }

    public boolean isStacked() {
        return stacked.isSet(this.field_1_formatFlags);
    }

    public void setDisplayAsPercentage(boolean value) {
        this.field_1_formatFlags = displayAsPercentage.setShortBoolean(this.field_1_formatFlags, value);
    }

    public boolean isDisplayAsPercentage() {
        return displayAsPercentage.isSet(this.field_1_formatFlags);
    }

    public void setShadow(boolean value) {
        this.field_1_formatFlags = shadow.setShortBoolean(this.field_1_formatFlags, value);
    }

    public boolean isShadow() {
        return shadow.isSet(this.field_1_formatFlags);
    }
}
