package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.RecordFormatException;

public final class EscherTextboxRecord extends EscherRecord implements Cloneable {
    private static final byte[] NO_BYTES = new byte[0];
    public static final String RECORD_DESCRIPTION = "msofbtClientTextbox";
    public static final short RECORD_ID = (short) -4083;
    private byte[] thedata = NO_BYTES;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        this.thedata = new byte[bytesRemaining];
        System.arraycopy(data, offset + 8, this.thedata, 0, bytesRemaining);
        return bytesRemaining + 8;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, this.thedata.length);
        System.arraycopy(this.thedata, 0, data, offset + 8, this.thedata.length);
        int pos = (offset + 8) + this.thedata.length;
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        int size = pos - offset;
        if (size == getRecordSize()) {
            return size;
        }
        throw new RecordFormatException(size + " bytes written but getRecordSize() reports " + getRecordSize());
    }

    public byte[] getData() {
        return this.thedata;
    }

    public void setData(byte[] b, int start, int length) {
        this.thedata = new byte[length];
        System.arraycopy(b, start, this.thedata, 0, length);
    }

    public void setData(byte[] b) {
        setData(b, 0, b.length);
    }

    public int getRecordSize() {
        return this.thedata.length + 8;
    }

    public EscherTextboxRecord clone() {
        EscherTextboxRecord etr = new EscherTextboxRecord();
        etr.setOptions(getOptions());
        etr.setRecordId(getRecordId());
        etr.thedata = (byte[]) this.thedata.clone();
        return etr;
    }

    public String getRecordName() {
        return "ClientTextbox";
    }

    public String toString() {
        String nl = System.getProperty("line.separator");
        String theDumpHex = "";
        try {
            if (this.thedata.length != 0) {
                theDumpHex = ("  Extra Data:" + nl) + HexDump.dump(this.thedata, 0, 0);
            }
        } catch (Exception e) {
            theDumpHex = "Error!!";
        }
        return getClass().getName() + ":" + nl + "  isContainer: " + isContainerRecord() + nl + "  version: 0x" + HexDump.toHex(getVersion()) + nl + "  instance: 0x" + HexDump.toHex(getInstance()) + nl + "  recordId: 0x" + HexDump.toHex(getRecordId()) + nl + "  numchildren: " + getChildRecords().size() + nl + theDumpHex;
    }

    public String toXml(String tab) {
        String theDumpHex = "";
        try {
            if (this.thedata.length != 0) {
                theDumpHex = theDumpHex + HexDump.dump(this.thedata, 0, 0);
            }
        } catch (Exception e) {
            theDumpHex = "Error!!";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance()))).append(tab).append("\t").append("<ExtraData>").append(theDumpHex).append("</ExtraData>\n");
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }
}
