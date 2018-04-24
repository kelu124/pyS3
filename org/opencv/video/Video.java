package org.opencv.video;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.utils.Converters;

public class Video {
    private static final int CV_LKFLOW_GET_MIN_EIGENVALS = 8;
    private static final int CV_LKFLOW_INITIAL_GUESSES = 4;
    public static final int OPTFLOW_FARNEBACK_GAUSSIAN = 256;
    public static final int OPTFLOW_LK_GET_MIN_EIGENVALS = 8;
    public static final int OPTFLOW_USE_INITIAL_FLOW = 4;

    private static native double[] CamShift_0(long j, int i, int i2, int i3, int i4, double[] dArr, int i5, int i6, double d);

    private static native int buildOpticalFlowPyramid_0(long j, long j2, double d, double d2, int i, boolean z, int i2, int i3, boolean z2);

    private static native int buildOpticalFlowPyramid_1(long j, long j2, double d, double d2, int i);

    private static native double calcGlobalOrientation_0(long j, long j2, long j3, double d, double d2);

    private static native void calcMotionGradient_0(long j, long j2, long j3, double d, double d2, int i);

    private static native void calcMotionGradient_1(long j, long j2, long j3, double d, double d2);

    private static native void calcOpticalFlowFarneback_0(long j, long j2, long j3, double d, int i, int i2, int i3, int i4, double d2, int i5);

    private static native void calcOpticalFlowPyrLK_0(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2, int i, int i2, int i3, double d3, int i4, double d4);

    private static native void calcOpticalFlowPyrLK_1(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2, int i);

    private static native void calcOpticalFlowPyrLK_2(long j, long j2, long j3, long j4, long j5, long j6);

    private static native void calcOpticalFlowSF_0(long j, long j2, long j3, int i, int i2, int i3);

    private static native void calcOpticalFlowSF_1(long j, long j2, long j3, int i, int i2, int i3, double d, double d2, int i4, double d3, double d4, double d5, int i5, double d6, double d7, double d8);

    private static native long estimateRigidTransform_0(long j, long j2, boolean z);

    private static native int meanShift_0(long j, int i, int i2, int i3, int i4, double[] dArr, int i5, int i6, double d);

    private static native void segmentMotion_0(long j, long j2, long j3, double d, double d2);

    private static native void updateMotionHistory_0(long j, long j2, double d, double d2);

    public static RotatedRect CamShift(Mat probImage, Rect window, TermCriteria criteria) {
        double[] window_out = new double[4];
        RotatedRect retVal = new RotatedRect(CamShift_0(probImage.nativeObj, window.f205x, window.f206y, window.width, window.height, window_out, criteria.type, criteria.maxCount, criteria.epsilon));
        if (window != null) {
            window.f205x = (int) window_out[0];
            window.f206y = (int) window_out[1];
            window.width = (int) window_out[2];
            window.height = (int) window_out[3];
        }
        return retVal;
    }

    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel, boolean withDerivatives, int pyrBorder, int derivBorder, boolean tryReuseInputImage) {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_0(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel, withDerivatives, pyrBorder, derivBorder, tryReuseInputImage);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        return retVal;
    }

    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel) {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_1(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        return retVal;
    }

    public static double calcGlobalOrientation(Mat orientation, Mat mask, Mat mhi, double timestamp, double duration) {
        return calcGlobalOrientation_0(orientation.nativeObj, mask.nativeObj, mhi.nativeObj, timestamp, duration);
    }

    public static void calcMotionGradient(Mat mhi, Mat mask, Mat orientation, double delta1, double delta2, int apertureSize) {
        calcMotionGradient_0(mhi.nativeObj, mask.nativeObj, orientation.nativeObj, delta1, delta2, apertureSize);
    }

    public static void calcMotionGradient(Mat mhi, Mat mask, Mat orientation, double delta1, double delta2) {
        calcMotionGradient_1(mhi.nativeObj, mask.nativeObj, orientation.nativeObj, delta1, delta2);
    }

    public static void calcOpticalFlowFarneback(Mat prev, Mat next, Mat flow, double pyr_scale, int levels, int winsize, int iterations, int poly_n, double poly_sigma, int flags) {
        calcOpticalFlowFarneback_0(prev.nativeObj, next.nativeObj, flow.nativeObj, pyr_scale, levels, winsize, iterations, poly_n, poly_sigma, flags);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel, TermCriteria criteria, int flags, double minEigThreshold) {
        int i = maxLevel;
        calcOpticalFlowPyrLK_0(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj, winSize.width, winSize.height, i, criteria.type, criteria.maxCount, criteria.epsilon, flags, minEigThreshold);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel) {
        calcOpticalFlowPyrLK_1(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj, winSize.width, winSize.height, maxLevel);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err) {
        calcOpticalFlowPyrLK_2(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj);
    }

    public static void calcOpticalFlowSF(Mat from, Mat to, Mat flow, int layers, int averaging_block_size, int max_flow) {
        calcOpticalFlowSF_0(from.nativeObj, to.nativeObj, flow.nativeObj, layers, averaging_block_size, max_flow);
    }

    public static void calcOpticalFlowSF(Mat from, Mat to, Mat flow, int layers, int averaging_block_size, int max_flow, double sigma_dist, double sigma_color, int postprocess_window, double sigma_dist_fix, double sigma_color_fix, double occ_thr, int upscale_averaging_radius, double upscale_sigma_dist, double upscale_sigma_color, double speed_up_thr) {
        calcOpticalFlowSF_1(from.nativeObj, to.nativeObj, flow.nativeObj, layers, averaging_block_size, max_flow, sigma_dist, sigma_color, postprocess_window, sigma_dist_fix, sigma_color_fix, occ_thr, upscale_averaging_radius, upscale_sigma_dist, upscale_sigma_color, speed_up_thr);
    }

    public static Mat estimateRigidTransform(Mat src, Mat dst, boolean fullAffine) {
        return new Mat(estimateRigidTransform_0(src.nativeObj, dst.nativeObj, fullAffine));
    }

    public static int meanShift(Mat probImage, Rect window, TermCriteria criteria) {
        double[] window_out = new double[4];
        int retVal = meanShift_0(probImage.nativeObj, window.f205x, window.f206y, window.width, window.height, window_out, criteria.type, criteria.maxCount, criteria.epsilon);
        if (window != null) {
            window.f205x = (int) window_out[0];
            window.f206y = (int) window_out[1];
            window.width = (int) window_out[2];
            window.height = (int) window_out[3];
        }
        return retVal;
    }

    public static void segmentMotion(Mat mhi, Mat segmask, MatOfRect boundingRects, double timestamp, double segThresh) {
        segmentMotion_0(mhi.nativeObj, segmask.nativeObj, boundingRects.nativeObj, timestamp, segThresh);
    }

    public static void updateMotionHistory(Mat silhouette, Mat mhi, double timestamp, double duration) {
        updateMotionHistory_0(silhouette.nativeObj, mhi.nativeObj, timestamp, duration);
    }
}
