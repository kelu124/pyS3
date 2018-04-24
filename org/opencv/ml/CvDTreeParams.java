package org.opencv.ml;

public class CvDTreeParams {
    protected final long nativeObj;

    private static native long CvDTreeParams_0();

    private static native void delete(long j);

    private static native int get_cv_folds_0(long j);

    private static native int get_max_categories_0(long j);

    private static native int get_max_depth_0(long j);

    private static native int get_min_sample_count_0(long j);

    private static native float get_regression_accuracy_0(long j);

    private static native boolean get_truncate_pruned_tree_0(long j);

    private static native boolean get_use_1se_rule_0(long j);

    private static native boolean get_use_surrogates_0(long j);

    private static native void set_cv_folds_0(long j, int i);

    private static native void set_max_categories_0(long j, int i);

    private static native void set_max_depth_0(long j, int i);

    private static native void set_min_sample_count_0(long j, int i);

    private static native void set_regression_accuracy_0(long j, float f);

    private static native void set_truncate_pruned_tree_0(long j, boolean z);

    private static native void set_use_1se_rule_0(long j, boolean z);

    private static native void set_use_surrogates_0(long j, boolean z);

    protected CvDTreeParams(long addr) {
        this.nativeObj = addr;
    }

    public CvDTreeParams() {
        this.nativeObj = CvDTreeParams_0();
    }

    public int get_max_categories() {
        return get_max_categories_0(this.nativeObj);
    }

    public void set_max_categories(int max_categories) {
        set_max_categories_0(this.nativeObj, max_categories);
    }

    public int get_max_depth() {
        return get_max_depth_0(this.nativeObj);
    }

    public void set_max_depth(int max_depth) {
        set_max_depth_0(this.nativeObj, max_depth);
    }

    public int get_min_sample_count() {
        return get_min_sample_count_0(this.nativeObj);
    }

    public void set_min_sample_count(int min_sample_count) {
        set_min_sample_count_0(this.nativeObj, min_sample_count);
    }

    public int get_cv_folds() {
        return get_cv_folds_0(this.nativeObj);
    }

    public void set_cv_folds(int cv_folds) {
        set_cv_folds_0(this.nativeObj, cv_folds);
    }

    public boolean get_use_surrogates() {
        return get_use_surrogates_0(this.nativeObj);
    }

    public void set_use_surrogates(boolean use_surrogates) {
        set_use_surrogates_0(this.nativeObj, use_surrogates);
    }

    public boolean get_use_1se_rule() {
        return get_use_1se_rule_0(this.nativeObj);
    }

    public void set_use_1se_rule(boolean use_1se_rule) {
        set_use_1se_rule_0(this.nativeObj, use_1se_rule);
    }

    public boolean get_truncate_pruned_tree() {
        return get_truncate_pruned_tree_0(this.nativeObj);
    }

    public void set_truncate_pruned_tree(boolean truncate_pruned_tree) {
        set_truncate_pruned_tree_0(this.nativeObj, truncate_pruned_tree);
    }

    public float get_regression_accuracy() {
        return get_regression_accuracy_0(this.nativeObj);
    }

    public void set_regression_accuracy(float regression_accuracy) {
        set_regression_accuracy_0(this.nativeObj, regression_accuracy);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
