package org.opencv.ml;

public class CvParamGrid {
    public static final int SVM_C = 0;
    public static final int SVM_COEF = 4;
    public static final int SVM_DEGREE = 5;
    public static final int SVM_GAMMA = 1;
    public static final int SVM_NU = 3;
    public static final int SVM_P = 2;
    protected final long nativeObj;

    private static native long CvParamGrid_0();

    private static native void delete(long j);

    private static native double get_max_val_0(long j);

    private static native double get_min_val_0(long j);

    private static native double get_step_0(long j);

    private static native void set_max_val_0(long j, double d);

    private static native void set_min_val_0(long j, double d);

    private static native void set_step_0(long j, double d);

    protected CvParamGrid(long addr) {
        this.nativeObj = addr;
    }

    public CvParamGrid() {
        this.nativeObj = CvParamGrid_0();
    }

    public double get_min_val() {
        return get_min_val_0(this.nativeObj);
    }

    public void set_min_val(double min_val) {
        set_min_val_0(this.nativeObj, min_val);
    }

    public double get_max_val() {
        return get_max_val_0(this.nativeObj);
    }

    public void set_max_val(double max_val) {
        set_max_val_0(this.nativeObj, max_val);
    }

    public double get_step() {
        return get_step_0(this.nativeObj);
    }

    public void set_step(double step) {
        set_step_0(this.nativeObj, step);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
