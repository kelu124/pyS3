package org.bytedeco.javacv;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvBox2D;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvSize2D32f;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_imgproc.CvMoments;
import org.bytedeco.javacv.Parallel.Looper;

public class JavaCV {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static ThreadLocal<CvMat> A3x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> A8x8 = CvMat.createThreadLocal(8, 8);
    public static final double DBL_EPSILON = 2.220446049250313E-16d;
    public static final double FLT_EPSILON = 1.1920928955078125E-7d;
    private static ThreadLocal<CvMat> H13x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> H23x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> H3x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> M3x2 = CvMat.createThreadLocal(3, 2);
    private static ThreadLocal<CvMat> R13x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> R23x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> S2x2 = CvMat.createThreadLocal(2, 2);
    private static ThreadLocal<CvMat> S3x3 = CvMat.createThreadLocal(3, 3);
    public static final double SQRT2 = 1.4142135623730951d;
    private static ThreadLocal<CvMat> U3x2 = CvMat.createThreadLocal(3, 2);
    private static ThreadLocal<CvMat> U3x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> V2x2 = CvMat.createThreadLocal(2, 2);
    private static ThreadLocal<CvMat> V3x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> b3x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> b8x1 = CvMat.createThreadLocal(8, 1);
    private static ThreadLocal<CvMoments> moments = CvMoments.createThreadLocal();
    private static ThreadLocal<CvMat> n13x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> n23x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> n3x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> t13x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> t23x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> x8x1 = CvMat.createThreadLocal(8, 1);

    static {
        boolean z;
        if (JavaCV.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public static double distanceToLine(double x1, double y1, double x2, double y2, double x3, double y3) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double u = (((x3 - x1) * dx) + ((y3 - y1) * dy)) / ((dx * dx) + (dy * dy));
        dx = (x1 + (u * dx)) - x3;
        dy = (y1 + (u * dy)) - y3;
        return (dx * dx) + (dy * dy);
    }

    public static CvBox2D boundedRect(CvMat contour, CvBox2D box) {
        int contourLength = contour.length();
        CvMoments m = (CvMoments) moments.get();
        opencv_imgproc.cvMoments(contour, m, 0);
        double inv_m00 = 1.0d / m.m00();
        double centerX = m.m10() * inv_m00;
        double centerY = m.m01() * inv_m00;
        float[] pts = new float[8];
        CvPoint2D32f center = box.center();
        CvSize2D32f size = box.size();
        center.put(centerX, centerY);
        opencv_imgproc.cvBoxPoints(box, pts);
        float scale = Float.POSITIVE_INFINITY;
        for (int i = 0; i < 4; i++) {
            double x1 = centerX;
            double y1 = centerY;
            double x2 = (double) pts[i * 2];
            double y2 = (double) pts[(i * 2) + 1];
            for (int j = 0; j < contourLength; j++) {
                int k = (j + 1) % contourLength;
                double x3 = contour.get(j * 2);
                double y3 = contour.get((j * 2) + 1);
                double x4 = contour.get(k * 2);
                double y4 = contour.get((k * 2) + 1);
                double d = ((y4 - y3) * (x2 - x1)) - ((x4 - x3) * (y2 - y1));
                double ua = (((x4 - x3) * (y1 - y3)) - ((y4 - y3) * (x1 - x3))) / d;
                double ub = (((x2 - x1) * (y1 - y3)) - ((y2 - y1) * (x1 - x3))) / d;
                if (ub >= 0.0d && ub <= 1.0d && ua >= 0.0d && ua < ((double) scale)) {
                    scale = (float) ua;
                }
            }
        }
        size.width(size.width() * scale).height(size.height() * scale);
        return box;
    }

    public static CvRect boundingRect(double[] contour, CvRect rect, int padX, int padY, int alignX, int alignY) {
        double minX = contour[0];
        double minY = contour[1];
        double maxX = contour[0];
        double maxY = contour[1];
        for (int i = 1; i < contour.length / 2; i++) {
            double x = contour[i * 2];
            double y = contour[(i * 2) + 1];
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
        int x2 = ((int) Math.floor(Math.max((double) rect.x(), minX - ((double) padX)) / ((double) alignX))) * alignX;
        int y2 = ((int) Math.floor(Math.max((double) rect.y(), minY - ((double) padY)) / ((double) alignY))) * alignY;
        return rect.x(x2).y(y2).width(Math.max(0, (((int) Math.ceil(Math.min((double) rect.width(), ((double) padX) + maxX) / ((double) alignX))) * alignX) - x2)).height(Math.max(0, (((int) Math.ceil(Math.min((double) rect.height(), ((double) padY) + maxY) / ((double) alignY))) * alignY) - y2));
    }

    public static CvMat getPerspectiveTransform(double[] src, double[] dst, CvMat map_matrix) {
        CvMat A = (CvMat) A8x8.get();
        CvMat b = (CvMat) b8x1.get();
        CvMat x = (CvMat) x8x1.get();
        for (int i = 0; i < 4; i++) {
            A.put((i * 8) + 0, src[i * 2]);
            A.put(((i + 4) * 8) + 3, src[i * 2]);
            A.put((i * 8) + 1, src[(i * 2) + 1]);
            A.put(((i + 4) * 8) + 4, src[(i * 2) + 1]);
            A.put((i * 8) + 2, 1.0d);
            A.put(((i + 4) * 8) + 5, 1.0d);
            A.put((i * 8) + 3, 0.0d);
            A.put((i * 8) + 4, 0.0d);
            A.put((i * 8) + 5, 0.0d);
            A.put(((i + 4) * 8) + 0, 0.0d);
            A.put(((i + 4) * 8) + 1, 0.0d);
            A.put(((i + 4) * 8) + 2, 0.0d);
            A.put((i * 8) + 6, (-src[i * 2]) * dst[i * 2]);
            A.put((i * 8) + 7, (-src[(i * 2) + 1]) * dst[i * 2]);
            A.put(((i + 4) * 8) + 6, (-src[i * 2]) * dst[(i * 2) + 1]);
            A.put(((i + 4) * 8) + 7, (-src[(i * 2) + 1]) * dst[(i * 2) + 1]);
            b.put(i, dst[i * 2]);
            b.put(i + 4, dst[(i * 2) + 1]);
        }
        opencv_core.cvSolve(A, b, x, 0);
        map_matrix.put(x.get());
        map_matrix.put(8, 1.0d);
        return map_matrix;
    }

    public static void perspectiveTransform(double[] src, double[] dst, CvMat map_matrix) {
        double[] mat = map_matrix.get();
        for (int j = 0; j < src.length; j += 2) {
            double x = src[j];
            double y = src[j + 1];
            double w = ((mat[6] * x) + (mat[7] * y)) + mat[8];
            if (Math.abs(w) > FLT_EPSILON) {
                w = 1.0d / w;
                dst[j] = (((mat[0] * x) + (mat[1] * y)) + mat[2]) * w;
                dst[j + 1] = (((mat[3] * x) + (mat[4] * y)) + mat[5]) * w;
            } else {
                dst[j + 1] = 0.0d;
                dst[j] = 0.0d;
            }
        }
    }

    public static CvMat getPlaneParameters(double[] src, double[] dst, CvMat invSrcK, CvMat dstK, CvMat R, CvMat t, CvMat n) {
        CvMat A = (CvMat) A3x3.get();
        CvMat b = (CvMat) b3x1.get();
        double[] x = new double[6];
        double[] y = new double[6];
        perspectiveTransform(src, x, invSrcK);
        opencv_core.cvInvert(dstK, A);
        perspectiveTransform(dst, y, A);
        for (int i = 0; i < 3; i++) {
            A.put(i, 0, ((t.get(2) * y[i * 2]) - t.get(0)) * x[i * 2]);
            A.put(i, 1, ((t.get(2) * y[i * 2]) - t.get(0)) * x[(i * 2) + 1]);
            A.put(i, 2, (t.get(2) * y[i * 2]) - t.get(0));
            b.put(i, ((((R.get(2, 0) * x[i * 2]) + (R.get(2, 1) * x[(i * 2) + 1])) + R.get(2, 2)) * y[i * 2]) - (((R.get(0, 0) * x[i * 2]) + (R.get(0, 1) * x[(i * 2) + 1])) + R.get(0, 2)));
        }
        opencv_core.cvSolve(A, b, n, 0);
        return n;
    }

    public static CvMat getPerspectiveTransform(double[] src, double[] dst, CvMat invSrcK, CvMat dstK, CvMat R, CvMat t, CvMat H) {
        CvMat n = (CvMat) n3x1.get();
        getPlaneParameters(src, dst, invSrcK, dstK, R, t, n);
        opencv_core.cvGEMM(t, n, -1.0d, R, 1.0d, H, 2);
        opencv_core.cvMatMul(dstK, H, H);
        opencv_core.cvMatMul(H, invSrcK, H);
        return H;
    }

    public static void perspectiveTransform(double[] src, double[] dst, CvMat invSrcK, CvMat dstK, CvMat R, CvMat t, CvMat n, boolean invert) {
        CvMat H = (CvMat) H3x3.get();
        opencv_core.cvGEMM(t, n, -1.0d, R, 1.0d, H, 2);
        opencv_core.cvMatMul(dstK, H, H);
        opencv_core.cvMatMul(H, invSrcK, H);
        if (invert) {
            opencv_core.cvInvert(H, H);
        }
        perspectiveTransform(src, dst, H);
    }

    public static void HtoRt(CvMat H, CvMat R, CvMat t) {
        CvMat M = (CvMat) M3x2.get();
        CvMat S = (CvMat) S2x2.get();
        CvMat U = (CvMat) U3x2.get();
        CvMat V = (CvMat) V2x2.get();
        M.put(new double[]{H.get(0), H.get(1), H.get(3), H.get(4), H.get(6), H.get(7)});
        opencv_core.cvSVD(M, S, U, V, 4);
        double lambda = S.get(3);
        t.put(new double[]{H.get(2) / lambda, H.get(5) / lambda, H.get(8) / lambda});
        opencv_core.cvMatMul(U, V, M);
        R.put(new double[]{M.get(0), M.get(1), (M.get(2) * M.get(5)) - (M.get(3) * M.get(4)), M.get(2), M.get(3), (M.get(1) * M.get(4)) - (M.get(0) * M.get(5)), M.get(4), M.get(5), (M.get(0) * M.get(3)) - (M.get(1) * M.get(2))});
    }

    public static double HnToRt(CvMat H, CvMat n, CvMat R, CvMat t) {
        CvMat S = (CvMat) S3x3.get();
        CvMat U = (CvMat) U3x3.get();
        CvMat V = (CvMat) V3x3.get();
        opencv_core.cvSVD(H, S, U, V, 0);
        CvArr R1 = (CvMat) R13x3.get();
        CvArr R2 = (CvMat) R23x3.get();
        CvArr t1 = (CvMat) t13x1.get();
        CvArr t2 = (CvMat) t23x1.get();
        CvArr H1 = (CvMat) H13x3.get();
        CvArr H2 = (CvMat) H23x3.get();
        double zeta = homogToRt(S, U, V, R1, t1, (CvMat) n13x1.get(), R2, t2, (CvMat) n23x1.get());
        opencv_core.cvGEMM(R1, H, 1.0d / S.get(4), null, 0.0d, H1, 1);
        opencv_core.cvGEMM(R2, H, 1.0d / S.get(4), null, 0.0d, H2, 1);
        H1.put(0, H1.get(0) - 1.0d);
        H1.put(4, H1.get(4) - 1.0d);
        H1.put(8, H1.get(8) - 1.0d);
        H2.put(0, H2.get(0) - 1.0d);
        H2.put(4, H2.get(4) - 1.0d);
        H2.put(8, H2.get(8) - 1.0d);
        double d = (Math.abs(n.get(0)) + Math.abs(n.get(1))) + Math.abs(n.get(2));
        double[] s = new double[]{-Math.signum(n.get(0)), -Math.signum(n.get(1)), -Math.signum(n.get(2))};
        t1.put(new double[]{0.0d, 0.0d, 0.0d});
        t2.put(new double[]{0.0d, 0.0d, 0.0d});
        for (int i = 0; i < 3; i++) {
            t1.put(0, t1.get(0) + ((s[i] * H1.get(i)) / d));
            t1.put(1, t1.get(1) + ((s[i] * H1.get(i + 3)) / d));
            t1.put(2, t1.get(2) + ((s[i] * H1.get(i + 6)) / d));
            t2.put(0, t2.get(0) + ((s[i] * H2.get(i)) / d));
            t2.put(1, t2.get(1) + ((s[i] * H2.get(i + 3)) / d));
            t2.put(2, t2.get(2) + ((s[i] * H2.get(i + 6)) / d));
        }
        opencv_core.cvGEMM(t1, n, 1.0d, H1, 1.0d, H1, 2);
        opencv_core.cvGEMM(t2, n, 1.0d, H2, 1.0d, H2, 2);
        double err1 = opencv_core.cvNorm(H1);
        double err2 = opencv_core.cvNorm(H2);
        if (err1 < err2) {
            if (R != null) {
                R.put(R1);
            }
            if (t != null) {
                t.put(t1);
            }
            return err1;
        }
        if (R != null) {
            R.put(R2);
        }
        if (t != null) {
            t.put(t2);
        }
        return err2;
    }

    public static double homogToRt(CvMat H, CvMat R1, CvMat t1, CvMat n1, CvMat R2, CvMat t2, CvMat n2) {
        CvMat S = (CvMat) S3x3.get();
        CvMat U = (CvMat) U3x3.get();
        CvMat V = (CvMat) V3x3.get();
        opencv_core.cvSVD(H, S, U, V, 0);
        return homogToRt(S, U, V, R1, t1, n1, R2, t2, n2);
    }

    public static double homogToRt(CvMat S, CvMat U, CvMat V, CvMat R1, CvMat t1, CvMat n1, CvMat R2, CvMat t2, CvMat n2) {
        double s1 = S.get(0) / S.get(4);
        double s3 = S.get(8) / S.get(4);
        double zeta = s1 - s3;
        double a1 = Math.sqrt(1.0d - (s3 * s3));
        double b1 = Math.sqrt((s1 * s1) - 1.0d);
        double[] ab = unitize(a1, b1);
        double[] cd = unitize(1.0d + (s1 * s3), a1 * b1);
        double[] ef = unitize((-ab[1]) / s1, (-ab[0]) / s3);
        R1.put(new double[]{cd[0], 0.0d, cd[1], 0.0d, 1.0d, 0.0d, -cd[1], 0.0d, cd[0]});
        opencv_core.cvGEMM(U, R1, 1.0d, null, 0.0d, R1, 0);
        opencv_core.cvGEMM(R1, V, 1.0d, null, 0.0d, R1, 2);
        R2.put(new double[]{cd[0], 0.0d, -cd[1], 0.0d, 1.0d, 0.0d, cd[1], 0.0d, cd[0]});
        opencv_core.cvGEMM(U, R2, 1.0d, null, 0.0d, R2, 0);
        opencv_core.cvGEMM(R2, V, 1.0d, null, 0.0d, R2, 2);
        double[] v1 = new double[]{V.get(0), V.get(3), V.get(6)};
        double[] v3 = new double[]{V.get(2), V.get(5), V.get(8)};
        double sign1 = 1.0d;
        double sign2 = 1.0d;
        for (int i = 2; i >= 0; i--) {
            n1.put(i, ((ab[1] * v1[i]) - (ab[0] * v3[i])) * sign1);
            n2.put(i, ((ab[1] * v1[i]) + (ab[0] * v3[i])) * sign2);
            t1.put(i, ((ef[0] * v1[i]) + (ef[1] * v3[i])) * sign1);
            t2.put(i, ((ef[0] * v1[i]) - (ef[1] * v3[i])) * sign2);
            if (i == 2) {
                if (n1.get(2) < 0.0d) {
                    n1.put(2, -n1.get(2));
                    t1.put(2, -t1.get(2));
                    sign1 = -1.0d;
                }
                if (n2.get(2) < 0.0d) {
                    n2.put(2, -n2.get(2));
                    t2.put(2, -t2.get(2));
                    sign2 = -1.0d;
                }
            }
        }
        return zeta;
    }

    public static double[] unitize(double a, double b) {
        double norm = Math.sqrt((a * a) + (b * b));
        if (norm > FLT_EPSILON) {
            a /= norm;
            b /= norm;
        }
        return new double[]{a, b};
    }

    public static void adaptiveThreshold(IplImage srcImage, IplImage sumImage, IplImage sqSumImage, IplImage dstImage, boolean invert, int windowMax, int windowMin, double varMultiplier, double k) {
        CvArr srcImage2;
        final int w = srcImage.width();
        final int h = srcImage.height();
        int srcChannels = srcImage.nChannels();
        final int srcDepth = srcImage.depth();
        int dstDepth = dstImage.depth();
        if (srcChannels > 1 && dstDepth == 8) {
            opencv_imgproc.cvCvtColor(srcImage, dstImage, srcChannels == 4 ? 11 : 6);
            srcImage2 = dstImage;
        }
        final ByteBuffer srcBuf = srcImage2.getByteBuffer();
        final ByteBuffer dstBuf = dstImage.getByteBuffer();
        final DoubleBuffer sumBuf = sumImage.getDoubleBuffer();
        final DoubleBuffer sqSumBuf = sqSumImage.getDoubleBuffer();
        final int srcStep = srcImage2.widthStep();
        final int dstStep = dstImage.widthStep();
        final int sumStep = sumImage.widthStep();
        final int sqSumStep = sqSumImage.widthStep();
        opencv_imgproc.cvIntegral(srcImage2, sumImage, sqSumImage, null);
        double totalMean = (((sumBuf.get((((h - 1) * sumStep) / 8) + (w - 1)) - sumBuf.get(((h - 1) * sumStep) / 8)) - sumBuf.get(w - 1)) + sumBuf.get(0)) / ((double) (w * h));
        final double targetVar = (((((sqSumBuf.get((((h - 1) * sqSumStep) / 8) + (w - 1)) - sqSumBuf.get(((h - 1) * sqSumStep) / 8)) - sqSumBuf.get(w - 1)) + sqSumBuf.get(0)) / ((double) (w * h))) - (totalMean * totalMean)) * varMultiplier;
        final int i = windowMax;
        final int i2 = windowMin;
        final boolean z = invert;
        final double d = k;
        Parallel.loop(0, h, new Looper() {
            static final /* synthetic */ boolean $assertionsDisabled = (!JavaCV.class.desiredAssertionStatus());

            public void loop(int from, int to, int looperID) {
                for (int y = from; y < to; y++) {
                    for (int x = 0; x < w; x++) {
                        double mean = 0.0d;
                        int upperLimit = i;
                        int lowerLimit = i2;
                        int window = upperLimit;
                        while (upperLimit - lowerLimit > 2) {
                            int x1 = Math.max(x - (window / 2), 0);
                            int x2 = Math.min(((window / 2) + x) + 1, w);
                            int y1 = Math.max(y - (window / 2), 0);
                            int y2 = Math.min(((window / 2) + y) + 1, h);
                            mean = (((sumBuf.get(((sumStep * y2) / 8) + x2) - sumBuf.get(((sumStep * y2) / 8) + x1)) - sumBuf.get(((sumStep * y1) / 8) + x2)) + sumBuf.get(((sumStep * y1) / 8) + x1)) / ((double) (window * window));
                            double var = ((((sqSumBuf.get(((sqSumStep * y2) / 8) + x2) - sqSumBuf.get(((sqSumStep * y2) / 8) + x1)) - sqSumBuf.get(((sqSumStep * y1) / 8) + x2)) + sqSumBuf.get(((sqSumStep * y1) / 8) + x1)) / ((double) (window * window))) - (mean * mean);
                            if (window == upperLimit && var < targetVar) {
                                break;
                            }
                            if (var > targetVar) {
                                upperLimit = window;
                            } else {
                                lowerLimit = window;
                            }
                            window = (((lowerLimit + ((upperLimit - lowerLimit) / 2)) / 2) * 2) + 1;
                        }
                        double value = 0.0d;
                        if (srcDepth == 8) {
                            value = (double) (srcBuf.get((srcStep * y) + x) & 255);
                        } else if (srcDepth == 32) {
                            value = (double) srcBuf.getFloat((srcStep * y) + (x * 4));
                        } else if (srcDepth == 64) {
                            value = srcBuf.getDouble((srcStep * y) + (x * 8));
                        } else if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        if (z) {
                            byte b;
                            double threshold = 255.0d - ((255.0d - mean) * d);
                            ByteBuffer byteBuffer = dstBuf;
                            int i = (dstStep * y) + x;
                            if (value < threshold) {
                                b = (byte) -1;
                            } else {
                                b = (byte) 0;
                            }
                            byteBuffer.put(i, b);
                        } else {
                            dstBuf.put((dstStep * y) + x, value > mean * d ? (byte) -1 : (byte) 0);
                        }
                    }
                }
            }
        });
    }

    public static void hysteresisThreshold(IplImage srcImage, IplImage dstImage, double highThresh, double lowThresh, double maxValue) {
        int i;
        int highThreshold = (int) Math.round(highThresh);
        int lowThreshold = (int) Math.round(lowThresh);
        byte medValue = (byte) ((int) Math.round(maxValue / 2.0d));
        byte highValue = (byte) ((int) Math.round(maxValue));
        int height = srcImage.height();
        int width = srcImage.width();
        ByteBuffer srcData = srcImage.getByteBuffer();
        ByteBuffer dstData = dstImage.getByteBuffer();
        int srcStep = srcImage.widthStep();
        int dstStep = dstImage.widthStep();
        int srcIndex = 0;
        int dstIndex = 0;
        int in = srcData.get(0) & 255;
        if (in >= highThreshold) {
            dstData.put(0, highValue);
        } else if (in < lowThreshold) {
            dstData.put(0, (byte) 0);
        } else {
            dstData.put(0, medValue);
        }
        for (i = 1; i < width - 1; i++) {
            in = srcData.get(0 + i) & 255;
            if (in >= highThreshold) {
                dstData.put(0 + i, highValue);
            } else if (in < lowThreshold) {
                dstData.put(0 + i, (byte) 0);
            } else if (dstData.get((0 + i) - 1) == highValue) {
                dstData.put(0 + i, highValue);
            } else {
                dstData.put(0 + i, medValue);
            }
        }
        i = width - 1;
        in = srcData.get(0 + i) & 255;
        if (in >= highThreshold) {
            dstData.put(0 + i, highValue);
        } else if (in < lowThreshold) {
            dstData.put(0 + i, (byte) 0);
        } else if (dstData.get((0 + i) - 1) == highValue) {
            dstData.put(0 + i, highValue);
        } else {
            dstData.put(0 + i, medValue);
        }
        int height2 = height - 1;
        while (true) {
            height = height2 - 1;
            if (height2 <= 0) {
                break;
            }
            byte prev1;
            srcIndex += srcStep;
            dstIndex += dstStep;
            in = srcData.get(srcIndex + 0) & 255;
            if (in >= highThreshold) {
                dstData.put(dstIndex + 0, highValue);
            } else if (in < lowThreshold) {
                dstData.put(dstIndex + 0, (byte) 0);
            } else {
                prev1 = dstData.get((dstIndex + 0) - dstStep);
                byte prev2 = dstData.get(((dstIndex + 0) - dstStep) + 1);
                if (prev1 == highValue || prev2 == highValue) {
                    dstData.put(dstIndex + 0, highValue);
                } else {
                    dstData.put(dstIndex + 0, medValue);
                }
            }
            for (i = 1; i < width - 1; i++) {
                in = srcData.get(srcIndex + i) & 255;
                if (in >= highThreshold) {
                    dstData.put(dstIndex + i, highValue);
                } else if (in < lowThreshold) {
                    dstData.put(dstIndex + i, (byte) 0);
                } else {
                    prev1 = dstData.get((dstIndex + i) - 1);
                    prev2 = dstData.get(((dstIndex + i) - dstStep) - 1);
                    byte prev3 = dstData.get((dstIndex + i) - dstStep);
                    byte prev4 = dstData.get(((dstIndex + i) - dstStep) + 1);
                    if (prev1 == highValue || prev2 == highValue || prev3 == highValue || prev4 == highValue) {
                        dstData.put(dstIndex + i, highValue);
                    } else {
                        dstData.put(dstIndex + i, medValue);
                    }
                }
            }
            i = width - 1;
            in = srcData.get(srcIndex + i) & 255;
            if (in >= highThreshold) {
                dstData.put(dstIndex + i, highValue);
                height2 = height;
            } else if (in < lowThreshold) {
                dstData.put(dstIndex + i, (byte) 0);
                height2 = height;
            } else {
                prev1 = dstData.get((dstIndex + i) - 1);
                prev2 = dstData.get(((dstIndex + i) - dstStep) - 1);
                prev3 = dstData.get((dstIndex + i) - dstStep);
                if (prev1 == highValue || prev2 == highValue || prev3 == highValue) {
                    dstData.put(dstIndex + i, highValue);
                } else {
                    dstData.put(dstIndex + i, medValue);
                }
                height2 = height;
            }
        }
        height = srcImage.height();
        width = srcImage.width();
        dstIndex = (height - 1) * dstStep;
        i = width - 1;
        if (dstData.get(dstIndex + i) == medValue) {
            dstData.put(dstIndex + i, (byte) 0);
        }
        for (i = width - 2; i > 0; i--) {
            if (dstData.get(dstIndex + i) == medValue) {
                if (dstData.get((dstIndex + i) + 1) == highValue) {
                    dstData.put(dstIndex + i, highValue);
                } else {
                    dstData.put(dstIndex + i, (byte) 0);
                }
            }
        }
        if (dstData.get(dstIndex + 0) == medValue) {
            if (dstData.get((dstIndex + 0) + 1) == highValue) {
                dstData.put(dstIndex + 0, highValue);
            } else {
                dstData.put(dstIndex + 0, (byte) 0);
            }
        }
        height2 = height - 1;
        while (true) {
            height = height2 - 1;
            if (height2 > 0) {
                dstIndex -= dstStep;
                i = width - 1;
                if (dstData.get(dstIndex + i) == medValue) {
                    if (dstData.get((dstIndex + i) + dstStep) == highValue || dstData.get(((dstIndex + i) + dstStep) - 1) == highValue) {
                        dstData.put(dstIndex + i, highValue);
                    } else {
                        dstData.put(dstIndex + i, (byte) 0);
                    }
                }
                i = width - 2;
                while (i > 0) {
                    if (dstData.get(dstIndex + i) == medValue) {
                        if (dstData.get((dstIndex + i) + 1) == highValue || dstData.get(((dstIndex + i) + dstStep) + 1) == highValue || dstData.get((dstIndex + i) + dstStep) == highValue || dstData.get(((dstIndex + i) + dstStep) - 1) == highValue) {
                            dstData.put(dstIndex + i, highValue);
                        } else {
                            dstData.put(dstIndex + i, (byte) 0);
                        }
                    }
                    i--;
                }
                if (dstData.get(dstIndex + 0) != medValue) {
                    height2 = height;
                } else if (dstData.get((dstIndex + 0) + 1) == highValue || dstData.get(((dstIndex + 0) + dstStep) + 1) == highValue || dstData.get((dstIndex + 0) + dstStep) == highValue) {
                    dstData.put(dstIndex + 0, highValue);
                    height2 = height;
                } else {
                    dstData.put(dstIndex + 0, (byte) 0);
                    height2 = height;
                }
            } else {
                return;
            }
        }
    }

    public static void clamp(IplImage src, IplImage dst, double min, double max) {
        ByteBuffer sb;
        ByteBuffer db;
        int i;
        ShortBuffer sb2;
        ShortBuffer db2;
        switch (src.depth()) {
            case -2147483640:
                sb = src.getByteBuffer();
                db = dst.getByteBuffer();
                for (i = 0; i < sb.capacity(); i++) {
                    db.put(i, (byte) ((int) Math.max(Math.min((double) sb.get(i), max), min)));
                }
                return;
            case opencv_core.IPL_DEPTH_16S /*-2147483632*/:
                sb2 = src.getShortBuffer();
                db2 = dst.getShortBuffer();
                for (i = 0; i < sb2.capacity(); i++) {
                    db2.put(i, (short) ((int) Math.max(Math.min((double) sb2.get(i), max), min)));
                }
                return;
            case opencv_core.IPL_DEPTH_32S /*-2147483616*/:
                IntBuffer sb3 = src.getIntBuffer();
                IntBuffer db3 = dst.getIntBuffer();
                for (i = 0; i < sb3.capacity(); i++) {
                    db3.put(i, (int) Math.max(Math.min((double) sb3.get(i), max), min));
                }
                return;
            case 8:
                sb = src.getByteBuffer();
                db = dst.getByteBuffer();
                for (i = 0; i < sb.capacity(); i++) {
                    db.put(i, (byte) ((int) Math.max(Math.min((double) (sb.get(i) & 255), max), min)));
                }
                return;
            case 16:
                sb2 = src.getShortBuffer();
                db2 = dst.getShortBuffer();
                for (i = 0; i < sb2.capacity(); i++) {
                    db2.put(i, (short) ((int) Math.max(Math.min((double) (sb2.get(i) & 65535), max), min)));
                }
                return;
            case 32:
                FloatBuffer sb4 = src.getFloatBuffer();
                FloatBuffer db4 = dst.getFloatBuffer();
                for (i = 0; i < sb4.capacity(); i++) {
                    db4.put(i, (float) Math.max(Math.min((double) sb4.get(i), max), min));
                }
                return;
            case 64:
                DoubleBuffer sb5 = src.getDoubleBuffer();
                DoubleBuffer db5 = dst.getDoubleBuffer();
                for (i = 0; i < sb5.capacity(); i++) {
                    db5.put(i, Math.max(Math.min(sb5.get(i), max), min));
                }
                return;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                return;
        }
    }

    public static double norm(double[] v) {
        return norm(v, 2.0d);
    }

    public static double norm(double[] v, double p) {
        int i = 0;
        double norm = 0.0d;
        if (p == 1.0d) {
            while (i < v.length) {
                norm += Math.abs(v[i]);
                i++;
            }
            return norm;
        } else if (p == 2.0d) {
            r5 = v.length;
            while (i < r5) {
                e = v[i];
                norm += e * e;
                i++;
            }
            return Math.sqrt(norm);
        } else if (p == Double.POSITIVE_INFINITY) {
            r5 = v.length;
            while (i < r5) {
                e = Math.abs(v[i]);
                if (e > norm) {
                    norm = e;
                }
                i++;
            }
            return norm;
        } else if (p == Double.NEGATIVE_INFINITY) {
            norm = Double.MAX_VALUE;
            r5 = v.length;
            while (i < r5) {
                e = Math.abs(v[i]);
                if (e < norm) {
                    norm = e;
                }
                i++;
            }
            return norm;
        } else {
            while (i < v.length) {
                norm += Math.pow(Math.abs(v[i]), p);
                i++;
            }
            return Math.pow(norm, 1.0d / p);
        }
    }

    public static double norm(CvMat A) {
        return norm(A, 2.0d);
    }

    public static double norm(CvMat A, double p) {
        return norm(A, p, null);
    }

    public static double norm(CvMat A, double p, CvMat W) {
        double norm = -1.0d;
        int cols;
        int rows;
        int j;
        double n;
        int i;
        if (p == 1.0d) {
            cols = A.cols();
            rows = A.rows();
            for (j = 0; j < cols; j++) {
                n = 0.0d;
                for (i = 0; i < rows; i++) {
                    n += Math.abs(A.get(i, j));
                }
                norm = Math.max(n, norm);
            }
            return norm;
        } else if (p == 2.0d) {
            CvArr W2;
            int size = Math.min(A.rows(), A.cols());
            if (!(W != null && W.rows() == size && W.cols() == 1)) {
                W2 = CvMat.create(size, 1);
            }
            opencv_core.cvSVD(A, W2, null, null, 0);
            return W2.get(0);
        } else if (p == Double.POSITIVE_INFINITY) {
            rows = A.rows();
            cols = A.cols();
            for (i = 0; i < rows; i++) {
                n = 0.0d;
                for (j = 0; j < cols; j++) {
                    n += Math.abs(A.get(i, j));
                }
                norm = Math.max(n, norm);
            }
            return norm;
        } else if ($assertionsDisabled) {
            return -1.0d;
        } else {
            throw new AssertionError();
        }
    }

    public static double cond(CvMat A) {
        return cond(A, 2.0d);
    }

    public static double cond(CvMat A, double p) {
        return cond(A, p, null);
    }

    public static double cond(CvMat A, double p, CvMat W) {
        if (p == 2.0d) {
            int size = Math.min(A.rows(), A.cols());
            if (!(W != null && W.rows() == size && W.cols() == 1)) {
                W = CvMat.create(size, 1);
            }
            opencv_core.cvSVD(A, W, null, null, 0);
            return W.get(0) / W.get(W.length() - 1);
        }
        int rows = A.rows();
        int cols = A.cols();
        if (!(W != null && W.rows() == rows && W.cols() == cols)) {
            W = CvMat.create(rows, cols);
        }
        CvMat Ainv = W;
        opencv_core.cvInvert(A, Ainv);
        return norm(A, p) * norm(Ainv, p);
    }

    public static double median(double[] doubles) {
        double[] sorted = (double[]) doubles.clone();
        Arrays.sort(sorted);
        if (doubles.length % 2 == 0) {
            return (sorted[(doubles.length / 2) - 1] + sorted[doubles.length / 2]) / 2.0d;
        }
        return sorted[doubles.length / 2];
    }

    public static <T> T median(T[] objects) {
        Object[] sorted = (Object[]) objects.clone();
        Arrays.sort(sorted);
        return sorted[sorted.length / 2];
    }

    public static void fractalTriangleWave(double[] line, int i, int j, double a) {
        fractalTriangleWave(line, i, j, a, -1);
    }

    public static void fractalTriangleWave(double[] line, int i, int j, double a, int roughness) {
        int m = ((j - i) / 2) + i;
        if (i != j && i != m) {
            line[m] = ((line[i] + line[j]) / 2.0d) + a;
            if (roughness <= 0 || line.length <= (j - i) * roughness) {
                fractalTriangleWave(line, i, m, a / 1.4142135623730951d, roughness);
                fractalTriangleWave(line, m, j, (-a) / 1.4142135623730951d, roughness);
                return;
            }
            fractalTriangleWave(line, i, m, 0.0d, roughness);
            fractalTriangleWave(line, m, j, 0.0d, roughness);
        }
    }

    public static void fractalTriangleWave(IplImage image, CvMat H) {
        fractalTriangleWave(image, H, -1);
    }

    public static void fractalTriangleWave(IplImage image, CvMat H, int roughness) {
        if ($assertionsDisabled || image.depth() == 32) {
            double[] line = new double[image.width()];
            fractalTriangleWave(line, 0, line.length / 2, 1.0d, roughness);
            fractalTriangleWave(line, line.length / 2, line.length - 1, -1.0d, roughness);
            double[] minMax = new double[]{Double.MAX_VALUE, Double.MIN_VALUE};
            int height = image.height();
            int width = image.width();
            int channels = image.nChannels();
            int step = image.widthStep();
            int start = 0;
            if (image.roi() != null) {
                height = image.roi().height();
                width = image.roi().width();
                start = ((image.roi().yOffset() * step) / 4) + (image.roi().xOffset() * channels);
            }
            FloatBuffer fb = image.getFloatBuffer(start);
            double[] h = H == null ? null : H.get();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    for (int z = 0; z < channels; z++) {
                        double sum;
                        if (h == null) {
                            sum = 0.0d + line[x];
                        } else {
                            double x2 = (((h[0] * ((double) x)) + (h[1] * ((double) y))) + h[2]) / (((h[6] * ((double) x)) + (h[7] * ((double) y))) + h[8]);
                            while (x2 < 0.0d) {
                                x2 += (double) line.length;
                            }
                            int xi2 = (int) x2;
                            double xn = x2 - ((double) xi2);
                            sum = 0.0d + ((line[xi2 % line.length] * (1.0d - xn)) + (line[(xi2 + 1) % line.length] * xn));
                        }
                        minMax[0] = Math.min(minMax[0], sum);
                        minMax[1] = Math.max(minMax[1], sum);
                        fb.put((((y * step) / 4) + (x * channels)) + z, (float) sum);
                    }
                }
            }
            opencv_core.cvConvertScale(image, image, 1.0d / (minMax[1] - minMax[0]), (-minMax[0]) / (minMax[1] - minMax[0]));
            return;
        }
        throw new AssertionError();
    }

    public static void main(String[] args) {
        String version = JavaCV.class.getPackage().getImplementationVersion();
        if (version == null) {
            version = "unknown";
        }
        System.out.println("JavaCV version " + version + "\nCopyright (C) 2009-2016 Samuel Audet <samuel.audet@gmail.com>\nProject site: https://github.com/bytedeco/javacv");
        System.exit(0);
    }
}
