package org.apache.poi.util;

public final class LittleEndianByteArrayInputStream implements LittleEndianInput {
    private final byte[] _buf;
    private final int _endIndex;
    private int _readIndex;

    public LittleEndianByteArrayInputStream(byte[] buf, int startOffset, int maxReadLen) {
        this._buf = buf;
        this._readIndex = startOffset;
        this._endIndex = startOffset + maxReadLen;
    }

    public LittleEndianByteArrayInputStream(byte[] buf, int startOffset) {
        this(buf, startOffset, buf.length - startOffset);
    }

    public LittleEndianByteArrayInputStream(byte[] buf) {
        this(buf, 0, buf.length);
    }

    public int available() {
        return this._endIndex - this._readIndex;
    }

    private void checkPosition(int i) {
        if (i > this._endIndex - this._readIndex) {
            throw new RuntimeException("Buffer overrun");
        }
    }

    public int getReadIndex() {
        return this._readIndex;
    }

    public byte readByte() {
        checkPosition(1);
        byte[] bArr = this._buf;
        int i = this._readIndex;
        this._readIndex = i + 1;
        return bArr[i];
    }

    public int readInt() {
        checkPosition(4);
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

    public long readLong() {
        checkPosition(8);
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

    public short readShort() {
        return (short) readUShort();
    }

    public int readUByte() {
        checkPosition(1);
        byte[] bArr = this._buf;
        int i = this._readIndex;
        this._readIndex = i + 1;
        return bArr[i] & 255;
    }

    public int readUShort() {
        checkPosition(2);
        int i = this._readIndex;
        int i2 = i + 1;
        int b0 = this._buf[i] & 255;
        int b1 = this._buf[i2] & 255;
        this._readIndex = i2 + 1;
        return (b1 << 8) + (b0 << 0);
    }

    public void readFully(byte[] buf, int off, int len) {
        checkPosition(len);
        System.arraycopy(this._buf, this._readIndex, buf, off, len);
        this._readIndex += len;
    }

    public void readFully(byte[] buf) {
        readFully(buf, 0, buf.length);
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }
}
