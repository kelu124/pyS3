package org.bytedeco.javacpp.indexer;

public class HalfArrayIndexer extends HalfIndexer {
    protected short[] array;

    public /* bridge */ /* synthetic */ Indexer putDouble(long[] jArr, double d) {
        return super.putDouble(jArr, d);
    }

    public HalfArrayIndexer(short[] array) {
        this(array, new long[]{(long) array.length}, ONE_STRIDE);
    }

    public HalfArrayIndexer(short[] array, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.array = array;
    }

    public short[] array() {
        return this.array;
    }

    public float get(long i) {
        return HalfIndexer.toFloat(this.array[(int) i]);
    }

    public HalfIndexer get(long i, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            h[offset + n] = HalfIndexer.toFloat(this.array[(((int) i) * ((int) this.strides[0])) + n]);
        }
        return this;
    }

    public float get(long i, long j) {
        return HalfIndexer.toFloat(this.array[(((int) i) * ((int) this.strides[0])) + ((int) j)]);
    }

    public HalfIndexer get(long i, long j, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            h[offset + n] = HalfIndexer.toFloat(this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n]);
        }
        return this;
    }

    public float get(long i, long j, long k) {
        return HalfIndexer.toFloat(this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k)]);
    }

    public float get(long... indices) {
        return HalfIndexer.toFloat(this.array[(int) index(indices)]);
    }

    public HalfIndexer get(long[] indices, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            h[offset + n] = HalfIndexer.toFloat(this.array[((int) index(indices)) + n]);
        }
        return this;
    }

    public HalfIndexer put(long i, float h) {
        this.array[(int) i] = (short) HalfIndexer.fromFloat(h);
        return this;
    }

    public HalfIndexer put(long i, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[(((int) i) * ((int) this.strides[0])) + n] = (short) HalfIndexer.fromFloat(h[offset + n]);
        }
        return this;
    }

    public HalfIndexer put(long i, long j, float h) {
        this.array[(((int) i) * ((int) this.strides[0])) + ((int) j)] = (short) HalfIndexer.fromFloat(h);
        return this;
    }

    public HalfIndexer put(long i, long j, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + n] = (short) HalfIndexer.fromFloat(h[offset + n]);
        }
        return this;
    }

    public HalfIndexer put(long i, long j, long k, float h) {
        this.array[((((int) i) * ((int) this.strides[0])) + (((int) j) * ((int) this.strides[1]))) + ((int) k)] = (short) HalfIndexer.fromFloat(h);
        return this;
    }

    public HalfIndexer put(long[] indices, float h) {
        this.array[(int) index(indices)] = (short) HalfIndexer.fromFloat(h);
        return this;
    }

    public HalfIndexer put(long[] indices, float[] h, int offset, int length) {
        for (int n = 0; n < length; n++) {
            this.array[((int) index(indices)) + n] = (short) HalfIndexer.fromFloat(h[offset + n]);
        }
        return this;
    }

    public void release() {
        this.array = null;
    }
}
