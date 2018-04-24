package org.apache.poi.hssf.record;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.record.FilePassRecord.Rc4KeyData;
import org.apache.poi.hssf.record.FilePassRecord.XorKeyData;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.record.crypto.Biff8RC4Key;
import org.apache.poi.hssf.record.crypto.Biff8XORKey;
import org.apache.poi.poifs.crypt.Decryptor;

public final class RecordFactoryInputStream {
    private int _bofDepth;
    private DrawingRecord _lastDrawingRecord = new DrawingRecord();
    private Record _lastRecord = null;
    private boolean _lastRecordWasEOFLevelZero;
    private final RecordInputStream _recStream;
    private final boolean _shouldIncludeContinueRecords;
    private Record[] _unreadRecordBuffer;
    private int _unreadRecordIndex = -1;

    private static final class StreamEncryptionInfo {
        private final FilePassRecord _filePassRec;
        private final boolean _hasBOFRecord;
        private final int _initialRecordsSize;
        private final Record _lastRecord;

        public StreamEncryptionInfo(RecordInputStream rs, List<Record> outputRecs) {
            rs.nextRecord();
            int recSize = rs.remaining() + 4;
            Record rec = RecordFactory.createSingleRecord(rs);
            outputRecs.add(rec);
            FilePassRecord fpr = null;
            if (rec instanceof BOFRecord) {
                this._hasBOFRecord = true;
                if (rs.hasNextRecord()) {
                    rs.nextRecord();
                    rec = RecordFactory.createSingleRecord(rs);
                    recSize += rec.getRecordSize();
                    outputRecs.add(rec);
                    if ((rec instanceof WriteProtectRecord) && rs.hasNextRecord()) {
                        rs.nextRecord();
                        rec = RecordFactory.createSingleRecord(rs);
                        recSize += rec.getRecordSize();
                        outputRecs.add(rec);
                    }
                    if (rec instanceof FilePassRecord) {
                        fpr = (FilePassRecord) rec;
                        outputRecs.remove(outputRecs.size() - 1);
                        rec = (Record) outputRecs.get(0);
                    } else if (rec instanceof EOFRecord) {
                        throw new IllegalStateException("Nothing between BOF and EOF");
                    }
                }
            }
            this._hasBOFRecord = false;
            this._initialRecordsSize = recSize;
            this._filePassRec = fpr;
            this._lastRecord = rec;
        }

        public RecordInputStream createDecryptingStream(InputStream original) {
            Biff8EncryptionKey key;
            FilePassRecord fpr = this._filePassRec;
            String userPassword = Biff8EncryptionKey.getCurrentUserPassword();
            if (userPassword == null) {
                userPassword = Decryptor.DEFAULT_PASSWORD;
            }
            if (fpr.getRc4KeyData() != null) {
                Rc4KeyData rc4 = fpr.getRc4KeyData();
                Biff8EncryptionKey rc4key = Biff8RC4Key.create(userPassword, rc4.getSalt());
                key = rc4key;
                if (!rc4key.validate(rc4.getEncryptedVerifier(), rc4.getEncryptedVerifierHash())) {
                    throw new EncryptedDocumentException((Decryptor.DEFAULT_PASSWORD.equals(userPassword) ? "Default" : "Supplied") + " password is invalid for salt/verifier/verifierHash");
                }
            } else if (fpr.getXorKeyData() != null) {
                XorKeyData xor = fpr.getXorKeyData();
                Biff8EncryptionKey xorKey = Biff8XORKey.create(userPassword, xor.getKey());
                key = xorKey;
                if (!xorKey.validate(userPassword, xor.getVerifier())) {
                    throw new EncryptedDocumentException((Decryptor.DEFAULT_PASSWORD.equals(userPassword) ? "Default" : "Supplied") + " password is invalid for key/verifier");
                }
            } else {
                throw new EncryptedDocumentException("Crypto API not yet supported.");
            }
            return new RecordInputStream(original, key, this._initialRecordsSize);
        }

        public boolean hasEncryption() {
            return this._filePassRec != null;
        }

        public Record getLastRecord() {
            return this._lastRecord;
        }

        public boolean hasBOFRecord() {
            return this._hasBOFRecord;
        }
    }

    public RecordFactoryInputStream(InputStream in, boolean shouldIncludeContinueRecords) {
        int i;
        RecordInputStream rs = new RecordInputStream(in);
        List<Record> records = new ArrayList();
        StreamEncryptionInfo sei = new StreamEncryptionInfo(rs, records);
        if (sei.hasEncryption()) {
            rs = sei.createDecryptingStream(in);
        }
        if (!records.isEmpty()) {
            this._unreadRecordBuffer = new Record[records.size()];
            records.toArray(this._unreadRecordBuffer);
            this._unreadRecordIndex = 0;
        }
        this._recStream = rs;
        this._shouldIncludeContinueRecords = shouldIncludeContinueRecords;
        this._lastRecord = sei.getLastRecord();
        if (sei.hasBOFRecord()) {
            i = 1;
        } else {
            i = 0;
        }
        this._bofDepth = i;
        this._lastRecordWasEOFLevelZero = false;
    }

    public Record nextRecord() {
        Record r = getNextUnreadRecord();
        if (r != null) {
            return r;
        }
        while (this._recStream.hasNextRecord()) {
            if (this._lastRecordWasEOFLevelZero && this._recStream.getNextSid() != 2057) {
                return null;
            }
            this._recStream.nextRecord();
            r = readNextRecord();
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    private Record getNextUnreadRecord() {
        if (this._unreadRecordBuffer == null) {
            return null;
        }
        int ix = this._unreadRecordIndex;
        if (ix < this._unreadRecordBuffer.length) {
            Record result = this._unreadRecordBuffer[ix];
            this._unreadRecordIndex = ix + 1;
            return result;
        }
        this._unreadRecordIndex = -1;
        this._unreadRecordBuffer = null;
        return null;
    }

    private Record readNextRecord() {
        Record record = RecordFactory.createSingleRecord(this._recStream);
        this._lastRecordWasEOFLevelZero = false;
        if (record instanceof BOFRecord) {
            this._bofDepth++;
            return record;
        } else if (record instanceof EOFRecord) {
            this._bofDepth--;
            if (this._bofDepth >= 1) {
                return record;
            }
            this._lastRecordWasEOFLevelZero = true;
            return record;
        } else if (record instanceof DBCellRecord) {
            return null;
        } else {
            if (record instanceof RKRecord) {
                return RecordFactory.convertToNumberRecord((RKRecord) record);
            }
            if (record instanceof MulRKRecord) {
                Record[] records = RecordFactory.convertRKRecords((MulRKRecord) record);
                this._unreadRecordBuffer = records;
                this._unreadRecordIndex = 1;
                return records[0];
            } else if (record.getSid() == DrawingGroupRecord.sid && (this._lastRecord instanceof DrawingGroupRecord)) {
                this._lastRecord.join((AbstractEscherHolderRecord) record);
                return null;
            } else if (record.getSid() == (short) 60) {
                Record contRec = (ContinueRecord) record;
                if ((this._lastRecord instanceof ObjRecord) || (this._lastRecord instanceof TextObjectRecord)) {
                    this._lastDrawingRecord.processContinueRecord(contRec.getData());
                    if (this._shouldIncludeContinueRecords) {
                        return record;
                    }
                    return null;
                } else if (this._lastRecord instanceof DrawingGroupRecord) {
                    ((DrawingGroupRecord) this._lastRecord).processContinueRecord(contRec.getData());
                    return null;
                } else if (this._lastRecord instanceof DrawingRecord) {
                    return contRec;
                } else {
                    if ((this._lastRecord instanceof UnknownRecord) || (this._lastRecord instanceof EOFRecord)) {
                        return record;
                    }
                    throw new RecordFormatException("Unhandled Continue Record followining " + this._lastRecord.getClass());
                }
            } else {
                this._lastRecord = record;
                if (!(record instanceof DrawingRecord)) {
                    return record;
                }
                this._lastDrawingRecord = (DrawingRecord) record;
                return record;
            }
        }
    }
}
