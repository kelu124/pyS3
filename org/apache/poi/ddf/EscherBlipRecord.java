package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public class EscherBlipRecord extends EscherRecord {
    private static final int HEADER_SIZE = 8;
    public static final String RECORD_DESCRIPTION = "msofbtBlip";
    public static final short RECORD_ID_END = (short) -3817;
    public static final short RECORD_ID_START = (short) -4072;
    protected byte[] field_pictureData;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesAfterHeader = readHeader(data, offset);
        int pos = offset + 8;
        this.field_pictureData = new byte[bytesAfterHeader];
        System.arraycopy(data, pos, this.field_pictureData, 0, bytesAfterHeader);
        return bytesAfterHeader + 8;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        System.arraycopy(this.field_pictureData, 0, data, offset + 4, this.field_pictureData.length);
        listener.afterRecordSerialize((offset + 4) + this.field_pictureData.length, getRecordId(), this.field_pictureData.length + 4, this);
        return this.field_pictureData.length + 4;
    }

    public int getRecordSize() {
        return this.field_pictureData.length + 8;
    }

    public String getRecordName() {
        return "Blip";
    }

    public byte[] getPicturedata() {
        return this.field_pictureData;
    }

    public void setPictureData(byte[] pictureData) {
        if (pictureData == null) {
            throw new IllegalArgumentException("picture data can't be null");
        }
        this.field_pictureData = (byte[]) pictureData.clone();
    }

    public String toString() {
        return getClass().getName() + ":" + '\n' + "  RecordId: 0x" + HexDump.toHex(getRecordId()) + '\n' + "  Version: 0x" + HexDump.toHex(getVersion()) + '\n' + "  Instance: 0x" + HexDump.toHex(getInstance()) + '\n' + "  Extra Data:" + '\n' + HexDump.toHex(this.field_pictureData, 32);
    }

    public String toXml(String tab) {
        String extraData = HexDump.toHex(this.field_pictureData, 32);
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance()))).append(tab).append("\t").append("<ExtraData>").append(extraData).append("</ExtraData>\n");
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }
}
