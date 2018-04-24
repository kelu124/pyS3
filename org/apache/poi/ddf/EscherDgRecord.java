package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public class EscherDgRecord extends EscherRecord {
    public static final String RECORD_DESCRIPTION = "MsofbtDg";
    public static final short RECORD_ID = (short) -4088;
    private int field_1_numShapes;
    private int field_2_lastMSOSPID;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        readHeader(data, offset);
        int pos = offset + 8;
        this.field_1_numShapes = LittleEndian.getInt(data, pos + 0);
        int size = 0 + 4;
        this.field_2_lastMSOSPID = LittleEndian.getInt(data, pos + 4);
        size += 4;
        return getRecordSize();
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, 8);
        LittleEndian.putInt(data, offset + 8, this.field_1_numShapes);
        LittleEndian.putInt(data, offset + 12, this.field_2_lastMSOSPID);
        listener.afterRecordSerialize(offset + 16, getRecordId(), getRecordSize(), this);
        return getRecordSize();
    }

    public int getRecordSize() {
        return 16;
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "Dg";
    }

    public String toString() {
        return getClass().getName() + ":" + '\n' + "  RecordId: 0x" + HexDump.toHex(RECORD_ID) + '\n' + "  Version: 0x" + HexDump.toHex(getVersion()) + '\n' + "  Instance: 0x" + HexDump.toHex(getInstance()) + '\n' + "  NumShapes: " + this.field_1_numShapes + '\n' + "  LastMSOSPID: " + this.field_2_lastMSOSPID + '\n';
    }

    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance()))).append(tab).append("\t").append("<NumShapes>").append(this.field_1_numShapes).append("</NumShapes>\n").append(tab).append("\t").append("<LastMSOSPID>").append(this.field_2_lastMSOSPID).append("</LastMSOSPID>\n");
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }

    public int getNumShapes() {
        return this.field_1_numShapes;
    }

    public void setNumShapes(int field_1_numShapes) {
        this.field_1_numShapes = field_1_numShapes;
    }

    public int getLastMSOSPID() {
        return this.field_2_lastMSOSPID;
    }

    public void setLastMSOSPID(int field_2_lastMSOSPID) {
        this.field_2_lastMSOSPID = field_2_lastMSOSPID;
    }

    public short getDrawingGroupId() {
        return (short) (getOptions() >> 4);
    }

    public void incrementShapeCount() {
        this.field_1_numShapes++;
    }
}
