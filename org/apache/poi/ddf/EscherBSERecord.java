package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public final class EscherBSERecord extends EscherRecord {
    public static final byte BT_DIB = (byte) 7;
    public static final byte BT_EMF = (byte) 2;
    public static final byte BT_ERROR = (byte) 0;
    public static final byte BT_JPEG = (byte) 5;
    public static final byte BT_PICT = (byte) 4;
    public static final byte BT_PNG = (byte) 6;
    public static final byte BT_UNKNOWN = (byte) 1;
    public static final byte BT_WMF = (byte) 3;
    public static final String RECORD_DESCRIPTION = "MsofbtBSE";
    public static final short RECORD_ID = (short) -4089;
    private byte[] _remainingData = new byte[0];
    private byte field_10_unused2;
    private byte field_11_unused3;
    private EscherBlipRecord field_12_blipRecord;
    private byte field_1_blipTypeWin32;
    private byte field_2_blipTypeMacOS;
    private final byte[] field_3_uid = new byte[16];
    private short field_4_tag;
    private int field_5_size;
    private int field_6_ref;
    private int field_7_offset;
    private byte field_8_usage;
    private byte field_9_name;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        this.field_1_blipTypeWin32 = data[pos];
        this.field_2_blipTypeMacOS = data[pos + 1];
        System.arraycopy(data, pos + 2, this.field_3_uid, 0, 16);
        this.field_4_tag = LittleEndian.getShort(data, pos + 18);
        this.field_5_size = LittleEndian.getInt(data, pos + 20);
        this.field_6_ref = LittleEndian.getInt(data, pos + 24);
        this.field_7_offset = LittleEndian.getInt(data, pos + 28);
        this.field_8_usage = data[pos + 32];
        this.field_9_name = data[pos + 33];
        this.field_10_unused2 = data[pos + 34];
        this.field_11_unused3 = data[pos + 35];
        bytesRemaining -= 36;
        int bytesRead = 0;
        if (bytesRemaining > 0) {
            this.field_12_blipRecord = (EscherBlipRecord) recordFactory.createRecord(data, pos + 36);
            bytesRead = this.field_12_blipRecord.fillFields(data, pos + 36, recordFactory);
        }
        pos += bytesRead + 36;
        bytesRemaining -= bytesRead;
        this._remainingData = new byte[bytesRemaining];
        System.arraycopy(data, pos, this._remainingData, 0, bytesRemaining);
        return (this.field_12_blipRecord == null ? 0 : this.field_12_blipRecord.getRecordSize()) + ((bytesRemaining + 8) + 36);
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        if (this._remainingData == null) {
            this._remainingData = new byte[0];
        }
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, (this._remainingData.length + 36) + (this.field_12_blipRecord == null ? 0 : this.field_12_blipRecord.getRecordSize()));
        data[offset + 8] = this.field_1_blipTypeWin32;
        data[offset + 9] = this.field_2_blipTypeMacOS;
        for (int i = 0; i < 16; i++) {
            data[(offset + 10) + i] = this.field_3_uid[i];
        }
        LittleEndian.putShort(data, offset + 26, this.field_4_tag);
        LittleEndian.putInt(data, offset + 28, this.field_5_size);
        LittleEndian.putInt(data, offset + 32, this.field_6_ref);
        LittleEndian.putInt(data, offset + 36, this.field_7_offset);
        data[offset + 40] = this.field_8_usage;
        data[offset + 41] = this.field_9_name;
        data[offset + 42] = this.field_10_unused2;
        data[offset + 43] = this.field_11_unused3;
        int bytesWritten = 0;
        if (this.field_12_blipRecord != null) {
            bytesWritten = this.field_12_blipRecord.serialize(offset + 44, data, new NullEscherSerializationListener());
        }
        System.arraycopy(this._remainingData, 0, data, (offset + 44) + bytesWritten, this._remainingData.length);
        int pos = (((offset + 8) + 36) + this._remainingData.length) + bytesWritten;
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public int getRecordSize() {
        int field_12_size = 0;
        if (this.field_12_blipRecord != null) {
            field_12_size = this.field_12_blipRecord.getRecordSize();
        }
        int remaining_size = 0;
        if (this._remainingData != null) {
            remaining_size = this._remainingData.length;
        }
        return (field_12_size + 44) + remaining_size;
    }

    public String getRecordName() {
        return "BSE";
    }

    public byte getBlipTypeWin32() {
        return this.field_1_blipTypeWin32;
    }

    public void setBlipTypeWin32(byte blipTypeWin32) {
        this.field_1_blipTypeWin32 = blipTypeWin32;
    }

    public byte getBlipTypeMacOS() {
        return this.field_2_blipTypeMacOS;
    }

    public void setBlipTypeMacOS(byte blipTypeMacOS) {
        this.field_2_blipTypeMacOS = blipTypeMacOS;
    }

    public byte[] getUid() {
        return this.field_3_uid;
    }

    public void setUid(byte[] uid) {
        if (uid == null || uid.length != 16) {
            throw new IllegalArgumentException("uid must be byte[16]");
        }
        System.arraycopy(uid, 0, this.field_3_uid, 0, this.field_3_uid.length);
    }

    public short getTag() {
        return this.field_4_tag;
    }

    public void setTag(short tag) {
        this.field_4_tag = tag;
    }

    public int getSize() {
        return this.field_5_size;
    }

    public void setSize(int size) {
        this.field_5_size = size;
    }

    public int getRef() {
        return this.field_6_ref;
    }

    public void setRef(int ref) {
        this.field_6_ref = ref;
    }

    public int getOffset() {
        return this.field_7_offset;
    }

    public void setOffset(int offset) {
        this.field_7_offset = offset;
    }

    public byte getUsage() {
        return this.field_8_usage;
    }

    public void setUsage(byte usage) {
        this.field_8_usage = usage;
    }

    public byte getName() {
        return this.field_9_name;
    }

    public void setName(byte name) {
        this.field_9_name = name;
    }

    public byte getUnused2() {
        return this.field_10_unused2;
    }

    public void setUnused2(byte unused2) {
        this.field_10_unused2 = unused2;
    }

    public byte getUnused3() {
        return this.field_11_unused3;
    }

    public void setUnused3(byte unused3) {
        this.field_11_unused3 = unused3;
    }

    public EscherBlipRecord getBlipRecord() {
        return this.field_12_blipRecord;
    }

    public void setBlipRecord(EscherBlipRecord blipRecord) {
        this.field_12_blipRecord = blipRecord;
    }

    public byte[] getRemainingData() {
        return this._remainingData;
    }

    public void setRemainingData(byte[] remainingData) {
        if (remainingData == null) {
            this._remainingData = new byte[0];
        } else {
            this._remainingData = (byte[]) remainingData.clone();
        }
    }

    public String toString() {
        return getClass().getName() + ":" + '\n' + "  RecordId: 0x" + HexDump.toHex(RECORD_ID) + '\n' + "  Version: 0x" + HexDump.toHex(getVersion()) + '\n' + "  Instance: 0x" + HexDump.toHex(getInstance()) + '\n' + "  BlipTypeWin32: " + this.field_1_blipTypeWin32 + '\n' + "  BlipTypeMacOS: " + this.field_2_blipTypeMacOS + '\n' + "  SUID: " + (this.field_3_uid == null ? "" : HexDump.toHex(this.field_3_uid)) + '\n' + "  Tag: " + this.field_4_tag + '\n' + "  Size: " + this.field_5_size + '\n' + "  Ref: " + this.field_6_ref + '\n' + "  Offset: " + this.field_7_offset + '\n' + "  Usage: " + this.field_8_usage + '\n' + "  Name: " + this.field_9_name + '\n' + "  Unused2: " + this.field_10_unused2 + '\n' + "  Unused3: " + this.field_11_unused3 + '\n' + "  blipRecord: " + this.field_12_blipRecord + '\n' + "  Extra Data:" + '\n' + (this._remainingData == null ? null : HexDump.toHex(this._remainingData, 32));
    }

    public String toXml(String tab) {
        String str;
        StringBuilder builder = new StringBuilder();
        StringBuilder append = builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance()))).append(tab).append("\t").append("<BlipTypeWin32>").append(this.field_1_blipTypeWin32).append("</BlipTypeWin32>\n").append(tab).append("\t").append("<BlipTypeMacOS>").append(this.field_2_blipTypeMacOS).append("</BlipTypeMacOS>\n").append(tab).append("\t").append("<SUID>");
        if (this.field_3_uid == null) {
            str = "";
        } else {
            str = HexDump.toHex(this.field_3_uid);
        }
        append.append(str).append("</SUID>\n").append(tab).append("\t").append("<Tag>").append(this.field_4_tag).append("</Tag>\n").append(tab).append("\t").append("<Size>").append(this.field_5_size).append("</Size>\n").append(tab).append("\t").append("<Ref>").append(this.field_6_ref).append("</Ref>\n").append(tab).append("\t").append("<Offset>").append(this.field_7_offset).append("</Offset>\n").append(tab).append("\t").append("<Usage>").append(this.field_8_usage).append("</Usage>\n").append(tab).append("\t").append("<Name>").append(this.field_9_name).append("</Name>\n").append(tab).append("\t").append("<Unused2>").append(this.field_10_unused2).append("</Unused2>\n").append(tab).append("\t").append("<Unused3>").append(this.field_11_unused3).append("</Unused3>\n");
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }

    public static String getBlipType(byte b) {
        switch (b) {
            case (byte) 0:
                return " ERROR";
            case (byte) 1:
                return " UNKNOWN";
            case (byte) 2:
                return " EMF";
            case (byte) 3:
                return " WMF";
            case (byte) 4:
                return " PICT";
            case (byte) 5:
                return " JPEG";
            case (byte) 6:
                return " PNG";
            case (byte) 7:
                return " DIB";
            default:
                if (b < (byte) 32) {
                    return " NotKnown";
                }
                return " Client";
        }
    }
}
