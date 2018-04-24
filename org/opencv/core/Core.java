package org.opencv.core;

import java.util.ArrayList;
import java.util.List;
import org.opencv.utils.Converters;

public class Core {
    public static final int CMP_EQ = 0;
    public static final int CMP_GE = 2;
    public static final int CMP_GT = 1;
    public static final int CMP_LE = 4;
    public static final int CMP_LT = 3;
    public static final int CMP_NE = 5;
    public static final int COVAR_COLS = 16;
    public static final int COVAR_NORMAL = 1;
    public static final int COVAR_ROWS = 8;
    public static final int COVAR_SCALE = 4;
    public static final int COVAR_SCRAMBLED = 0;
    public static final int COVAR_USE_AVG = 2;
    private static final int CV_16S = 3;
    private static final int CV_16U = 2;
    private static final int CV_32F = 5;
    private static final int CV_32S = 4;
    private static final int CV_64F = 6;
    private static final int CV_8S = 1;
    private static final int CV_8U = 0;
    private static final int CV_USRTYPE1 = 7;
    public static final int DCT_INVERSE = 1;
    public static final int DCT_ROWS = 4;
    public static final int DECOMP_CHOLESKY = 3;
    public static final int DECOMP_EIG = 2;
    public static final int DECOMP_LU = 0;
    public static final int DECOMP_NORMAL = 16;
    public static final int DECOMP_QR = 4;
    public static final int DECOMP_SVD = 1;
    public static final int DEPTH_MASK = 7;
    public static final int DEPTH_MASK_16S = 8;
    public static final int DEPTH_MASK_16U = 4;
    public static final int DEPTH_MASK_32F = 32;
    public static final int DEPTH_MASK_32S = 16;
    public static final int DEPTH_MASK_64F = 64;
    public static final int DEPTH_MASK_8S = 2;
    public static final int DEPTH_MASK_8U = 1;
    public static final int DEPTH_MASK_ALL = 127;
    public static final int DEPTH_MASK_ALL_BUT_8S = 125;
    public static final int DEPTH_MASK_FLT = 96;
    public static final int DFT_COMPLEX_OUTPUT = 16;
    public static final int DFT_INVERSE = 1;
    public static final int DFT_REAL_OUTPUT = 32;
    public static final int DFT_ROWS = 4;
    public static final int DFT_SCALE = 2;
    public static final int FILLED = -1;
    public static final int FONT_HERSHEY_COMPLEX = 3;
    public static final int FONT_HERSHEY_COMPLEX_SMALL = 5;
    public static final int FONT_HERSHEY_DUPLEX = 2;
    public static final int FONT_HERSHEY_PLAIN = 1;
    public static final int FONT_HERSHEY_SCRIPT_COMPLEX = 7;
    public static final int FONT_HERSHEY_SCRIPT_SIMPLEX = 6;
    public static final int FONT_HERSHEY_SIMPLEX = 0;
    public static final int FONT_HERSHEY_TRIPLEX = 4;
    public static final int FONT_ITALIC = 16;
    public static final int GEMM_1_T = 1;
    public static final int GEMM_2_T = 2;
    public static final int GEMM_3_T = 4;
    public static final int KMEANS_PP_CENTERS = 2;
    public static final int KMEANS_RANDOM_CENTERS = 0;
    public static final int KMEANS_USE_INITIAL_LABELS = 1;
    public static final int LINE_4 = 4;
    public static final int LINE_8 = 8;
    public static final int LINE_AA = 16;
    public static final int MAGIC_MASK = -65536;
    public static final String NATIVE_LIBRARY_NAME = getNativeLibraryName();
    public static final int NORM_HAMMING = 6;
    public static final int NORM_HAMMING2 = 7;
    public static final int NORM_INF = 1;
    public static final int NORM_L1 = 2;
    public static final int NORM_L2 = 4;
    public static final int NORM_L2SQR = 5;
    public static final int NORM_MINMAX = 32;
    public static final int NORM_RELATIVE = 8;
    public static final int NORM_TYPE_MASK = 7;
    public static final int REDUCE_AVG = 1;
    public static final int REDUCE_MAX = 2;
    public static final int REDUCE_MIN = 3;
    public static final int REDUCE_SUM = 0;
    public static final int SORT_ASCENDING = 0;
    public static final int SORT_DESCENDING = 16;
    public static final int SORT_EVERY_COLUMN = 1;
    public static final int SORT_EVERY_ROW = 0;
    public static final int SVD_FULL_UV = 4;
    public static final int SVD_MODIFY_A = 1;
    public static final int SVD_NO_UV = 2;
    public static final int TYPE_MASK = 4095;
    public static final String VERSION = getVersion();
    public static final int VERSION_EPOCH = getVersionEpoch();
    public static final int VERSION_MAJOR = getVersionMajor();
    public static final int VERSION_MINOR = getVersionMinor();
    public static final int VERSION_REVISION = getVersionRevision();

    public static class MinMaxLocResult {
        public Point maxLoc = new Point();
        public double maxVal = 0.0d;
        public Point minLoc = new Point();
        public double minVal = 0.0d;
    }

    private static native void LUT_0(long j, long j2, long j3, int i);

    private static native void LUT_1(long j, long j2, long j3);

    private static native double Mahalanobis_0(long j, long j2, long j3);

    private static native void PCABackProject_0(long j, long j2, long j3, long j4);

    private static native void PCAComputeVar_0(long j, long j2, long j3, double d);

    private static native void PCACompute_0(long j, long j2, long j3, int i);

    private static native void PCACompute_1(long j, long j2, long j3);

    private static native void PCAProject_0(long j, long j2, long j3, long j4);

    private static native void SVBackSubst_0(long j, long j2, long j3, long j4, long j5);

    private static native void SVDecomp_0(long j, long j2, long j3, long j4, int i);

    private static native void SVDecomp_1(long j, long j2, long j3, long j4);

    private static native void absdiff_0(long j, long j2, long j3);

    private static native void absdiff_1(long j, double d, double d2, double d3, double d4, long j2);

    private static native void addWeighted_0(long j, double d, long j2, double d2, double d3, long j3, int i);

    private static native void addWeighted_1(long j, double d, long j2, double d2, double d3, long j3);

    private static native void add_0(long j, long j2, long j3, long j4, int i);

    private static native void add_1(long j, long j2, long j3, long j4);

    private static native void add_2(long j, long j2, long j3);

    private static native void add_3(long j, double d, double d2, double d3, double d4, long j2, long j3, int i);

    private static native void add_4(long j, double d, double d2, double d3, double d4, long j2, long j3);

    private static native void add_5(long j, double d, double d2, double d3, double d4, long j2);

    private static native void batchDistance_0(long j, long j2, long j3, int i, long j4, int i2, int i3, long j5, int i4, boolean z);

    private static native void batchDistance_1(long j, long j2, long j3, int i, long j4, int i2, int i3);

    private static native void batchDistance_2(long j, long j2, long j3, int i, long j4);

    private static native void bitwise_and_0(long j, long j2, long j3, long j4);

    private static native void bitwise_and_1(long j, long j2, long j3);

    private static native void bitwise_not_0(long j, long j2, long j3);

    private static native void bitwise_not_1(long j, long j2);

    private static native void bitwise_or_0(long j, long j2, long j3, long j4);

    private static native void bitwise_or_1(long j, long j2, long j3);

    private static native void bitwise_xor_0(long j, long j2, long j3, long j4);

    private static native void bitwise_xor_1(long j, long j2, long j3);

    private static native void calcCovarMatrix_0(long j, long j2, long j3, int i, int i2);

    private static native void calcCovarMatrix_1(long j, long j2, long j3, int i);

    private static native void cartToPolar_0(long j, long j2, long j3, long j4, boolean z);

    private static native void cartToPolar_1(long j, long j2, long j3, long j4);

    private static native boolean checkRange_0(long j, boolean z, double d, double d2);

    private static native boolean checkRange_1(long j);

    private static native void circle_0(long j, double d, double d2, int i, double d3, double d4, double d5, double d6, int i2, int i3, int i4);

    private static native void circle_1(long j, double d, double d2, int i, double d3, double d4, double d5, double d6, int i2);

    private static native void circle_2(long j, double d, double d2, int i, double d3, double d4, double d5, double d6);

    private static native boolean clipLine_0(int i, int i2, int i3, int i4, double d, double d2, double[] dArr, double d3, double d4, double[] dArr2);

    private static native void compare_0(long j, long j2, long j3, int i);

    private static native void compare_1(long j, double d, double d2, double d3, double d4, long j2, int i);

    private static native void completeSymm_0(long j, boolean z);

    private static native void completeSymm_1(long j);

    private static native void convertScaleAbs_0(long j, long j2, double d, double d2);

    private static native void convertScaleAbs_1(long j, long j2);

    private static native int countNonZero_0(long j);

    private static native float cubeRoot_0(float f);

    private static native void dct_0(long j, long j2, int i);

    private static native void dct_1(long j, long j2);

    private static native double determinant_0(long j);

    private static native void dft_0(long j, long j2, int i, int i2);

    private static native void dft_1(long j, long j2);

    private static native void divide_0(long j, long j2, long j3, double d, int i);

    private static native void divide_1(long j, long j2, long j3, double d);

    private static native void divide_2(long j, long j2, long j3);

    private static native void divide_3(double d, long j, long j2, int i);

    private static native void divide_4(double d, long j, long j2);

    private static native void divide_5(long j, double d, double d2, double d3, double d4, long j2, double d5, int i);

    private static native void divide_6(long j, double d, double d2, double d3, double d4, long j2, double d5);

    private static native void divide_7(long j, double d, double d2, double d3, double d4, long j2);

    private static native boolean eigen_0(long j, boolean z, long j2, long j3);

    private static native void ellipse2Poly_0(double d, double d2, double d3, double d4, int i, int i2, int i3, int i4, long j);

    private static native void ellipse_0(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, int i, int i2, int i3);

    private static native void ellipse_1(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, int i);

    private static native void ellipse_2(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11);

    private static native void ellipse_3(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, int i, int i2);

    private static native void ellipse_4(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, int i);

    private static native void ellipse_5(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9);

    private static native void exp_0(long j, long j2);

    private static native void extractChannel_0(long j, long j2, int i);

    private static native float fastAtan2_0(float f, float f2);

    private static native void fillConvexPoly_0(long j, long j2, double d, double d2, double d3, double d4, int i, int i2);

    private static native void fillConvexPoly_1(long j, long j2, double d, double d2, double d3, double d4);

    private static native void fillPoly_0(long j, long j2, double d, double d2, double d3, double d4, int i, int i2, double d5, double d6);

    private static native void fillPoly_1(long j, long j2, double d, double d2, double d3, double d4);

    private static native void findNonZero_0(long j, long j2);

    private static native void flip_0(long j, long j2, int i);

    private static native void gemm_0(long j, long j2, double d, long j3, double d2, long j4, int i);

    private static native void gemm_1(long j, long j2, double d, long j3, double d2, long j4);

    private static native String getBuildInformation_0();

    private static native long getCPUTickCount_0();

    private static native int getNumberOfCPUs_0();

    private static native int getOptimalDFTSize_0(int i);

    private static native long getTickCount_0();

    private static native double getTickFrequency_0();

    private static native void hconcat_0(long j, long j2);

    private static native void idct_0(long j, long j2, int i);

    private static native void idct_1(long j, long j2);

    private static native void idft_0(long j, long j2, int i, int i2);

    private static native void idft_1(long j, long j2);

    private static native void inRange_0(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, long j2);

    private static native void insertChannel_0(long j, long j2, int i);

    private static native double invert_0(long j, long j2, int i);

    private static native double invert_1(long j, long j2);

    private static native double kmeans_0(long j, int i, long j2, int i2, int i3, double d, int i4, int i5, long j3);

    private static native double kmeans_1(long j, int i, long j2, int i2, int i3, double d, int i4, int i5);

    private static native void line_0(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i, int i2, int i3);

    private static native void line_1(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i);

    private static native void line_2(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8);

    private static native void log_0(long j, long j2);

    private static native void magnitude_0(long j, long j2, long j3);

    private static native void max_0(long j, long j2, long j3);

    private static native void max_1(long j, double d, double d2, double d3, double d4, long j2);

    private static native void meanStdDev_0(long j, long j2, long j3, long j4);

    private static native void meanStdDev_1(long j, long j2, long j3);

    private static native double[] mean_0(long j, long j2);

    private static native double[] mean_1(long j);

    private static native void merge_0(long j, long j2);

    private static native void min_0(long j, long j2, long j3);

    private static native void min_1(long j, double d, double d2, double d3, double d4, long j2);

    private static native void mixChannels_0(long j, long j2, long j3);

    private static native void mulSpectrums_0(long j, long j2, long j3, int i, boolean z);

    private static native void mulSpectrums_1(long j, long j2, long j3, int i);

    private static native void mulTransposed_0(long j, long j2, boolean z, long j3, double d, int i);

    private static native void mulTransposed_1(long j, long j2, boolean z, long j3, double d);

    private static native void mulTransposed_2(long j, long j2, boolean z);

    private static native void multiply_0(long j, long j2, long j3, double d, int i);

    private static native void multiply_1(long j, long j2, long j3, double d);

    private static native void multiply_2(long j, long j2, long j3);

    private static native void multiply_3(long j, double d, double d2, double d3, double d4, long j2, double d5, int i);

    private static native void multiply_4(long j, double d, double d2, double d3, double d4, long j2, double d5);

    private static native void multiply_5(long j, double d, double d2, double d3, double d4, long j2);

    private static native double[] n_getTextSize(String str, int i, double d, int i2, int[] iArr);

    private static native double[] n_minMaxLocManual(long j, long j2);

    private static native double norm_0(long j, int i, long j2);

    private static native double norm_1(long j, int i);

    private static native double norm_2(long j);

    private static native double norm_3(long j, long j2, int i, long j3);

    private static native double norm_4(long j, long j2, int i);

    private static native double norm_5(long j, long j2);

    private static native void normalize_0(long j, long j2, double d, double d2, int i, int i2, long j3);

    private static native void normalize_1(long j, long j2, double d, double d2, int i, int i2);

    private static native void normalize_2(long j, long j2, double d, double d2, int i);

    private static native void normalize_3(long j, long j2);

    private static native void patchNaNs_0(long j, double d);

    private static native void patchNaNs_1(long j);

    private static native void perspectiveTransform_0(long j, long j2, long j3);

    private static native void phase_0(long j, long j2, long j3, boolean z);

    private static native void phase_1(long j, long j2, long j3);

    private static native void polarToCart_0(long j, long j2, long j3, long j4, boolean z);

    private static native void polarToCart_1(long j, long j2, long j3, long j4);

    private static native void polylines_0(long j, long j2, boolean z, double d, double d2, double d3, double d4, int i, int i2, int i3);

    private static native void polylines_1(long j, long j2, boolean z, double d, double d2, double d3, double d4, int i);

    private static native void polylines_2(long j, long j2, boolean z, double d, double d2, double d3, double d4);

    private static native void pow_0(long j, double d, long j2);

    private static native void putText_0(long j, String str, double d, double d2, int i, double d3, double d4, double d5, double d6, double d7, int i2, int i3, boolean z);

    private static native void putText_1(long j, String str, double d, double d2, int i, double d3, double d4, double d5, double d6, double d7, int i2);

    private static native void putText_2(long j, String str, double d, double d2, int i, double d3, double d4, double d5, double d6, double d7);

    private static native void randShuffle_0(long j, double d);

    private static native void randShuffle_1(long j);

    private static native void randn_0(long j, double d, double d2);

    private static native void randu_0(long j, double d, double d2);

    private static native void rectangle_0(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i, int i2, int i3);

    private static native void rectangle_1(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i);

    private static native void rectangle_2(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8);

    private static native void reduce_0(long j, long j2, int i, int i2, int i3);

    private static native void reduce_1(long j, long j2, int i, int i2);

    private static native void repeat_0(long j, int i, int i2, long j2);

    private static native void scaleAdd_0(long j, double d, long j2, long j3);

    private static native void setErrorVerbosity_0(boolean z);

    private static native void setIdentity_0(long j, double d, double d2, double d3, double d4);

    private static native void setIdentity_1(long j);

    private static native int solveCubic_0(long j, long j2);

    private static native double solvePoly_0(long j, long j2, int i);

    private static native double solvePoly_1(long j, long j2);

    private static native boolean solve_0(long j, long j2, long j3, int i);

    private static native boolean solve_1(long j, long j2, long j3);

    private static native void sortIdx_0(long j, long j2, int i);

    private static native void sort_0(long j, long j2, int i);

    private static native void split_0(long j, long j2);

    private static native void sqrt_0(long j, long j2);

    private static native void subtract_0(long j, long j2, long j3, long j4, int i);

    private static native void subtract_1(long j, long j2, long j3, long j4);

    private static native void subtract_2(long j, long j2, long j3);

    private static native void subtract_3(long j, double d, double d2, double d3, double d4, long j2, long j3, int i);

    private static native void subtract_4(long j, double d, double d2, double d3, double d4, long j2, long j3);

    private static native void subtract_5(long j, double d, double d2, double d3, double d4, long j2);

    private static native double[] sumElems_0(long j);

    private static native double[] trace_0(long j);

    private static native void transform_0(long j, long j2, long j3);

    private static native void transpose_0(long j, long j2);

    private static native void vconcat_0(long j, long j2);

    private static String getVersion() {
        return "2.4.8.0";
    }

    private static String getNativeLibraryName() {
        return "opencv_java248";
    }

    private static int getVersionEpoch() {
        return 2;
    }

    private static int getVersionMajor() {
        return 4;
    }

    private static int getVersionMinor() {
        return 8;
    }

    private static int getVersionRevision() {
        return 0;
    }

    public static void LUT(Mat src, Mat lut, Mat dst, int interpolation) {
        LUT_0(src.nativeObj, lut.nativeObj, dst.nativeObj, interpolation);
    }

    public static void LUT(Mat src, Mat lut, Mat dst) {
        LUT_1(src.nativeObj, lut.nativeObj, dst.nativeObj);
    }

    public static double Mahalanobis(Mat v1, Mat v2, Mat icovar) {
        return Mahalanobis_0(v1.nativeObj, v2.nativeObj, icovar.nativeObj);
    }

    public static void PCABackProject(Mat data, Mat mean, Mat eigenvectors, Mat result) {
        PCABackProject_0(data.nativeObj, mean.nativeObj, eigenvectors.nativeObj, result.nativeObj);
    }

    public static void PCACompute(Mat data, Mat mean, Mat eigenvectors, int maxComponents) {
        PCACompute_0(data.nativeObj, mean.nativeObj, eigenvectors.nativeObj, maxComponents);
    }

    public static void PCACompute(Mat data, Mat mean, Mat eigenvectors) {
        PCACompute_1(data.nativeObj, mean.nativeObj, eigenvectors.nativeObj);
    }

    public static void PCAComputeVar(Mat data, Mat mean, Mat eigenvectors, double retainedVariance) {
        PCAComputeVar_0(data.nativeObj, mean.nativeObj, eigenvectors.nativeObj, retainedVariance);
    }

    public static void PCAProject(Mat data, Mat mean, Mat eigenvectors, Mat result) {
        PCAProject_0(data.nativeObj, mean.nativeObj, eigenvectors.nativeObj, result.nativeObj);
    }

    public static void SVBackSubst(Mat w, Mat u, Mat vt, Mat rhs, Mat dst) {
        SVBackSubst_0(w.nativeObj, u.nativeObj, vt.nativeObj, rhs.nativeObj, dst.nativeObj);
    }

    public static void SVDecomp(Mat src, Mat w, Mat u, Mat vt, int flags) {
        SVDecomp_0(src.nativeObj, w.nativeObj, u.nativeObj, vt.nativeObj, flags);
    }

    public static void SVDecomp(Mat src, Mat w, Mat u, Mat vt) {
        SVDecomp_1(src.nativeObj, w.nativeObj, u.nativeObj, vt.nativeObj);
    }

    public static void absdiff(Mat src1, Mat src2, Mat dst) {
        absdiff_0(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static void absdiff(Mat src1, Scalar src2, Mat dst) {
        absdiff_1(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj);
    }

    public static void add(Mat src1, Mat src2, Mat dst, Mat mask, int dtype) {
        add_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, mask.nativeObj, dtype);
    }

    public static void add(Mat src1, Mat src2, Mat dst, Mat mask) {
        add_1(src1.nativeObj, src2.nativeObj, dst.nativeObj, mask.nativeObj);
    }

    public static void add(Mat src1, Mat src2, Mat dst) {
        add_2(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static void add(Mat src1, Scalar src2, Mat dst, Mat mask, int dtype) {
        add_3(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj, mask.nativeObj, dtype);
    }

    public static void add(Mat src1, Scalar src2, Mat dst, Mat mask) {
        add_4(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj, mask.nativeObj);
    }

    public static void add(Mat src1, Scalar src2, Mat dst) {
        add_5(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj);
    }

    public static void addWeighted(Mat src1, double alpha, Mat src2, double beta, double gamma, Mat dst, int dtype) {
        addWeighted_0(src1.nativeObj, alpha, src2.nativeObj, beta, gamma, dst.nativeObj, dtype);
    }

    public static void addWeighted(Mat src1, double alpha, Mat src2, double beta, double gamma, Mat dst) {
        addWeighted_1(src1.nativeObj, alpha, src2.nativeObj, beta, gamma, dst.nativeObj);
    }

    public static void batchDistance(Mat src1, Mat src2, Mat dist, int dtype, Mat nidx, int normType, int K, Mat mask, int update, boolean crosscheck) {
        batchDistance_0(src1.nativeObj, src2.nativeObj, dist.nativeObj, dtype, nidx.nativeObj, normType, K, mask.nativeObj, update, crosscheck);
    }

    public static void batchDistance(Mat src1, Mat src2, Mat dist, int dtype, Mat nidx, int normType, int K) {
        batchDistance_1(src1.nativeObj, src2.nativeObj, dist.nativeObj, dtype, nidx.nativeObj, normType, K);
    }

    public static void batchDistance(Mat src1, Mat src2, Mat dist, int dtype, Mat nidx) {
        batchDistance_2(src1.nativeObj, src2.nativeObj, dist.nativeObj, dtype, nidx.nativeObj);
    }

    public static void bitwise_and(Mat src1, Mat src2, Mat dst, Mat mask) {
        bitwise_and_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, mask.nativeObj);
    }

    public static void bitwise_and(Mat src1, Mat src2, Mat dst) {
        bitwise_and_1(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static void bitwise_not(Mat src, Mat dst, Mat mask) {
        bitwise_not_0(src.nativeObj, dst.nativeObj, mask.nativeObj);
    }

    public static void bitwise_not(Mat src, Mat dst) {
        bitwise_not_1(src.nativeObj, dst.nativeObj);
    }

    public static void bitwise_or(Mat src1, Mat src2, Mat dst, Mat mask) {
        bitwise_or_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, mask.nativeObj);
    }

    public static void bitwise_or(Mat src1, Mat src2, Mat dst) {
        bitwise_or_1(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static void bitwise_xor(Mat src1, Mat src2, Mat dst, Mat mask) {
        bitwise_xor_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, mask.nativeObj);
    }

    public static void bitwise_xor(Mat src1, Mat src2, Mat dst) {
        bitwise_xor_1(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static void calcCovarMatrix(Mat samples, Mat covar, Mat mean, int flags, int ctype) {
        calcCovarMatrix_0(samples.nativeObj, covar.nativeObj, mean.nativeObj, flags, ctype);
    }

    public static void calcCovarMatrix(Mat samples, Mat covar, Mat mean, int flags) {
        calcCovarMatrix_1(samples.nativeObj, covar.nativeObj, mean.nativeObj, flags);
    }

    public static void cartToPolar(Mat x, Mat y, Mat magnitude, Mat angle, boolean angleInDegrees) {
        cartToPolar_0(x.nativeObj, y.nativeObj, magnitude.nativeObj, angle.nativeObj, angleInDegrees);
    }

    public static void cartToPolar(Mat x, Mat y, Mat magnitude, Mat angle) {
        cartToPolar_1(x.nativeObj, y.nativeObj, magnitude.nativeObj, angle.nativeObj);
    }

    public static boolean checkRange(Mat a, boolean quiet, double minVal, double maxVal) {
        return checkRange_0(a.nativeObj, quiet, minVal, maxVal);
    }

    public static boolean checkRange(Mat a) {
        return checkRange_1(a.nativeObj);
    }

    public static void circle(Mat img, Point center, int radius, Scalar color, int thickness, int lineType, int shift) {
        circle_0(img.nativeObj, center.f203x, center.f204y, radius, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
    }

    public static void circle(Mat img, Point center, int radius, Scalar color, int thickness) {
        circle_1(img.nativeObj, center.f203x, center.f204y, radius, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void circle(Mat img, Point center, int radius, Scalar color) {
        circle_2(img.nativeObj, center.f203x, center.f204y, radius, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static boolean clipLine(Rect imgRect, Point pt1, Point pt2) {
        double[] pt1_out = new double[2];
        double[] pt2_out = new double[2];
        boolean retVal = clipLine_0(imgRect.f205x, imgRect.f206y, imgRect.width, imgRect.height, pt1.f203x, pt1.f204y, pt1_out, pt2.f203x, pt2.f204y, pt2_out);
        if (pt1 != null) {
            pt1.f203x = pt1_out[0];
            pt1.f204y = pt1_out[1];
        }
        if (pt2 != null) {
            pt2.f203x = pt2_out[0];
            pt2.f204y = pt2_out[1];
        }
        return retVal;
    }

    public static void compare(Mat src1, Mat src2, Mat dst, int cmpop) {
        compare_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, cmpop);
    }

    public static void compare(Mat src1, Scalar src2, Mat dst, int cmpop) {
        compare_1(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj, cmpop);
    }

    public static void completeSymm(Mat mtx, boolean lowerToUpper) {
        completeSymm_0(mtx.nativeObj, lowerToUpper);
    }

    public static void completeSymm(Mat mtx) {
        completeSymm_1(mtx.nativeObj);
    }

    public static void convertScaleAbs(Mat src, Mat dst, double alpha, double beta) {
        convertScaleAbs_0(src.nativeObj, dst.nativeObj, alpha, beta);
    }

    public static void convertScaleAbs(Mat src, Mat dst) {
        convertScaleAbs_1(src.nativeObj, dst.nativeObj);
    }

    public static int countNonZero(Mat src) {
        return countNonZero_0(src.nativeObj);
    }

    public static float cubeRoot(float val) {
        return cubeRoot_0(val);
    }

    public static void dct(Mat src, Mat dst, int flags) {
        dct_0(src.nativeObj, dst.nativeObj, flags);
    }

    public static void dct(Mat src, Mat dst) {
        dct_1(src.nativeObj, dst.nativeObj);
    }

    public static double determinant(Mat mtx) {
        return determinant_0(mtx.nativeObj);
    }

    public static void dft(Mat src, Mat dst, int flags, int nonzeroRows) {
        dft_0(src.nativeObj, dst.nativeObj, flags, nonzeroRows);
    }

    public static void dft(Mat src, Mat dst) {
        dft_1(src.nativeObj, dst.nativeObj);
    }

    public static void divide(Mat src1, Mat src2, Mat dst, double scale, int dtype) {
        divide_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, scale, dtype);
    }

    public static void divide(Mat src1, Mat src2, Mat dst, double scale) {
        divide_1(src1.nativeObj, src2.nativeObj, dst.nativeObj, scale);
    }

    public static void divide(Mat src1, Mat src2, Mat dst) {
        divide_2(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static void divide(double scale, Mat src2, Mat dst, int dtype) {
        divide_3(scale, src2.nativeObj, dst.nativeObj, dtype);
    }

    public static void divide(double scale, Mat src2, Mat dst) {
        divide_4(scale, src2.nativeObj, dst.nativeObj);
    }

    public static void divide(Mat src1, Scalar src2, Mat dst, double scale, int dtype) {
        divide_5(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj, scale, dtype);
    }

    public static void divide(Mat src1, Scalar src2, Mat dst, double scale) {
        divide_6(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj, scale);
    }

    public static void divide(Mat src1, Scalar src2, Mat dst) {
        divide_7(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj);
    }

    public static boolean eigen(Mat src, boolean computeEigenvectors, Mat eigenvalues, Mat eigenvectors) {
        return eigen_0(src.nativeObj, computeEigenvectors, eigenvalues.nativeObj, eigenvectors.nativeObj);
    }

    public static void ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color, int thickness, int lineType, int shift) {
        ellipse_0(img.nativeObj, center.f203x, center.f204y, axes.width, axes.height, angle, startAngle, endAngle, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
    }

    public static void ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color, int thickness) {
        ellipse_1(img.nativeObj, center.f203x, center.f204y, axes.width, axes.height, angle, startAngle, endAngle, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color) {
        ellipse_2(img.nativeObj, center.f203x, center.f204y, axes.width, axes.height, angle, startAngle, endAngle, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void ellipse(Mat img, RotatedRect box, Scalar color, int thickness, int lineType) {
        ellipse_3(img.nativeObj, box.center.f203x, box.center.f204y, box.size.width, box.size.height, box.angle, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType);
    }

    public static void ellipse(Mat img, RotatedRect box, Scalar color, int thickness) {
        ellipse_4(img.nativeObj, box.center.f203x, box.center.f204y, box.size.width, box.size.height, box.angle, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void ellipse(Mat img, RotatedRect box, Scalar color) {
        ellipse_5(img.nativeObj, box.center.f203x, box.center.f204y, box.size.width, box.size.height, box.angle, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void ellipse2Poly(Point center, Size axes, int angle, int arcStart, int arcEnd, int delta, MatOfPoint pts) {
        int i = angle;
        int i2 = arcStart;
        int i3 = arcEnd;
        int i4 = delta;
        ellipse2Poly_0(center.f203x, center.f204y, axes.width, axes.height, i, i2, i3, i4, pts.nativeObj);
    }

    public static void exp(Mat src, Mat dst) {
        exp_0(src.nativeObj, dst.nativeObj);
    }

    public static void extractChannel(Mat src, Mat dst, int coi) {
        extractChannel_0(src.nativeObj, dst.nativeObj, coi);
    }

    public static float fastAtan2(float y, float x) {
        return fastAtan2_0(y, x);
    }

    public static void fillConvexPoly(Mat img, MatOfPoint points, Scalar color, int lineType, int shift) {
        fillConvexPoly_0(img.nativeObj, points.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3], lineType, shift);
    }

    public static void fillConvexPoly(Mat img, MatOfPoint points, Scalar color) {
        fillConvexPoly_1(img.nativeObj, points.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void fillPoly(Mat img, List<MatOfPoint> pts, Scalar color, int lineType, int shift, Point offset) {
        fillPoly_0(img.nativeObj, Converters.vector_vector_Point_to_Mat(pts, new ArrayList(pts != null ? pts.size() : 0)).nativeObj, color.val[0], color.val[1], color.val[2], color.val[3], lineType, shift, offset.f203x, offset.f204y);
    }

    public static void fillPoly(Mat img, List<MatOfPoint> pts, Scalar color) {
        fillPoly_1(img.nativeObj, Converters.vector_vector_Point_to_Mat(pts, new ArrayList(pts != null ? pts.size() : 0)).nativeObj, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void findNonZero(Mat src, Mat idx) {
        findNonZero_0(src.nativeObj, idx.nativeObj);
    }

    public static void flip(Mat src, Mat dst, int flipCode) {
        flip_0(src.nativeObj, dst.nativeObj, flipCode);
    }

    public static void gemm(Mat src1, Mat src2, double alpha, Mat src3, double gamma, Mat dst, int flags) {
        gemm_0(src1.nativeObj, src2.nativeObj, alpha, src3.nativeObj, gamma, dst.nativeObj, flags);
    }

    public static void gemm(Mat src1, Mat src2, double alpha, Mat src3, double gamma, Mat dst) {
        gemm_1(src1.nativeObj, src2.nativeObj, alpha, src3.nativeObj, gamma, dst.nativeObj);
    }

    public static String getBuildInformation() {
        return getBuildInformation_0();
    }

    public static long getCPUTickCount() {
        return getCPUTickCount_0();
    }

    public static int getNumberOfCPUs() {
        return getNumberOfCPUs_0();
    }

    public static int getOptimalDFTSize(int vecsize) {
        return getOptimalDFTSize_0(vecsize);
    }

    public static long getTickCount() {
        return getTickCount_0();
    }

    public static double getTickFrequency() {
        return getTickFrequency_0();
    }

    public static void hconcat(List<Mat> src, Mat dst) {
        hconcat_0(Converters.vector_Mat_to_Mat(src).nativeObj, dst.nativeObj);
    }

    public static void idct(Mat src, Mat dst, int flags) {
        idct_0(src.nativeObj, dst.nativeObj, flags);
    }

    public static void idct(Mat src, Mat dst) {
        idct_1(src.nativeObj, dst.nativeObj);
    }

    public static void idft(Mat src, Mat dst, int flags, int nonzeroRows) {
        idft_0(src.nativeObj, dst.nativeObj, flags, nonzeroRows);
    }

    public static void idft(Mat src, Mat dst) {
        idft_1(src.nativeObj, dst.nativeObj);
    }

    public static void inRange(Mat src, Scalar lowerb, Scalar upperb, Mat dst) {
        inRange_0(src.nativeObj, lowerb.val[0], lowerb.val[1], lowerb.val[2], lowerb.val[3], upperb.val[0], upperb.val[1], upperb.val[2], upperb.val[3], dst.nativeObj);
    }

    public static void insertChannel(Mat src, Mat dst, int coi) {
        insertChannel_0(src.nativeObj, dst.nativeObj, coi);
    }

    public static double invert(Mat src, Mat dst, int flags) {
        return invert_0(src.nativeObj, dst.nativeObj, flags);
    }

    public static double invert(Mat src, Mat dst) {
        return invert_1(src.nativeObj, dst.nativeObj);
    }

    public static double kmeans(Mat data, int K, Mat bestLabels, TermCriteria criteria, int attempts, int flags, Mat centers) {
        return kmeans_0(data.nativeObj, K, bestLabels.nativeObj, criteria.type, criteria.maxCount, criteria.epsilon, attempts, flags, centers.nativeObj);
    }

    public static double kmeans(Mat data, int K, Mat bestLabels, TermCriteria criteria, int attempts, int flags) {
        return kmeans_1(data.nativeObj, K, bestLabels.nativeObj, criteria.type, criteria.maxCount, criteria.epsilon, attempts, flags);
    }

    public static void line(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int lineType, int shift) {
        line_0(img.nativeObj, pt1.f203x, pt1.f204y, pt2.f203x, pt2.f204y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
    }

    public static void line(Mat img, Point pt1, Point pt2, Scalar color, int thickness) {
        line_1(img.nativeObj, pt1.f203x, pt1.f204y, pt2.f203x, pt2.f204y, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void line(Mat img, Point pt1, Point pt2, Scalar color) {
        line_2(img.nativeObj, pt1.f203x, pt1.f204y, pt2.f203x, pt2.f204y, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void log(Mat src, Mat dst) {
        log_0(src.nativeObj, dst.nativeObj);
    }

    public static void magnitude(Mat x, Mat y, Mat magnitude) {
        magnitude_0(x.nativeObj, y.nativeObj, magnitude.nativeObj);
    }

    public static void max(Mat src1, Mat src2, Mat dst) {
        max_0(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static void max(Mat src1, Scalar src2, Mat dst) {
        max_1(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj);
    }

    public static Scalar mean(Mat src, Mat mask) {
        return new Scalar(mean_0(src.nativeObj, mask.nativeObj));
    }

    public static Scalar mean(Mat src) {
        return new Scalar(mean_1(src.nativeObj));
    }

    public static void meanStdDev(Mat src, MatOfDouble mean, MatOfDouble stddev, Mat mask) {
        meanStdDev_0(src.nativeObj, mean.nativeObj, stddev.nativeObj, mask.nativeObj);
    }

    public static void meanStdDev(Mat src, MatOfDouble mean, MatOfDouble stddev) {
        meanStdDev_1(src.nativeObj, mean.nativeObj, stddev.nativeObj);
    }

    public static void merge(List<Mat> mv, Mat dst) {
        merge_0(Converters.vector_Mat_to_Mat(mv).nativeObj, dst.nativeObj);
    }

    public static void min(Mat src1, Mat src2, Mat dst) {
        min_0(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static void min(Mat src1, Scalar src2, Mat dst) {
        min_1(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj);
    }

    public static void mixChannels(List<Mat> src, List<Mat> dst, MatOfInt fromTo) {
        mixChannels_0(Converters.vector_Mat_to_Mat(src).nativeObj, Converters.vector_Mat_to_Mat(dst).nativeObj, fromTo.nativeObj);
    }

    public static void mulSpectrums(Mat a, Mat b, Mat c, int flags, boolean conjB) {
        mulSpectrums_0(a.nativeObj, b.nativeObj, c.nativeObj, flags, conjB);
    }

    public static void mulSpectrums(Mat a, Mat b, Mat c, int flags) {
        mulSpectrums_1(a.nativeObj, b.nativeObj, c.nativeObj, flags);
    }

    public static void mulTransposed(Mat src, Mat dst, boolean aTa, Mat delta, double scale, int dtype) {
        mulTransposed_0(src.nativeObj, dst.nativeObj, aTa, delta.nativeObj, scale, dtype);
    }

    public static void mulTransposed(Mat src, Mat dst, boolean aTa, Mat delta, double scale) {
        mulTransposed_1(src.nativeObj, dst.nativeObj, aTa, delta.nativeObj, scale);
    }

    public static void mulTransposed(Mat src, Mat dst, boolean aTa) {
        mulTransposed_2(src.nativeObj, dst.nativeObj, aTa);
    }

    public static void multiply(Mat src1, Mat src2, Mat dst, double scale, int dtype) {
        multiply_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, scale, dtype);
    }

    public static void multiply(Mat src1, Mat src2, Mat dst, double scale) {
        multiply_1(src1.nativeObj, src2.nativeObj, dst.nativeObj, scale);
    }

    public static void multiply(Mat src1, Mat src2, Mat dst) {
        multiply_2(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static void multiply(Mat src1, Scalar src2, Mat dst, double scale, int dtype) {
        multiply_3(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj, scale, dtype);
    }

    public static void multiply(Mat src1, Scalar src2, Mat dst, double scale) {
        multiply_4(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj, scale);
    }

    public static void multiply(Mat src1, Scalar src2, Mat dst) {
        multiply_5(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj);
    }

    public static double norm(Mat src1, int normType, Mat mask) {
        return norm_0(src1.nativeObj, normType, mask.nativeObj);
    }

    public static double norm(Mat src1, int normType) {
        return norm_1(src1.nativeObj, normType);
    }

    public static double norm(Mat src1) {
        return norm_2(src1.nativeObj);
    }

    public static double norm(Mat src1, Mat src2, int normType, Mat mask) {
        return norm_3(src1.nativeObj, src2.nativeObj, normType, mask.nativeObj);
    }

    public static double norm(Mat src1, Mat src2, int normType) {
        return norm_4(src1.nativeObj, src2.nativeObj, normType);
    }

    public static double norm(Mat src1, Mat src2) {
        return norm_5(src1.nativeObj, src2.nativeObj);
    }

    public static void normalize(Mat src, Mat dst, double alpha, double beta, int norm_type, int dtype, Mat mask) {
        normalize_0(src.nativeObj, dst.nativeObj, alpha, beta, norm_type, dtype, mask.nativeObj);
    }

    public static void normalize(Mat src, Mat dst, double alpha, double beta, int norm_type, int dtype) {
        normalize_1(src.nativeObj, dst.nativeObj, alpha, beta, norm_type, dtype);
    }

    public static void normalize(Mat src, Mat dst, double alpha, double beta, int norm_type) {
        normalize_2(src.nativeObj, dst.nativeObj, alpha, beta, norm_type);
    }

    public static void normalize(Mat src, Mat dst) {
        normalize_3(src.nativeObj, dst.nativeObj);
    }

    public static void patchNaNs(Mat a, double val) {
        patchNaNs_0(a.nativeObj, val);
    }

    public static void patchNaNs(Mat a) {
        patchNaNs_1(a.nativeObj);
    }

    public static void perspectiveTransform(Mat src, Mat dst, Mat m) {
        perspectiveTransform_0(src.nativeObj, dst.nativeObj, m.nativeObj);
    }

    public static void phase(Mat x, Mat y, Mat angle, boolean angleInDegrees) {
        phase_0(x.nativeObj, y.nativeObj, angle.nativeObj, angleInDegrees);
    }

    public static void phase(Mat x, Mat y, Mat angle) {
        phase_1(x.nativeObj, y.nativeObj, angle.nativeObj);
    }

    public static void polarToCart(Mat magnitude, Mat angle, Mat x, Mat y, boolean angleInDegrees) {
        polarToCart_0(magnitude.nativeObj, angle.nativeObj, x.nativeObj, y.nativeObj, angleInDegrees);
    }

    public static void polarToCart(Mat magnitude, Mat angle, Mat x, Mat y) {
        polarToCart_1(magnitude.nativeObj, angle.nativeObj, x.nativeObj, y.nativeObj);
    }

    public static void polylines(Mat img, List<MatOfPoint> pts, boolean isClosed, Scalar color, int thickness, int lineType, int shift) {
        polylines_0(img.nativeObj, Converters.vector_vector_Point_to_Mat(pts, new ArrayList(pts != null ? pts.size() : 0)).nativeObj, isClosed, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
    }

    public static void polylines(Mat img, List<MatOfPoint> pts, boolean isClosed, Scalar color, int thickness) {
        polylines_1(img.nativeObj, Converters.vector_vector_Point_to_Mat(pts, new ArrayList(pts != null ? pts.size() : 0)).nativeObj, isClosed, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void polylines(Mat img, List<MatOfPoint> pts, boolean isClosed, Scalar color) {
        polylines_2(img.nativeObj, Converters.vector_vector_Point_to_Mat(pts, new ArrayList(pts != null ? pts.size() : 0)).nativeObj, isClosed, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void pow(Mat src, double power, Mat dst) {
        pow_0(src.nativeObj, power, dst.nativeObj);
    }

    public static void putText(Mat img, String text, Point org, int fontFace, double fontScale, Scalar color, int thickness, int lineType, boolean bottomLeftOrigin) {
        putText_0(img.nativeObj, text, org.f203x, org.f204y, fontFace, fontScale, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, bottomLeftOrigin);
    }

    public static void putText(Mat img, String text, Point org, int fontFace, double fontScale, Scalar color, int thickness) {
        putText_1(img.nativeObj, text, org.f203x, org.f204y, fontFace, fontScale, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void putText(Mat img, String text, Point org, int fontFace, double fontScale, Scalar color) {
        putText_2(img.nativeObj, text, org.f203x, org.f204y, fontFace, fontScale, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void randShuffle(Mat dst, double iterFactor) {
        randShuffle_0(dst.nativeObj, iterFactor);
    }

    public static void randShuffle(Mat dst) {
        randShuffle_1(dst.nativeObj);
    }

    public static void randn(Mat dst, double mean, double stddev) {
        randn_0(dst.nativeObj, mean, stddev);
    }

    public static void randu(Mat dst, double low, double high) {
        randu_0(dst.nativeObj, low, high);
    }

    public static void rectangle(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int lineType, int shift) {
        rectangle_0(img.nativeObj, pt1.f203x, pt1.f204y, pt2.f203x, pt2.f204y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
    }

    public static void rectangle(Mat img, Point pt1, Point pt2, Scalar color, int thickness) {
        rectangle_1(img.nativeObj, pt1.f203x, pt1.f204y, pt2.f203x, pt2.f204y, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void rectangle(Mat img, Point pt1, Point pt2, Scalar color) {
        rectangle_2(img.nativeObj, pt1.f203x, pt1.f204y, pt2.f203x, pt2.f204y, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void reduce(Mat src, Mat dst, int dim, int rtype, int dtype) {
        reduce_0(src.nativeObj, dst.nativeObj, dim, rtype, dtype);
    }

    public static void reduce(Mat src, Mat dst, int dim, int rtype) {
        reduce_1(src.nativeObj, dst.nativeObj, dim, rtype);
    }

    public static void repeat(Mat src, int ny, int nx, Mat dst) {
        repeat_0(src.nativeObj, ny, nx, dst.nativeObj);
    }

    public static void scaleAdd(Mat src1, double alpha, Mat src2, Mat dst) {
        scaleAdd_0(src1.nativeObj, alpha, src2.nativeObj, dst.nativeObj);
    }

    public static void setErrorVerbosity(boolean verbose) {
        setErrorVerbosity_0(verbose);
    }

    public static void setIdentity(Mat mtx, Scalar s) {
        setIdentity_0(mtx.nativeObj, s.val[0], s.val[1], s.val[2], s.val[3]);
    }

    public static void setIdentity(Mat mtx) {
        setIdentity_1(mtx.nativeObj);
    }

    public static boolean solve(Mat src1, Mat src2, Mat dst, int flags) {
        return solve_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, flags);
    }

    public static boolean solve(Mat src1, Mat src2, Mat dst) {
        return solve_1(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static int solveCubic(Mat coeffs, Mat roots) {
        return solveCubic_0(coeffs.nativeObj, roots.nativeObj);
    }

    public static double solvePoly(Mat coeffs, Mat roots, int maxIters) {
        return solvePoly_0(coeffs.nativeObj, roots.nativeObj, maxIters);
    }

    public static double solvePoly(Mat coeffs, Mat roots) {
        return solvePoly_1(coeffs.nativeObj, roots.nativeObj);
    }

    public static void sort(Mat src, Mat dst, int flags) {
        sort_0(src.nativeObj, dst.nativeObj, flags);
    }

    public static void sortIdx(Mat src, Mat dst, int flags) {
        sortIdx_0(src.nativeObj, dst.nativeObj, flags);
    }

    public static void split(Mat m, List<Mat> mv) {
        Mat mv_mat = new Mat();
        split_0(m.nativeObj, mv_mat.nativeObj);
        Converters.Mat_to_vector_Mat(mv_mat, mv);
    }

    public static void sqrt(Mat src, Mat dst) {
        sqrt_0(src.nativeObj, dst.nativeObj);
    }

    public static void subtract(Mat src1, Mat src2, Mat dst, Mat mask, int dtype) {
        subtract_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, mask.nativeObj, dtype);
    }

    public static void subtract(Mat src1, Mat src2, Mat dst, Mat mask) {
        subtract_1(src1.nativeObj, src2.nativeObj, dst.nativeObj, mask.nativeObj);
    }

    public static void subtract(Mat src1, Mat src2, Mat dst) {
        subtract_2(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static void subtract(Mat src1, Scalar src2, Mat dst, Mat mask, int dtype) {
        subtract_3(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj, mask.nativeObj, dtype);
    }

    public static void subtract(Mat src1, Scalar src2, Mat dst, Mat mask) {
        subtract_4(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj, mask.nativeObj);
    }

    public static void subtract(Mat src1, Scalar src2, Mat dst) {
        subtract_5(src1.nativeObj, src2.val[0], src2.val[1], src2.val[2], src2.val[3], dst.nativeObj);
    }

    public static Scalar sumElems(Mat src) {
        return new Scalar(sumElems_0(src.nativeObj));
    }

    public static Scalar trace(Mat mtx) {
        return new Scalar(trace_0(mtx.nativeObj));
    }

    public static void transform(Mat src, Mat dst, Mat m) {
        transform_0(src.nativeObj, dst.nativeObj, m.nativeObj);
    }

    public static void transpose(Mat src, Mat dst) {
        transpose_0(src.nativeObj, dst.nativeObj);
    }

    public static void vconcat(List<Mat> src, Mat dst) {
        vconcat_0(Converters.vector_Mat_to_Mat(src).nativeObj, dst.nativeObj);
    }

    public static MinMaxLocResult minMaxLoc(Mat src, Mat mask) {
        MinMaxLocResult res = new MinMaxLocResult();
        long maskNativeObj = 0;
        if (mask != null) {
            maskNativeObj = mask.nativeObj;
        }
        double[] resarr = n_minMaxLocManual(src.nativeObj, maskNativeObj);
        res.minVal = resarr[0];
        res.maxVal = resarr[1];
        res.minLoc.f203x = resarr[2];
        res.minLoc.f204y = resarr[3];
        res.maxLoc.f203x = resarr[4];
        res.maxLoc.f204y = resarr[5];
        return res;
    }

    public static MinMaxLocResult minMaxLoc(Mat src) {
        return minMaxLoc(src, null);
    }

    public static Size getTextSize(String text, int fontFace, double fontScale, int thickness, int[] baseLine) {
        if (baseLine == null || baseLine.length == 1) {
            return new Size(n_getTextSize(text, fontFace, fontScale, thickness, baseLine));
        }
        throw new IllegalArgumentException("'baseLine' must be 'int[1]' or 'null'.");
    }
}
