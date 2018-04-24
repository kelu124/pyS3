package org.apache.poi.hssf.record;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.poi.hssf.record.crypto.Biff8DecryptingStream;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianInputStream;

public final class RecordInputStream implements LittleEndianInput {
    static final /* synthetic */ boolean $assertionsDisabled = (!RecordInputStream.class.desiredAssertionStatus());
    private static final int DATA_LEN_NEEDS_TO_BE_READ = -1;
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final int INVALID_SID_VALUE = -1;
    public static final short MAX_RECORD_DATA_SIZE = (short) 8224;
    private final BiffHeaderInput _bhi;
    private int _currentDataLength;
    private int _currentDataOffset;
    private int _currentSid;
    private final LittleEndianInput _dataInput;
    private int _nextSid;

    public RecordInputStream(InputStream in) throws RecordFormatException {
        this(in, null, 0);
    }

    public RecordInputStream(InputStream in, Biff8EncryptionKey key, int initialOffset) throws RecordFormatException {
        if (key == null) {
            this._dataInput = getLEI(in);
            this._bhi = new SimpleHeaderInput(in);
        } else {
            Biff8DecryptingStream bds = new Biff8DecryptingStream(in, initialOffset, key);
            this._bhi = bds;
            this._dataInput = bds;
        }
        this._nextSid = readNextSid();
    }

    static LittleEndianInput getLEI(InputStream is) {
        if (is instanceof LittleEndianInput) {
            return (LittleEndianInput) is;
        }
        return new LittleEndianInputStream(is);
    }

    public int available() {
        return remaining();
    }

    public int read(byte[] b, int off, int len) {
        int limit = Math.min(len, remaining());
        if (limit == 0) {
            return 0;
        }
        readFully(b, off, limit);
        return limit;
    }

    public short getSid() {
        return (short) this._currentSid;
    }

    public boolean hasNextRecord() throws LeftoverDataException {
        if (this._currentDataLength == -1 || this._currentDataLength == this._currentDataOffset) {
            if (this._currentDataLength != -1) {
                this._nextSid = readNextSid();
            }
            return this._nextSid != -1;
        } else {
            throw new LeftoverDataException(this._currentSid, remaining());
        }
    }

    private int readNextSid() {
        int nAvailable = this._bhi.available();
        if (nAvailable < 4) {
            if (nAvailable > 0) {
            }
            return -1;
        }
        int result = this._bhi.readRecordSID();
        if (result == -1) {
            throw new RecordFormatException("Found invalid sid (" + result + ")");
        }
        this._currentDataLength = -1;
        return result;
    }

    public void nextRecord() throws RecordFormatException {
        if (this._nextSid == -1) {
            throw new IllegalStateException("EOF - next record not available");
        } else if (this._currentDataLength != -1) {
            throw new IllegalStateException("Cannot call nextRecord() without checking hasNextRecord() first");
        } else {
            this._currentSid = this._nextSid;
            this._currentDataOffset = 0;
            this._currentDataLength = this._bhi.readDataSize();
            if (this._currentDataLength > 8224) {
                throw new RecordFormatException("The content of an excel record cannot exceed 8224 bytes");
            }
        }
    }

    private void checkRecordPosition(int requiredByteCount) {
        int nAvailable = remaining();
        if (nAvailable < requiredByteCount) {
            if (nAvailable == 0 && isContinueNext()) {
                nextRecord();
                return;
            }
            throw new RecordFormatException("Not enough data (" + nAvailable + ") to read requested (" + requiredByteCount + ") bytes");
        }
    }

    public byte readByte() {
        checkRecordPosition(1);
        this._currentDataOffset++;
        return this._dataInput.readByte();
    }

    public short readShort() {
        checkRecordPosition(2);
        this._currentDataOffset += 2;
        return this._dataInput.readShort();
    }

    public int readInt() {
        checkRecordPosition(4);
        this._currentDataOffset += 4;
        return this._dataInput.readInt();
    }

    public long readLong() {
        checkRecordPosition(8);
        this._currentDataOffset += 8;
        return this._dataInput.readLong();
    }

    public int readUByte() {
        return readByte() & 255;
    }

    public int readUShort() {
        checkRecordPosition(2);
        this._currentDataOffset += 2;
        return this._dataInput.readUShort();
    }

    public double readDouble() {
        double result = Double.longBitsToDouble(readLong());
        return Double.isNaN(result) ? result : result;
    }

    public void readFully(byte[] buf) {
        readFully(buf, 0, buf.length);
    }

    public void readFully(byte[] buf, int off, int len) {
        int origLen = len;
        if (buf == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > buf.length - off) {
            throw new IndexOutOfBoundsException();
        } else {
            while (len > 0) {
                int nextChunk = Math.min(available(), len);
                if (nextChunk == 0) {
                    if (hasNextRecord()) {
                        nextRecord();
                        nextChunk = Math.min(available(), len);
                        if (!$assertionsDisabled && nextChunk <= 0) {
                            throw new AssertionError();
                        }
                    }
                    throw new RecordFormatException("Can't read the remaining " + len + " bytes of the requested " + origLen + " bytes. No further record exists.");
                }
                checkRecordPosition(nextChunk);
                this._dataInput.readFully(buf, off, nextChunk);
                this._currentDataOffset += nextChunk;
                off += nextChunk;
                len -= nextChunk;
            }
        }
    }

    public String readString() {
        return readStringCommon(readUShort(), readByte() == (byte) 0);
    }

    public String readUnicodeLEString(int requestedLength) {
        return readStringCommon(requestedLength, false);
    }

    public String readCompressedUnicode(int requestedLength) {
        return readStringCommon(requestedLength, true);
    }

    private String readStringCommon(int requestedLength, boolean pIsCompressedEncoding) {
        if (requestedLength < 0 || requestedLength > 1048576) {
            throw new IllegalArgumentException("Bad requested string length (" + requestedLength + ")");
        }
        char[] buf = new char[requestedLength];
        boolean isCompressedEncoding = pIsCompressedEncoding;
        int curLen = 0;
        while (true) {
            int availableChars = isCompressedEncoding ? remaining() : remaining() / 2;
            if (requestedLength - curLen <= availableChars) {
                break;
            }
            while (availableChars > 0) {
                char ch;
                if (isCompressedEncoding) {
                    ch = (char) readUByte();
                } else {
                    ch = (char) readShort();
                }
                buf[curLen] = ch;
                curLen++;
                availableChars--;
            }
            if (!isContinueNext()) {
                throw new RecordFormatException("Expected to find a ContinueRecord in order to read remaining " + (requestedLength - curLen) + " of " + requestedLength + " chars");
            } else if (remaining() != 0) {
                throw new RecordFormatException("Odd number of bytes(" + remaining() + ") left behind");
            } else {
                nextRecord();
                byte compressFlag = readByte();
                if ($assertionsDisabled || compressFlag == (byte) 0 || compressFlag == (byte) 1) {
                    isCompressedEncoding = compressFlag == (byte) 0;
                } else {
                    throw new AssertionError();
                }
            }
        }
        while (curLen < requestedLength) {
            if (isCompressedEncoding) {
                ch = (char) readUByte();
            } else {
                ch = (char) readShort();
            }
            buf[curLen] = ch;
            curLen++;
        }
        return new String(buf);
    }

    public byte[] readRemainder() {
        int size = remaining();
        if (size == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] result = new byte[size];
        readFully(result);
        return result;
    }

    @Deprecated
    public byte[] readAllContinuedRemainder() {
        ByteArrayOutputStream out = new ByteArrayOutputStream(16448);
        while (true) {
            byte[] b = readRemainder();
            out.write(b, 0, b.length);
            if (!isContinueNext()) {
                return out.toByteArray();
            }
            nextRecord();
        }
    }

    public int remaining() {
        if (this._currentDataLength == -1) {
            return 0;
        }
        return this._currentDataLength - this._currentDataOffset;
    }

    private boolean isContinueNext() {
        if (this._currentDataLength != -1 && this._currentDataOffset != this._currentDataLength) {
            throw new IllegalStateException("Should never be called before end of current record");
        } else if (hasNextRecord() && this._nextSid == 60) {
            return true;
        } else {
            return false;
        }
    }

    public int getNextSid() {
        return this._nextSid;
    }
}
