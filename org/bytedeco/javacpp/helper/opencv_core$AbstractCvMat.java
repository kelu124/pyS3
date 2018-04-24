package org.bytedeco.javacpp.helper;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.ShortPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.IplROI;

public abstract class opencv_core$AbstractCvMat extends opencv_core$CvArr {
    static final /* synthetic */ boolean $assertionsDisabled = (!opencv_core.class.desiredAssertionStatus());
    private ByteBuffer byteBuffer = null;
    private DoubleBuffer doubleBuffer = null;
    private FloatBuffer floatBuffer = null;
    private int fullSize = 0;
    private IntBuffer intBuffer = null;
    private ShortBuffer shortBuffer = null;

    public abstract int cols();

    public abstract DoublePointer data_db();

    public abstract FloatPointer data_fl();

    public abstract IntPointer data_i();

    public abstract BytePointer data_ptr();

    public abstract ShortPointer data_s();

    public abstract int rows();

    public abstract int step();

    public abstract int type();

    public abstract CvMat type(int i);

    public opencv_core$AbstractCvMat(Pointer p) {
        super(p);
    }

    public static CvMat create(int rows, int cols, int type) {
        CvMat m = opencv_core.cvCreateMat(rows, cols, type);
        if (m != null) {
            m.fullSize = m.size();
            m.deallocator(new ReleaseDeallocator(m));
        }
        return m;
    }

    public static CvMat create(int rows, int cols, int depth, int channels) {
        return create(rows, cols, opencv_core.CV_MAKETYPE(depth, channels));
    }

    public static CvMat create(int rows, int cols) {
        return create(rows, cols, 6, 1);
    }

    public static CvMat createHeader(int rows, int cols, int type) {
        CvMat m = opencv_core.cvCreateMatHeader(rows, cols, type);
        if (m != null) {
            m.fullSize = m.size();
            m.deallocator(new ReleaseDeallocator(m));
        }
        return m;
    }

    public static CvMat createHeader(int rows, int cols, int depth, int channels) {
        return createHeader(rows, cols, opencv_core.CV_MAKETYPE(depth, channels));
    }

    public static CvMat createHeader(int rows, int cols) {
        return createHeader(rows, cols, 6, 1);
    }

    public static ThreadLocal<CvMat> createThreadLocal(int rows, int cols, int type) {
        return new 1(rows, cols, type);
    }

    public static ThreadLocal<CvMat> createThreadLocal(int rows, int cols, int depth, int channels) {
        return createThreadLocal(rows, cols, opencv_core.CV_MAKETYPE(depth, channels));
    }

    public static ThreadLocal<CvMat> createThreadLocal(int rows, int cols) {
        return createThreadLocal(rows, cols, 6, 1);
    }

    public static ThreadLocal<CvMat> createHeaderThreadLocal(int rows, int cols, int type) {
        return new 2(rows, cols, type);
    }

    public static ThreadLocal<CvMat> createHeaderThreadLocal(int rows, int cols, int depth, int channels) {
        return createHeaderThreadLocal(rows, cols, opencv_core.CV_MAKETYPE(depth, channels));
    }

    public static ThreadLocal<CvMat> createHeaderThreadLocal(int rows, int cols) {
        return createHeaderThreadLocal(rows, cols, 6, 1);
    }

    public CvMat clone() {
        CvMat m = opencv_core.cvCloneMat((CvMat) this);
        if (m != null) {
            m.deallocator(new ReleaseDeallocator(m));
        }
        return m;
    }

    public void release() {
        deallocate();
    }

    public int matType() {
        return opencv_core.CV_MAT_TYPE(type());
    }

    public void type(int depth, int cn) {
        type(opencv_core.CV_MAKETYPE(depth, cn) | opencv_core.CV_MAT_MAGIC_VAL);
    }

    public int depth() {
        return opencv_core.CV_MAT_DEPTH(type());
    }

    public int channels() {
        return opencv_core.CV_MAT_CN(type());
    }

    public int nChannels() {
        return opencv_core.CV_MAT_CN(type());
    }

    public boolean isContinuous() {
        return opencv_core.CV_IS_MAT_CONT(type()) != 0;
    }

    public int elemSize() {
        switch (depth()) {
            case 0:
            case 1:
                return 1;
            case 2:
            case 3:
                return 2;
            case 4:
            case 5:
                return 4;
            case 6:
                return 8;
            default:
                if ($assertionsDisabled) {
                    return 0;
                }
                throw new AssertionError();
        }
    }

    public int length() {
        return rows() * cols();
    }

    public int total() {
        return rows() * cols();
    }

    public boolean empty() {
        return length() == 0;
    }

    public int size() {
        int rows = rows();
        return (rows > 1 ? step() * (rows - 1) : 0) + (channels() * (cols() * elemSize()));
    }

    public int arrayChannels() {
        return channels();
    }

    public int arrayDepth() {
        switch (depth()) {
            case 0:
                return 8;
            case 1:
                return -2147483640;
            case 2:
                return 16;
            case 3:
                return opencv_core.IPL_DEPTH_16S;
            case 4:
                return opencv_core.IPL_DEPTH_32S;
            case 5:
                return 32;
            case 6:
                return 64;
            default:
                if ($assertionsDisabled) {
                    return -1;
                }
                throw new AssertionError();
        }
    }

    public int arrayOrigin() {
        return 0;
    }

    public void arrayOrigin(int origin) {
    }

    public int arrayWidth() {
        return cols();
    }

    public int arrayHeight() {
        return rows();
    }

    public IplROI arrayROI() {
        return null;
    }

    public int arraySize() {
        return size();
    }

    public BytePointer arrayData() {
        return data_ptr();
    }

    public int arrayStep() {
        return step();
    }

    @Deprecated
    public void reset() {
        this.fullSize = 0;
        this.byteBuffer = null;
        this.shortBuffer = null;
        this.intBuffer = null;
        this.floatBuffer = null;
        this.doubleBuffer = null;
    }

    private int fullSize() {
        if (this.fullSize > 0) {
            return this.fullSize;
        }
        int size = size();
        this.fullSize = size;
        return size;
    }

    @Deprecated
    public ByteBuffer getByteBuffer() {
        if (this.byteBuffer == null) {
            this.byteBuffer = data_ptr().capacity((long) fullSize()).asBuffer();
        }
        this.byteBuffer.position(0);
        return this.byteBuffer;
    }

    @Deprecated
    public ShortBuffer getShortBuffer() {
        if (this.shortBuffer == null) {
            this.shortBuffer = data_s().capacity((long) (fullSize() / 2)).asBuffer();
        }
        this.shortBuffer.position(0);
        return this.shortBuffer;
    }

    @Deprecated
    public IntBuffer getIntBuffer() {
        if (this.intBuffer == null) {
            this.intBuffer = data_i().capacity((long) (fullSize() / 4)).asBuffer();
        }
        this.intBuffer.position(0);
        return this.intBuffer;
    }

    @Deprecated
    public FloatBuffer getFloatBuffer() {
        if (this.floatBuffer == null) {
            this.floatBuffer = data_fl().capacity((long) (fullSize() / 4)).asBuffer();
        }
        this.floatBuffer.position(0);
        return this.floatBuffer;
    }

    @Deprecated
    public DoubleBuffer getDoubleBuffer() {
        if (this.doubleBuffer == null) {
            this.doubleBuffer = data_db().capacity((long) (fullSize() / 8)).asBuffer();
        }
        this.doubleBuffer.position(0);
        return this.doubleBuffer;
    }

    @Deprecated
    public double get(int i) {
        switch (depth()) {
            case 0:
                return (double) (getByteBuffer().get(i) & 255);
            case 1:
                return (double) getByteBuffer().get(i);
            case 2:
                return (double) (getShortBuffer().get(i) & 65535);
            case 3:
                return (double) getShortBuffer().get(i);
            case 4:
                return (double) getIntBuffer().get(i);
            case 5:
                return (double) getFloatBuffer().get(i);
            case 6:
                return getDoubleBuffer().get(i);
            default:
                if ($assertionsDisabled) {
                    return Double.NaN;
                }
                throw new AssertionError();
        }
    }

    @Deprecated
    public double get(int i, int j) {
        return get(((step() * i) / elemSize()) + (channels() * j));
    }

    @Deprecated
    public double get(int i, int j, int k) {
        return get((((step() * i) / elemSize()) + (channels() * j)) + k);
    }

    @Deprecated
    public synchronized CvMat get(int index, double[] vv, int offset, int length) {
        int d = depth();
        int i;
        switch (d) {
            case 0:
            case 1:
                ByteBuffer bb = getByteBuffer();
                bb.position(index);
                for (i = 0; i < length; i++) {
                    if (d == 0) {
                        vv[i + offset] = (double) (bb.get(i) & 255);
                    } else {
                        vv[i + offset] = (double) bb.get(i);
                    }
                }
                break;
            case 2:
            case 3:
                ShortBuffer sb = getShortBuffer();
                sb.position(index);
                for (i = 0; i < length; i++) {
                    if (d == 2) {
                        vv[i + offset] = (double) (sb.get() & 65535);
                    } else {
                        vv[i + offset] = (double) sb.get();
                    }
                }
                break;
            case 4:
                IntBuffer ib = getIntBuffer();
                ib.position(index);
                for (i = 0; i < length; i++) {
                    vv[i + offset] = (double) ib.get();
                }
                break;
            case 5:
                FloatBuffer fb = getFloatBuffer();
                fb.position(index);
                for (i = 0; i < length; i++) {
                    vv[i + offset] = (double) fb.get();
                }
                break;
            case 6:
                getDoubleBuffer().position(index);
                getDoubleBuffer().get(vv, offset, length);
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        return (CvMat) this;
    }

    @Deprecated
    public CvMat get(int index, double[] vv) {
        return get(index, vv, 0, vv.length);
    }

    @Deprecated
    public CvMat get(double[] vv) {
        return get(0, vv);
    }

    @Deprecated
    public double[] get() {
        double[] vv = new double[(fullSize() / elemSize())];
        get(vv);
        return vv;
    }

    @Deprecated
    public CvMat put(int i, double v) {
        switch (depth()) {
            case 0:
            case 1:
                getByteBuffer().put(i, (byte) ((int) v));
                break;
            case 2:
            case 3:
                getShortBuffer().put(i, (short) ((int) v));
                break;
            case 4:
                getIntBuffer().put(i, (int) v);
                break;
            case 5:
                getFloatBuffer().put(i, (float) v);
                break;
            case 6:
                getDoubleBuffer().put(i, v);
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        return (CvMat) this;
    }

    @Deprecated
    public CvMat put(int i, int j, double v) {
        return put(((step() * i) / elemSize()) + (channels() * j), v);
    }

    @Deprecated
    public CvMat put(int i, int j, int k, double v) {
        return put((((step() * i) / elemSize()) + (channels() * j)) + k, v);
    }

    @Deprecated
    public synchronized CvMat put(int index, double[] vv, int offset, int length) {
        int i;
        switch (depth()) {
            case 0:
            case 1:
                ByteBuffer bb = getByteBuffer();
                bb.position(index);
                for (i = 0; i < length; i++) {
                    bb.put((byte) ((int) vv[i + offset]));
                }
                break;
            case 2:
            case 3:
                ShortBuffer sb = getShortBuffer();
                sb.position(index);
                for (i = 0; i < length; i++) {
                    sb.put((short) ((int) vv[i + offset]));
                }
                break;
            case 4:
                IntBuffer ib = getIntBuffer();
                ib.position(index);
                for (i = 0; i < length; i++) {
                    ib.put((int) vv[i + offset]);
                }
                break;
            case 5:
                FloatBuffer fb = getFloatBuffer();
                fb.position(index);
                for (i = 0; i < length; i++) {
                    fb.put((float) vv[i + offset]);
                }
                break;
            case 6:
                DoubleBuffer db = getDoubleBuffer();
                db.position(index);
                db.put(vv, offset, length);
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        return (CvMat) this;
    }

    @Deprecated
    public CvMat put(int index, double... vv) {
        return put(index, vv, 0, vv.length);
    }

    @Deprecated
    public CvMat put(double... vv) {
        return put(0, vv);
    }

    public CvMat put(CvMat mat) {
        return put(0, 0, 0, mat, 0, 0, 0);
    }

    public synchronized CvMat put(int dsti, int dstj, int dstk, CvMat mat, int srci, int srcj, int srck) {
        if (rows() == mat.rows() && cols() == mat.cols() && step() == mat.step() && type() == mat.type() && dsti == 0 && dstj == 0 && dstk == 0 && srci == 0 && srcj == 0 && srck == 0) {
            getByteBuffer().clear();
            mat.getByteBuffer().clear();
            getByteBuffer().put(mat.getByteBuffer());
        } else {
            int w = Math.min(rows() - dsti, mat.rows() - srci);
            int h = Math.min(cols() - dstj, mat.cols() - srcj);
            int d = Math.min(channels() - dstk, mat.channels() - srck);
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    for (int k = 0; k < d; k++) {
                        put(i + dsti, j + dstj, k + dstk, mat.get(i + srci, j + srcj, k + srck));
                    }
                }
            }
        }
        return (CvMat) this;
    }

    public IplImage asIplImage() {
        IplImage image = new IplImage();
        opencv_core.cvGetImage(this, image);
        return image;
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int indent) {
        StringBuilder s = new StringBuilder("[ ");
        int channels = channels();
        for (int i = 0; i < rows(); i++) {
            int j;
            for (j = 0; j < cols(); j++) {
                CvScalar v = opencv_core.cvGet2D(this, i, j);
                if (channels > 1) {
                    s.append("(");
                }
                for (int k = 0; k < channels; k++) {
                    s.append((float) v.val(k));
                    if (k < channels - 1) {
                        s.append(", ");
                    }
                }
                if (channels > 1) {
                    s.append(")");
                }
                if (j < cols() - 1) {
                    s.append(", ");
                }
            }
            if (i < rows() - 1) {
                s.append("\n  ");
                for (j = 0; j < indent; j++) {
                    s.append(' ');
                }
            }
        }
        s.append(" ]");
        return s.toString();
    }
}
