package org.apache.poi.hssf.record;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.LittleEndianOutput;

public class ExternSheetRecord extends StandardRecord {
    public static final short sid = (short) 23;
    private final List<RefSubRecord> _list = new ArrayList();

    private static final class RefSubRecord {
        public static final int ENCODED_SIZE = 6;
        private final int _extBookIndex;
        private int _firstSheetIndex;
        private int _lastSheetIndex;

        public void adjustIndex(int offset) {
            this._firstSheetIndex += offset;
            this._lastSheetIndex += offset;
        }

        public RefSubRecord(int extBookIndex, int firstSheetIndex, int lastSheetIndex) {
            this._extBookIndex = extBookIndex;
            this._firstSheetIndex = firstSheetIndex;
            this._lastSheetIndex = lastSheetIndex;
        }

        public RefSubRecord(RecordInputStream in) {
            this(in.readShort(), in.readShort(), in.readShort());
        }

        public int getExtBookIndex() {
            return this._extBookIndex;
        }

        public int getFirstSheetIndex() {
            return this._firstSheetIndex;
        }

        public int getLastSheetIndex() {
            return this._lastSheetIndex;
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("extBook=").append(this._extBookIndex);
            buffer.append(" firstSheet=").append(this._firstSheetIndex);
            buffer.append(" lastSheet=").append(this._lastSheetIndex);
            return buffer.toString();
        }

        public void serialize(LittleEndianOutput out) {
            out.writeShort(this._extBookIndex);
            out.writeShort(this._firstSheetIndex);
            out.writeShort(this._lastSheetIndex);
        }
    }

    public ExternSheetRecord(RecordInputStream in) {
        int nItems = in.readShort();
        for (int i = 0; i < nItems; i++) {
            this._list.add(new RefSubRecord(in));
        }
    }

    public int getNumOfRefs() {
        return this._list.size();
    }

    public void addREFRecord(RefSubRecord rec) {
        this._list.add(rec);
    }

    public int getNumOfREFRecords() {
        return this._list.size();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        int nItems = this._list.size();
        sb.append("[EXTERNSHEET]\n");
        sb.append("   numOfRefs     = ").append(nItems).append("\n");
        for (int i = 0; i < nItems; i++) {
            sb.append("refrec         #").append(i).append(": ");
            sb.append(getRef(i).toString());
            sb.append('\n');
        }
        sb.append("[/EXTERNSHEET]\n");
        return sb.toString();
    }

    protected int getDataSize() {
        return (this._list.size() * 6) + 2;
    }

    public void serialize(LittleEndianOutput out) {
        int nItems = this._list.size();
        out.writeShort(nItems);
        for (int i = 0; i < nItems; i++) {
            getRef(i).serialize(out);
        }
    }

    private RefSubRecord getRef(int i) {
        return (RefSubRecord) this._list.get(i);
    }

    public void removeSheet(int sheetIdx) {
        int nItems = this._list.size();
        for (int i = 0; i < nItems; i++) {
            RefSubRecord refSubRecord = (RefSubRecord) this._list.get(i);
            if (refSubRecord.getFirstSheetIndex() == sheetIdx && refSubRecord.getLastSheetIndex() == sheetIdx) {
                this._list.set(i, new RefSubRecord(refSubRecord.getExtBookIndex(), -1, -1));
            } else if (refSubRecord.getFirstSheetIndex() > sheetIdx && refSubRecord.getLastSheetIndex() > sheetIdx) {
                this._list.set(i, new RefSubRecord(refSubRecord.getExtBookIndex(), refSubRecord.getFirstSheetIndex() - 1, refSubRecord.getLastSheetIndex() - 1));
            }
        }
    }

    public short getSid() {
        return (short) 23;
    }

    public int getExtbookIndexFromRefIndex(int refIndex) {
        return getRef(refIndex).getExtBookIndex();
    }

    public int findRefIndexFromExtBookIndex(int extBookIndex) {
        int nItems = this._list.size();
        for (int i = 0; i < nItems; i++) {
            if (getRef(i).getExtBookIndex() == extBookIndex) {
                return i;
            }
        }
        return -1;
    }

    public int getFirstSheetIndexFromRefIndex(int extRefIndex) {
        return getRef(extRefIndex).getFirstSheetIndex();
    }

    public int getLastSheetIndexFromRefIndex(int extRefIndex) {
        return getRef(extRefIndex).getLastSheetIndex();
    }

    public int addRef(int extBookIndex, int firstSheetIndex, int lastSheetIndex) {
        this._list.add(new RefSubRecord(extBookIndex, firstSheetIndex, lastSheetIndex));
        return this._list.size() - 1;
    }

    public int getRefIxForSheet(int externalBookIndex, int firstSheetIndex, int lastSheetIndex) {
        int nItems = this._list.size();
        for (int i = 0; i < nItems; i++) {
            RefSubRecord ref = getRef(i);
            if (ref.getExtBookIndex() == externalBookIndex && ref.getFirstSheetIndex() == firstSheetIndex && ref.getLastSheetIndex() == lastSheetIndex) {
                return i;
            }
        }
        return -1;
    }

    public static ExternSheetRecord combine(ExternSheetRecord[] esrs) {
        ExternSheetRecord result = new ExternSheetRecord();
        for (ExternSheetRecord esr : esrs) {
            int nRefs = esr.getNumOfREFRecords();
            for (int j = 0; j < nRefs; j++) {
                result.addREFRecord(esr.getRef(j));
            }
        }
        return result;
    }
}
