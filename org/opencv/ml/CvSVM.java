package org.opencv.ml;

import org.opencv.core.Mat;

public class CvSVM extends CvStatModel {
    public static final int f207C = 0;
    public static final int COEF = 4;
    public static final int C_SVC = 100;
    public static final int DEGREE = 5;
    public static final int EPS_SVR = 103;
    public static final int GAMMA = 1;
    public static final int LINEAR = 0;
    public static final int NU = 3;
    public static final int NU_SVC = 101;
    public static final int NU_SVR = 104;
    public static final int ONE_CLASS = 102;
    public static final int f208P = 2;
    public static final int POLY = 1;
    public static final int RBF = 2;
    public static final int SIGMOID = 3;

    private static native long CvSVM_0();

    private static native long CvSVM_1(long j, long j2, long j3, long j4, long j5);

    private static native long CvSVM_2(long j, long j2);

    private static native void clear_0(long j);

    private static native void delete(long j);

    private static native int get_support_vector_count_0(long j);

    private static native int get_var_count_0(long j);

    private static native float predict_0(long j, long j2, boolean z);

    private static native float predict_1(long j, long j2);

    private static native void predict_all_0(long j, long j2, long j3);

    private static native boolean train_0(long j, long j2, long j3, long j4, long j5, long j6);

    private static native boolean train_1(long j, long j2, long j3);

    private static native boolean train_auto_0(long j, long j2, long j3, long j4, long j5, long j6, int i, long j7, long j8, long j9, long j10, long j11, long j12, boolean z);

    private static native boolean train_auto_1(long j, long j2, long j3, long j4, long j5, long j6);

    protected CvSVM(long addr) {
        super(addr);
    }

    public CvSVM() {
        super(CvSVM_0());
    }

    public CvSVM(Mat trainData, Mat responses, Mat varIdx, Mat sampleIdx, CvSVMParams params) {
        super(CvSVM_1(trainData.nativeObj, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, params.nativeObj));
    }

    public CvSVM(Mat trainData, Mat responses) {
        super(CvSVM_2(trainData.nativeObj, responses.nativeObj));
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public int get_support_vector_count() {
        return get_support_vector_count_0(this.nativeObj);
    }

    public int get_var_count() {
        return get_var_count_0(this.nativeObj);
    }

    public float predict(Mat sample, boolean returnDFVal) {
        return predict_0(this.nativeObj, sample.nativeObj, returnDFVal);
    }

    public float predict(Mat sample) {
        return predict_1(this.nativeObj, sample.nativeObj);
    }

    public void predict_all(Mat samples, Mat results) {
        predict_all_0(this.nativeObj, samples.nativeObj, results.nativeObj);
    }

    public boolean train(Mat trainData, Mat responses, Mat varIdx, Mat sampleIdx, CvSVMParams params) {
        return train_0(this.nativeObj, trainData.nativeObj, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, params.nativeObj);
    }

    public boolean train(Mat trainData, Mat responses) {
        return train_1(this.nativeObj, trainData.nativeObj, responses.nativeObj);
    }

    public boolean train_auto(Mat trainData, Mat responses, Mat varIdx, Mat sampleIdx, CvSVMParams params, int k_fold, CvParamGrid Cgrid, CvParamGrid gammaGrid, CvParamGrid pGrid, CvParamGrid nuGrid, CvParamGrid coeffGrid, CvParamGrid degreeGrid, boolean balanced) {
        return train_auto_0(this.nativeObj, trainData.nativeObj, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, params.nativeObj, k_fold, Cgrid.nativeObj, gammaGrid.nativeObj, pGrid.nativeObj, nuGrid.nativeObj, coeffGrid.nativeObj, degreeGrid.nativeObj, balanced);
    }

    public boolean train_auto(Mat trainData, Mat responses, Mat varIdx, Mat sampleIdx, CvSVMParams params) {
        return train_auto_1(this.nativeObj, trainData.nativeObj, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, params.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
