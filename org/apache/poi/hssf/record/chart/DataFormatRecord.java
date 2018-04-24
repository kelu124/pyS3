package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class DataFormatRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 4102;
    private static final BitField useExcel4Colors = BitFieldFactory.getInstance(1);
    private short field_1_pointNumber;
    private short field_2_seriesIndex;
    private short field_3_seriesNumber;
    private short field_4_formatFlags;

    public DataFormatRecord(RecordInputStream in) {
        this.field_1_pointNumber = in.readShort();
        this.field_2_seriesIndex = in.readShort();
        this.field_3_seriesNumber = in.readShort();
        this.field_4_formatFlags = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DATAFORMAT]\n");
        buffer.append("    .pointNumber          = ").append("0x").append(HexDump.toHex(getPointNumber())).append(" (").append(getPointNumber()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .seriesIndex          = ").append("0x").append(HexDump.toHex(getSeriesIndex())).append(" (").append(getSeriesIndex()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .seriesNumber         = ").append("0x").append(HexDump.toHex(getSeriesNumber())).append(" (").append(getSeriesNumber()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("    .formatFlags          = ").append("0x").append(HexDump.toHex(getFormatFlags())).append(" (").append(getFormatFlags()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("         .useExcel4Colors          = ").append(isUseExcel4Colors()).append('\n');
        buffer.append("[/DATAFORMAT]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_pointNumber);
        out.writeShort(this.field_2_seriesIndex);
        out.writeShort(this.field_3_seriesNumber);
        out.writeShort(this.field_4_formatFlags);
    }

    protected int getDataSize() {
        return 8;
    }

    public short getSid() {
        return sid;
    }

    public DataFormatRecord clone() {
        DataFormatRecord rec = new DataFormatRecord();
        rec.field_1_pointNumber = this.field_1_pointNumber;
        rec.field_2_seriesIndex = this.field_2_seriesIndex;
        rec.field_3_seriesNumber = this.field_3_seriesNumber;
        rec.field_4_formatFlags = this.field_4_formatFlags;
        return rec;
    }

    public short getPointNumber() {
        return this.field_1_pointNumber;
    }

    public void setPointNumber(short field_1_pointNumber) {
        this.field_1_pointNumber = field_1_pointNumber;
    }

    public short getSeriesIndex() {
        return this.field_2_seriesIndex;
    }

    public void setSeriesIndex(short field_2_seriesIndex) {
        this.field_2_seriesIndex = field_2_seriesIndex;
    }

    public short getSeriesNumber() {
        return this.field_3_seriesNumber;
    }

    public void setSeriesNumber(short field_3_seriesNumber) {
        this.field_3_seriesNumber = field_3_seriesNumber;
    }

    public short getFormatFlags() {
        return this.field_4_formatFlags;
    }

    public void setFormatFlags(short field_4_formatFlags) {
        this.field_4_formatFlags = field_4_formatFlags;
    }

    public void setUseExcel4Colors(boolean value) {
        this.field_4_formatFlags = useExcel4Colors.setShortBoolean(this.field_4_formatFlags, value);
    }

    public boolean isUseExcel4Colors() {
        return useExcel4Colors.isSet(this.field_4_formatFlags);
    }
}
