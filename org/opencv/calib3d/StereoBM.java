package org.opencv.calib3d;

import org.opencv.core.Mat;

public class StereoBM {
    public static final int BASIC_PRESET = 0;
    public static final int FISH_EYE_PRESET = 1;
    public static final int NARROW_PRESET = 2;
    public static final int PREFILTER_NORMALIZED_RESPONSE = 0;
    public static final int PREFILTER_XSOBEL = 1;
    protected final long nativeObj;

    private static native long StereoBM_0();

    private static native long StereoBM_1(int i, int i2, int i3);

    private static native long StereoBM_2(int i);

    private static native void compute_0(long j, long j2, long j3, long j4, int i);

    private static native void compute_1(long j, long j2, long j3, long j4);

    private static native void delete(long j);

    protected StereoBM(long addr) {
        this.nativeObj = addr;
    }

    public StereoBM() {
        this.nativeObj = StereoBM_0();
    }

    public StereoBM(int preset, int ndisparities, int SADWindowSize) {
        this.nativeObj = StereoBM_1(preset, ndisparities, SADWindowSize);
    }

    public StereoBM(int preset) {
        this.nativeObj = StereoBM_2(preset);
    }

    public void compute(Mat left, Mat right, Mat disparity, int disptype) {
        compute_0(this.nativeObj, left.nativeObj, right.nativeObj, disparity.nativeObj, disptype);
    }

    public void compute(Mat left, Mat right, Mat disparity) {
        compute_1(this.nativeObj, left.nativeObj, right.nativeObj, disparity.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
