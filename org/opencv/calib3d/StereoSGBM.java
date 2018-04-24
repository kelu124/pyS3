package org.opencv.calib3d;

import org.opencv.core.Mat;

public class StereoSGBM {
    public static final int DISP_SCALE = 16;
    public static final int DISP_SHIFT = 4;
    protected final long nativeObj;

    private static native long StereoSGBM_0();

    private static native long StereoSGBM_1(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, boolean z);

    private static native long StereoSGBM_2(int i, int i2, int i3);

    private static native void compute_0(long j, long j2, long j3, long j4);

    private static native void delete(long j);

    private static native int get_P1_0(long j);

    private static native int get_P2_0(long j);

    private static native int get_SADWindowSize_0(long j);

    private static native int get_disp12MaxDiff_0(long j);

    private static native boolean get_fullDP_0(long j);

    private static native int get_minDisparity_0(long j);

    private static native int get_numberOfDisparities_0(long j);

    private static native int get_preFilterCap_0(long j);

    private static native int get_speckleRange_0(long j);

    private static native int get_speckleWindowSize_0(long j);

    private static native int get_uniquenessRatio_0(long j);

    private static native void set_P1_0(long j, int i);

    private static native void set_P2_0(long j, int i);

    private static native void set_SADWindowSize_0(long j, int i);

    private static native void set_disp12MaxDiff_0(long j, int i);

    private static native void set_fullDP_0(long j, boolean z);

    private static native void set_minDisparity_0(long j, int i);

    private static native void set_numberOfDisparities_0(long j, int i);

    private static native void set_preFilterCap_0(long j, int i);

    private static native void set_speckleRange_0(long j, int i);

    private static native void set_speckleWindowSize_0(long j, int i);

    private static native void set_uniquenessRatio_0(long j, int i);

    protected StereoSGBM(long addr) {
        this.nativeObj = addr;
    }

    public StereoSGBM() {
        this.nativeObj = StereoSGBM_0();
    }

    public StereoSGBM(int minDisparity, int numDisparities, int SADWindowSize, int P1, int P2, int disp12MaxDiff, int preFilterCap, int uniquenessRatio, int speckleWindowSize, int speckleRange, boolean fullDP) {
        this.nativeObj = StereoSGBM_1(minDisparity, numDisparities, SADWindowSize, P1, P2, disp12MaxDiff, preFilterCap, uniquenessRatio, speckleWindowSize, speckleRange, fullDP);
    }

    public StereoSGBM(int minDisparity, int numDisparities, int SADWindowSize) {
        this.nativeObj = StereoSGBM_2(minDisparity, numDisparities, SADWindowSize);
    }

    public void compute(Mat left, Mat right, Mat disp) {
        compute_0(this.nativeObj, left.nativeObj, right.nativeObj, disp.nativeObj);
    }

    public int get_minDisparity() {
        return get_minDisparity_0(this.nativeObj);
    }

    public void set_minDisparity(int minDisparity) {
        set_minDisparity_0(this.nativeObj, minDisparity);
    }

    public int get_numberOfDisparities() {
        return get_numberOfDisparities_0(this.nativeObj);
    }

    public void set_numberOfDisparities(int numberOfDisparities) {
        set_numberOfDisparities_0(this.nativeObj, numberOfDisparities);
    }

    public int get_SADWindowSize() {
        return get_SADWindowSize_0(this.nativeObj);
    }

    public void set_SADWindowSize(int SADWindowSize) {
        set_SADWindowSize_0(this.nativeObj, SADWindowSize);
    }

    public int get_preFilterCap() {
        return get_preFilterCap_0(this.nativeObj);
    }

    public void set_preFilterCap(int preFilterCap) {
        set_preFilterCap_0(this.nativeObj, preFilterCap);
    }

    public int get_uniquenessRatio() {
        return get_uniquenessRatio_0(this.nativeObj);
    }

    public void set_uniquenessRatio(int uniquenessRatio) {
        set_uniquenessRatio_0(this.nativeObj, uniquenessRatio);
    }

    public int get_P1() {
        return get_P1_0(this.nativeObj);
    }

    public void set_P1(int P1) {
        set_P1_0(this.nativeObj, P1);
    }

    public int get_P2() {
        return get_P2_0(this.nativeObj);
    }

    public void set_P2(int P2) {
        set_P2_0(this.nativeObj, P2);
    }

    public int get_speckleWindowSize() {
        return get_speckleWindowSize_0(this.nativeObj);
    }

    public void set_speckleWindowSize(int speckleWindowSize) {
        set_speckleWindowSize_0(this.nativeObj, speckleWindowSize);
    }

    public int get_speckleRange() {
        return get_speckleRange_0(this.nativeObj);
    }

    public void set_speckleRange(int speckleRange) {
        set_speckleRange_0(this.nativeObj, speckleRange);
    }

    public int get_disp12MaxDiff() {
        return get_disp12MaxDiff_0(this.nativeObj);
    }

    public void set_disp12MaxDiff(int disp12MaxDiff) {
        set_disp12MaxDiff_0(this.nativeObj, disp12MaxDiff);
    }

    public boolean get_fullDP() {
        return get_fullDP_0(this.nativeObj);
    }

    public void set_fullDP(boolean fullDP) {
        set_fullDP_0(this.nativeObj, fullDP);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
