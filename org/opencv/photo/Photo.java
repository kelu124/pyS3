package org.opencv.photo;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;

public class Photo {
    private static final int CV_INPAINT_NS = 0;
    private static final int CV_INPAINT_TELEA = 1;
    public static final int INPAINT_NS = 0;
    public static final int INPAINT_TELEA = 1;

    private static native void fastNlMeansDenoisingColoredMulti_0(long j, long j2, int i, int i2, float f, float f2, int i3, int i4);

    private static native void fastNlMeansDenoisingColoredMulti_1(long j, long j2, int i, int i2);

    private static native void fastNlMeansDenoisingColored_0(long j, long j2, float f, float f2, int i, int i2);

    private static native void fastNlMeansDenoisingColored_1(long j, long j2);

    private static native void fastNlMeansDenoisingMulti_0(long j, long j2, int i, int i2, float f, int i3, int i4);

    private static native void fastNlMeansDenoisingMulti_1(long j, long j2, int i, int i2);

    private static native void fastNlMeansDenoising_0(long j, long j2, float f, int i, int i2);

    private static native void fastNlMeansDenoising_1(long j, long j2);

    private static native void inpaint_0(long j, long j2, long j3, double d, int i);

    public static void fastNlMeansDenoising(Mat src, Mat dst, float h, int templateWindowSize, int searchWindowSize) {
        fastNlMeansDenoising_0(src.nativeObj, dst.nativeObj, h, templateWindowSize, searchWindowSize);
    }

    public static void fastNlMeansDenoising(Mat src, Mat dst) {
        fastNlMeansDenoising_1(src.nativeObj, dst.nativeObj);
    }

    public static void fastNlMeansDenoisingColored(Mat src, Mat dst, float h, float hColor, int templateWindowSize, int searchWindowSize) {
        fastNlMeansDenoisingColored_0(src.nativeObj, dst.nativeObj, h, hColor, templateWindowSize, searchWindowSize);
    }

    public static void fastNlMeansDenoisingColored(Mat src, Mat dst) {
        fastNlMeansDenoisingColored_1(src.nativeObj, dst.nativeObj);
    }

    public static void fastNlMeansDenoisingColoredMulti(List<Mat> srcImgs, Mat dst, int imgToDenoiseIndex, int temporalWindowSize, float h, float hColor, int templateWindowSize, int searchWindowSize) {
        fastNlMeansDenoisingColoredMulti_0(Converters.vector_Mat_to_Mat(srcImgs).nativeObj, dst.nativeObj, imgToDenoiseIndex, temporalWindowSize, h, hColor, templateWindowSize, searchWindowSize);
    }

    public static void fastNlMeansDenoisingColoredMulti(List<Mat> srcImgs, Mat dst, int imgToDenoiseIndex, int temporalWindowSize) {
        fastNlMeansDenoisingColoredMulti_1(Converters.vector_Mat_to_Mat(srcImgs).nativeObj, dst.nativeObj, imgToDenoiseIndex, temporalWindowSize);
    }

    public static void fastNlMeansDenoisingMulti(List<Mat> srcImgs, Mat dst, int imgToDenoiseIndex, int temporalWindowSize, float h, int templateWindowSize, int searchWindowSize) {
        fastNlMeansDenoisingMulti_0(Converters.vector_Mat_to_Mat(srcImgs).nativeObj, dst.nativeObj, imgToDenoiseIndex, temporalWindowSize, h, templateWindowSize, searchWindowSize);
    }

    public static void fastNlMeansDenoisingMulti(List<Mat> srcImgs, Mat dst, int imgToDenoiseIndex, int temporalWindowSize) {
        fastNlMeansDenoisingMulti_1(Converters.vector_Mat_to_Mat(srcImgs).nativeObj, dst.nativeObj, imgToDenoiseIndex, temporalWindowSize);
    }

    public static void inpaint(Mat src, Mat inpaintMask, Mat dst, double inpaintRadius, int flags) {
        inpaint_0(src.nativeObj, inpaintMask.nativeObj, dst.nativeObj, inpaintRadius, flags);
    }
}
