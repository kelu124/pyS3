package org.opencv.contrib;

import org.opencv.core.Mat;

public class StereoVar {
    public static final int CYCLE_O = 0;
    public static final int CYCLE_V = 1;
    public static final int PENALIZATION_CHARBONNIER = 1;
    public static final int PENALIZATION_PERONA_MALIK = 2;
    public static final int PENALIZATION_TICHONOV = 0;
    public static final int USE_AUTO_PARAMS = 8;
    public static final int USE_EQUALIZE_HIST = 2;
    public static final int USE_INITIAL_DISPARITY = 1;
    public static final int USE_MEDIAN_FILTERING = 16;
    public static final int USE_SMART_ID = 4;
    protected final long nativeObj;

    private static native long StereoVar_0();

    private static native long StereoVar_1(int i, double d, int i2, int i3, int i4, int i5, double d2, float f, float f2, int i6, int i7, int i8);

    private static native void compute_0(long j, long j2, long j3, long j4);

    private static native void delete(long j);

    private static native int get_cycle_0(long j);

    private static native float get_fi_0(long j);

    private static native int get_flags_0(long j);

    private static native float get_lambda_0(long j);

    private static native int get_levels_0(long j);

    private static native int get_maxDisp_0(long j);

    private static native int get_minDisp_0(long j);

    private static native int get_nIt_0(long j);

    private static native int get_penalization_0(long j);

    private static native int get_poly_n_0(long j);

    private static native double get_poly_sigma_0(long j);

    private static native double get_pyrScale_0(long j);

    private static native void set_cycle_0(long j, int i);

    private static native void set_fi_0(long j, float f);

    private static native void set_flags_0(long j, int i);

    private static native void set_lambda_0(long j, float f);

    private static native void set_levels_0(long j, int i);

    private static native void set_maxDisp_0(long j, int i);

    private static native void set_minDisp_0(long j, int i);

    private static native void set_nIt_0(long j, int i);

    private static native void set_penalization_0(long j, int i);

    private static native void set_poly_n_0(long j, int i);

    private static native void set_poly_sigma_0(long j, double d);

    private static native void set_pyrScale_0(long j, double d);

    protected StereoVar(long addr) {
        this.nativeObj = addr;
    }

    public StereoVar() {
        this.nativeObj = StereoVar_0();
    }

    public StereoVar(int levels, double pyrScale, int nIt, int minDisp, int maxDisp, int poly_n, double poly_sigma, float fi, float lambda, int penalization, int cycle, int flags) {
        this.nativeObj = StereoVar_1(levels, pyrScale, nIt, minDisp, maxDisp, poly_n, poly_sigma, fi, lambda, penalization, cycle, flags);
    }

    public void compute(Mat left, Mat right, Mat disp) {
        compute_0(this.nativeObj, left.nativeObj, right.nativeObj, disp.nativeObj);
    }

    public int get_levels() {
        return get_levels_0(this.nativeObj);
    }

    public void set_levels(int levels) {
        set_levels_0(this.nativeObj, levels);
    }

    public double get_pyrScale() {
        return get_pyrScale_0(this.nativeObj);
    }

    public void set_pyrScale(double pyrScale) {
        set_pyrScale_0(this.nativeObj, pyrScale);
    }

    public int get_nIt() {
        return get_nIt_0(this.nativeObj);
    }

    public void set_nIt(int nIt) {
        set_nIt_0(this.nativeObj, nIt);
    }

    public int get_minDisp() {
        return get_minDisp_0(this.nativeObj);
    }

    public void set_minDisp(int minDisp) {
        set_minDisp_0(this.nativeObj, minDisp);
    }

    public int get_maxDisp() {
        return get_maxDisp_0(this.nativeObj);
    }

    public void set_maxDisp(int maxDisp) {
        set_maxDisp_0(this.nativeObj, maxDisp);
    }

    public int get_poly_n() {
        return get_poly_n_0(this.nativeObj);
    }

    public void set_poly_n(int poly_n) {
        set_poly_n_0(this.nativeObj, poly_n);
    }

    public double get_poly_sigma() {
        return get_poly_sigma_0(this.nativeObj);
    }

    public void set_poly_sigma(double poly_sigma) {
        set_poly_sigma_0(this.nativeObj, poly_sigma);
    }

    public float get_fi() {
        return get_fi_0(this.nativeObj);
    }

    public void set_fi(float fi) {
        set_fi_0(this.nativeObj, fi);
    }

    public float get_lambda() {
        return get_lambda_0(this.nativeObj);
    }

    public void set_lambda(float lambda) {
        set_lambda_0(this.nativeObj, lambda);
    }

    public int get_penalization() {
        return get_penalization_0(this.nativeObj);
    }

    public void set_penalization(int penalization) {
        set_penalization_0(this.nativeObj, penalization);
    }

    public int get_cycle() {
        return get_cycle_0(this.nativeObj);
    }

    public void set_cycle(int cycle) {
        set_cycle_0(this.nativeObj, cycle);
    }

    public int get_flags() {
        return get_flags_0(this.nativeObj);
    }

    public void set_flags(int flags) {
        set_flags_0(this.nativeObj, flags);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
