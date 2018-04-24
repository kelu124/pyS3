package org.opencv.ml;

import org.opencv.core.Mat;

public class CvRTrees extends CvStatModel {
    private static native long CvRTrees_0();

    private static native void clear_0(long j);

    private static native void delete(long j);

    private static native long getVarImportance_0(long j);

    private static native float predict_0(long j, long j2, long j3);

    private static native float predict_1(long j, long j2);

    private static native float predict_prob_0(long j, long j2, long j3);

    private static native float predict_prob_1(long j, long j2);

    private static native boolean train_0(long j, long j2, int i, long j3, long j4, long j5, long j6, long j7, long j8);

    private static native boolean train_1(long j, long j2, int i, long j3);

    protected CvRTrees(long addr) {
        super(addr);
    }

    public CvRTrees() {
        super(CvRTrees_0());
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public Mat getVarImportance() {
        return new Mat(getVarImportance_0(this.nativeObj));
    }

    public float predict(Mat sample, Mat missing) {
        return predict_0(this.nativeObj, sample.nativeObj, missing.nativeObj);
    }

    public float predict(Mat sample) {
        return predict_1(this.nativeObj, sample.nativeObj);
    }

    public float predict_prob(Mat sample, Mat missing) {
        return predict_prob_0(this.nativeObj, sample.nativeObj, missing.nativeObj);
    }

    public float predict_prob(Mat sample) {
        return predict_prob_1(this.nativeObj, sample.nativeObj);
    }

    public boolean train(Mat trainData, int tflag, Mat responses, Mat varIdx, Mat sampleIdx, Mat varType, Mat missingDataMask, CvRTParams params) {
        return train_0(this.nativeObj, trainData.nativeObj, tflag, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, varType.nativeObj, missingDataMask.nativeObj, params.nativeObj);
    }

    public boolean train(Mat trainData, int tflag, Mat responses) {
        return train_1(this.nativeObj, trainData.nativeObj, tflag, responses.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
