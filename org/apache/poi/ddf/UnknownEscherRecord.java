package org.apache.poi.ddf;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public final class UnknownEscherRecord extends EscherRecord implements Cloneable {
    private static final byte[] NO_BYTES = new byte[0];
    private List<EscherRecord> _childRecords = new ArrayList();
    private byte[] thedata = NO_BYTES;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int avaliable = data.length - (offset + 8);
        if (bytesRemaining > avaliable) {
            bytesRemaining = avaliable;
        }
        if (isContainerRecord()) {
            this.thedata = new byte[0];
            offset += 8;
            int bytesWritten = 0 + 8;
            while (bytesRemaining > 0) {
                EscherRecord child = recordFactory.createRecord(data, offset);
                int childBytesWritten = child.fillFields(data, offset, recordFactory);
                bytesWritten += childBytesWritten;
                offset += childBytesWritten;
                bytesRemaining -= childBytesWritten;
                getChildRecords().add(child);
            }
            return bytesWritten;
        }
        this.thedata = new byte[bytesRemaining];
        System.arraycopy(data, offset + 8, this.thedata, 0, bytesRemaining);
        return bytesRemaining + 8;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        int remainingBytes = this.thedata.length;
        for (EscherRecord r : this._childRecords) {
            remainingBytes += r.getRecordSize();
        }
        LittleEndian.putInt(data, offset + 4, remainingBytes);
        System.arraycopy(this.thedata, 0, data, offset + 8, this.thedata.length);
        int pos = (offset + 8) + this.thedata.length;
        for (EscherRecord r2 : this._childRecords) {
            pos += r2.serialize(pos, data, listener);
        }
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public byte[] getData() {
        return this.thedata;
    }

    public int getRecordSize() {
        return this.thedata.length + 8;
    }

    public List<EscherRecord> getChildRecords() {
        return this._childRecords;
    }

    public void setChildRecords(List<EscherRecord> childRecords) {
        this._childRecords = childRecords;
    }

    public UnknownEscherRecord clone() {
        UnknownEscherRecord uer = new UnknownEscherRecord();
        uer.thedata = (byte[]) this.thedata.clone();
        uer.setOptions(getOptions());
        uer.setRecordId(getRecordId());
        return uer;
    }

    public String getRecordName() {
        return "Unknown 0x" + HexDump.toHex(getRecordId());
    }

    public String toString() {
        StringBuffer children = new StringBuffer();
        if (getChildRecords().size() > 0) {
            children.append("  children: \n");
            for (EscherRecord record : this._childRecords) {
                children.append(record.toString());
                children.append('\n');
            }
        }
        return getClass().getName() + ":" + '\n' + "  isContainer: " + isContainerRecord() + '\n' + "  version: 0x" + HexDump.toHex(getVersion()) + '\n' + "  instance: 0x" + HexDump.toHex(getInstance()) + '\n' + "  recordId: 0x" + HexDump.toHex(getRecordId()) + '\n' + "  numchildren: " + getChildRecords().size() + '\n' + HexDump.toHex(this.thedata, 32) + children.toString();
    }

    public String toXml(String tab) {
        String theDumpHex = HexDump.toHex(this.thedata, 32);
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance()))).append(tab).append("\t").append("<IsContainer>").append(isContainerRecord()).append("</IsContainer>\n").append(tab).append("\t").append("<Numchildren>").append(HexDump.toHex(this._childRecords.size())).append("</Numchildren>\n");
        for (EscherRecord record : this._childRecords) {
            builder.append(record.toXml(tab + "\t"));
        }
        builder.append(theDumpHex).append("\n");
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }

    public void addChildRecord(EscherRecord childRecord) {
        getChildRecords().add(childRecord);
    }
}
