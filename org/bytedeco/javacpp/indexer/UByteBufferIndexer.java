package org.bytedeco.javacpp.indexer;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class UByteBufferIndexer extends UByteIndexer {
    protected ByteBuffer buffer;

    public UByteBufferIndexer(ByteBuffer buffer) {
        this(buffer, new long[]{(long) buffer.limit()}, ONE_STRIDE);
    }

    public UByteBufferIndexer(ByteBuffer buffer, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.buffer = buffer;
    }

    public Buffer buffer() {
        return this.buffer;
    }

    public int get(long i) {
        return this.buffer.get((int) i) & 255;
    }

    public UByteIndexer get(long i, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = this.buffer.get((((int) i) * ((int) this.strides[0])) + n) & 255;
        }
        return this;
    }

    public int get(long i, long j) {
        return this.buffer.get((((int) i) * ((int) this.strides[0])) + ((int) j)) & 255;
    }

    public UByteIndexer get(long i, long j, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = this.buffer.get(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n) & 255;
        }
        return this;
    }

    public int get(long i, long j, long k) {
        return this.buffer.get(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k)) & 255;
    }

    public int get(long... indices) {
        return this.buffer.get((int) index(indices)) & 255;
    }

    public UByteIndexer get(long[] indices, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = this.buffer.get(((int) index(indices)) + n) & 255;
        }
        return this;
    }

    public UByteIndexer put(long i, int b) {
        this.buffer.put((int) i, (byte) b);
        return this;
    }

    public UByteIndexer put(long i, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put((((int) i) * ((int) this.strides[0])) + n, (byte) b[offset + n]);
        }
        return this;
    }

    public UByteIndexer put(long i, long j, int b) {
        this.buffer.put((((int) i) * ((int) this.strides[0])) + ((int) j), (byte) b);
        return this;
    }

    public UByteIndexer put(long i, long j, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n, (byte) b[offset + n]);
        }
        return this;
    }

    public UByteIndexer put(long i, long j, long k, int b) {
        this.buffer.put(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k), (byte) b);
        return this;
    }

    public UByteIndexer put(long[] indices, int b) {
        this.buffer.put((int) index(indices), (byte) b);
        return this;
    }

    public UByteIndexer put(long[] indices, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put(((int) index(indices)) + n, (byte) b[offset + n]);
        }
        return this;
    }

    public void release() {
        this.buffer = null;
    }
}
