package org.opencv.contrib;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.utils.Converters;

public class Contrib {
    public static final int COLORMAP_AUTUMN = 0;
    public static final int COLORMAP_BONE = 1;
    public static final int COLORMAP_COOL = 8;
    public static final int COLORMAP_HOT = 11;
    public static final int COLORMAP_HSV = 9;
    public static final int COLORMAP_JET = 2;
    public static final int COLORMAP_OCEAN = 5;
    public static final int COLORMAP_PINK = 10;
    public static final int COLORMAP_RAINBOW = 4;
    public static final int COLORMAP_SPRING = 7;
    public static final int COLORMAP_SUMMER = 6;
    public static final int COLORMAP_WINTER = 3;
    public static final int RETINA_COLOR_BAYER = 2;
    public static final int RETINA_COLOR_DIAGONAL = 1;
    public static final int RETINA_COLOR_RANDOM = 0;
    public static final int RIGID_BODY_MOTION = 4;
    public static final int ROTATION = 1;
    public static final int TRANSLATION = 2;

    private static native void applyColorMap_0(long j, long j2, int i);

    private static native int chamerMatching_0(long j, long j2, long j3, long j4, double d, int i, double d2, int i2, int i3, int i4, double d3, double d4, double d5, double d6);

    private static native int chamerMatching_1(long j, long j2, long j3, long j4);

    public static void applyColorMap(Mat src, Mat dst, int colormap) {
        applyColorMap_0(src.nativeObj, dst.nativeObj, colormap);
    }

    public static int chamerMatching(Mat img, Mat templ, List<MatOfPoint> results, MatOfFloat cost, double templScale, int maxMatches, double minMatchDistance, int padX, int padY, int scales, double minScale, double maxScale, double orientationWeight, double truncate) {
        Mat results_mat = new Mat();
        int retVal = chamerMatching_0(img.nativeObj, templ.nativeObj, results_mat.nativeObj, cost.nativeObj, templScale, maxMatches, minMatchDistance, padX, padY, scales, minScale, maxScale, orientationWeight, truncate);
        Converters.Mat_to_vector_vector_Point(results_mat, results);
        return retVal;
    }

    public static int chamerMatching(Mat img, Mat templ, List<MatOfPoint> results, MatOfFloat cost) {
        Mat results_mat = new Mat();
        int retVal = chamerMatching_1(img.nativeObj, templ.nativeObj, results_mat.nativeObj, cost.nativeObj);
        Converters.Mat_to_vector_vector_Point(results_mat, results);
        return retVal;
    }
}
