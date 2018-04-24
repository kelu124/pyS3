package org.bytedeco.javacpp.indexer;

import java.nio.Buffer;
import org.bytedeco.javacpp.Pointer;

public abstract class Indexer implements AutoCloseable {
    protected static final long[] ONE_STRIDE = new long[]{1};
    protected Indexable indexable;
    protected long[] sizes;
    protected long[] strides;

    public abstract double getDouble(long... jArr);

    public abstract Indexer putDouble(long[] jArr, double d);

    public abstract void release();

    public void close() throws Exception {
        release();
    }

    protected Indexer(long[] sizes, long[] strides) {
        this.sizes = sizes;
        this.strides = strides;
    }

    public long[] sizes() {
        return this.sizes;
    }

    public long[] strides() {
        return this.strides;
    }

    public long rows() {
        return this.sizes[0];
    }

    public long cols() {
        return this.sizes[1];
    }

    public long width() {
        return this.sizes[1];
    }

    public long height() {
        return this.sizes[0];
    }

    public long channels() {
        return this.sizes[2];
    }

    protected static final long checkIndex(long i, long size) {
        if (i >= 0 && i < size) {
            return i;
        }
        throw new IndexOutOfBoundsException(Long.toString(i));
    }

    public long index(long... indices) {
        long index = 0;
        int i = 0;
        while (i < indices.length && i < this.strides.length) {
            index += indices[i] * this.strides[i];
            i++;
        }
        return index;
    }

    public Indexable indexable() {
        return this.indexable;
    }

    public Indexer indexable(Indexable indexable) {
        this.indexable = indexable;
        return this;
    }

    public Object array() {
        return null;
    }

    public Buffer buffer() {
        return null;
    }

    public Pointer pointer() {
        return null;
    }

    public String toString() {
        long rows = this.sizes.length > 0 ? this.sizes[0] : 1;
        long cols = this.sizes.length > 1 ? this.sizes[1] : 1;
        long channels = this.sizes.length > 2 ? this.sizes[2] : 1;
        StringBuilder s = new StringBuilder(rows > 1 ? "\n[ " : "[ ");
        for (int i = 0; ((long) i) < rows; i++) {
            for (int j = 0; ((long) j) < cols; j++) {
                if (channels > 1) {
                    s.append("(");
                }
                for (int k = 0; ((long) k) < channels; k++) {
                    s.append((float) getDouble((long) i, (long) j, (long) k));
                    if (((long) k) < channels - 1) {
                        s.append(", ");
                    }
                }
                if (channels > 1) {
                    s.append(")");
                }
                if (((long) j) < cols - 1) {
                    s.append(", ");
                }
            }
            if (((long) i) < rows - 1) {
                s.append("\n  ");
            }
        }
        s.append(" ]");
        return s.toString();
    }
}
