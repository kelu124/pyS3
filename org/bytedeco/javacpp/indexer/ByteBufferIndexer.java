package org.bytedeco.javacpp.indexer;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ByteBufferIndexer extends ByteIndexer {
    protected ByteBuffer buffer;

    public ByteBufferIndexer(ByteBuffer buffer) {
        this(buffer, new long[]{(long) buffer.limit()}, ONE_STRIDE);
    }

    public ByteBufferIndexer(ByteBuffer buffer, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.buffer = buffer;
    }

    public Buffer buffer() {
        return this.buffer;
    }

    public byte get(long i) {
        return this.buffer.get((int) i);
    }

    public ByteIndexer get(long i, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = this.buffer.get((((int) i) * ((int) this.strides[0])) + n);
        }
        return this;
    }

    public byte get(long i, long j) {
        return this.buffer.get((((int) i) * ((int) this.strides[0])) + ((int) j));
    }

    public ByteIndexer get(long i, long j, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = this.buffer.get(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n);
        }
        return this;
    }

    public byte get(long i, long j, long k) {
        return this.buffer.get(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k));
    }

    public byte get(long... indices) {
        return this.buffer.get((int) index(indices));
    }

    public ByteIndexer get(long[] indices, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = this.buffer.get(((int) index(indices)) + n);
        }
        return this;
    }

    public ByteIndexer put(long i, byte b) {
        this.buffer.put((int) i, b);
        return this;
    }

    public ByteIndexer put(long i, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put((((int) i) * ((int) this.strides[0])) + n, b[offset + n]);
        }
        return this;
    }

    public ByteIndexer put(long i, long j, byte b) {
        this.buffer.put((((int) i) * ((int) this.strides[0])) + ((int) j), b);
        return this;
    }

    public ByteIndexer put(long i, long j, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n, b[offset + n]);
        }
        return this;
    }

    public ByteIndexer put(long i, long j, long k, byte b) {
        this.buffer.put(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k), b);
        return this;
    }

    public ByteIndexer put(long[] indices, byte b) {
        this.buffer.put((int) index(indices), b);
        return this;
    }

    public ByteIndexer put(long[] indices, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put(((int) index(indices)) + n, b[offset + n]);
        }
        return this;
    }

    public void release() {
        this.buffer = null;
    }
}
