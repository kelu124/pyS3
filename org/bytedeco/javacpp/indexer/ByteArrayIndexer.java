package org.bytedeco.javacpp.indexer;

public class ByteArrayIndexer extends ByteIndexer {
    protected byte[] array;

    public /* bridge */ /* synthetic */ Indexer putDouble(long[] jArr, double d) {
        return super.putDouble(jArr, d);
    }

    public ByteArrayIndexer(byte[] array) {
        this(array, new long[]{(long) array.length}, ONE_STRIDE);
    }

    public ByteArrayIndexer(byte[] array, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.array = array;
    }

    public byte[] array() {
        return this.array;
    }

    public byte get(long i) {
        return this.array[(int) i];
    }

    public ByteIndexer get(long i, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = this.array[(((int) i) * ((int) this.strides[0])) + n];
        }
        return this;
    }

    public byte get(long i, long j) {
        return this.array[(((int) i) * ((int) this.strides[0])) + ((int) j)];
    }

    public ByteIndexer get(long i, long j, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n];
        }
        return this;
    }

    public byte get(long i, long j, long k) {
        return this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k)];
    }

    public byte get(long... indices) {
        return this.array[(int) index(indices)];
    }

    public ByteIndexer get(long[] indices, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = this.array[((int) index(indices)) + n];
        }
        return this;
    }

    public ByteIndexer put(long i, byte b) {
        this.array[(int) i] = b;
        return this;
    }

    public ByteIndexer put(long i, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[(((int) i) * ((int) this.strides[0])) + n] = b[offset + n];
        }
        return this;
    }

    public ByteIndexer put(long i, long j, byte b) {
        this.array[(((int) i) * ((int) this.strides[0])) + ((int) j)] = b;
        return this;
    }

    public ByteIndexer put(long i, long j, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n] = b[offset + n];
        }
        return this;
    }

    public ByteIndexer put(long i, long j, long k, byte b) {
        this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k)] = b;
        return this;
    }

    public ByteIndexer put(long[] indices, byte b) {
        this.array[(int) index(indices)] = b;
        return this;
    }

    public ByteIndexer put(long[] indices, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[((int) index(indices)) + n] = b[offset + n];
        }
        return this;
    }

    public void release() {
        this.array = null;
    }
}
