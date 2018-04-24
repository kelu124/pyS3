package org.apache.poi.poifs.storage;

public final class DataInputBlock {
    private final byte[] _buf;
    private int _maxIndex = this._buf.length;
    private int _readIndex;

    DataInputBlock(byte[] data, int startOffset) {
        this._buf = data;
        this._readIndex = startOffset;
    }

    public int available() {
        return this._maxIndex - this._readIndex;
    }

    public int readUByte() {
        byte[] bArr = this._buf;
        int i = this._readIndex;
        this._readIndex = i + 1;
        return bArr[i] & 255;
    }

    public int readUShortLE() {
        int i = this._readIndex;
        int i2 = i + 1;
        int b0 = this._buf[i] & 255;
        int b1 = this._buf[i2] & 255;
        this._readIndex = i2 + 1;
        return (b1 << 8) + (b0 << 0);
    }

    public int readUShortLE(DataInputBlock prevBlock) {
        int b0 = prevBlock._buf[prevBlock._buf.length - 1] & 255;
        byte[] bArr = this._buf;
        int i = this._readIndex;
        this._readIndex = i + 1;
        return ((bArr[i] & 255) << 8) + (b0 << 0);
    }

    public int readIntLE() {
        int i = this._readIndex;
        int i2 = i + 1;
        int b0 = this._buf[i] & 255;
        i = i2 + 1;
        int b1 = this._buf[i2] & 255;
        i2 = i + 1;
        int b2 = this._buf[i] & 255;
        int b3 = this._buf[i2] & 255;
        this._readIndex = i2 + 1;
        return (((b3 << 24) + (b2 << 16)) + (b1 << 8)) + (b0 << 0);
    }

    public int readIntLE(DataInputBlock prevBlock, int prevBlockAvailable) {
        byte[] buf = new byte[4];
        readSpanning(prevBlock, prevBlockAvailable, buf);
        return ((((buf[3] & 255) << 24) + ((buf[2] & 255) << 16)) + ((buf[1] & 255) << 8)) + ((buf[0] & 255) << 0);
    }

    public long readLongLE() {
        int i = this._readIndex;
        int i2 = i + 1;
        int b0 = this._buf[i] & 255;
        i = i2 + 1;
        int b1 = this._buf[i2] & 255;
        i2 = i + 1;
        int b2 = this._buf[i] & 255;
        i = i2 + 1;
        int b3 = this._buf[i2] & 255;
        i2 = i + 1;
        int b4 = this._buf[i] & 255;
        i = i2 + 1;
        int b5 = this._buf[i2] & 255;
        i2 = i + 1;
        int b6 = this._buf[i] & 255;
        int b7 = this._buf[i2] & 255;
        this._readIndex = i2 + 1;
        return (((((((((long) b7) << 56) + (((long) b6) << 48)) + (((long) b5) << 40)) + (((long) b4) << 32)) + (((long) b3) << 24)) + ((long) (b2 << 16))) + ((long) (b1 << 8))) + ((long) (b0 << 0));
    }

    public long readLongLE(DataInputBlock prevBlock, int prevBlockAvailable) {
        byte[] buf = new byte[8];
        readSpanning(prevBlock, prevBlockAvailable, buf);
        return (((((((((long) (buf[7] & 255)) << 56) + (((long) (buf[6] & 255)) << 48)) + (((long) (buf[5] & 255)) << 40)) + (((long) (buf[4] & 255)) << 32)) + (((long) (buf[3] & 255)) << 24)) + ((long) ((buf[2] & 255) << 16))) + ((long) ((buf[1] & 255) << 8))) + ((long) ((buf[0] & 255) << 0));
    }

    private void readSpanning(DataInputBlock prevBlock, int prevBlockAvailable, byte[] buf) {
        System.arraycopy(prevBlock._buf, prevBlock._readIndex, buf, 0, prevBlockAvailable);
        int secondReadLen = buf.length - prevBlockAvailable;
        System.arraycopy(this._buf, 0, buf, prevBlockAvailable, secondReadLen);
        this._readIndex = secondReadLen;
    }

    public void readFully(byte[] buf, int off, int len) {
        System.arraycopy(this._buf, this._readIndex, buf, off, len);
        this._readIndex += len;
    }
}
