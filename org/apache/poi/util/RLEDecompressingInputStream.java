package org.apache.poi.util;

import com.lee.ultrascan.service.ProbeMessageID;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class RLEDecompressingInputStream extends InputStream {
    private static final int[] POWER2 = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768};
    private final byte[] buf = new byte[4096];
    private final InputStream in;
    private int len;
    private int pos = 0;

    public RLEDecompressingInputStream(InputStream in) throws IOException {
        this.in = in;
        if (in.read() != 1) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "Header byte 0x01 expected, received 0x%02X", new Object[]{Integer.valueOf(in.read() & 255)}));
        } else {
            this.len = readChunk();
        }
    }

    public int read() throws IOException {
        if (this.len == -1) {
            return -1;
        }
        int readChunk;
        if (this.pos >= this.len) {
            readChunk = readChunk();
            this.len = readChunk;
            if (readChunk == -1) {
                return -1;
            }
        }
        byte[] bArr = this.buf;
        readChunk = this.pos;
        this.pos = readChunk + 1;
        return bArr[readChunk];
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int l) throws IOException {
        if (this.len == -1) {
            return -1;
        }
        int offset = off;
        int length = l;
        while (length > 0) {
            if (this.pos >= this.len) {
                int readChunk = readChunk();
                this.len = readChunk;
                if (readChunk == -1) {
                    if (offset > off) {
                        return offset - off;
                    }
                    return -1;
                }
            }
            int c = Math.min(length, this.len - this.pos);
            System.arraycopy(this.buf, this.pos, b, offset, c);
            this.pos += c;
            length -= c;
            offset += c;
        }
        return l;
    }

    public long skip(long n) throws IOException {
        long length = n;
        while (length > 0) {
            if (this.pos >= this.len) {
                int readChunk = readChunk();
                this.len = readChunk;
                if (readChunk == -1) {
                    return -1;
                }
            }
            int c = (int) Math.min(n, (long) (this.len - this.pos));
            this.pos += c;
            length -= (long) c;
        }
        return n;
    }

    public int available() {
        return this.len > 0 ? this.len - this.pos : 0;
    }

    public void close() throws IOException {
        this.in.close();
    }

    private int readChunk() throws IOException {
        this.pos = 0;
        int w = readShort(this.in);
        if (w == -1) {
            return -1;
        }
        int chunkSize = (w & 4095) + 1;
        if ((w & ProbeMessageID.ID_SETTINGS) != 12288) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "Chunksize header A should be 0x3000, received 0x%04X", new Object[]{Integer.valueOf(57344 & w)}));
        }
        if ((32768 & w) == 0) {
            if (this.in.read(this.buf, 0, chunkSize) >= chunkSize) {
                return chunkSize;
            }
            throw new IllegalStateException(String.format(Locale.ROOT, "Not enough bytes read, expected %d", new Object[]{Integer.valueOf(chunkSize)}));
        }
        int inOffset = 0;
        int outOffset = 0;
        while (inOffset < chunkSize) {
            int tokenFlags = this.in.read();
            inOffset++;
            if (tokenFlags == -1) {
                break;
            }
            int n = 0;
            int outOffset2 = outOffset;
            while (n < 8 && inOffset < chunkSize) {
                if ((POWER2[n] & tokenFlags) == 0) {
                    int b = this.in.read();
                    if (b == -1) {
                        return -1;
                    }
                    outOffset = outOffset2 + 1;
                    this.buf[outOffset2] = (byte) b;
                    inOffset++;
                } else {
                    int token = readShort(this.in);
                    if (token == -1) {
                        return -1;
                    }
                    inOffset += 2;
                    int copyLenBits = getCopyLenBits(outOffset2 - 1);
                    int startPos = outOffset2 - ((token >> copyLenBits) + 1);
                    int endPos = startPos + (((POWER2[copyLenBits] - 1) & token) + 3);
                    int i = startPos;
                    while (i < endPos) {
                        outOffset = outOffset2 + 1;
                        this.buf[outOffset2] = this.buf[i];
                        i++;
                        outOffset2 = outOffset;
                    }
                    outOffset = outOffset2;
                }
                n++;
                outOffset2 = outOffset;
            }
            outOffset = outOffset2;
        }
        return outOffset;
    }

    static int getCopyLenBits(int offset) {
        for (int n = 11; n >= 4; n--) {
            if ((POWER2[n] & offset) != 0) {
                return 15 - n;
            }
        }
        return 12;
    }

    public int readShort() throws IOException {
        return readShort(this);
    }

    public int readInt() throws IOException {
        return readInt(this);
    }

    private int readShort(InputStream stream) throws IOException {
        int b0 = stream.read();
        if (b0 == -1) {
            return -1;
        }
        int b1 = stream.read();
        if (b1 != -1) {
            return (b0 & 255) | ((b1 & 255) << 8);
        }
        return -1;
    }

    private int readInt(InputStream stream) throws IOException {
        int b0 = stream.read();
        if (b0 == -1) {
            return -1;
        }
        int b1 = stream.read();
        if (b1 == -1) {
            return -1;
        }
        int b2 = stream.read();
        if (b2 == -1) {
            return -1;
        }
        int b3 = stream.read();
        if (b3 != -1) {
            return (((b0 & 255) | ((b1 & 255) << 8)) | ((b2 & 255) << 16)) | ((b3 & 255) << 24);
        }
        return -1;
    }

    public static byte[] decompress(byte[] compressed) throws IOException {
        return decompress(compressed, 0, compressed.length);
    }

    public static byte[] decompress(byte[] compressed, int offset, int length) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream stream = new RLEDecompressingInputStream(new ByteArrayInputStream(compressed, offset, length));
        IOUtils.copy(stream, out);
        stream.close();
        out.close();
        return out.toByteArray();
    }
}
