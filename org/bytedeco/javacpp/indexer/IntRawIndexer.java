package org.bytedeco.javacpp.indexer;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Pointer;

public class IntRawIndexer extends IntIndexer {
    protected static final Raw RAW = Raw.getInstance();
    final long base;
    protected IntPointer pointer;
    final long size;

    public IntRawIndexer(IntPointer pointer) {
        this(pointer, new long[]{pointer.limit() - pointer.position()}, ONE_STRIDE);
    }

    public IntRawIndexer(IntPointer pointer, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.pointer = pointer;
        this.base = pointer.address() + (pointer.position() * 4);
        this.size = pointer.limit() - pointer.position();
    }

    public Pointer pointer() {
        return this.pointer;
    }

    public int get(long i) {
        return RAW.getInt(this.base + (Indexer.checkIndex(i, this.size) * 4));
    }

    public IntIndexer get(long i, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            m[offset + n] = get((this.strides[0] * i) + ((long) n));
        }
        return this;
    }

    public int get(long i, long j) {
        return get((this.strides[0] * i) + j);
    }

    public IntIndexer get(long i, long j, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            m[offset + n] = get(((this.strides[0] * i) + (this.strides[1] * j)) + ((long) n));
        }
        return this;
    }

    public int get(long i, long j, long k) {
        return get(((this.strides[0] * i) + (this.strides[1] * j)) + k);
    }

    public int get(long... indices) {
        return get(index(indices));
    }

    public IntIndexer get(long[] indices, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            m[offset + n] = get(index(indices) + ((long) n));
        }
        return this;
    }

    public IntIndexer put(long i, int n) {
        RAW.putInt(this.base + (Indexer.checkIndex(i, this.size) * 4), n);
        return this;
    }

    public IntIndexer put(long i, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put((this.strides[0] * i) + ((long) n), m[offset + n]);
        }
        return this;
    }

    public IntIndexer put(long i, long j, int n) {
        put((this.strides[0] * i) + j, n);
        return this;
    }

    public IntIndexer put(long i, long j, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put(((this.strides[0] * i) + (this.strides[1] * j)) + ((long) n), m[offset + n]);
        }
        return this;
    }

    public IntIndexer put(long i, long j, long k, int n) {
        put(((this.strides[0] * i) + (this.strides[1] * j)) + k, n);
        return this;
    }

    public IntIndexer put(long[] indices, int n) {
        put(index(indices), n);
        return this;
    }

    public IntIndexer put(long[] indices, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put(index(indices) + ((long) n), m[offset + n]);
        }
        return this;
    }

    public void release() {
        this.pointer = null;
    }
}
