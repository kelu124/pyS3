package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class SeriesLabelsRecord extends StandardRecord {
    private static final BitField labelAsPercentage = BitFieldFactory.getInstance(4);
    private static final BitField showActual = BitFieldFactory.getInstance(1);
    private static final BitField showBubbleSizes = BitFieldFactory.getInstance(32);
    private static final BitField showLabel = BitFieldFactory.getInstance(16);
    private static final BitField showPercent = BitFieldFactory.getInstance(2);
    public static final short sid = (short) 4108;
    private static final BitField smoothedLine = BitFieldFactory.getInstance(8);
    private short field_1_formatFlags;

    public SeriesLabelsRecord(RecordInputStream in) {
        this.field_1_formatFlags = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ATTACHEDLABEL]\n");
        buffer.append("    .formatFlags          = ").append("0x").append(HexDump.toHex(getFormatFlags())).append(" (").append(getFormatFlags()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("         .showActual               = ").append(isShowActual()).append('\n');
        buffer.append("         .showPercent              = ").append(isShowPercent()).append('\n');
        buffer.append("         .labelAsPercentage        = ").append(isLabelAsPercentage()).append('\n');
        buffer.append("         .smoothedLine             = ").append(isSmoothedLine()).append('\n');
        buffer.append("         .showLabel                = ").append(isShowLabel()).append('\n');
        buffer.append("         .showBubbleSizes          = ").append(isShowBubbleSizes()).append('\n');
        buffer.append("[/ATTACHEDLABEL]\n");
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

    public Object clone() {
        SeriesLabelsRecord rec = new SeriesLabelsRecord();
        rec.field_1_formatFlags = this.field_1_formatFlags;
        return rec;
    }

    public short getFormatFlags() {
        return this.field_1_formatFlags;
    }

    public void setFormatFlags(short field_1_formatFlags) {
        this.field_1_formatFlags = field_1_formatFlags;
    }

    public void setShowActual(boolean value) {
        this.field_1_formatFlags = showActual.setShortBoolean(this.field_1_formatFlags, value);
    }

    public boolean isShowActual() {
        return showActual.isSet(this.field_1_formatFlags);
    }

    public void setShowPercent(boolean value) {
        this.field_1_formatFlags = showPercent.setShortBoolean(this.field_1_formatFlags, value);
    }

    public boolean isShowPercent() {
        return showPercent.isSet(this.field_1_formatFlags);
    }

    public void setLabelAsPercentage(boolean value) {
        this.field_1_formatFlags = labelAsPercentage.setShortBoolean(this.field_1_formatFlags, value);
    }

    public boolean isLabelAsPercentage() {
        return labelAsPercentage.isSet(this.field_1_formatFlags);
    }

    public void setSmoothedLine(boolean value) {
        this.field_1_formatFlags = smoothedLine.setShortBoolean(this.field_1_formatFlags, value);
    }

    public boolean isSmoothedLine() {
        return smoothedLine.isSet(this.field_1_formatFlags);
    }

    public void setShowLabel(boolean value) {
        this.field_1_formatFlags = showLabel.setShortBoolean(this.field_1_formatFlags, value);
    }

    public boolean isShowLabel() {
        return showLabel.isSet(this.field_1_formatFlags);
    }

    public void setShowBubbleSizes(boolean value) {
        this.field_1_formatFlags = showBubbleSizes.setShortBoolean(this.field_1_formatFlags, value);
    }

    public boolean isShowBubbleSizes() {
        return showBubbleSizes.isSet(this.field_1_formatFlags);
    }
}
