package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@GwtIncompatible
@Beta
public final class LittleEndianDataInputStream extends FilterInputStream implements DataInput {
    public LittleEndianDataInputStream(InputStream in) {
        super((InputStream) Preconditions.checkNotNull(in));
    }

    @CanIgnoreReturnValue
    public String readLine() {
        throw new UnsupportedOperationException("readLine is not supported");
    }

    public void readFully(byte[] b) throws IOException {
        ByteStreams.readFully(this, b);
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        ByteStreams.readFully(this, b, off, len);
    }

    public int skipBytes(int n) throws IOException {
        return (int) this.in.skip((long) n);
    }

    @CanIgnoreReturnValue
    public int readUnsignedByte() throws IOException {
        int b1 = this.in.read();
        if (b1 >= 0) {
            return b1;
        }
        throw new EOFException();
    }

    @CanIgnoreReturnValue
    public int readUnsignedShort() throws IOException {
        return Ints.fromBytes((byte) 0, (byte) 0, readAndCheckByte(), readAndCheckByte());
    }

    @CanIgnoreReturnValue
    public int readInt() throws IOException {
        byte b1 = readAndCheckByte();
        byte b2 = readAndCheckByte();
        return Ints.fromBytes(readAndCheckByte(), readAndCheckByte(), b2, b1);
    }

    @CanIgnoreReturnValue
    public long readLong() throws IOException {
        byte b1 = readAndCheckByte();
        byte b2 = readAndCheckByte();
        byte b3 = readAndCheckByte();
        byte b4 = readAndCheckByte();
        byte b5 = readAndCheckByte();
        byte b6 = readAndCheckByte();
        return Longs.fromBytes(readAndCheckByte(), readAndCheckByte(), b6, b5, b4, b3, b2, b1);
    }

    @CanIgnoreReturnValue
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @CanIgnoreReturnValue
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @CanIgnoreReturnValue
    public String readUTF() throws IOException {
        return new DataInputStream(this.in).readUTF();
    }

    @CanIgnoreReturnValue
    public short readShort() throws IOException {
        return (short) readUnsignedShort();
    }

    @CanIgnoreReturnValue
    public char readChar() throws IOException {
        return (char) readUnsignedShort();
    }

    @CanIgnoreReturnValue
    public byte readByte() throws IOException {
        return (byte) readUnsignedByte();
    }

    @CanIgnoreReturnValue
    public boolean readBoolean() throws IOException {
        return readUnsignedByte() != 0;
    }

    private byte readAndCheckByte() throws IOException, EOFException {
        int b1 = this.in.read();
        if (-1 != b1) {
            return (byte) b1;
        }
        throw new EOFException();
    }
}
