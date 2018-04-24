package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.helper.opencv_core.CvArrArray;
import org.bytedeco.javacpp.helper.opencv_core.IplImageArray;
import org.bytedeco.javacpp.opencv_core$CvMemStorage;
import org.bytedeco.javacpp.opencv_core$IplConvKernel;
import org.bytedeco.javacpp.opencv_core.CvHistogram;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc.CvContourScanner;
import org.bytedeco.javacpp.opencv_imgproc.CvMoments;

public class opencv_imgproc extends org.bytedeco.javacpp.presets.opencv_imgproc {

    public static abstract class AbstractCvHistogram extends Pointer {

        static class ReleaseDeallocator extends CvHistogram implements Pointer$Deallocator {
            ReleaseDeallocator(CvHistogram p) {
                super(p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_imgproc.cvReleaseHist(this);
            }
        }

        public AbstractCvHistogram(Pointer p) {
            super(p);
        }

        public static CvHistogram create(int dims, int[] sizes, int type, float[][] ranges, int uniform) {
            CvHistogram h = opencv_imgproc.cvCreateHist(dims, sizes, type, ranges, uniform);
            if (h != null) {
                h.deallocator(new ReleaseDeallocator(h));
            }
            return h;
        }

        public void release() {
            deallocate();
        }
    }

    public static abstract class AbstractCvMoments extends Pointer {

        static class C12221 extends ThreadLocal<CvMoments> {
            C12221() {
            }

            protected CvMoments initialValue() {
                return new CvMoments();
            }
        }

        public AbstractCvMoments(Pointer p) {
            super(p);
        }

        public static ThreadLocal<CvMoments> createThreadLocal() {
            return new C12221();
        }
    }

    public static abstract class AbstractIplConvKernel extends Pointer {

        static class ReleaseDeallocator extends opencv_core$IplConvKernel implements Pointer$Deallocator {
            ReleaseDeallocator(opencv_core$IplConvKernel p) {
                super((Pointer) p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_imgproc.cvReleaseStructuringElement(this);
            }
        }

        public AbstractIplConvKernel(Pointer p) {
            super(p);
        }

        public static opencv_core$IplConvKernel create(int cols, int rows, int anchor_x, int anchor_y, int shape, int[] values) {
            opencv_core$IplConvKernel p = org.bytedeco.javacpp.opencv_imgproc.cvCreateStructuringElementEx(cols, rows, anchor_x, anchor_y, shape, values);
            if (p != null) {
                p.deallocator(new ReleaseDeallocator(p));
            }
            return p;
        }

        public void release() {
            deallocate();
        }
    }

    public static int cvFindContours(CvArr image, opencv_core$CvMemStorage storage, CvSeq first_contour, int header_size, int mode, int method) {
        return org.bytedeco.javacpp.opencv_imgproc.cvFindContours(image, storage, first_contour, header_size, mode, method, CvPoint.ZERO);
    }

    public static CvContourScanner cvStartFindContours(CvArr image, opencv_core$CvMemStorage storage, int header_size, int mode, int method) {
        return org.bytedeco.javacpp.opencv_imgproc.cvStartFindContours(image, storage, header_size, mode, method, CvPoint.ZERO);
    }

    public static CvHistogram cvCreateHist(int dims, int[] sizes, int type, float[][] ranges, int uniform) {
        return org.bytedeco.javacpp.opencv_imgproc.cvCreateHist(dims, new IntPointer(sizes), type, ranges == null ? null : new PointerPointer(ranges), uniform);
    }

    public static void cvSetHistBinRanges(CvHistogram hist, float[][] ranges, int uniform) {
        org.bytedeco.javacpp.opencv_imgproc.cvSetHistBinRanges(hist, ranges == null ? null : new PointerPointer(ranges), uniform);
    }

    public static CvHistogram cvMakeHistHeaderForArray(int dims, int[] sizes, CvHistogram hist, float[] data, float[][] ranges, int uniform) {
        return org.bytedeco.javacpp.opencv_imgproc.cvMakeHistHeaderForArray(dims, new IntPointer(sizes), hist, new FloatPointer(data), ranges == null ? null : new PointerPointer(ranges), uniform);
    }

    public static CvHistogram cvMakeHistHeaderForArray(int dims, int[] sizes, CvHistogram hist, FloatPointer data, float[][] ranges, int uniform) {
        return org.bytedeco.javacpp.opencv_imgproc.cvMakeHistHeaderForArray(dims, new IntPointer(sizes), hist, new FloatPointer((Pointer) data), ranges == null ? null : new PointerPointer(ranges), uniform);
    }

    public static void cvCalcArrHist(CvArr[] arr, CvHistogram hist, int accumulate, CvArr mask) {
        org.bytedeco.javacpp.opencv_imgproc.cvCalcArrHist(new CvArrArray(arr), hist, accumulate, mask);
    }

    public static void cvCalcHist(IplImage[] arr, CvHistogram hist, int accumulate, CvArr mask) {
        org.bytedeco.javacpp.opencv_imgproc.cvCalcHist(new IplImageArray(arr), hist, accumulate, mask);
    }

    public static void cvCalcHist(IplImageArray arr, CvHistogram hist, int accumulate, CvArr mask) {
        org.bytedeco.javacpp.opencv_imgproc.cvCalcArrHist(arr, hist, accumulate, mask);
    }

    public static void cvCalcArrBackProject(CvArr[] image, CvArr dst, CvHistogram hist) {
        org.bytedeco.javacpp.opencv_imgproc.cvCalcArrBackProject(new CvArrArray(image), dst, hist);
    }

    public static void cvCalcBackProject(IplImage[] image, CvArr dst, CvHistogram hist) {
        org.bytedeco.javacpp.opencv_imgproc.cvCalcBackProject(new IplImageArray(image), dst, hist);
    }

    public static void cvCalcBackProject(IplImageArray image, CvArr dst, CvHistogram hist) {
        org.bytedeco.javacpp.opencv_imgproc.cvCalcArrBackProject(image, dst, hist);
    }

    public static void cvCalcArrBackProjectPatch(CvArr[] image, CvArr dst, CvSize range, CvHistogram hist, int method, double factor) {
        org.bytedeco.javacpp.opencv_imgproc.cvCalcArrBackProjectPatch(new CvArrArray(image), dst, range, hist, method, factor);
    }

    public static void cvCalcBackProjectPatch(IplImage[] image, CvArr dst, CvSize range, CvHistogram hist, int method, double factor) {
        org.bytedeco.javacpp.opencv_imgproc.cvCalcBackProjectPatch(new IplImageArray(image), dst, range, hist, method, factor);
    }

    public static void cvCalcBackProjectPatch(IplImageArray image, CvArr dst, CvSize range, CvHistogram hist, int method, double factor) {
        org.bytedeco.javacpp.opencv_imgproc.cvCalcArrBackProjectPatch(image, dst, range, hist, method, factor);
    }

    public static void cvFillPoly(CvArr img, CvPoint[] pts, int[] npts, int contours, CvScalar color, int line_type, int shift) {
        org.bytedeco.javacpp.opencv_imgproc.cvFillPoly(img, new PointerPointer((Pointer[]) pts), new IntPointer(npts), contours, color, line_type, shift);
    }

    public static void cvPolyLine(CvArr img, CvPoint[] pts, int[] npts, int contours, int is_closed, CvScalar color, int thickness, int line_type, int shift) {
        org.bytedeco.javacpp.opencv_imgproc.cvPolyLine(img, new PointerPointer((Pointer[]) pts), new IntPointer(npts), contours, is_closed, color, thickness, line_type, shift);
    }

    public static void cvDrawPolyLine(CvArr img, CvPoint[] pts, int[] npts, int contours, int is_closed, CvScalar color, int thickness, int line_type, int shift) {
        cvPolyLine(img, pts, npts, contours, is_closed, color, thickness, line_type, shift);
    }

    public static void cvDrawContours(CvArr img, CvSeq contour, CvScalar external_color, CvScalar hole_color, int max_level, int thickness, int line_type) {
        org.bytedeco.javacpp.opencv_imgproc.cvDrawContours(img, contour, external_color, hole_color, max_level, thickness, line_type, CvPoint.ZERO);
    }
}
