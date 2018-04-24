package org.opencv.ml;

import org.opencv.core.Mat;
import org.opencv.core.Range;

public class CvGBTrees extends CvStatModel {
    public static final int ABSOLUTE_LOSS = 1;
    public static final int DEVIANCE_LOSS = 4;
    public static final int HUBER_LOSS = 3;
    public static final int SQUARED_LOSS = 0;

    private static native long CvGBTrees_0();

    private static native long CvGBTrees_1(long j, int i, long j2, long j3, long j4, long j5, long j6, long j7);

    private static native long CvGBTrees_2(long j, int i, long j2);

    private static native void clear_0(long j);

    private static native void delete(long j);

    private static native float predict_0(long j, long j2, long j3, int i, int i2, int i3);

    private static native float predict_1(long j, long j2);

    private static native boolean train_0(long j, long j2, int i, long j3, long j4, long j5, long j6, long j7, long j8, boolean z);

    private static native boolean train_1(long j, long j2, int i, long j3);

    protected CvGBTrees(long addr) {
        super(addr);
    }

    public CvGBTrees() {
        super(CvGBTrees_0());
    }

    public CvGBTrees(Mat trainData, int tflag, Mat responses, Mat varIdx, Mat sampleIdx, Mat varType, Mat missingDataMask, CvGBTreesParams params) {
        super(CvGBTrees_1(trainData.nativeObj, tflag, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, varType.nativeObj, missingDataMask.nativeObj, params.nativeObj));
    }

    public CvGBTrees(Mat trainData, int tflag, Mat responses) {
        super(CvGBTrees_2(trainData.nativeObj, tflag, responses.nativeObj));
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public float predict(Mat sample, Mat missing, Range slice, int k) {
        return predict_0(this.nativeObj, sample.nativeObj, missing.nativeObj, slice.start, slice.end, k);
    }

    public float predict(Mat sample) {
        return predict_1(this.nativeObj, sample.nativeObj);
    }

    public boolean train(Mat trainData, int tflag, Mat responses, Mat varIdx, Mat sampleIdx, Mat varType, Mat missingDataMask, CvGBTreesParams params, boolean update) {
        return train_0(this.nativeObj, trainData.nativeObj, tflag, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, varType.nativeObj, missingDataMask.nativeObj, params.nativeObj, update);
    }

    public boolean train(Mat trainData, int tflag, Mat responses) {
        return train_1(this.nativeObj, trainData.nativeObj, tflag, responses.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
