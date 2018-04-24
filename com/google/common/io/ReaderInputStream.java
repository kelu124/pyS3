package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedBytes;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

@GwtIncompatible
final class ReaderInputStream extends InputStream {
    private ByteBuffer byteBuffer;
    private CharBuffer charBuffer;
    private boolean doneFlushing;
    private boolean draining;
    private final CharsetEncoder encoder;
    private boolean endOfInput;
    private final Reader reader;
    private final byte[] singleByte;

    ReaderInputStream(Reader reader, Charset charset, int bufferSize) {
        this(reader, charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), bufferSize);
    }

    ReaderInputStream(Reader reader, CharsetEncoder encoder, int bufferSize) {
        this.singleByte = new byte[1];
        this.reader = (Reader) Preconditions.checkNotNull(reader);
        this.encoder = (CharsetEncoder) Preconditions.checkNotNull(encoder);
        Preconditions.checkArgument(bufferSize > 0, "bufferSize must be positive: %s", bufferSize);
        encoder.reset();
        this.charBuffer = CharBuffer.allocate(bufferSize);
        this.charBuffer.flip();
        this.byteBuffer = ByteBuffer.allocate(bufferSize);
    }

    public void close() throws IOException {
        this.reader.close();
    }

    public int read() throws IOException {
        return read(this.singleByte) == 1 ? UnsignedBytes.toInt(this.singleByte[0]) : -1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int read(byte[] r10, int r11, int r12) throws java.io.IOException {
        /*
        r9 = this;
        r8 = 1;
        r3 = 0;
        r4 = r11 + r12;
        r5 = r10.length;
        com.google.common.base.Preconditions.checkPositionIndexes(r11, r4, r5);
        if (r12 != 0) goto L_0x000c;
    L_0x000a:
        r2 = r3;
    L_0x000b:
        return r2;
    L_0x000c:
        r2 = 0;
        r0 = r9.endOfInput;
    L_0x000f:
        r4 = r9.draining;
        if (r4 == 0) goto L_0x002d;
    L_0x0013:
        r4 = r11 + r2;
        r5 = r12 - r2;
        r4 = r9.drain(r10, r4, r5);
        r2 = r2 + r4;
        if (r2 == r12) goto L_0x0022;
    L_0x001e:
        r4 = r9.doneFlushing;
        if (r4 == 0) goto L_0x0026;
    L_0x0022:
        if (r2 > 0) goto L_0x000b;
    L_0x0024:
        r2 = -1;
        goto L_0x000b;
    L_0x0026:
        r9.draining = r3;
        r4 = r9.byteBuffer;
        r4.clear();
    L_0x002d:
        r4 = r9.doneFlushing;
        if (r4 == 0) goto L_0x003d;
    L_0x0031:
        r1 = java.nio.charset.CoderResult.UNDERFLOW;
    L_0x0033:
        r4 = r1.isOverflow();
        if (r4 == 0) goto L_0x0055;
    L_0x0039:
        r9.startDraining(r8);
        goto L_0x000f;
    L_0x003d:
        if (r0 == 0) goto L_0x0048;
    L_0x003f:
        r4 = r9.encoder;
        r5 = r9.byteBuffer;
        r1 = r4.flush(r5);
        goto L_0x0033;
    L_0x0048:
        r4 = r9.encoder;
        r5 = r9.charBuffer;
        r6 = r9.byteBuffer;
        r7 = r9.endOfInput;
        r1 = r4.encode(r5, r6, r7);
        goto L_0x0033;
    L_0x0055:
        r4 = r1.isUnderflow();
        if (r4 == 0) goto L_0x006d;
    L_0x005b:
        if (r0 == 0) goto L_0x0063;
    L_0x005d:
        r9.doneFlushing = r8;
        r9.startDraining(r3);
        goto L_0x000f;
    L_0x0063:
        r4 = r9.endOfInput;
        if (r4 == 0) goto L_0x0069;
    L_0x0067:
        r0 = 1;
        goto L_0x002d;
    L_0x0069:
        r9.readMoreChars();
        goto L_0x002d;
    L_0x006d:
        r4 = r1.isError();
        if (r4 == 0) goto L_0x002d;
    L_0x0073:
        r1.throwException();
        r2 = r3;
        goto L_0x000b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.io.ReaderInputStream.read(byte[], int, int):int");
    }

    private static CharBuffer grow(CharBuffer buf) {
        CharBuffer bigger = CharBuffer.wrap(Arrays.copyOf(buf.array(), buf.capacity() * 2));
        bigger.position(buf.position());
        bigger.limit(buf.limit());
        return bigger;
    }

    private void readMoreChars() throws IOException {
        if (availableCapacity(this.charBuffer) == 0) {
            if (this.charBuffer.position() > 0) {
                this.charBuffer.compact().flip();
            } else {
                this.charBuffer = grow(this.charBuffer);
            }
        }
        int limit = this.charBuffer.limit();
        int numChars = this.reader.read(this.charBuffer.array(), limit, availableCapacity(this.charBuffer));
        if (numChars == -1) {
            this.endOfInput = true;
        } else {
            this.charBuffer.limit(limit + numChars);
        }
    }

    private static int availableCapacity(Buffer buffer) {
        return buffer.capacity() - buffer.limit();
    }

    private void startDraining(boolean overflow) {
        this.byteBuffer.flip();
        if (overflow && this.byteBuffer.remaining() == 0) {
            this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() * 2);
        } else {
            this.draining = true;
        }
    }

    private int drain(byte[] b, int off, int len) {
        int remaining = Math.min(len, this.byteBuffer.remaining());
        this.byteBuffer.get(b, off, remaining);
        return remaining;
    }
}
