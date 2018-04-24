package org.bytedeco.javacpp.indexer;

public class CharArrayIndexer extends CharIndexer {
    protected char[] array;

    public /* bridge */ /* synthetic */ Indexer putDouble(long[] jArr, double d) {
        return super.putDouble(jArr, d);
    }

    public CharArrayIndexer(char[] array) {
        this(array, new long[]{(long) array.length}, ONE_STRIDE);
    }

    public CharArrayIndexer(char[] array, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.array = array;
    }

    public char[] array() {
        return this.array;
    }

    public char get(long i) {
        return this.array[(int) i];
    }

    public CharIndexer get(long i, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            c[offset + n] = this.array[(((int) i) * ((int) this.strides[0])) + n];
        }
        return this;
    }

    public char get(long i, long j) {
        return this.array[(((int) i) * ((int) this.strides[0])) + ((int) j)];
    }

    public CharIndexer get(long i, long j, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            c[offset + n] = this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n];
        }
        return this;
    }

    public char get(long i, long j, long k) {
        return this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k)];
    }

    public char get(long... indices) {
        return this.array[(int) index(indices)];
    }

    public CharIndexer get(long[] indices, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            c[offset + n] = this.array[((int) index(indices)) + n];
        }
        return this;
    }

    public CharIndexer put(long i, char c) {
        this.array[(int) i] = c;
        return this;
    }

    public CharIndexer put(long i, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[(((int) i) * ((int) this.strides[0])) + n] = c[offset + n];
        }
        return this;
    }

    public CharIndexer put(long i, long j, char c) {
        this.array[(((int) i) * ((int) this.strides[0])) + ((int) j)] = c;
        return this;
    }

    public CharIndexer put(long i, long j, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n] = c[offset + n];
        }
        return this;
    }

    public CharIndexer put(long i, long j, long k, char c) {
        this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k)] = c;
        return this;
    }

    public CharIndexer put(long[] indices, char c) {
        this.array[(int) index(indices)] = c;
        return this;
    }

    public CharIndexer put(long[] indices, char[] c, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[((int) index(indices)) + n] = c[offset + n];
        }
        return this;
    }

    public void release() {
        this.array = null;
    }
}
