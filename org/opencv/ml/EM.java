package org.opencv.ml;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;

public class EM extends Algorithm {
    public static final int COV_MAT_DEFAULT = 1;
    public static final int COV_MAT_DIAGONAL = 1;
    public static final int COV_MAT_GENERIC = 2;
    public static final int COV_MAT_SPHERICAL = 0;
    public static final int DEFAULT_MAX_ITERS = 100;
    public static final int DEFAULT_NCLUSTERS = 5;
    public static final int START_AUTO_STEP = 0;
    public static final int START_E_STEP = 1;
    public static final int START_M_STEP = 2;

    private static native long EM_0(int i, int i2, int i3, int i4, double d);

    private static native long EM_1();

    private static native void clear_0(long j);

    private static native void delete(long j);

    private static native boolean isTrained_0(long j);

    private static native double[] predict_0(long j, long j2, long j3);

    private static native double[] predict_1(long j, long j2);

    private static native boolean trainE_0(long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8);

    private static native boolean trainE_1(long j, long j2, long j3);

    private static native boolean trainM_0(long j, long j2, long j3, long j4, long j5, long j6);

    private static native boolean trainM_1(long j, long j2, long j3);

    private static native boolean train_0(long j, long j2, long j3, long j4, long j5);

    private static native boolean train_1(long j, long j2);

    protected EM(long addr) {
        super(addr);
    }

    public EM(int nclusters, int covMatType, TermCriteria termCrit) {
        super(EM_0(nclusters, covMatType, termCrit.type, termCrit.maxCount, termCrit.epsilon));
    }

    public EM() {
        super(EM_1());
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public boolean isTrained() {
        return isTrained_0(this.nativeObj);
    }

    public double[] predict(Mat sample, Mat probs) {
        return predict_0(this.nativeObj, sample.nativeObj, probs.nativeObj);
    }

    public double[] predict(Mat sample) {
        return predict_1(this.nativeObj, sample.nativeObj);
    }

    public boolean train(Mat samples, Mat logLikelihoods, Mat labels, Mat probs) {
        return train_0(this.nativeObj, samples.nativeObj, logLikelihoods.nativeObj, labels.nativeObj, probs.nativeObj);
    }

    public boolean train(Mat samples) {
        return train_1(this.nativeObj, samples.nativeObj);
    }

    public boolean trainE(Mat samples, Mat means0, Mat covs0, Mat weights0, Mat logLikelihoods, Mat labels, Mat probs) {
        return trainE_0(this.nativeObj, samples.nativeObj, means0.nativeObj, covs0.nativeObj, weights0.nativeObj, logLikelihoods.nativeObj, labels.nativeObj, probs.nativeObj);
    }

    public boolean trainE(Mat samples, Mat means0) {
        return trainE_1(this.nativeObj, samples.nativeObj, means0.nativeObj);
    }

    public boolean trainM(Mat samples, Mat probs0, Mat logLikelihoods, Mat labels, Mat probs) {
        return trainM_0(this.nativeObj, samples.nativeObj, probs0.nativeObj, logLikelihoods.nativeObj, labels.nativeObj, probs.nativeObj);
    }

    public boolean trainM(Mat samples, Mat probs0) {
        return trainM_1(this.nativeObj, samples.nativeObj, probs0.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
