package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class FontIndexRecord extends StandardRecord implements Cloneable {
    public static final short sid = (short) 4134;
    private short field_1_fontIndex;

    public FontIndexRecord(RecordInputStream in) {
        this.field_1_fontIndex = in.readShort();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FONTX]\n");
        buffer.append("    .fontIndex            = ").append("0x").append(HexDump.toHex(getFontIndex())).append(" (").append(getFontIndex()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("[/FONTX]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_fontIndex);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public FontIndexRecord clone() {
        FontIndexRecord rec = new FontIndexRecord();
        rec.field_1_fontIndex = this.field_1_fontIndex;
        return rec;
    }

    public short getFontIndex() {
        return this.field_1_fontIndex;
    }

    public void setFontIndex(short field_1_fontIndex) {
        this.field_1_fontIndex = field_1_fontIndex;
    }
}
