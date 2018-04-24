package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public class EscherChildAnchorRecord extends EscherRecord {
    public static final String RECORD_DESCRIPTION = "MsofbtChildAnchor";
    public static final short RECORD_ID = (short) -4081;
    private int field_1_dx1;
    private int field_2_dy1;
    private int field_3_dx2;
    private int field_4_dy2;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int size;
        int pos = offset + 8;
        switch (readHeader(data, offset)) {
            case 8:
                this.field_1_dx1 = LittleEndian.getShort(data, pos + 0);
                size = 0 + 2;
                this.field_2_dy1 = LittleEndian.getShort(data, pos + 2);
                size += 2;
                this.field_3_dx2 = LittleEndian.getShort(data, pos + 4);
                size += 2;
                this.field_4_dy2 = LittleEndian.getShort(data, pos + 6);
                size += 2;
                break;
            case 16:
                this.field_1_dx1 = LittleEndian.getInt(data, pos + 0);
                size = 0 + 4;
                this.field_2_dy1 = LittleEndian.getInt(data, pos + 4);
                size += 4;
                this.field_3_dx2 = LittleEndian.getInt(data, pos + 8);
                size += 4;
                this.field_4_dy2 = LittleEndian.getInt(data, pos + 12);
                size += 4;
                break;
            default:
                throw new RuntimeException("Invalid EscherChildAnchorRecord - neither 8 nor 16 bytes.");
        }
        return size + 8;
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
        LittleEndian.putInt(data, pos, this.field_1_dx1);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_2_dy1);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_3_dx2);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_4_dy2);
        pos += 4;
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public int getRecordSize() {
        return 24;
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "ChildAnchor";
    }

    public String toString() {
        String nl = System.getProperty("line.separator");
        return getClass().getName() + ":" + nl + "  RecordId: 0x" + HexDump.toHex(RECORD_ID) + nl + "  Version: 0x" + HexDump.toHex(getVersion()) + nl + "  Instance: 0x" + HexDump.toHex(getInstance()) + nl + "  X1: " + this.field_1_dx1 + nl + "  Y1: " + this.field_2_dy1 + nl + "  X2: " + this.field_3_dx2 + nl + "  Y2: " + this.field_4_dy2 + nl;
    }

    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance()))).append(tab).append("\t").append("<X1>").append(this.field_1_dx1).append("</X1>\n").append(tab).append("\t").append("<Y1>").append(this.field_2_dy1).append("</Y1>\n").append(tab).append("\t").append("<X2>").append(this.field_3_dx2).append("</X2>\n").append(tab).append("\t").append("<Y2>").append(this.field_4_dy2).append("</Y2>\n");
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }

    public int getDx1() {
        return this.field_1_dx1;
    }

    public void setDx1(int field_1_dx1) {
        this.field_1_dx1 = field_1_dx1;
    }

    public int getDy1() {
        return this.field_2_dy1;
    }

    public void setDy1(int field_2_dy1) {
        this.field_2_dy1 = field_2_dy1;
    }

    public int getDx2() {
        return this.field_3_dx2;
    }

    public void setDx2(int field_3_dx2) {
        this.field_3_dx2 = field_3_dx2;
    }

    public int getDy2() {
        return this.field_4_dy2;
    }

    public void setDy2(int field_4_dy2) {
        this.field_4_dy2 = field_4_dy2;
    }
}
