package org.bytedeco.javacpp.indexer;

public class IntArrayIndexer extends IntIndexer {
    protected int[] array;

    public /* bridge */ /* synthetic */ Indexer putDouble(long[] jArr, double d) {
        return super.putDouble(jArr, d);
    }

    public IntArrayIndexer(int[] array) {
        this(array, new long[]{(long) array.length}, ONE_STRIDE);
    }

    public IntArrayIndexer(int[] array, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.array = array;
    }

    public int[] array() {
        return this.array;
    }

    public int get(long i) {
        return this.array[(int) i];
    }

    public IntIndexer get(long i, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            m[offset + n] = this.array[(((int) i) * ((int) this.strides[0])) + n];
        }
        return this;
    }

    public int get(long i, long j) {
        return this.array[(((int) i) * ((int) this.strides[0])) + ((int) j)];
    }

    public IntIndexer get(long i, long j, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            m[offset + n] = this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n];
        }
        return this;
    }

    public int get(long i, long j, long k) {
        return this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k)];
    }

    public int get(long... indices) {
        return this.array[(int) index(indices)];
    }

    public IntIndexer get(long[] indices, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            m[offset + n] = this.array[((int) index(indices)) + n];
        }
        return this;
    }

    public IntIndexer put(long i, int n) {
        this.array[(int) i] = n;
        return this;
    }

    public IntIndexer put(long i, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[(((int) i) * ((int) this.strides[0])) + n] = m[offset + n];
        }
        return this;
    }

    public IntIndexer put(long i, long j, int n) {
        this.array[(((int) i) * ((int) this.strides[0])) + ((int) j)] = n;
        return this;
    }

    public IntIndexer put(long i, long j, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n] = m[offset + n];
        }
        return this;
    }

    public IntIndexer put(long i, long j, long k, int n) {
        this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k)] = n;
        return this;
    }

    public IntIndexer put(long[] indices, int n) {
        this.array[(int) index(indices)] = n;
        return this;
    }

    public IntIndexer put(long[] indices, int[] m, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[((int) index(indices)) + n] = m[offset + n];
        }
        return this;
    }

    public void release() {
        this.array = null;
    }
}
