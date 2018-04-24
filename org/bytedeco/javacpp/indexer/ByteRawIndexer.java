package org.bytedeco.javacpp.indexer;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;

public class ByteRawIndexer extends ByteIndexer {
    protected static final Raw RAW = Raw.getInstance();
    final long base;
    protected BytePointer pointer;
    final long size;

    public ByteRawIndexer(BytePointer pointer) {
        this(pointer, new long[]{pointer.limit() - pointer.position()}, ONE_STRIDE);
    }

    public ByteRawIndexer(BytePointer pointer, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.pointer = pointer;
        this.base = pointer.address() + pointer.position();
        this.size = pointer.limit() - pointer.position();
    }

    public Pointer pointer() {
        return this.pointer;
    }

    public byte get(long i) {
        return RAW.getByte(this.base + Indexer.checkIndex(i, this.size));
    }

    public ByteIndexer get(long i, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = get((this.strides[0] * i) + ((long) n));
        }
        return this;
    }

    public byte get(long i, long j) {
        return get((this.strides[0] * i) + ((long) ((int) j)));
    }

    public ByteIndexer get(long i, long j, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = get(((this.strides[0] * i) + (this.strides[1] * j)) + ((long) n));
        }
        return this;
    }

    public byte get(long i, long j, long k) {
        return get(((this.strides[0] * i) + (this.strides[1] * j)) + k);
    }

    public byte get(long... indices) {
        return get(index(indices));
    }

    public ByteIndexer get(long[] indices, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = get(index(indices) + ((long) n));
        }
        return this;
    }

    public ByteIndexer put(long i, byte b) {
        RAW.putByte(this.base + Indexer.checkIndex(i, this.size), b);
        return this;
    }

    public ByteIndexer put(long i, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put((this.strides[0] * i) + ((long) n), b[offset + n]);
        }
        return this;
    }

    public ByteIndexer put(long i, long j, byte b) {
        put((this.strides[0] * i) + j, b);
        return this;
    }

    public ByteIndexer put(long i, long j, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put(((this.strides[0] * i) + (this.strides[1] * j)) + ((long) n), b[offset + n]);
        }
        return this;
    }

    public ByteIndexer put(long i, long j, long k, byte b) {
        put(((this.strides[0] * i) + (this.strides[1] * j)) + k, b);
        return this;
    }

    public ByteIndexer put(long[] indices, byte b) {
        put(index(indices), b);
        return this;
    }

    public ByteIndexer put(long[] indices, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put(index(indices) + ((long) n), b[offset + n]);
        }
        return this;
    }

    public void release() {
        this.pointer = null;
    }
}
