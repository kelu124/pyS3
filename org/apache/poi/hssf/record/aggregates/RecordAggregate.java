package org.apache.poi.hssf.record.aggregates;

import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordBase;

public abstract class RecordAggregate extends RecordBase {

    public interface RecordVisitor {
        void visitRecord(Record record);
    }

    public static final class PositionTrackingVisitor implements RecordVisitor {
        private int _position;
        private final RecordVisitor _rv;

        public PositionTrackingVisitor(RecordVisitor rv, int initialPosition) {
            this._rv = rv;
            this._position = initialPosition;
        }

        public void visitRecord(Record r) {
            this._position += r.getRecordSize();
            this._rv.visitRecord(r);
        }

        public void setPosition(int position) {
            this._position = position;
        }

        public int getPosition() {
            return this._position;
        }
    }

    private static final class RecordSizingVisitor implements RecordVisitor {
        private int _totalSize = 0;

        public int getTotalSize() {
            return this._totalSize;
        }

        public void visitRecord(Record r) {
            this._totalSize += r.getRecordSize();
        }
    }

    private static final class SerializingRecordVisitor implements RecordVisitor {
        private int _countBytesWritten = 0;
        private final byte[] _data;
        private final int _startOffset;

        public SerializingRecordVisitor(byte[] data, int startOffset) {
            this._data = data;
            this._startOffset = startOffset;
        }

        public int countBytesWritten() {
            return this._countBytesWritten;
        }

        public void visitRecord(Record r) {
            this._countBytesWritten += r.serialize(this._startOffset + this._countBytesWritten, this._data);
        }
    }

    public abstract void visitContainedRecords(RecordVisitor recordVisitor);

    public final int serialize(int offset, byte[] data) {
        SerializingRecordVisitor srv = new SerializingRecordVisitor(data, offset);
        visitContainedRecords(srv);
        return srv.countBytesWritten();
    }

    public int getRecordSize() {
        RecordSizingVisitor rsv = new RecordSizingVisitor();
        visitContainedRecords(rsv);
        return rsv.getTotalSize();
    }
}
