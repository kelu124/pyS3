package com.lee.ultrascan.library;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class FatDetectUtil {

    public static class FatResult {
        public final Point center;
        public final MatOfPoint contour;
        public final boolean isDetected;

        public FatResult(MatOfPoint contour, Point center, boolean isDetected) {
            this.contour = contour;
            this.center = center;
            this.isDetected = isDetected;
        }
    }

    public static FatResult detectFat(@NonNull Bitmap image) {
        Mat grayMat = generateGrayMat(image);
        enhanceEdges(grayMat);
        morph(grayMat);
        bwmorphThin(grayMat);
        List<MatOfPoint> contours = findContours(grayMat);
        Point center = new Point();
        MatOfPoint targetContour = findTargetContour(contours, grayMat, center);
        grayMat.release();
        grayMat.release();
        if (targetContour != null) {
            return new FatResult(targetContour, center, true);
        }
        return new FatResult(null, null, false);
    }

    private static Mat generateGrayMat(Bitmap image) {
        Mat imageMat = new Mat();
        Utils.bitmapToMat(image, imageMat);
        Mat grayMat = new Mat(imageMat.height(), imageMat.width(), CvType.CV_8UC1);
        Imgproc.cvtColor(imageMat, grayMat, 11);
        imageMat.release();
        return grayMat;
    }

    private static void enhanceEdges(Mat mat) {
        MinMaxLocResult minMaxLocResult = Core.minMaxLoc(mat);
        Mat diffMat = mat.clone();
        diffFilter(mat, diffMat);
        Core.add(mat, diffMat, mat);
        Imgproc.threshold(mat, mat, minMaxLocResult.maxVal * ((double) 0.6f), 255.0d, 0);
        diffMat.release();
    }

    private static void diffFilter(Mat src, Mat dst) {
        Mat diffKernel = generateDiffKernel();
        Imgproc.filter2D(src, dst, src.depth(), diffKernel);
        diffKernel.release();
    }

    private static void morph(Mat mat) {
        Mat closeKernel = Imgproc.getStructuringElement(0, new Size(20.0d, 1.0d));
        Mat openKernel = Imgproc.getStructuringElement(0, new Size(20.0d, 5.0d));
        Imgproc.morphologyEx(mat, mat, 3, closeKernel);
        Imgproc.morphologyEx(mat, mat, 2, openKernel);
        closeKernel.release();
        openKernel.release();
    }

    private static List<MatOfPoint> findContours(Mat mat) {
        List<MatOfPoint> contours = new ArrayList();
        Mat matCopy = mat.clone();
        Mat hierarchy = new Mat();
        Imgproc.findContours(matCopy, contours, hierarchy, 0, 2);
        matCopy.release();
        hierarchy.release();
        return contours;
    }

    @Nullable
    private static MatOfPoint findTargetContour(List<MatOfPoint> contours, Mat mat, Point outTargetCenter) {
        MatOfPoint targetContour = null;
        double maxY = 0.0d;
        float sizeThresh = ((float) mat.width()) * 0.3f;
        for (MatOfPoint contour : contours) {
            if (contour.size().area() > ((double) sizeThresh)) {
                Moments moments = Imgproc.moments(contour, true);
                double centerX = moments.get_m10() / moments.get_m00();
                double centerY = moments.get_m01() / moments.get_m00();
                if (maxY < centerY) {
                    maxY = centerY;
                    targetContour = contour;
                    outTargetCenter.f203x = centerX;
                    outTargetCenter.f204y = centerY;
                }
            }
        }
        return targetContour;
    }

    private static Mat generateDiffKernel() {
        Mat diffKernel = new Mat(4, 4, CvType.CV_8UC1);
        diffKernel.put(0, 0, -0.25d);
        diffKernel.put(0, 1, -0.25d);
        diffKernel.put(0, 2, -0.25d);
        diffKernel.put(0, 3, -0.25d);
        diffKernel.put(1, 0, -0.25d);
        diffKernel.put(1, 1, -0.25d);
        diffKernel.put(1, 2, -0.25d);
        diffKernel.put(1, 3, -0.25d);
        diffKernel.put(2, 0, 0.25d);
        diffKernel.put(2, 1, 0.25d);
        diffKernel.put(2, 2, 0.25d);
        diffKernel.put(2, 3, 0.25d);
        diffKernel.put(3, 0, 0.25d);
        diffKernel.put(3, 1, 0.25d);
        diffKernel.put(3, 2, 0.25d);
        diffKernel.put(3, 3, 0.25d);
        return diffKernel;
    }

    private static void bwmorphThin(Mat img) {
        if (img == null) {
            throw new RuntimeException("params can not be null!");
        } else if (img.type() != CvType.CV_8UC1) {
            throw new RuntimeException("param img type must be CV_8UC1!");
        } else {
            int size = (int) (img.total() * ((long) img.channels()));
            int rows = img.rows();
            int cols = img.cols();
            byte[] buffer = new byte[size];
            byte[] marker = new byte[size];
            img.get(0, 0, buffer);
            boolean has_pixel_cleared;
            do {
                boolean step1_has_pixel_cleared = thinIteration(buffer, marker, rows, cols, true);
                boolean step2_has_pixel_cleared = thinIteration(buffer, marker, rows, cols, false);
                if (step1_has_pixel_cleared || step2_has_pixel_cleared) {
                    has_pixel_cleared = true;
                    continue;
                } else {
                    has_pixel_cleared = false;
                    continue;
                }
            } while (has_pixel_cleared);
            img.put(0, 0, buffer);
        }
    }

    private static boolean thinIteration(byte[] buffer, byte[] marker, int rows, int cols, boolean isOddIter) {
        boolean hasPixelCleared = false;
        Arrays.fill(marker, (byte) 1);
        for (int y = 1; y < rows - 1; y++) {
            for (int x = 1; x < cols - 1; x++) {
                if (buffer[(y * cols) + x] != 0) {
                    int p2 = -buffer[((y - 1) * cols) + x];
                    int p3 = -buffer[((y - 1) * cols) + (x + 1)];
                    int p4 = -buffer[(y * cols) + (x + 1)];
                    int p5 = -buffer[((y + 1) * cols) + (x + 1)];
                    int p6 = -buffer[((y + 1) * cols) + x];
                    int p7 = -buffer[((y + 1) * cols) + (x - 1)];
                    int p8 = -buffer[(y * cols) + (x - 1)];
                    int p9 = -buffer[((y - 1) * cols) + (x - 1)];
                    int N = ((((((p2 + p3) + p4) + p5) + p6) + p7) + p8) + p9;
                    int i = (p2 == 0 && p3 == 1) ? 1 : 0;
                    int i2 = (p3 == 0 && p4 == 1) ? 1 : 0;
                    i += i2;
                    i2 = (p4 == 0 && p5 == 1) ? 1 : 0;
                    i += i2;
                    i2 = (p5 == 0 && p6 == 1) ? 1 : 0;
                    i += i2;
                    i2 = (p6 == 0 && p7 == 1) ? 1 : 0;
                    i += i2;
                    i2 = (p7 == 0 && p8 == 1) ? 1 : 0;
                    i += i2;
                    i2 = (p8 == 0 && p9 == 1) ? 1 : 0;
                    i += i2;
                    i2 = (p9 == 0 && p2 == 1) ? 1 : 0;
                    boolean needClear = i + i2 == 1 && (isOddIter ? (p2 * p4) * p6 : (p2 * p4) * p8) == 0 && (isOddIter ? (p4 * p6) * p8 : (p2 * p6) * p8) == 0 && N >= 2 && N <= 6;
                    if (needClear) {
                        marker[(y * cols) + x] = (byte) 0;
                        hasPixelCleared = true;
                    }
                }
            }
        }
        for (int i3 = 0; i3 < marker.length; i3++) {
            buffer[i3] = (byte) (buffer[i3] * marker[i3]);
        }
        return hasPixelCleared;
    }
}
