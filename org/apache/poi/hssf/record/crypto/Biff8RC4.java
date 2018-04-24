package org.apache.poi.hssf.record.crypto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;
import org.apache.poi.EncryptedDocumentException;

final class Biff8RC4 implements Biff8Cipher {
    private static final int RC4_REKEYING_INTERVAL = 1024;
    private ByteBuffer _buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
    private int _currentKeyIndex;
    private final Biff8RC4Key _key;
    private int _nextRC4BlockStart;
    private Cipher _rc4;
    private boolean _shouldSkipEncryptionOnCurrentRecord;
    private int _streamPos;

    public Biff8RC4(int initialOffset, Biff8RC4Key key) {
        if (initialOffset >= 1024) {
            throw new RuntimeException("initialOffset (" + initialOffset + ")>" + 1024 + " not supported yet");
        }
        this._key = key;
        this._rc4 = this._key.getCipher();
        this._streamPos = 0;
        rekeyForNextBlock();
        this._streamPos = initialOffset;
        this._shouldSkipEncryptionOnCurrentRecord = false;
        encryptBytes(new byte[initialOffset], 0, initialOffset);
    }

    private void rekeyForNextBlock() {
        this._currentKeyIndex = this._streamPos / 1024;
        this._key.initCipherForBlock(this._rc4, this._currentKeyIndex);
        this._nextRC4BlockStart = (this._currentKeyIndex + 1) * 1024;
    }

    private void encryptBytes(byte[] data, int offset, int bytesToRead) {
        if (bytesToRead != 0) {
            if (this._shouldSkipEncryptionOnCurrentRecord) {
                byte[] dataCpy = new byte[bytesToRead];
                System.arraycopy(data, offset, dataCpy, 0, bytesToRead);
                data = dataCpy;
                offset = 0;
            }
            try {
                this._rc4.update(data, offset, bytesToRead, data, offset);
            } catch (ShortBufferException e) {
                throw new EncryptedDocumentException("input buffer too small", e);
            }
        }
    }

    public void startRecord(int currentSid) {
        this._shouldSkipEncryptionOnCurrentRecord = isNeverEncryptedRecord(currentSid);
    }

    private static boolean isNeverEncryptedRecord(int sid) {
        switch (sid) {
            case 47:
            case 225:
            case 2057:
                return true;
            default:
                return false;
        }
    }

    public void skipTwoBytes() {
        xor(this._buffer.array(), 0, 2);
    }

    public void xor(byte[] buf, int pOffset, int pLen) {
        int nLeftInBlock = this._nextRC4BlockStart - this._streamPos;
        if (pLen <= nLeftInBlock) {
            encryptBytes(buf, pOffset, pLen);
            this._streamPos += pLen;
            return;
        }
        int offset = pOffset;
        int len = pLen;
        if (len > nLeftInBlock) {
            if (nLeftInBlock > 0) {
                encryptBytes(buf, offset, nLeftInBlock);
                this._streamPos += nLeftInBlock;
                offset += nLeftInBlock;
                len -= nLeftInBlock;
            }
            rekeyForNextBlock();
        }
        while (len > 1024) {
            encryptBytes(buf, offset, 1024);
            this._streamPos += 1024;
            offset += 1024;
            len -= 1024;
            rekeyForNextBlock();
        }
        encryptBytes(buf, offset, len);
        this._streamPos += len;
    }

    public int xorByte(int rawVal) {
        this._buffer.put(0, (byte) rawVal);
        xor(this._buffer.array(), 0, 1);
        return this._buffer.get(0);
    }

    public int xorShort(int rawVal) {
        this._buffer.putShort(0, (short) rawVal);
        xor(this._buffer.array(), 0, 2);
        return this._buffer.getShort(0);
    }

    public int xorInt(int rawVal) {
        this._buffer.putInt(0, rawVal);
        xor(this._buffer.array(), 0, 4);
        return this._buffer.getInt(0);
    }

    public long xorLong(long rawVal) {
        this._buffer.putLong(0, rawVal);
        xor(this._buffer.array(), 0, 8);
        return this._buffer.getLong(0);
    }

    public void setNextRecordSize(int recordSize) {
    }
}
