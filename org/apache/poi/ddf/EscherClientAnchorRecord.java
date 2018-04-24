package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public class EscherClientAnchorRecord extends EscherRecord {
    public static final String RECORD_DESCRIPTION = "MsofbtClientAnchor";
    public static final short RECORD_ID = (short) -4080;
    private short field_1_flag;
    private short field_2_col1;
    private short field_3_dx1;
    private short field_4_row1;
    private short field_5_dy1;
    private short field_6_col2;
    private short field_7_dx2;
    private short field_8_row2;
    private short field_9_dy2;
    private byte[] remainingData = new byte[0];
    private boolean shortRecord = false;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        int size = 0;
        if (bytesRemaining != 4) {
            this.field_1_flag = LittleEndian.getShort(data, pos + 0);
            size = 0 + 2;
            this.field_2_col1 = LittleEndian.getShort(data, pos + 2);
            size += 2;
            this.field_3_dx1 = LittleEndian.getShort(data, pos + 4);
            size += 2;
            this.field_4_row1 = LittleEndian.getShort(data, pos + 6);
            size += 2;
            if (bytesRemaining >= 18) {
                this.field_5_dy1 = LittleEndian.getShort(data, pos + 8);
                size += 2;
                this.field_6_col2 = LittleEndian.getShort(data, pos + 10);
                size += 2;
                this.field_7_dx2 = LittleEndian.getShort(data, pos + 12);
                size += 2;
                this.field_8_row2 = LittleEndian.getShort(data, pos + 14);
                size += 2;
                this.field_9_dy2 = LittleEndian.getShort(data, pos + 16);
                size += 2;
                this.shortRecord = false;
            } else {
                this.shortRecord = true;
            }
        }
        bytesRemaining -= size;
        this.remainingData = new byte[bytesRemaining];
        System.arraycopy(data, pos + size, this.remainingData, 0, bytesRemaining);
        return (size + 8) + bytesRemaining;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        int i;
        int i2 = 8;
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        if (this.remainingData == null) {
            this.remainingData = new byte[0];
        }
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        int length = this.remainingData.length;
        if (this.shortRecord) {
            i = 8;
        } else {
            i = 18;
        }
        LittleEndian.putInt(data, offset + 4, length + i);
        LittleEndian.putShort(data, offset + 8, this.field_1_flag);
        LittleEndian.putShort(data, offset + 10, this.field_2_col1);
        LittleEndian.putShort(data, offset + 12, this.field_3_dx1);
        LittleEndian.putShort(data, offset + 14, this.field_4_row1);
        if (!this.shortRecord) {
            LittleEndian.putShort(data, offset + 16, this.field_5_dy1);
            LittleEndian.putShort(data, offset + 18, this.field_6_col2);
            LittleEndian.putShort(data, offset + 20, this.field_7_dx2);
            LittleEndian.putShort(data, offset + 22, this.field_8_row2);
            LittleEndian.putShort(data, offset + 24, this.field_9_dy2);
        }
        System.arraycopy(this.remainingData, 0, data, (this.shortRecord ? 16 : 26) + offset, this.remainingData.length);
        i = offset + 8;
        if (!this.shortRecord) {
            i2 = 18;
        }
        int pos = (i + i2) + this.remainingData.length;
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public int getRecordSize() {
        return (this.remainingData == null ? 0 : this.remainingData.length) + ((this.shortRecord ? 8 : 18) + 8);
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "ClientAnchor";
    }

    public String toString() {
        String nl = System.getProperty("line.separator");
        return getClass().getName() + ":" + nl + "  RecordId: 0x" + HexDump.toHex(RECORD_ID) + nl + "  Version: 0x" + HexDump.toHex(getVersion()) + nl + "  Instance: 0x" + HexDump.toHex(getInstance()) + nl + "  Flag: " + this.field_1_flag + nl + "  Col1: " + this.field_2_col1 + nl + "  DX1: " + this.field_3_dx1 + nl + "  Row1: " + this.field_4_row1 + nl + "  DY1: " + this.field_5_dy1 + nl + "  Col2: " + this.field_6_col2 + nl + "  DX2: " + this.field_7_dx2 + nl + "  Row2: " + this.field_8_row2 + nl + "  DY2: " + this.field_9_dy2 + nl + "  Extra Data:" + nl + HexDump.dump(this.remainingData, 0, 0);
    }

    public String toXml(String tab) {
        return tab + formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance())) + tab + "\t" + "<Flag>" + this.field_1_flag + "</Flag>\n" + tab + "\t" + "<Col1>" + this.field_2_col1 + "</Col1>\n" + tab + "\t" + "<DX1>" + this.field_3_dx1 + "</DX1>\n" + tab + "\t" + "<Row1>" + this.field_4_row1 + "</Row1>\n" + tab + "\t" + "<DY1>" + this.field_5_dy1 + "</DY1>\n" + tab + "\t" + "<Col2>" + this.field_6_col2 + "</Col2>\n" + tab + "\t" + "<DX2>" + this.field_7_dx2 + "</DX2>\n" + tab + "\t" + "<Row2>" + this.field_8_row2 + "</Row2>\n" + tab + "\t" + "<DY2>" + this.field_9_dy2 + "</DY2>\n" + tab + "\t" + "<ExtraData>" + HexDump.dump(this.remainingData, 0, 0).trim() + "</ExtraData>\n" + tab + "</" + getClass().getSimpleName() + ">\n";
    }

    public short getFlag() {
        return this.field_1_flag;
    }

    public void setFlag(short field_1_flag) {
        this.field_1_flag = field_1_flag;
    }

    public short getCol1() {
        return this.field_2_col1;
    }

    public void setCol1(short field_2_col1) {
        this.field_2_col1 = field_2_col1;
    }

    public short getDx1() {
        return this.field_3_dx1;
    }

    public void setDx1(short field_3_dx1) {
        this.field_3_dx1 = field_3_dx1;
    }

    public short getRow1() {
        return this.field_4_row1;
    }

    public void setRow1(short field_4_row1) {
        this.field_4_row1 = field_4_row1;
    }

    public short getDy1() {
        return this.field_5_dy1;
    }

    public void setDy1(short field_5_dy1) {
        this.shortRecord = false;
        this.field_5_dy1 = field_5_dy1;
    }

    public short getCol2() {
        return this.field_6_col2;
    }

    public void setCol2(short field_6_col2) {
        this.shortRecord = false;
        this.field_6_col2 = field_6_col2;
    }

    public short getDx2() {
        return this.field_7_dx2;
    }

    public void setDx2(short field_7_dx2) {
        this.shortRecord = false;
        this.field_7_dx2 = field_7_dx2;
    }

    public short getRow2() {
        return this.field_8_row2;
    }

    public void setRow2(short field_8_row2) {
        this.shortRecord = false;
        this.field_8_row2 = field_8_row2;
    }

    public short getDy2() {
        return this.field_9_dy2;
    }

    public void setDy2(short field_9_dy2) {
        this.shortRecord = false;
        this.field_9_dy2 = field_9_dy2;
    }

    public byte[] getRemainingData() {
        return this.remainingData;
    }

    public void setRemainingData(byte[] remainingData) {
        if (remainingData == null) {
            this.remainingData = new byte[0];
        } else {
            this.remainingData = (byte[]) remainingData.clone();
        }
    }
}
