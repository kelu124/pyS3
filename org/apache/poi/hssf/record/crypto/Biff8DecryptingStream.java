package org.apache.poi.hssf.record.crypto;

import java.io.InputStream;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.record.BiffHeaderInput;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianInputStream;

public final class Biff8DecryptingStream implements BiffHeaderInput, LittleEndianInput {
    private final Biff8Cipher _cipher;
    private final LittleEndianInput _le;

    public Biff8DecryptingStream(InputStream in, int initialOffset, Biff8EncryptionKey key) {
        if (key instanceof Biff8RC4Key) {
            this._cipher = new Biff8RC4(initialOffset, (Biff8RC4Key) key);
        } else if (key instanceof Biff8XORKey) {
            this._cipher = new Biff8XOR(initialOffset, (Biff8XORKey) key);
        } else {
            throw new EncryptedDocumentException("Crypto API not supported yet.");
        }
        if (in instanceof LittleEndianInput) {
            this._le = (LittleEndianInput) in;
        } else {
            this._le = new LittleEndianInputStream(in);
        }
    }

    public int available() {
        return this._le.available();
    }

    public int readRecordSID() {
        int sid = this._le.readUShort();
        this._cipher.skipTwoBytes();
        this._cipher.startRecord(sid);
        return sid;
    }

    public int readDataSize() {
        int dataSize = this._le.readUShort();
        this._cipher.skipTwoBytes();
        this._cipher.setNextRecordSize(dataSize);
        return dataSize;
    }

    public double readDouble() {
        double result = Double.longBitsToDouble(readLong());
        if (!Double.isNaN(result)) {
            return result;
        }
        throw new RuntimeException("Did not expect to read NaN");
    }

    public void readFully(byte[] buf) {
        readFully(buf, 0, buf.length);
    }

    public void readFully(byte[] buf, int off, int len) {
        this._le.readFully(buf, off, len);
        this._cipher.xor(buf, off, len);
    }

    public int readUByte() {
        return readByte() & 255;
    }

    public byte readByte() {
        return (byte) this._cipher.xorByte(this._le.readUByte());
    }

    public int readUShort() {
        return readShort() & 65535;
    }

    public short readShort() {
        return (short) this._cipher.xorShort(this._le.readUShort());
    }

    public int readInt() {
        return this._cipher.xorInt(this._le.readInt());
    }

    public long readLong() {
        return this._cipher.xorLong(this._le.readLong());
    }
}
