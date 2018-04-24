package org.apache.poi.hssf.record;

import java.util.ArrayList;
import org.apache.poi.hssf.record.cont.ContinuableRecord;
import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
import org.apache.poi.util.LittleEndianOutput;

public final class ExtSSTRecord extends ContinuableRecord {
    public static final int DEFAULT_BUCKET_SIZE = 8;
    public static final int MAX_BUCKETS = 128;
    public static final short sid = (short) 255;
    private InfoSubRecord[] _sstInfos;
    private short _stringsPerBucket;

    public static final class InfoSubRecord {
        public static final int ENCODED_SIZE = 8;
        private int field_1_stream_pos;
        private int field_2_bucket_sst_offset;
        private short field_3_zero;

        public InfoSubRecord(int streamPos, int bucketSstOffset) {
            this.field_1_stream_pos = streamPos;
            this.field_2_bucket_sst_offset = bucketSstOffset;
        }

        public InfoSubRecord(RecordInputStream in) {
            this.field_1_stream_pos = in.readInt();
            this.field_2_bucket_sst_offset = in.readShort();
            this.field_3_zero = in.readShort();
        }

        public int getStreamPos() {
            return this.field_1_stream_pos;
        }

        public int getBucketSSTOffset() {
            return this.field_2_bucket_sst_offset;
        }

        public void serialize(LittleEndianOutput out) {
            out.writeInt(this.field_1_stream_pos);
            out.writeShort(this.field_2_bucket_sst_offset);
            out.writeShort(this.field_3_zero);
        }
    }

    public ExtSSTRecord() {
        this._stringsPerBucket = (short) 8;
        this._sstInfos = new InfoSubRecord[0];
    }

    public ExtSSTRecord(RecordInputStream in) {
        this._stringsPerBucket = in.readShort();
        ArrayList<InfoSubRecord> lst = new ArrayList(in.remaining() / 8);
        while (in.available() > 0) {
            lst.add(new InfoSubRecord(in));
            if (in.available() == 0 && in.hasNextRecord() && in.getNextSid() == 60) {
                in.nextRecord();
            }
        }
        this._sstInfos = (InfoSubRecord[]) lst.toArray(new InfoSubRecord[lst.size()]);
    }

    public void setNumStringsPerBucket(short numStrings) {
        this._stringsPerBucket = numStrings;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[EXTSST]\n");
        buffer.append("    .dsst           = ").append(Integer.toHexString(this._stringsPerBucket)).append("\n");
        buffer.append("    .numInfoRecords = ").append(this._sstInfos.length).append("\n");
        for (int k = 0; k < this._sstInfos.length; k++) {
            buffer.append("    .inforecord     = ").append(k).append("\n");
            buffer.append("    .streampos      = ").append(Integer.toHexString(this._sstInfos[k].getStreamPos())).append("\n");
            buffer.append("    .sstoffset      = ").append(Integer.toHexString(this._sstInfos[k].getBucketSSTOffset())).append("\n");
        }
        buffer.append("[/EXTSST]\n");
        return buffer.toString();
    }

    public void serialize(ContinuableRecordOutput out) {
        out.writeShort(this._stringsPerBucket);
        for (InfoSubRecord serialize : this._sstInfos) {
            serialize.serialize(out);
        }
    }

    protected int getDataSize() {
        return (this._sstInfos.length * 8) + 2;
    }

    protected InfoSubRecord[] getInfoSubRecords() {
        return this._sstInfos;
    }

    public static final int getNumberOfInfoRecsForStrings(int numStrings) {
        int infoRecs = numStrings / 8;
        if (numStrings % 8 != 0) {
            infoRecs++;
        }
        if (infoRecs > 128) {
            return 128;
        }
        return infoRecs;
    }

    public static final int getRecordSizeForStrings(int numStrings) {
        return (getNumberOfInfoRecsForStrings(numStrings) * 8) + 6;
    }

    public short getSid() {
        return (short) 255;
    }

    public void setBucketOffsets(int[] bucketAbsoluteOffsets, int[] bucketRelativeOffsets) {
        this._sstInfos = new InfoSubRecord[bucketAbsoluteOffsets.length];
        for (int i = 0; i < bucketAbsoluteOffsets.length; i++) {
            this._sstInfos[i] = new InfoSubRecord(bucketAbsoluteOffsets[i], bucketRelativeOffsets[i]);
        }
    }
}
