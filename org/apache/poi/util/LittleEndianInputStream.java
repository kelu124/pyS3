package org.apache.poi.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LittleEndianInputStream extends FilterInputStream implements LittleEndianInput {
    public LittleEndianInputStream(InputStream is) {
        super(is);
    }

    public int available() {
        try {
            return super.available();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte readByte() {
        return (byte) readUByte();
    }

    public int readUByte() {
        byte[] buf = new byte[1];
        try {
            checkEOF(read(buf), 1);
            return LittleEndian.getUByte(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public int readInt() {
        byte[] buf = new byte[4];
        try {
            checkEOF(read(buf), buf.length);
            return LittleEndian.getInt(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long readUInt() {
        return 4294967295L & ((long) readInt());
    }

    public long readLong() {
        byte[] buf = new byte[8];
        try {
            checkEOF(read(buf), 8);
            return LittleEndian.getLong(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public short readShort() {
        return (short) readUShort();
    }

    public int readUShort() {
        byte[] buf = new byte[2];
        try {
            checkEOF(read(buf), 2);
            return LittleEndian.getUShort(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkEOF(int actualBytes, int expectedBytes) {
        if (expectedBytes == 0) {
            return;
        }
        if (actualBytes == -1 || actualBytes != expectedBytes) {
            throw new RuntimeException("Unexpected end-of-file");
        }
    }

    public void readFully(byte[] buf) {
        readFully(buf, 0, buf.length);
    }

    public void readFully(byte[] buf, int off, int len) {
        try {
            checkEOF(read(buf, off, len), len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
