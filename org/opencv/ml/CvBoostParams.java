package org.opencv.ml;

public class CvBoostParams extends CvDTreeParams {
    private static native long CvBoostParams_0();

    private static native void delete(long j);

    private static native int get_boost_type_0(long j);

    private static native int get_split_criteria_0(long j);

    private static native int get_weak_count_0(long j);

    private static native double get_weight_trim_rate_0(long j);

    private static native void set_boost_type_0(long j, int i);

    private static native void set_split_criteria_0(long j, int i);

    private static native void set_weak_count_0(long j, int i);

    private static native void set_weight_trim_rate_0(long j, double d);

    protected CvBoostParams(long addr) {
        super(addr);
    }

    public CvBoostParams() {
        super(CvBoostParams_0());
    }

    public int get_boost_type() {
        return get_boost_type_0(this.nativeObj);
    }

    public void set_boost_type(int boost_type) {
        set_boost_type_0(this.nativeObj, boost_type);
    }

    public int get_weak_count() {
        return get_weak_count_0(this.nativeObj);
    }

    public void set_weak_count(int weak_count) {
        set_weak_count_0(this.nativeObj, weak_count);
    }

    public int get_split_criteria() {
        return get_split_criteria_0(this.nativeObj);
    }

    public void set_split_criteria(int split_criteria) {
        set_split_criteria_0(this.nativeObj, split_criteria);
    }

    public double get_weight_trim_rate() {
        return get_weight_trim_rate_0(this.nativeObj);
    }

    public void set_weight_trim_rate(double weight_trim_rate) {
        set_weight_trim_rate_0(this.nativeObj, weight_trim_rate);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
