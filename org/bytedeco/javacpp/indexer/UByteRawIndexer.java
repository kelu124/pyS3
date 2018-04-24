package org.bytedeco.javacpp.indexer;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;

public class UByteRawIndexer extends UByteIndexer {
    protected static final Raw RAW = Raw.getInstance();
    final long base;
    protected BytePointer pointer;
    final long size;

    public UByteRawIndexer(BytePointer pointer) {
        this(pointer, new long[]{pointer.limit() - pointer.position()}, ONE_STRIDE);
    }

    public UByteRawIndexer(BytePointer pointer, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.pointer = pointer;
        this.base = pointer.address() + pointer.position();
        this.size = pointer.limit() - pointer.position();
    }

    public Pointer pointer() {
        return this.pointer;
    }

    public int get(long i) {
        return RAW.getByte(this.base + Indexer.checkIndex(i, this.size)) & 255;
    }

    public UByteIndexer get(long i, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = get((this.strides[0] * i) + ((long) n)) & 255;
        }
        return this;
    }

    public int get(long i, long j) {
        return get((this.strides[0] * i) + j) & 255;
    }

    public UByteIndexer get(long i, long j, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = get(((this.strides[0] * i) + (this.strides[1] * j)) + ((long) n)) & 255;
        }
        return this;
    }

    public int get(long i, long j, long k) {
        return get(((this.strides[0] * i) + (this.strides[1] * j)) + k) & 255;
    }

    public int get(long... indices) {
        return get(index(indices)) & 255;
    }

    public UByteIndexer get(long[] indices, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = get(index(indices) + ((long) n)) & 255;
        }
        return this;
    }

    public UByteIndexer put(long i, int b) {
        RAW.putByte(this.base + Indexer.checkIndex(i, this.size), (byte) b);
        return this;
    }

    public UByteIndexer put(long i, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put((this.strides[0] * i) + ((long) n), (byte) b[offset + n]);
        }
        return this;
    }

    public UByteIndexer put(long i, long j, int b) {
        put((this.strides[0] * i) + j, (byte) b);
        return this;
    }

    public UByteIndexer put(long i, long j, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put(((this.strides[0] * i) + (this.strides[1] * j)) + ((long) n), (byte) b[offset + n]);
        }
        return this;
    }

    public UByteIndexer put(long i, long j, long k, int b) {
        put(((this.strides[0] * i) + (this.strides[1] * j)) + k, (byte) b);
        return this;
    }

    public UByteIndexer put(long[] indices, int b) {
        put(index(indices), (byte) b);
        return this;
    }

    public UByteIndexer put(long[] indices, int[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put(index(indices) + ((long) n), (byte) b[offset + n]);
        }
        return this;
    }

    public void release() {
        this.pointer = null;
    }
}
