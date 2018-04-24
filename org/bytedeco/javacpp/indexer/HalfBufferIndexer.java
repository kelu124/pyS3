package org.bytedeco.javacpp.indexer;

import java.nio.Buffer;
import java.nio.ShortBuffer;

public class HalfBufferIndexer extends HalfIndexer {
    protected ShortBuffer buffer;

    public HalfBufferIndexer(ShortBuffer buffer) {
        this(buffer, new long[]{(long) buffer.limit()}, ONE_STRIDE);
    }

    public HalfBufferIndexer(ShortBuffer buffer, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.buffer = buffer;
    }

    public Buffer buffer() {
        return this.buffer;
    }

    public float get(long i) {
        return HalfIndexer.toFloat(this.buffer.get((int) i));
    }

    public HalfIndexer get(long i, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            h[offset + n] = HalfIndexer.toFloat(this.buffer.get((((int) i) * ((int) this.strides[0])) + n));
        }
        return this;
    }

    public float get(long i, long j) {
        return HalfIndexer.toFloat(this.buffer.get((((int) i) * ((int) this.strides[0])) + ((int) j)));
    }

    public HalfIndexer get(long i, long j, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            h[offset + n] = HalfIndexer.toFloat(this.buffer.get(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n));
        }
        return this;
    }

    public float get(long i, long j, long k) {
        return HalfIndexer.toFloat(this.buffer.get(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k)));
    }

    public float get(long... indices) {
        return HalfIndexer.toFloat(this.buffer.get((int) index(indices)));
    }

    public HalfIndexer get(long[] indices, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            h[offset + n] = HalfIndexer.toFloat(this.buffer.get(((int) index(indices)) + n));
        }
        return this;
    }

    public HalfIndexer put(long i, float h) {
        this.buffer.put((int) i, (short) HalfIndexer.fromFloat(h));
        return this;
    }

    public HalfIndexer put(long i, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put((((int) i) * ((int) this.strides[0])) + n, (short) HalfIndexer.fromFloat(h[offset + n]));
        }
        return this;
    }

    public HalfIndexer put(long i, long j, float h) {
        this.buffer.put((((int) i) * ((int) this.strides[0])) + ((int) j), (short) HalfIndexer.fromFloat(h));
        return this;
    }

    public HalfIndexer put(long i, long j, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n, (short) HalfIndexer.fromFloat(h[offset + n]));
        }
        return this;
    }

    public HalfIndexer put(long i, long j, long k, float h) {
        this.buffer.put(((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k), (short) HalfIndexer.fromFloat(h));
        return this;
    }

    public HalfIndexer put(long[] indices, float h) {
        this.buffer.put((int) index(indices), (short) HalfIndexer.fromFloat(h));
        return this;
    }

    public HalfIndexer put(long[] indices, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.buffer.put(((int) index(indices)) + n, (short) HalfIndexer.fromFloat(h[offset + n]));
        }
        return this;
    }

    public void release() {
        this.buffer = null;
    }
}
