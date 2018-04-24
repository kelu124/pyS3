package org.opencv.ml;

import org.opencv.core.Mat;

public class CvNormalBayesClassifier extends CvStatModel {
    private static native long CvNormalBayesClassifier_0();

    private static native long CvNormalBayesClassifier_1(long j, long j2, long j3, long j4);

    private static native long CvNormalBayesClassifier_2(long j, long j2);

    private static native void clear_0(long j);

    private static native void delete(long j);

    private static native float predict_0(long j, long j2, long j3);

    private static native float predict_1(long j, long j2);

    private static native boolean train_0(long j, long j2, long j3, long j4, long j5, boolean z);

    private static native boolean train_1(long j, long j2, long j3);

    protected CvNormalBayesClassifier(long addr) {
        super(addr);
    }

    public CvNormalBayesClassifier() {
        super(CvNormalBayesClassifier_0());
    }

    public CvNormalBayesClassifier(Mat trainData, Mat responses, Mat varIdx, Mat sampleIdx) {
        super(CvNormalBayesClassifier_1(trainData.nativeObj, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj));
    }

    public CvNormalBayesClassifier(Mat trainData, Mat responses) {
        super(CvNormalBayesClassifier_2(trainData.nativeObj, responses.nativeObj));
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public float predict(Mat samples, Mat results) {
        return predict_0(this.nativeObj, samples.nativeObj, results.nativeObj);
    }

    public float predict(Mat samples) {
        return predict_1(this.nativeObj, samples.nativeObj);
    }

    public boolean train(Mat trainData, Mat responses, Mat varIdx, Mat sampleIdx, boolean update) {
        return train_0(this.nativeObj, trainData.nativeObj, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, update);
    }

    public boolean train(Mat trainData, Mat responses) {
        return train_1(this.nativeObj, trainData.nativeObj, responses.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
