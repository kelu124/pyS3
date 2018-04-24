package org.bytedeco.javacpp.helper;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.ShortPointer;
import org.bytedeco.javacpp.indexer.ByteIndexer;
import org.bytedeco.javacpp.indexer.DoubleIndexer;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacpp.indexer.Indexable;
import org.bytedeco.javacpp.indexer.Indexer;
import org.bytedeco.javacpp.indexer.IntIndexer;
import org.bytedeco.javacpp.indexer.ShortIndexer;
import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.indexer.UShortIndexer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplROI;

public abstract class opencv_core$AbstractArray extends Pointer implements Indexable {
    static final /* synthetic */ boolean $assertionsDisabled = (!opencv_core.class.desiredAssertionStatus());

    public abstract int arrayChannels();

    public abstract BytePointer arrayData();

    public abstract int arrayDepth();

    public abstract int arrayHeight();

    public abstract int arrayOrigin();

    public abstract void arrayOrigin(int i);

    public abstract IplROI arrayROI();

    public abstract int arraySize();

    public abstract int arrayStep();

    public abstract int arrayWidth();

    static {
        Loader.load();
    }

    public opencv_core$AbstractArray(Pointer p) {
        super(p);
    }

    public <B extends Buffer> B createBuffer() {
        return createBuffer(0);
    }

    public <B extends Buffer> B createBuffer(int index) {
        BytePointer ptr = arrayData();
        int size = arraySize();
        switch (arrayDepth()) {
            case -2147483640:
            case 8:
                return ptr.position((long) index).capacity((long) size).asBuffer();
            case opencv_core.IPL_DEPTH_16S /*-2147483632*/:
            case 16:
                return new ShortPointer(ptr).position((long) index).capacity((long) (size / 2)).asBuffer();
            case opencv_core.IPL_DEPTH_32S /*-2147483616*/:
                return new IntPointer(ptr).position((long) index).capacity((long) (size / 4)).asBuffer();
            case 32:
                return new FloatPointer(ptr).position((long) index).capacity((long) (size / 4)).asBuffer();
            case 64:
                return new DoublePointer(ptr).position((long) index).capacity((long) (size / 8)).asBuffer();
            default:
                if ($assertionsDisabled) {
                    return null;
                }
                throw new AssertionError();
        }
    }

    public <I extends Indexer> I createIndexer() {
        return createIndexer(true);
    }

    public <I extends Indexer> I createIndexer(boolean direct) {
        BytePointer ptr = arrayData();
        int size = arraySize();
        long[] sizes = new long[]{(long) arrayHeight(), (long) arrayWidth(), (long) arrayChannels()};
        long[] strides = new long[]{(long) arrayStep(), (long) arrayChannels(), 1};
        switch (arrayDepth()) {
            case -2147483640:
                return ByteIndexer.create(ptr.capacity((long) size), sizes, strides, direct).indexable(this);
            case opencv_core.IPL_DEPTH_16S /*-2147483632*/:
                strides[0] = strides[0] / 2;
                return ShortIndexer.create(new ShortPointer(ptr).capacity((long) (size / 2)), sizes, strides, direct).indexable(this);
            case opencv_core.IPL_DEPTH_32S /*-2147483616*/:
                strides[0] = strides[0] / 4;
                return IntIndexer.create(new IntPointer(ptr).capacity((long) (size / 4)), sizes, strides, direct).indexable(this);
            case 8:
                return UByteIndexer.create(ptr.capacity((long) size), sizes, strides, direct).indexable(this);
            case 16:
                strides[0] = strides[0] / 2;
                return UShortIndexer.create(new ShortPointer(ptr).capacity((long) (size / 2)), sizes, strides, direct).indexable(this);
            case 32:
                strides[0] = strides[0] / 4;
                return FloatIndexer.create(new FloatPointer(ptr).capacity((long) (size / 4)), sizes, strides, direct).indexable(this);
            case 64:
                strides[0] = strides[0] / 8;
                return DoubleIndexer.create(new DoublePointer(ptr).capacity((long) (size / 8)), sizes, strides, direct).indexable(this);
            default:
                if ($assertionsDisabled) {
                    return null;
                }
                throw new AssertionError();
        }
    }

    public double highValue() {
        switch (arrayDepth()) {
            case -2147483640:
                return 127.0d;
            case opencv_core.IPL_DEPTH_16S /*-2147483632*/:
                return 32767.0d;
            case opencv_core.IPL_DEPTH_32S /*-2147483616*/:
                return 2.147483647E9d;
            case 1:
            case 32:
            case 64:
                return 1.0d;
            case 8:
                return 255.0d;
            case 16:
                return 65535.0d;
            default:
                if ($assertionsDisabled) {
                    return 0.0d;
                }
                throw new AssertionError();
        }
    }

    public CvSize cvSize() {
        return opencv_core.cvSize(arrayWidth(), arrayHeight());
    }

    @Deprecated
    public ByteBuffer getByteBuffer(int index) {
        return arrayData().position((long) index).capacity((long) arraySize()).asByteBuffer();
    }

    @Deprecated
    public ShortBuffer getShortBuffer(int index) {
        return getByteBuffer(index * 2).asShortBuffer();
    }

    @Deprecated
    public IntBuffer getIntBuffer(int index) {
        return getByteBuffer(index * 4).asIntBuffer();
    }

    @Deprecated
    public FloatBuffer getFloatBuffer(int index) {
        return getByteBuffer(index * 4).asFloatBuffer();
    }

    @Deprecated
    public DoubleBuffer getDoubleBuffer(int index) {
        return getByteBuffer(index * 8).asDoubleBuffer();
    }

    @Deprecated
    public ByteBuffer getByteBuffer() {
        return getByteBuffer(0);
    }

    @Deprecated
    public ShortBuffer getShortBuffer() {
        return getShortBuffer(0);
    }

    @Deprecated
    public IntBuffer getIntBuffer() {
        return getIntBuffer(0);
    }

    @Deprecated
    public FloatBuffer getFloatBuffer() {
        return getFloatBuffer(0);
    }

    @Deprecated
    public DoubleBuffer getDoubleBuffer() {
        return getDoubleBuffer(0);
    }

    public String toString() {
        if (isNull()) {
            return super.toString();
        }
        try {
            return getClass().getName() + "[width=" + arrayWidth() + ",height=" + arrayHeight() + ",depth=" + arrayDepth() + ",channels=" + arrayChannels() + "]";
        } catch (Exception e) {
            return super.toString();
        }
    }
}
