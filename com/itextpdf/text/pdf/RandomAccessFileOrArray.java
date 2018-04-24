package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.io.IndependentRandomAccessSource;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class RandomAccessFileOrArray implements DataInput {
    private byte back;
    private final RandomAccessSource byteSource;
    private long byteSourcePosition;
    private boolean isBack;

    @Deprecated
    public RandomAccessFileOrArray(String filename) throws IOException {
        this(new RandomAccessSourceFactory().setForceRead(false).setUsePlainRandomAccess(Document.plainRandomAccess).createBestSource(filename));
    }

    @Deprecated
    public RandomAccessFileOrArray(RandomAccessFileOrArray source) {
        this(new IndependentRandomAccessSource(source.byteSource));
    }

    public RandomAccessFileOrArray createView() {
        return new RandomAccessFileOrArray(new IndependentRandomAccessSource(this.byteSource));
    }

    public RandomAccessSource createSourceView() {
        return new IndependentRandomAccessSource(this.byteSource);
    }

    public RandomAccessFileOrArray(RandomAccessSource byteSource) {
        this.isBack = false;
        this.byteSource = byteSource;
    }

    @Deprecated
    public RandomAccessFileOrArray(String filename, boolean forceRead, boolean plainRandomAccess) throws IOException {
        this(new RandomAccessSourceFactory().setForceRead(forceRead).setUsePlainRandomAccess(plainRandomAccess).createBestSource(filename));
    }

    @Deprecated
    public RandomAccessFileOrArray(URL url) throws IOException {
        this(new RandomAccessSourceFactory().createSource(url));
    }

    @Deprecated
    public RandomAccessFileOrArray(InputStream is) throws IOException {
        this(new RandomAccessSourceFactory().createSource(is));
    }

    @Deprecated
    public RandomAccessFileOrArray(byte[] arrayIn) {
        this(new RandomAccessSourceFactory().createSource(arrayIn));
    }

    @Deprecated
    protected RandomAccessSource getByteSource() {
        return this.byteSource;
    }

    public void pushBack(byte b) {
        this.back = b;
        this.isBack = true;
    }

    public int read() throws IOException {
        if (this.isBack) {
            this.isBack = false;
            return this.back & 255;
        }
        RandomAccessSource randomAccessSource = this.byteSource;
        long j = this.byteSourcePosition;
        this.byteSourcePosition = 1 + j;
        return randomAccessSource.get(j);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        int count = 0;
        if (this.isBack && len > 0) {
            this.isBack = false;
            int off2 = off + 1;
            b[off] = this.back;
            len--;
            count = 0 + 1;
            off = off2;
        }
        if (len > 0) {
            int byteSourceCount = this.byteSource.get(this.byteSourcePosition, b, off, len);
            if (byteSourceCount > 0) {
                count += byteSourceCount;
                this.byteSourcePosition += (long) byteSourceCount;
            }
        }
        if (count == 0) {
            return -1;
        }
        return count;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        int n = 0;
        do {
            int count = read(b, off + n, len - n);
            if (count < 0) {
                throw new EOFException();
            }
            n += count;
        } while (n < len);
    }

    public long skip(long n) throws IOException {
        if (n <= 0) {
            return 0;
        }
        int adj = 0;
        if (this.isBack) {
            this.isBack = false;
            if (n == 1) {
                return 1;
            }
            n--;
            adj = 1;
        }
        long pos = getFilePointer();
        long len = length();
        long newpos = pos + n;
        if (newpos > len) {
            newpos = len;
        }
        seek(newpos);
        return (newpos - pos) + ((long) adj);
    }

    public int skipBytes(int n) throws IOException {
        return (int) skip((long) n);
    }

    @Deprecated
    public void reOpen() throws IOException {
        seek(0);
    }

    public void close() throws IOException {
        this.isBack = false;
        this.byteSource.close();
    }

    public long length() throws IOException {
        return this.byteSource.length();
    }

    public void seek(long pos) throws IOException {
        this.byteSourcePosition = pos;
        this.isBack = false;
    }

    public long getFilePointer() throws IOException {
        return this.byteSourcePosition - ((long) (this.isBack ? 1 : 0));
    }

    public boolean readBoolean() throws IOException {
        int ch = read();
        if (ch >= 0) {
            return ch != 0;
        } else {
            throw new EOFException();
        }
    }

    public byte readByte() throws IOException {
        int ch = read();
        if (ch >= 0) {
            return (byte) ch;
        }
        throw new EOFException();
    }

    public int readUnsignedByte() throws IOException {
        int ch = read();
        if (ch >= 0) {
            return ch;
        }
        throw new EOFException();
    }

    public short readShort() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) >= 0) {
            return (short) ((ch1 << 8) + ch2);
        }
        throw new EOFException();
    }

    public final short readShortLE() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) >= 0) {
            return (short) ((ch2 << 8) + (ch1 << 0));
        }
        throw new EOFException();
    }

    public int readUnsignedShort() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) >= 0) {
            return (ch1 << 8) + ch2;
        }
        throw new EOFException();
    }

    public final int readUnsignedShortLE() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) >= 0) {
            return (ch2 << 8) + (ch1 << 0);
        }
        throw new EOFException();
    }

    public char readChar() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) >= 0) {
            return (char) ((ch1 << 8) + ch2);
        }
        throw new EOFException();
    }

    public final char readCharLE() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) >= 0) {
            return (char) ((ch2 << 8) + (ch1 << 0));
        }
        throw new EOFException();
    }

    public int readInt() throws IOException {
        int ch1 = read();
        int ch2 = read();
        int ch3 = read();
        int ch4 = read();
        if ((((ch1 | ch2) | ch3) | ch4) >= 0) {
            return (((ch1 << 24) + (ch2 << 16)) + (ch3 << 8)) + ch4;
        }
        throw new EOFException();
    }

    public final int readIntLE() throws IOException {
        int ch1 = read();
        int ch2 = read();
        int ch3 = read();
        int ch4 = read();
        if ((((ch1 | ch2) | ch3) | ch4) >= 0) {
            return (((ch4 << 24) + (ch3 << 16)) + (ch2 << 8)) + (ch1 << 0);
        }
        throw new EOFException();
    }

    public final long readUnsignedInt() throws IOException {
        long ch1 = (long) read();
        long ch2 = (long) read();
        long ch3 = (long) read();
        long ch4 = (long) read();
        if ((((ch1 | ch2) | ch3) | ch4) >= 0) {
            return (((ch1 << 24) + (ch2 << 16)) + (ch3 << 8)) + (ch4 << null);
        }
        throw new EOFException();
    }

    public final long readUnsignedIntLE() throws IOException {
        long ch1 = (long) read();
        long ch2 = (long) read();
        long ch3 = (long) read();
        long ch4 = (long) read();
        if ((((ch1 | ch2) | ch3) | ch4) >= 0) {
            return (((ch4 << 24) + (ch3 << 16)) + (ch2 << 8)) + (ch1 << null);
        }
        throw new EOFException();
    }

    public long readLong() throws IOException {
        return (((long) readInt()) << 32) + (((long) readInt()) & 4294967295L);
    }

    public final long readLongLE() throws IOException {
        return (((long) readIntLE()) << 32) + (((long) readIntLE()) & 4294967295L);
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public final float readFloatLE() throws IOException {
        return Float.intBitsToFloat(readIntLE());
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public final double readDoubleLE() throws IOException {
        return Double.longBitsToDouble(readLongLE());
    }

    public String readLine() throws IOException {
        StringBuilder input = new StringBuilder();
        int c = -1;
        boolean eol = false;
        while (!eol) {
            c = read();
            switch (c) {
                case -1:
                case 10:
                    eol = true;
                    break;
                case 13:
                    eol = true;
                    long cur = getFilePointer();
                    if (read() == 10) {
                        break;
                    }
                    seek(cur);
                    break;
                default:
                    input.append((char) c);
                    break;
            }
        }
        if (c == -1 && input.length() == 0) {
            return null;
        }
        return input.toString();
    }

    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }

    public String readString(int length, String encoding) throws IOException {
        byte[] buf = new byte[length];
        readFully(buf);
        try {
            return new String(buf, encoding);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
}
