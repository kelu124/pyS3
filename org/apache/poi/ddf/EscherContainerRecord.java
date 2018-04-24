package org.apache.poi.ddf;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public final class EscherContainerRecord extends EscherRecord {
    public static final short BSTORE_CONTAINER = (short) -4095;
    public static final short DGG_CONTAINER = (short) -4096;
    public static final short DG_CONTAINER = (short) -4094;
    public static final short SOLVER_CONTAINER = (short) -4091;
    public static final short SPGR_CONTAINER = (short) -4093;
    public static final short SP_CONTAINER = (short) -4092;
    private static final POILogger log = POILogFactory.getLogger(EscherContainerRecord.class);
    private final List<EscherRecord> _childRecords = new ArrayList();
    private int _remainingLength;

    public int fillFields(byte[] data, int pOffset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, pOffset);
        int bytesWritten = 8;
        int offset = pOffset + 8;
        while (bytesRemaining > 0 && offset < data.length) {
            EscherRecord child = recordFactory.createRecord(data, offset);
            int childBytesWritten = child.fillFields(data, offset, recordFactory);
            bytesWritten += childBytesWritten;
            offset += childBytesWritten;
            bytesRemaining -= childBytesWritten;
            addChildRecord(child);
            if (offset >= data.length && bytesRemaining > 0) {
                this._remainingLength = bytesRemaining;
                if (log.check(5)) {
                    log.log(5, new Object[]{"Not enough Escher data: " + bytesRemaining + " bytes remaining but no space left"});
                }
            }
        }
        return bytesWritten;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        int remainingBytes = 0;
        for (EscherRecord r : this._childRecords) {
            remainingBytes += r.getRecordSize();
        }
        LittleEndian.putInt(data, offset + 4, remainingBytes + this._remainingLength);
        int pos = offset + 8;
        for (EscherRecord r2 : this._childRecords) {
            pos += r2.serialize(pos, data, listener);
        }
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public int getRecordSize() {
        int childRecordsSize = 0;
        for (EscherRecord r : this._childRecords) {
            childRecordsSize += r.getRecordSize();
        }
        return childRecordsSize + 8;
    }

    public boolean hasChildOfType(short recordId) {
        for (EscherRecord r : this._childRecords) {
            if (r.getRecordId() == recordId) {
                return true;
            }
        }
        return false;
    }

    public EscherRecord getChild(int index) {
        return (EscherRecord) this._childRecords.get(index);
    }

    public List<EscherRecord> getChildRecords() {
        return new ArrayList(this._childRecords);
    }

    public Iterator<EscherRecord> getChildIterator() {
        return Collections.unmodifiableList(this._childRecords).iterator();
    }

    public void setChildRecords(List<EscherRecord> childRecords) {
        if (childRecords == this._childRecords) {
            throw new IllegalStateException("Child records private data member has escaped");
        }
        this._childRecords.clear();
        this._childRecords.addAll(childRecords);
    }

    public boolean removeChildRecord(EscherRecord toBeRemoved) {
        return this._childRecords.remove(toBeRemoved);
    }

    public List<EscherContainerRecord> getChildContainers() {
        List<EscherContainerRecord> containers = new ArrayList();
        for (EscherRecord r : this._childRecords) {
            if (r instanceof EscherContainerRecord) {
                containers.add((EscherContainerRecord) r);
            }
        }
        return containers;
    }

    public String getRecordName() {
        switch (getRecordId()) {
            case (short) -4096:
                return "DggContainer";
            case (short) -4095:
                return "BStoreContainer";
            case (short) -4094:
                return "DgContainer";
            case (short) -4093:
                return "SpgrContainer";
            case (short) -4092:
                return "SpContainer";
            case (short) -4091:
                return "SolverContainer";
            default:
                return "Container 0x" + HexDump.toHex(getRecordId());
        }
    }

    public void display(PrintWriter w, int indent) {
        super.display(w, indent);
        for (EscherRecord escherRecord : this._childRecords) {
            escherRecord.display(w, indent + 1);
        }
    }

    public void addChildRecord(EscherRecord record) {
        this._childRecords.add(record);
    }

    public void addChildBefore(EscherRecord record, int insertBeforeRecordId) {
        int idx = 0;
        Iterator i$ = this._childRecords.iterator();
        while (i$.hasNext() && ((EscherRecord) i$.next()).getRecordId() != ((short) insertBeforeRecordId)) {
            idx++;
        }
        this._childRecords.add(idx, record);
    }

    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuffer children = new StringBuffer();
        if (this._childRecords.size() > 0) {
            children.append("  children: " + nl);
            int count = 0;
            for (EscherRecord record : this._childRecords) {
                children.append("   Child " + count + ":" + nl);
                String childResult = String.valueOf(record).replaceAll("\n", "\n    ");
                children.append("    ");
                children.append(childResult);
                children.append(nl);
                count++;
            }
        }
        return getClass().getName() + " (" + getRecordName() + "):" + nl + "  isContainer: " + isContainerRecord() + nl + "  version: 0x" + HexDump.toHex(getVersion()) + nl + "  instance: 0x" + HexDump.toHex(getInstance()) + nl + "  recordId: 0x" + HexDump.toHex(getRecordId()) + nl + "  numchildren: " + this._childRecords.size() + nl + children.toString();
    }

    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append(formatXmlRecordHeader(getRecordName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance())));
        for (EscherRecord record : this._childRecords) {
            builder.append(record.toXml(tab + "\t"));
        }
        builder.append(tab).append("</").append(getRecordName()).append(">\n");
        return builder.toString();
    }

    public <T extends EscherRecord> T getChildById(short recordId) {
        for (T childRecord : this._childRecords) {
            if (childRecord.getRecordId() == recordId) {
                return childRecord;
            }
        }
        return null;
    }

    public void getRecordsById(short recordId, List<EscherRecord> out) {
        for (EscherRecord r : this._childRecords) {
            if (r instanceof EscherContainerRecord) {
                ((EscherContainerRecord) r).getRecordsById(recordId, out);
            } else if (r.getRecordId() == recordId) {
                out.add(r);
            }
        }
    }
}
