package org.bytedeco.javacpp.indexer;

import java.nio.Buffer;
import java.nio.CharBuffer;

public class CharBufferIndexer extends CharIndexer {
    protected CharBuffer buffer;

    public CharBufferIndexer(CharBuffer buffer) {
        this(buffer, new long[]{(long) buffer.limit()}, ONE_STRIDE);
    }

    public CharBufferIndexer(CharBuffer buffer, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.buffer = buffer;
    }

    public Buffer buffer() {
        return this.buffer;
    }

    public char get(long i) {
        return this.buffer.get((int) i);
    }

    public CharIndexer get(long i, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            c[offset + n] = this.buffer.get((((int) i) * ((int) this.strides[0])) + n);
        }
        return this;
    }

    public char get(long i, long j) {
        return this.buffer.get((((int) i) * ((int) this.strides[0])) + ((int) j));
    }

    public CharIndexer get(long i, long j, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            c[offset + n] = this.buffer.get(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n);
        }
        return this;
    }

    public char get(long i, long j, long k) {
        return this.buffer.get(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k));
    }

    public char get(long... indices) {
        return this.buffer.get((int) index(indices));
    }

    public CharIndexer get(long[] indices, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            c[offset + n] = this.buffer.get(((int) index(indices)) + n);
        }
        return this;
    }

    public CharIndexer put(long i, char c) {
        this.buffer.put((int) i, c);
        return this;
    }

    public CharIndexer put(long i, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put((((int) i) * ((int) this.strides[0])) + n, c[offset + n]);
        }
        return this;
    }

    public CharIndexer put(long i, long j, char c) {
        this.buffer.put((((int) i) * ((int) this.strides[0])) + ((int) j), c);
        return this;
    }

    public CharIndexer put(long i, long j, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n, c[offset + n]);
        }
        return this;
    }

    public CharIndexer put(long i, long j, long k, char c) {
        this.buffer.put(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k), c);
        return this;
    }

    public CharIndexer put(long[] indices, char c) {
        this.buffer.put((int) index(indices), c);
        return this;
    }

    public CharIndexer put(long[] indices, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put(((int) index(indices)) + n, c[offset + n]);
        }
        return this;
    }

    public void release() {
        this.buffer = null;
    }
}
