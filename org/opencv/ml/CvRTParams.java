package org.opencv.ml;

import org.opencv.core.TermCriteria;

public class CvRTParams extends CvDTreeParams {
    private static native long CvRTParams_0();

    private static native void delete(long j);

    private static native boolean get_calc_var_importance_0(long j);

    private static native int get_nactive_vars_0(long j);

    private static native double[] get_term_crit_0(long j);

    private static native void set_calc_var_importance_0(long j, boolean z);

    private static native void set_nactive_vars_0(long j, int i);

    private static native void set_term_crit_0(long j, int i, int i2, double d);

    protected CvRTParams(long addr) {
        super(addr);
    }

    public CvRTParams() {
        super(CvRTParams_0());
    }

    public boolean get_calc_var_importance() {
        return get_calc_var_importance_0(this.nativeObj);
    }

    public void set_calc_var_importance(boolean calc_var_importance) {
        set_calc_var_importance_0(this.nativeObj, calc_var_importance);
    }

    public int get_nactive_vars() {
        return get_nactive_vars_0(this.nativeObj);
    }

    public void set_nactive_vars(int nactive_vars) {
        set_nactive_vars_0(this.nativeObj, nactive_vars);
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
