package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public class EscherClientDataRecord extends EscherRecord {
    public static final String RECORD_DESCRIPTION = "MsofbtClientData";
    public static final short RECORD_ID = (short) -4079;
    private byte[] remainingData;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        this.remainingData = new byte[bytesRemaining];
        System.arraycopy(data, pos, this.remainingData, 0, bytesRemaining);
        return bytesRemaining + 8;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        if (this.remainingData == null) {
            this.remainingData = new byte[0];
        }
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, this.remainingData.length);
        System.arraycopy(this.remainingData, 0, data, offset + 8, this.remainingData.length);
        int pos = (offset + 8) + this.remainingData.length;
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public int getRecordSize() {
        return (this.remainingData == null ? 0 : this.remainingData.length) + 8;
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "ClientData";
    }

    public String toString() {
        String nl = System.getProperty("line.separator");
        return getClass().getName() + ":" + nl + "  RecordId: 0x" + HexDump.toHex(RECORD_ID) + nl + "  Version: 0x" + HexDump.toHex(getVersion()) + nl + "  Instance: 0x" + HexDump.toHex(getInstance()) + nl + "  Extra Data:" + nl + HexDump.dump(getRemainingData(), 0, 0);
    }

    public String toXml(String tab) {
        String extraData = HexDump.dump(getRemainingData(), 0, 0).trim();
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance()))).append(tab).append("\t").append("<ExtraData>").append(extraData).append("</ExtraData>\n");
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }

    public byte[] getRemainingData() {
        return this.remainingData;
    }

    public void setRemainingData(byte[] remainingData) {
        this.remainingData = remainingData == null ? new byte[0] : (byte[]) remainingData.clone();
    }
}
