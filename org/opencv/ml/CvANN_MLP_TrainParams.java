package org.opencv.ml;

import org.opencv.core.TermCriteria;

public class CvANN_MLP_TrainParams {
    public static final int BACKPROP = 0;
    public static final int RPROP = 1;
    protected final long nativeObj;

    private static native long CvANN_MLP_TrainParams_0();

    private static native void delete(long j);

    private static native double get_bp_dw_scale_0(long j);

    private static native double get_bp_moment_scale_0(long j);

    private static native double get_rp_dw0_0(long j);

    private static native double get_rp_dw_max_0(long j);

    private static native double get_rp_dw_min_0(long j);

    private static native double get_rp_dw_minus_0(long j);

    private static native double get_rp_dw_plus_0(long j);

    private static native double[] get_term_crit_0(long j);

    private static native int get_train_method_0(long j);

    private static native void set_bp_dw_scale_0(long j, double d);

    private static native void set_bp_moment_scale_0(long j, double d);

    private static native void set_rp_dw0_0(long j, double d);

    private static native void set_rp_dw_max_0(long j, double d);

    private static native void set_rp_dw_min_0(long j, double d);

    private static native void set_rp_dw_minus_0(long j, double d);

    private static native void set_rp_dw_plus_0(long j, double d);

    private static native void set_term_crit_0(long j, int i, int i2, double d);

    private static native void set_train_method_0(long j, int i);

    protected CvANN_MLP_TrainParams(long addr) {
        this.nativeObj = addr;
    }

    public CvANN_MLP_TrainParams() {
        this.nativeObj = CvANN_MLP_TrainParams_0();
    }

    public TermCriteria get_term_crit() {
        return new TermCriteria(get_term_crit_0(this.nativeObj));
    }

    public void set_term_crit(TermCriteria term_crit) {
        set_term_crit_0(this.nativeObj, term_crit.type, term_crit.maxCount, term_crit.epsilon);
    }

    public int get_train_method() {
        return get_train_method_0(this.nativeObj);
    }

    public void set_train_method(int train_method) {
        set_train_method_0(this.nativeObj, train_method);
    }

    public double get_bp_dw_scale() {
        return get_bp_dw_scale_0(this.nativeObj);
    }

    public void set_bp_dw_scale(double bp_dw_scale) {
        set_bp_dw_scale_0(this.nativeObj, bp_dw_scale);
    }

    public double get_bp_moment_scale() {
        return get_bp_moment_scale_0(this.nativeObj);
    }

    public void set_bp_moment_scale(double bp_moment_scale) {
        set_bp_moment_scale_0(this.nativeObj, bp_moment_scale);
    }

    public double get_rp_dw0() {
        return get_rp_dw0_0(this.nativeObj);
    }

    public void set_rp_dw0(double rp_dw0) {
        set_rp_dw0_0(this.nativeObj, rp_dw0);
    }

    public double get_rp_dw_plus() {
        return get_rp_dw_plus_0(this.nativeObj);
    }

    public void set_rp_dw_plus(double rp_dw_plus) {
        set_rp_dw_plus_0(this.nativeObj, rp_dw_plus);
    }

    public double get_rp_dw_minus() {
        return get_rp_dw_minus_0(this.nativeObj);
    }

    public void set_rp_dw_minus(double rp_dw_minus) {
        set_rp_dw_minus_0(this.nativeObj, rp_dw_minus);
    }

    public double get_rp_dw_min() {
        return get_rp_dw_min_0(this.nativeObj);
    }

    public void set_rp_dw_min(double rp_dw_min) {
        set_rp_dw_min_0(this.nativeObj, rp_dw_min);
    }

    public double get_rp_dw_max() {
        return get_rp_dw_max_0(this.nativeObj);
    }

    public void set_rp_dw_max(double rp_dw_max) {
        set_rp_dw_max_0(this.nativeObj, rp_dw_max);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
