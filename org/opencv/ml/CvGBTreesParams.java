package org.opencv.ml;

public class CvGBTreesParams extends CvDTreeParams {
    private static native long CvGBTreesParams_0();

    private static native void delete(long j);

    private static native int get_loss_function_type_0(long j);

    private static native float get_shrinkage_0(long j);

    private static native float get_subsample_portion_0(long j);

    private static native int get_weak_count_0(long j);

    private static native void set_loss_function_type_0(long j, int i);

    private static native void set_shrinkage_0(long j, float f);

    private static native void set_subsample_portion_0(long j, float f);

    private static native void set_weak_count_0(long j, int i);

    protected CvGBTreesParams(long addr) {
        super(addr);
    }

    public CvGBTreesParams() {
        super(CvGBTreesParams_0());
    }

    public int get_weak_count() {
        return get_weak_count_0(this.nativeObj);
    }

    public void set_weak_count(int weak_count) {
        set_weak_count_0(this.nativeObj, weak_count);
    }

    public int get_loss_function_type() {
        return get_loss_function_type_0(this.nativeObj);
    }

    public void set_loss_function_type(int loss_function_type) {
        set_loss_function_type_0(this.nativeObj, loss_function_type);
    }

    public float get_subsample_portion() {
        return get_subsample_portion_0(this.nativeObj);
    }

    public void set_subsample_portion(float subsample_portion) {
        set_subsample_portion_0(this.nativeObj, subsample_portion);
    }

    public float get_shrinkage() {
        return get_shrinkage_0(this.nativeObj);
    }

    public void set_shrinkage(float shrinkage) {
        set_shrinkage_0(this.nativeObj, shrinkage);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
