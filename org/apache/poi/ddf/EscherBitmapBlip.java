package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public class EscherBitmapBlip extends EscherBlipRecord {
    private static final int HEADER_SIZE = 8;
    public static final short RECORD_ID_DIB = (short) -4065;
    public static final short RECORD_ID_JPEG = (short) -4067;
    public static final short RECORD_ID_PNG = (short) -4066;
    private final byte[] field_1_UID = new byte[16];
    private byte field_2_marker = (byte) -1;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesAfterHeader = readHeader(data, offset);
        int pos = offset + 8;
        System.arraycopy(data, pos, this.field_1_UID, 0, 16);
        pos += 16;
        this.field_2_marker = data[pos];
        pos++;
        this.field_pictureData = new byte[(bytesAfterHeader - 17)];
        System.arraycopy(data, pos, this.field_pictureData, 0, this.field_pictureData.length);
        return bytesAfterHeader + 8;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, getRecordSize() - 8);
        int pos = offset + 8;
        System.arraycopy(this.field_1_UID, 0, data, pos, 16);
        data[pos + 16] = this.field_2_marker;
        System.arraycopy(this.field_pictureData, 0, data, pos + 17, this.field_pictureData.length);
        listener.afterRecordSerialize(getRecordSize() + offset, getRecordId(), getRecordSize(), this);
        return this.field_pictureData.length + 25;
    }

    public int getRecordSize() {
        return this.field_pictureData.length + 25;
    }

    public byte[] getUID() {
        return this.field_1_UID;
    }

    public void setUID(byte[] field_1_UID) {
        if (field_1_UID == null || field_1_UID.length != 16) {
            throw new IllegalArgumentException("field_1_UID must be byte[16]");
        }
        System.arraycopy(field_1_UID, 0, this.field_1_UID, 0, 16);
    }

    public byte getMarker() {
        return this.field_2_marker;
    }

    public void setMarker(byte field_2_marker) {
        this.field_2_marker = field_2_marker;
    }

    public String toString() {
        String nl = System.getProperty("line.separator");
        return getClass().getName() + ":" + nl + "  RecordId: 0x" + HexDump.toHex(getRecordId()) + nl + "  Version: 0x" + HexDump.toHex(getVersion()) + nl + "  Instance: 0x" + HexDump.toHex(getInstance()) + nl + "  UID: 0x" + HexDump.toHex(this.field_1_UID) + nl + "  Marker: 0x" + HexDump.toHex(this.field_2_marker) + nl + "  Extra Data:" + nl + HexDump.dump(this.field_pictureData, 0, 0);
    }

    public String toXml(String tab) {
        String extraData = HexDump.dump(this.field_pictureData, 0, 0);
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance()))).append(tab).append("\t").append("<UID>0x").append(HexDump.toHex(this.field_1_UID)).append("</UID>\n").append(tab).append("\t").append("<Marker>0x").append(HexDump.toHex(this.field_2_marker)).append("</Marker>\n").append(tab).append("\t").append("<ExtraData>").append(extraData).append("</ExtraData>\n");
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }
}
