package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class SheetPropertiesRecord extends StandardRecord {
    public static final byte EMPTY_INTERPOLATED = (byte) 2;
    public static final byte EMPTY_NOT_PLOTTED = (byte) 0;
    public static final byte EMPTY_ZERO = (byte) 1;
    private static final BitField autoPlotArea = BitFieldFactory.getInstance(16);
    private static final BitField chartTypeManuallyFormatted = BitFieldFactory.getInstance(1);
    private static final BitField defaultPlotDimensions = BitFieldFactory.getInstance(8);
    private static final BitField doNotSizeWithWindow = BitFieldFactory.getInstance(4);
    private static final BitField plotVisibleOnly = BitFieldFactory.getInstance(2);
    public static final short sid = (short) 4164;
    private int field_1_flags;
    private int field_2_empty;

    public SheetPropertiesRecord(RecordInputStream in) {
        this.field_1_flags = in.readUShort();
        this.field_2_empty = in.readUShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SHTPROPS]\n");
        buffer.append("    .flags                = ").append(HexDump.shortToHex(this.field_1_flags)).append('\n');
        buffer.append("         .chartTypeManuallyFormatted= ").append(isChartTypeManuallyFormatted()).append('\n');
        buffer.append("         .plotVisibleOnly           = ").append(isPlotVisibleOnly()).append('\n');
        buffer.append("         .doNotSizeWithWindow       = ").append(isDoNotSizeWithWindow()).append('\n');
        buffer.append("         .defaultPlotDimensions     = ").append(isDefaultPlotDimensions()).append('\n');
        buffer.append("         .autoPlotArea              = ").append(isAutoPlotArea()).append('\n');
        buffer.append("    .empty                = ").append(HexDump.shortToHex(this.field_2_empty)).append('\n');
        buffer.append("[/SHTPROPS]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_flags);
        out.writeShort(this.field_2_empty);
    }

    protected int getDataSize() {
        return 4;
    }

    public short getSid() {
        return sid;
    }

    public Object clone() {
        SheetPropertiesRecord rec = new SheetPropertiesRecord();
        rec.field_1_flags = this.field_1_flags;
        rec.field_2_empty = this.field_2_empty;
        return rec;
    }

    public int getFlags() {
        return this.field_1_flags;
    }

    public int getEmpty() {
        return this.field_2_empty;
    }

    public void setEmpty(byte empty) {
        this.field_2_empty = empty;
    }

    public void setChartTypeManuallyFormatted(boolean value) {
        this.field_1_flags = chartTypeManuallyFormatted.setBoolean(this.field_1_flags, value);
    }

    public boolean isChartTypeManuallyFormatted() {
        return chartTypeManuallyFormatted.isSet(this.field_1_flags);
    }

    public void setPlotVisibleOnly(boolean value) {
        this.field_1_flags = plotVisibleOnly.setBoolean(this.field_1_flags, value);
    }

    public boolean isPlotVisibleOnly() {
        return plotVisibleOnly.isSet(this.field_1_flags);
    }

    public void setDoNotSizeWithWindow(boolean value) {
        this.field_1_flags = doNotSizeWithWindow.setBoolean(this.field_1_flags, value);
    }

    public boolean isDoNotSizeWithWindow() {
        return doNotSizeWithWindow.isSet(this.field_1_flags);
    }

    public void setDefaultPlotDimensions(boolean value) {
        this.field_1_flags = defaultPlotDimensions.setBoolean(this.field_1_flags, value);
    }

    public boolean isDefaultPlotDimensions() {
        return defaultPlotDimensions.isSet(this.field_1_flags);
    }

    public void setAutoPlotArea(boolean value) {
        this.field_1_flags = autoPlotArea.setBoolean(this.field_1_flags, value);
    }

    public boolean isAutoPlotArea() {
        return autoPlotArea.isSet(this.field_1_flags);
    }
}
