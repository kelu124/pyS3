package org.opencv.ml;

import org.opencv.core.TermCriteria;

public class CvSVMParams {
    protected final long nativeObj;

    private static native long CvSVMParams_0();

    private static native void delete(long j);

    private static native double get_C_0(long j);

    private static native double get_coef0_0(long j);

    private static native double get_degree_0(long j);

    private static native double get_gamma_0(long j);

    private static native int get_kernel_type_0(long j);

    private static native double get_nu_0(long j);

    private static native double get_p_0(long j);

    private static native int get_svm_type_0(long j);

    private static native double[] get_term_crit_0(long j);

    private static native void set_C_0(long j, double d);

    private static native void set_coef0_0(long j, double d);

    private static native void set_degree_0(long j, double d);

    private static native void set_gamma_0(long j, double d);

    private static native void set_kernel_type_0(long j, int i);

    private static native void set_nu_0(long j, double d);

    private static native void set_p_0(long j, double d);

    private static native void set_svm_type_0(long j, int i);

    private static native void set_term_crit_0(long j, int i, int i2, double d);

    protected CvSVMParams(long addr) {
        this.nativeObj = addr;
    }

    public CvSVMParams() {
        this.nativeObj = CvSVMParams_0();
    }

    public int get_svm_type() {
        return get_svm_type_0(this.nativeObj);
    }

    public void set_svm_type(int svm_type) {
        set_svm_type_0(this.nativeObj, svm_type);
    }

    public int get_kernel_type() {
        return get_kernel_type_0(this.nativeObj);
    }

    public void set_kernel_type(int kernel_type) {
        set_kernel_type_0(this.nativeObj, kernel_type);
    }

    public double get_degree() {
        return get_degree_0(this.nativeObj);
    }

    public void set_degree(double degree) {
        set_degree_0(this.nativeObj, degree);
    }

    public double get_gamma() {
        return get_gamma_0(this.nativeObj);
    }

    public void set_gamma(double gamma) {
        set_gamma_0(this.nativeObj, gamma);
    }

    public double get_coef0() {
        return get_coef0_0(this.nativeObj);
    }

    public void set_coef0(double coef0) {
        set_coef0_0(this.nativeObj, coef0);
    }

    public double get_C() {
        return get_C_0(this.nativeObj);
    }

    public void set_C(double C) {
        set_C_0(this.nativeObj, C);
    }

    public double get_nu() {
        return get_nu_0(this.nativeObj);
    }

    public void set_nu(double nu) {
        set_nu_0(this.nativeObj, nu);
    }

    public double get_p() {
        return get_p_0(this.nativeObj);
    }

    public void set_p(double p) {
        set_p_0(this.nativeObj, p);
    }

    public TermCriteria get_term_crit() {
        return new TermCriteria(get_term_crit_0(this.nativeObj));
    }

    public void set_term_crit(TermCriteria term_crit) {
        set_term_crit_0(this.nativeObj, term_crit.type, term_crit.maxCount, term_crit.epsilon);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
