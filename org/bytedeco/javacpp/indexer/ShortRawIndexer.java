package org.bytedeco.javacpp.indexer;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.ShortPointer;

public class ShortRawIndexer extends ShortIndexer {
    protected static final Raw RAW = Raw.getInstance();
    final long base;
    protected ShortPointer pointer;
    final long size;

    public ShortRawIndexer(ShortPointer pointer) {
        this(pointer, new long[]{pointer.limit() - pointer.position()}, ONE_STRIDE);
    }

    public ShortRawIndexer(ShortPointer pointer, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.pointer = pointer;
        this.base = pointer.address() + (pointer.position() * 2);
        this.size = pointer.limit() - pointer.position();
    }

    public Pointer pointer() {
        return this.pointer;
    }

    public short get(long i) {
        return RAW.getShort(this.base + (Indexer.checkIndex(i, this.size) * 2));
    }

    public ShortIndexer get(long i, short[] s, int offset, int length) {
        for (int n = 0; n < length; n++) {
            s[offset + n] = get((this.strides[0] * i) + ((long) n));
        }
        return this;
    }

    public short get(long i, long j) {
        return get((this.strides[0] * i) + j);
    }

    public ShortIndexer get(long i, long j, short[] s, int offset, int length) {
        for (int n = 0; n < length; n++) {
            s[offset + n] = get(((this.strides[0] * i) + (this.strides[1] * j)) + ((long) n));
        }
        return this;
    }

    public short get(long i, long j, long k) {
        return get(((this.strides[0] * i) + (this.strides[1] * j)) + k);
    }

    public short get(long... indices) {
        return get(index(indices));
    }

    public ShortIndexer get(long[] indices, short[] s, int offset, int length) {
        for (int n = 0; n < length; n++) {
            s[offset + n] = get(index(indices) + ((long) n));
        }
        return this;
    }

    public ShortIndexer put(long i, short s) {
        RAW.putShort(this.base + (Indexer.checkIndex(i, this.size) * 2), s);
        return this;
    }

    public ShortIndexer put(long i, short[] s, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put((this.strides[0] * i) + ((long) n), s[offset + n]);
        }
        return this;
    }

    public ShortIndexer put(long i, long j, short s) {
        put((this.strides[0] * i) + j, s);
        return this;
    }

    public ShortIndexer put(long i, long j, short[] s, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put(((this.strides[0] * i) + (this.strides[1] * j)) + ((long) n), s[offset + n]);
        }
        return this;
    }

    public ShortIndexer put(long i, long j, long k, short s) {
        put(((this.strides[0] * i) + (this.strides[1] * j)) + k, s);
        return this;
    }

    public ShortIndexer put(long[] indices, short s) {
        put(index(indices), s);
        return this;
    }

    public ShortIndexer put(long[] indices, short[] s, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put(index(indices) + ((long) n), s[offset + n]);
        }
        return this;
    }

    public void release() {
        this.pointer = null;
    }
}
