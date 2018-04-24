package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.RecordFormatException;

public class EscherSplitMenuColorsRecord extends EscherRecord {
    public static final String RECORD_DESCRIPTION = "MsofbtSplitMenuColors";
    public static final short RECORD_ID = (short) -3810;
    private int field_1_color1;
    private int field_2_color2;
    private int field_3_color3;
    private int field_4_color4;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        this.field_1_color1 = LittleEndian.getInt(data, pos + 0);
        int size = 0 + 4;
        this.field_2_color2 = LittleEndian.getInt(data, pos + 4);
        size += 4;
        this.field_3_color3 = LittleEndian.getInt(data, pos + 8);
        size += 4;
        this.field_4_color4 = LittleEndian.getInt(data, pos + 12);
        size += 4;
        bytesRemaining -= 16;
        if (bytesRemaining == 0) {
            return bytesRemaining + 24;
        }
        throw new RecordFormatException("Expecting no remaining data but got " + bytesRemaining + " byte(s).");
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        int pos = offset;
        LittleEndian.putShort(data, pos, getOptions());
        pos += 2;
        LittleEndian.putShort(data, pos, getRecordId());
        pos += 2;
        LittleEndian.putInt(data, pos, getRecordSize() - 8);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_1_color1);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_2_color2);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_3_color3);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_4_color4);
        pos += 4;
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return getRecordSize();
    }

    public int getRecordSize() {
        return 24;
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "SplitMenuColors";
    }

    public String toString() {
        return getClass().getName() + ":" + '\n' + "  RecordId: 0x" + HexDump.toHex(RECORD_ID) + '\n' + "  Version: 0x" + HexDump.toHex(getVersion()) + '\n' + "  Instance: 0x" + HexDump.toHex(getInstance()) + '\n' + "  Color1: 0x" + HexDump.toHex(this.field_1_color1) + '\n' + "  Color2: 0x" + HexDump.toHex(this.field_2_color2) + '\n' + "  Color3: 0x" + HexDump.toHex(this.field_3_color3) + '\n' + "  Color4: 0x" + HexDump.toHex(this.field_4_color4) + '\n' + "";
    }

    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance()))).append(tab).append("\t").append("<Color1>0x").append(HexDump.toHex(this.field_1_color1)).append("</Color1>\n").append(tab).append("\t").append("<Color2>0x").append(HexDump.toHex(this.field_2_color2)).append("</Color2>\n").append(tab).append("\t").append("<Color3>0x").append(HexDump.toHex(this.field_3_color3)).append("</Color3>\n").append(tab).append("\t").append("<Color4>0x").append(HexDump.toHex(this.field_4_color4)).append("</Color4>\n");
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }

    public int getColor1() {
        return this.field_1_color1;
    }

    public void setColor1(int field_1_color1) {
        this.field_1_color1 = field_1_color1;
    }

    public int getColor2() {
        return this.field_2_color2;
    }

    public void setColor2(int field_2_color2) {
        this.field_2_color2 = field_2_color2;
    }

    public int getColor3() {
        return this.field_3_color3;
    }

    public void setColor3(int field_3_color3) {
        this.field_3_color3 = field_3_color3;
    }

    public int getColor4() {
        return this.field_4_color4;
    }

    public void setColor4(int field_4_color4) {
        this.field_4_color4 = field_4_color4;
    }
}
