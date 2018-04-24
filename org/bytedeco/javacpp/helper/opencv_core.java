package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_core$CvGraphScanner;
import org.bytedeco.javacpp.opencv_core$CvGraphVtx;
import org.bytedeco.javacpp.opencv_core$CvMemStorage;
import org.bytedeco.javacpp.opencv_core$CvPoint2D64f;
import org.bytedeco.javacpp.opencv_core$CvPoint3D64f;
import org.bytedeco.javacpp.opencv_core$IplROI;
import org.bytedeco.javacpp.opencv_core.CvBox2D;
import org.bytedeco.javacpp.opencv_core.CvFileStorage;
import org.bytedeco.javacpp.opencv_core.CvGraph;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvMatND;
import org.bytedeco.javacpp.opencv_core.CvNArrayIterator;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvPoint3D32f;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.CvSet;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.CvSize2D32f;
import org.bytedeco.javacpp.opencv_core.CvSparseMat;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;

public class opencv_core extends org.bytedeco.javacpp.presets.opencv_core {

    public static abstract class AbstractCvBox2D extends FloatPointer {
        public abstract float angle();

        public abstract CvBox2D angle(float f);

        public abstract CvBox2D center(CvPoint2D32f cvPoint2D32f);

        public abstract CvPoint2D32f center();

        public abstract CvBox2D size(CvSize2D32f cvSize2D32f);

        public abstract CvSize2D32f size();

        static {
            Loader.load();
        }

        public AbstractCvBox2D(Pointer p) {
            super(p);
        }

        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + center() + ", " + size() + ", " + angle() + ")";
            }
            String s = "";
            long p = position();
            long i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + center() + ", " + size() + ", " + angle() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static abstract class AbstractCvFileStorage extends Pointer {

        protected static class ReleaseDeallocator extends CvFileStorage implements Pointer$Deallocator {
            ReleaseDeallocator(CvFileStorage p) {
                super(p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_core.cvReleaseFileStorage(this);
            }
        }

        public AbstractCvFileStorage(Pointer p) {
            super(p);
        }

        public static CvFileStorage open(String filename, opencv_core$CvMemStorage memstorage, int flags) {
            return open(filename, memstorage, flags, null);
        }

        public static CvFileStorage open(String filename, opencv_core$CvMemStorage memstorage, int flags, String encoding) {
            CvFileStorage f = org.bytedeco.javacpp.opencv_core.cvOpenFileStorage(filename, memstorage, flags, encoding);
            if (f != null) {
                f.deallocator(new ReleaseDeallocator(f));
            }
            return f;
        }

        public void release() {
            deallocate();
        }
    }

    public static abstract class AbstractCvFont extends Pointer {
        public AbstractCvFont(Pointer p) {
            super(p);
        }
    }

    public static abstract class AbstractCvGraph extends CvSet {
        public AbstractCvGraph(Pointer p) {
            super(p);
        }

        public static CvGraph create(int graph_flags, int header_size, int vtx_size, int edge_size, opencv_core$CvMemStorage storage) {
            return org.bytedeco.javacpp.opencv_core.cvCreateGraph(graph_flags, header_size, vtx_size, edge_size, storage);
        }
    }

    public static abstract class AbstractCvGraphScanner extends Pointer {

        protected static class ReleaseDeallocator extends opencv_core$CvGraphScanner implements Pointer$Deallocator {
            ReleaseDeallocator(opencv_core$CvGraphScanner p) {
                super((Pointer) p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_core.cvReleaseGraphScanner(this);
            }
        }

        public AbstractCvGraphScanner(Pointer p) {
            super(p);
        }

        public static opencv_core$CvGraphScanner create(CvGraph graph, opencv_core$CvGraphVtx vtx, int mask) {
            opencv_core$CvGraphScanner g = org.bytedeco.javacpp.opencv_core.cvCreateGraphScanner(graph, vtx, mask);
            if (g != null) {
                g.deallocator(new ReleaseDeallocator(g));
            }
            return g;
        }

        public void release() {
            deallocate();
        }
    }

    public static abstract class AbstractCvMatND extends CvArr {

        protected static class ReleaseDeallocator extends CvMatND implements Pointer$Deallocator {
            ReleaseDeallocator(CvMatND p) {
                super(p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_core.cvReleaseMatND(this);
            }
        }

        public AbstractCvMatND(Pointer p) {
            super(p);
        }

        public static CvMatND create(int dims, int[] sizes, int type) {
            CvMatND m = org.bytedeco.javacpp.opencv_core.cvCreateMatND(dims, sizes, type);
            if (m != null) {
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        public CvMatND clone() {
            CvMatND m = org.bytedeco.javacpp.opencv_core.cvCloneMatND((CvMatND) this);
            if (m != null) {
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        public void release() {
            deallocate();
        }
    }

    public static abstract class AbstractCvMemStorage extends Pointer {

        protected static class ReleaseDeallocator extends opencv_core$CvMemStorage implements Pointer$Deallocator {
            ReleaseDeallocator(opencv_core$CvMemStorage p) {
                super((Pointer) p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_core.cvReleaseMemStorage(this);
            }
        }

        static {
            Loader.load();
        }

        public AbstractCvMemStorage(Pointer p) {
            super(p);
        }

        public static opencv_core$CvMemStorage create(int block_size) {
            opencv_core$CvMemStorage m = org.bytedeco.javacpp.opencv_core.cvCreateMemStorage(block_size);
            if (m != null) {
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        public static opencv_core$CvMemStorage create() {
            return create(0);
        }

        public void release() {
            deallocate();
        }
    }

    public static abstract class AbstractCvPoint2D32f extends FloatPointer {
        public abstract float m202x();

        public abstract CvPoint2D32f m203x(float f);

        public abstract float m204y();

        public abstract CvPoint2D32f m205y(float f);

        static {
            Loader.load();
        }

        public AbstractCvPoint2D32f(Pointer p) {
            super(p);
        }

        public CvPoint2D32f get(double[] pts) {
            return get(pts, 0, pts.length);
        }

        public CvPoint2D32f get(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position((long) i);
                pts[(i * 2) + offset] = (double) m202x();
                pts[((i * 2) + offset) + 1] = (double) m204y();
            }
            return (CvPoint2D32f) position(0);
        }

        public final CvPoint2D32f put(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position((long) i);
                put(pts[(i * 2) + offset], pts[((i * 2) + offset) + 1]);
            }
            return (CvPoint2D32f) position(0);
        }

        public final CvPoint2D32f put(double... pts) {
            return put(pts, 0, pts.length);
        }

        public CvPoint2D32f put(double x, double y) {
            return m203x((float) x).y((float) y);
        }

        public CvPoint2D32f put(CvPoint o) {
            return m203x((float) o.x()).y((float) o.y());
        }

        public CvPoint2D32f put(CvPoint2D32f o) {
            return m203x(o.x()).y(o.y());
        }

        public CvPoint2D32f put(opencv_core$CvPoint2D64f o) {
            return m203x((float) o.mo4835x()).y((float) o.mo4837y());
        }

        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + m202x() + ", " + m204y() + ")";
            }
            String s = "";
            long p = position();
            long i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + m202x() + ", " + m204y() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static abstract class AbstractCvPoint2D64f extends DoublePointer {
        public abstract double mo4835x();

        public abstract opencv_core$CvPoint2D64f mo4836x(double d);

        public abstract double mo4837y();

        public abstract opencv_core$CvPoint2D64f mo4838y(double d);

        static {
            Loader.load();
        }

        public AbstractCvPoint2D64f(Pointer p) {
            super(p);
        }

        public opencv_core$CvPoint2D64f get(double[] pts) {
            return get(pts, 0, pts.length);
        }

        public opencv_core$CvPoint2D64f get(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position((long) i);
                pts[(i * 2) + offset] = mo4835x();
                pts[((i * 2) + offset) + 1] = mo4837y();
            }
            return (opencv_core$CvPoint2D64f) position(0);
        }

        public final opencv_core$CvPoint2D64f put(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position((long) i);
                put(pts[(i * 2) + offset], pts[((i * 2) + offset) + 1]);
            }
            return (opencv_core$CvPoint2D64f) position(0);
        }

        public final opencv_core$CvPoint2D64f put(double... pts) {
            return put(pts, 0, pts.length);
        }

        public opencv_core$CvPoint2D64f put(double x, double y) {
            return mo4836x(x).mo4838y(y);
        }

        public opencv_core$CvPoint2D64f put(CvPoint o) {
            return mo4836x((double) o.x()).mo4838y((double) o.y());
        }

        public opencv_core$CvPoint2D64f put(CvPoint2D32f o) {
            return mo4836x((double) o.x()).mo4838y((double) o.y());
        }

        public opencv_core$CvPoint2D64f put(opencv_core$CvPoint2D64f o) {
            return mo4836x(o.mo4835x()).mo4838y(o.mo4837y());
        }

        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + ((float) mo4835x()) + ", " + ((float) mo4837y()) + ")";
            }
            String s = "";
            long p = position();
            long i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + ((float) mo4835x()) + ", " + ((float) mo4837y()) + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static abstract class AbstractCvPoint3D32f extends FloatPointer {
        public abstract float m210x();

        public abstract CvPoint3D32f m211x(float f);

        public abstract float m212y();

        public abstract CvPoint3D32f m213y(float f);

        public abstract float m214z();

        public abstract CvPoint3D32f m215z(float f);

        static {
            Loader.load();
        }

        public AbstractCvPoint3D32f(Pointer p) {
            super(p);
        }

        public CvPoint3D32f get(double[] pts) {
            return get(pts, 0, pts.length);
        }

        public CvPoint3D32f get(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 3; i++) {
                position((long) i);
                pts[(i * 3) + offset] = (double) m210x();
                pts[((i * 3) + offset) + 1] = (double) m212y();
                pts[((i * 3) + offset) + 2] = (double) m214z();
            }
            return (CvPoint3D32f) position(0);
        }

        public final CvPoint3D32f put(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 3; i++) {
                position((long) i);
                put(pts[(i * 3) + offset], pts[((i * 3) + offset) + 1], pts[((i * 3) + offset) + 2]);
            }
            return (CvPoint3D32f) position(0);
        }

        public final CvPoint3D32f put(double... pts) {
            return put(pts, 0, pts.length);
        }

        public CvPoint3D32f put(double x, double y, double z) {
            return m211x((float) x).y((float) y).z((float) z);
        }

        public CvPoint3D32f put(CvPoint o) {
            return m211x((float) o.x()).y((float) o.y()).z(0.0f);
        }

        public CvPoint3D32f put(CvPoint2D32f o) {
            return m211x(o.x()).y(o.y()).z(0.0f);
        }

        public CvPoint3D32f put(opencv_core$CvPoint2D64f o) {
            return m211x((float) o.mo4835x()).y((float) o.mo4837y()).z(0.0f);
        }

        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + m210x() + ", " + m212y() + ", " + m214z() + ")";
            }
            String s = "";
            long p = position();
            long i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + m210x() + ", " + m212y() + ", " + m214z() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static abstract class AbstractCvPoint3D64f extends DoublePointer {
        public abstract double mo4839x();

        public abstract opencv_core$CvPoint3D64f mo4840x(double d);

        public abstract double mo4841y();

        public abstract opencv_core$CvPoint3D64f mo4842y(double d);

        public abstract double mo4843z();

        public abstract opencv_core$CvPoint3D64f mo4844z(double d);

        static {
            Loader.load();
        }

        public AbstractCvPoint3D64f(Pointer p) {
            super(p);
        }

        public opencv_core$CvPoint3D64f get(double[] pts) {
            return get(pts, 0, pts.length);
        }

        public opencv_core$CvPoint3D64f get(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 3; i++) {
                position((long) i);
                pts[(i * 3) + offset] = mo4839x();
                pts[((i * 3) + offset) + 1] = mo4841y();
                pts[((i * 3) + offset) + 2] = mo4843z();
            }
            return (opencv_core$CvPoint3D64f) position(0);
        }

        public final opencv_core$CvPoint3D64f put(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 3; i++) {
                position((long) i);
                put(pts[(i * 3) + offset], pts[((i * 3) + offset) + 1], pts[((i * 3) + offset) + 2]);
            }
            return (opencv_core$CvPoint3D64f) position(0);
        }

        public final opencv_core$CvPoint3D64f put(double... pts) {
            return put(pts, 0, pts.length);
        }

        public opencv_core$CvPoint3D64f put(double x, double y, double z) {
            return mo4840x(mo4839x()).mo4842y(mo4841y()).mo4844z(mo4843z());
        }

        public opencv_core$CvPoint3D64f put(CvPoint o) {
            return mo4840x((double) o.x()).mo4842y((double) o.y()).mo4844z(0.0d);
        }

        public opencv_core$CvPoint3D64f put(CvPoint2D32f o) {
            return mo4840x((double) o.x()).mo4842y((double) o.y()).mo4844z(0.0d);
        }

        public opencv_core$CvPoint3D64f put(opencv_core$CvPoint2D64f o) {
            return mo4840x(o.mo4835x()).mo4842y(o.mo4837y()).mo4844z(0.0d);
        }

        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + ((float) mo4839x()) + ", " + ((float) mo4841y()) + ", " + ((float) mo4843z()) + ")";
            }
            String s = "";
            long p = position();
            long i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + ((float) mo4839x()) + ", " + ((float) mo4841y()) + ", " + ((float) mo4843z()) + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static abstract class AbstractCvPoint extends IntPointer {
        public static final CvPoint ZERO = new CvPoint().x(0).y(0);

        public abstract int m222x();

        public abstract CvPoint m223x(int i);

        public abstract int m224y();

        public abstract CvPoint m225y(int i);

        static {
            Loader.load();
        }

        public AbstractCvPoint(Pointer p) {
            super(p);
        }

        public CvPoint get(int[] pts) {
            return get(pts, 0, pts.length);
        }

        public CvPoint get(int[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position((long) i);
                pts[(i * 2) + offset] = m222x();
                pts[((i * 2) + offset) + 1] = m224y();
            }
            return (CvPoint) position(0);
        }

        public final CvPoint put(int[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position((long) i);
                put(pts[(i * 2) + offset], pts[((i * 2) + offset) + 1]);
            }
            return (CvPoint) position(0);
        }

        public final CvPoint put(int... pts) {
            return put(pts, 0, pts.length);
        }

        public final CvPoint put(byte shift, double[] pts, int offset, int length) {
            int[] a = new int[length];
            for (int i = 0; i < length; i++) {
                a[i] = (int) Math.round(pts[offset + i] * ((double) (1 << shift)));
            }
            return put(a, 0, length);
        }

        public final CvPoint put(byte shift, double... pts) {
            return put(shift, pts, 0, pts.length);
        }

        public CvPoint put(int x, int y) {
            return m223x(x).y(y);
        }

        public CvPoint put(CvPoint o) {
            return m223x(o.x()).y(o.y());
        }

        public CvPoint put(byte shift, CvPoint2D32f o) {
            m223x(Math.round(o.x() * ((float) (1 << shift))));
            m225y(Math.round(o.y() * ((float) (1 << shift))));
            return (CvPoint) this;
        }

        public CvPoint put(byte shift, opencv_core$CvPoint2D64f o) {
            m223x((int) Math.round(o.mo4835x() * ((double) (1 << shift))));
            m225y((int) Math.round(o.mo4837y() * ((double) (1 << shift))));
            return (CvPoint) this;
        }

        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + m222x() + ", " + m224y() + ")";
            }
            String s = "";
            long p = position();
            long i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + m222x() + ", " + m224y() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static abstract class AbstractCvRect extends IntPointer {
        public abstract int height();

        public abstract int width();

        public abstract int m226x();

        public abstract int m227y();

        static {
            Loader.load();
        }

        public AbstractCvRect(Pointer p) {
            super(p);
        }

        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + m226x() + ", " + m227y() + "; " + width() + ", " + height() + ")";
            }
            String s = "";
            long p = position();
            long i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + m226x() + ", " + m227y() + "; " + width() + ", " + height() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static abstract class AbstractCvScalar extends DoublePointer {
        public static final CvScalar ALPHA1 = new CvScalar().val(0, 0.0d).val(1, 0.0d).val(2, 0.0d).val(3, 1.0d);
        public static final CvScalar ALPHA255 = new CvScalar().val(0, 0.0d).val(1, 0.0d).val(2, 0.0d).val(3, 255.0d);
        public static final CvScalar BLACK = opencv_core.CV_RGB(0.0d, 0.0d, 0.0d);
        public static final CvScalar BLUE = opencv_core.CV_RGB(0.0d, 0.0d, 255.0d);
        public static final CvScalar CYAN = opencv_core.CV_RGB(0.0d, 255.0d, 255.0d);
        public static final CvScalar GRAY = opencv_core.CV_RGB(128.0d, 128.0d, 128.0d);
        public static final CvScalar GREEN = opencv_core.CV_RGB(0.0d, 255.0d, 0.0d);
        public static final CvScalar MAGENTA = opencv_core.CV_RGB(255.0d, 0.0d, 255.0d);
        public static final CvScalar ONE = new CvScalar().val(0, 1.0d).val(1, 1.0d).val(2, 1.0d).val(3, 1.0d);
        public static final CvScalar ONEHALF = new CvScalar().val(0, 0.5d).val(1, 0.5d).val(2, 0.5d).val(3, 0.5d);
        public static final CvScalar RED = opencv_core.CV_RGB(255.0d, 0.0d, 0.0d);
        public static final CvScalar WHITE = opencv_core.CV_RGB(255.0d, 255.0d, 255.0d);
        public static final CvScalar YELLOW = opencv_core.CV_RGB(255.0d, 255.0d, 0.0d);
        public static final CvScalar ZERO = new CvScalar().val(0, 0.0d).val(1, 0.0d).val(2, 0.0d).val(3, 0.0d);

        public abstract double val(int i);

        public abstract DoublePointer val();

        public abstract CvScalar val(int i, double d);

        static {
            Loader.load();
        }

        public AbstractCvScalar(Pointer p) {
            super(p);
        }

        public double getVal(int i) {
            return val(i);
        }

        public CvScalar setVal(int i, double val) {
            return val(i, val);
        }

        public DoublePointer getDoublePointerVal() {
            return val();
        }

        public LongPointer getLongPointerVal() {
            return new LongPointer(val());
        }

        public void scale(double s) {
            for (int i = 0; i < 4; i++) {
                val(i, val(i) * s);
            }
        }

        public double red() {
            return val(2);
        }

        public double green() {
            return val(1);
        }

        public double blue() {
            return val(0);
        }

        public CvScalar red(double r) {
            val(2, r);
            return (CvScalar) this;
        }

        public CvScalar green(double g) {
            val(1, g);
            return (CvScalar) this;
        }

        public CvScalar blue(double b) {
            val(0, b);
            return (CvScalar) this;
        }

        public double magnitude() {
            return Math.sqrt((((val(0) * val(0)) + (val(1) * val(1))) + (val(2) * val(2))) + (val(3) * val(3)));
        }

        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + ((float) val(0)) + ", " + ((float) val(1)) + ", " + ((float) val(2)) + ", " + ((float) val(3)) + ")";
            }
            String s = "";
            long p = position();
            long i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + ((float) val(0)) + ", " + ((float) val(1)) + ", " + ((float) val(2)) + ", " + ((float) val(3)) + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static abstract class AbstractCvSeq extends CvArr {
        public AbstractCvSeq(Pointer p) {
            super(p);
        }

        public static CvSeq create(int seq_flags, int header_size, int elem_size, opencv_core$CvMemStorage storage) {
            return org.bytedeco.javacpp.opencv_core.cvCreateSeq(seq_flags, (long) header_size, (long) elem_size, storage);
        }
    }

    public static abstract class AbstractCvSet extends CvSeq {
        public AbstractCvSet(Pointer p) {
            super(p);
        }

        public static CvSet create(int set_flags, int header_size, int elem_size, opencv_core$CvMemStorage storage) {
            return org.bytedeco.javacpp.opencv_core.cvCreateSet(set_flags, header_size, elem_size, storage);
        }
    }

    public static abstract class AbstractCvSize2D32f extends FloatPointer {
        public abstract float height();

        public abstract CvSize2D32f height(float f);

        public abstract float width();

        public abstract CvSize2D32f width(float f);

        static {
            Loader.load();
        }

        public AbstractCvSize2D32f(Pointer p) {
            super(p);
        }

        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + width() + ", " + height() + ")";
            }
            String s = "";
            long p = position();
            long i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + width() + ", " + height() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static abstract class AbstractCvSize extends IntPointer {
        public static final CvSize ZERO = new CvSize().width(0).height(0);

        public abstract int height();

        public abstract CvSize height(int i);

        public abstract int width();

        public abstract CvSize width(int i);

        static {
            Loader.load();
        }

        public AbstractCvSize(Pointer p) {
            super(p);
        }

        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + width() + ", " + height() + ")";
            }
            String s = "";
            long p = position();
            long i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + width() + ", " + height() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static abstract class AbstractCvSparseMat extends CvArr {

        protected static class ReleaseDeallocator extends CvSparseMat implements Pointer$Deallocator {
            ReleaseDeallocator(CvSparseMat p) {
                super(p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_core.cvReleaseSparseMat(this);
            }
        }

        public AbstractCvSparseMat(Pointer p) {
            super(p);
        }

        public static CvSparseMat create(int dims, int[] sizes, int type) {
            CvSparseMat m = org.bytedeco.javacpp.opencv_core.cvCreateSparseMat(dims, sizes, type);
            if (m != null) {
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        public CvSparseMat clone() {
            CvSparseMat m = org.bytedeco.javacpp.opencv_core.cvCloneSparseMat((CvSparseMat) this);
            if (m != null) {
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        public void release() {
            deallocate();
        }
    }

    public static abstract class AbstractIplImage extends CvArr {

        protected static class HeaderReleaseDeallocator extends IplImage implements Pointer$Deallocator {
            HeaderReleaseDeallocator(IplImage p) {
                super(p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_core.cvReleaseImageHeader(this);
            }
        }

        protected static class ReleaseDeallocator extends IplImage implements Pointer$Deallocator {
            ReleaseDeallocator(IplImage p) {
                super(p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_core.cvReleaseImage(this);
            }
        }

        public abstract int depth();

        public abstract int height();

        public abstract BytePointer imageData();

        public abstract int imageSize();

        public abstract int nChannels();

        public abstract int origin();

        public abstract IplImage origin(int i);

        public abstract opencv_core$IplROI roi();

        public abstract int width();

        public abstract int widthStep();

        public AbstractIplImage(Pointer p) {
            super(p);
        }

        public static IplImage create(CvSize size, int depth, int channels) {
            IplImage i = org.bytedeco.javacpp.opencv_core.cvCreateImage(size, depth, channels);
            if (i != null) {
                i.deallocator(new ReleaseDeallocator(i));
            }
            return i;
        }

        public static IplImage create(int width, int height, int depth, int channels) {
            return create(org.bytedeco.javacpp.opencv_core.cvSize(width, height), depth, channels);
        }

        public static IplImage create(CvSize size, int depth, int channels, int origin) {
            IplImage i = create(size, depth, channels);
            if (i != null) {
                i.origin(origin);
            }
            return i;
        }

        public static IplImage create(int width, int height, int depth, int channels, int origin) {
            IplImage i = create(width, height, depth, channels);
            if (i != null) {
                i.origin(origin);
            }
            return i;
        }

        public static IplImage createHeader(CvSize size, int depth, int channels) {
            IplImage i = org.bytedeco.javacpp.opencv_core.cvCreateImageHeader(size, depth, channels);
            if (i != null) {
                i.deallocator(new HeaderReleaseDeallocator(i));
            }
            return i;
        }

        public static IplImage createHeader(int width, int height, int depth, int channels) {
            return createHeader(org.bytedeco.javacpp.opencv_core.cvSize(width, height), depth, channels);
        }

        public static IplImage createHeader(CvSize size, int depth, int channels, int origin) {
            IplImage i = createHeader(size, depth, channels);
            if (i != null) {
                i.origin(origin);
            }
            return i;
        }

        public static IplImage createHeader(int width, int height, int depth, int channels, int origin) {
            IplImage i = createHeader(width, height, depth, channels);
            if (i != null) {
                i.origin(origin);
            }
            return i;
        }

        public static IplImage createCompatible(IplImage template) {
            return createIfNotCompatible(null, template);
        }

        public static IplImage createIfNotCompatible(IplImage image, IplImage template) {
            if (!(image != null && image.width() == template.width() && image.height() == template.height() && image.depth() == template.depth() && image.nChannels() == template.nChannels())) {
                image = create(template.width(), template.height(), template.depth(), template.nChannels(), template.origin());
            }
            image.origin(template.origin());
            return image;
        }

        public IplImage clone() {
            IplImage i = org.bytedeco.javacpp.opencv_core.cvCloneImage((IplImage) this);
            if (i != null) {
                i.deallocator(new ReleaseDeallocator(i));
            }
            return i;
        }

        public void release() {
            deallocate();
        }

        public int arrayChannels() {
            return nChannels();
        }

        public int arrayDepth() {
            return depth();
        }

        public int arrayOrigin() {
            return origin();
        }

        public void arrayOrigin(int origin) {
            origin(origin);
        }

        public int arrayWidth() {
            return width();
        }

        public int arrayHeight() {
            return height();
        }

        public opencv_core$IplROI arrayROI() {
            return roi();
        }

        public int arraySize() {
            return imageSize();
        }

        public BytePointer arrayData() {
            return imageData();
        }

        public int arrayStep() {
            return widthStep();
        }

        public CvMat asCvMat() {
            CvMat mat = new CvMat();
            org.bytedeco.javacpp.opencv_core.cvGetMat(this, mat, (IntPointer) null, 0);
            return mat;
        }
    }

    public static abstract class AbstractMat extends AbstractArray {
        static final /* synthetic */ boolean $assertionsDisabled;
        public static final Mat EMPTY = null;

        public abstract int channels();

        public abstract int cols();

        public abstract void create(int i, int i2, int i3);

        public abstract BytePointer data();

        public abstract int depth();

        public abstract void release();

        public abstract int rows();

        public abstract int size(int i);

        public abstract int step(int i);

        public abstract int type();

        static {
            boolean z;
            if (opencv_core.class.desiredAssertionStatus()) {
                z = false;
            } else {
                z = true;
            }
            $assertionsDisabled = z;
        }

        public AbstractMat(Pointer p) {
            super(p);
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
                    return org.bytedeco.javacpp.opencv_core.IPL_DEPTH_16S;
                case 4:
                    return org.bytedeco.javacpp.opencv_core.IPL_DEPTH_32S;
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

        public opencv_core$IplROI arrayROI() {
            return null;
        }

        public int arraySize() {
            return step(0) * size(0);
        }

        public BytePointer arrayData() {
            return data();
        }

        public int arrayStep() {
            return step(0);
        }
    }

    public static abstract class AbstractScalar extends DoublePointer {
        public static final Scalar ALPHA1 = new Scalar(0.0d, 0.0d, 0.0d, 1.0d);
        public static final Scalar ALPHA255 = new Scalar(0.0d, 0.0d, 0.0d, 255.0d);
        public static final Scalar BLACK = opencv_core.RGB(0.0d, 0.0d, 0.0d);
        public static final Scalar BLUE = opencv_core.RGB(0.0d, 0.0d, 255.0d);
        public static final Scalar CYAN = opencv_core.RGB(0.0d, 255.0d, 255.0d);
        public static final Scalar GRAY = opencv_core.RGB(128.0d, 128.0d, 128.0d);
        public static final Scalar GREEN = opencv_core.RGB(0.0d, 255.0d, 0.0d);
        public static final Scalar MAGENTA = opencv_core.RGB(255.0d, 0.0d, 255.0d);
        public static final Scalar ONE = new Scalar(1.0d, 1.0d, 1.0d, 1.0d);
        public static final Scalar ONEHALF = new Scalar(0.5d, 0.5d, 0.5d, 0.5d);
        public static final Scalar RED = opencv_core.RGB(255.0d, 0.0d, 0.0d);
        public static final Scalar WHITE = opencv_core.RGB(255.0d, 255.0d, 255.0d);
        public static final Scalar YELLOW = opencv_core.RGB(255.0d, 255.0d, 0.0d);
        public static final Scalar ZERO = new Scalar(0.0d, 0.0d, 0.0d, 0.0d);

        static {
            Loader.load();
        }

        public AbstractScalar(Pointer p) {
            super(p);
        }

        public void scale(double s) {
            for (int i = 0; i < 4; i++) {
                put((long) i, get((long) i) * s);
            }
        }

        public double red() {
            return get(2);
        }

        public double green() {
            return get(1);
        }

        public double blue() {
            return get(0);
        }

        public Scalar red(double r) {
            put(2, r);
            return (Scalar) this;
        }

        public Scalar green(double g) {
            put(1, g);
            return (Scalar) this;
        }

        public Scalar blue(double b) {
            put(0, b);
            return (Scalar) this;
        }

        public double magnitude() {
            return Math.sqrt((((get(0) * get(0)) + (get(1) * get(1))) + (get(2) * get(2))) + (get(3) * get(3)));
        }

        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + ((float) get(0)) + ", " + ((float) get(1)) + ", " + ((float) get(2)) + ", " + ((float) get(3)) + ")";
            }
            String s = "";
            long p = position();
            long i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + ((float) get(0)) + ", " + ((float) get(1)) + ", " + ((float) get(2)) + ", " + ((float) get(3)) + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static CvScalar CV_RGB(double r, double g, double b) {
        return org.bytedeco.javacpp.opencv_core.cvScalar(b, g, r, 0.0d);
    }

    public static int cvInitNArrayIterator(int count, CvArr[] arrs, CvArr mask, CvMatND stubs, CvNArrayIterator array_iterator, int flags) {
        return org.bytedeco.javacpp.opencv_core.cvInitNArrayIterator(count, new CvArrArray(arrs), mask, stubs, array_iterator, flags);
    }

    public static void cvMixChannels(CvArr[] src, int src_count, CvArr[] dst, int dst_count, int[] from_to, int pair_count) {
        org.bytedeco.javacpp.opencv_core.cvMixChannels(new CvArrArray(src), src_count, new CvArrArray(dst), dst_count, new IntPointer(from_to), pair_count);
    }

    public static void cvCalcCovarMatrix(CvArr[] vects, int count, CvArr cov_mat, CvArr avg, int flags) {
        org.bytedeco.javacpp.opencv_core.cvCalcCovarMatrix(new CvArrArray(vects), count, cov_mat, avg, flags);
    }

    public static double cvNorm(CvArr arr1, CvArr arr2) {
        return org.bytedeco.javacpp.opencv_core.cvNorm(arr1, arr2, 4, null);
    }

    public static Scalar RGB(double r, double g, double b) {
        return new Scalar(b, g, r, 0.0d);
    }
}
