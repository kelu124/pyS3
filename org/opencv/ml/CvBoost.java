package org.opencv.ml;

import org.opencv.core.Mat;
import org.opencv.core.Range;

public class CvBoost extends CvStatModel {
    public static final int DEFAULT = 0;
    public static final int DISCRETE = 0;
    public static final int GENTLE = 3;
    public static final int GINI = 1;
    public static final int LOGIT = 2;
    public static final int MISCLASS = 3;
    public static final int REAL = 1;
    public static final int SQERR = 4;

    private static native long CvBoost_0();

    private static native long CvBoost_1(long j, int i, long j2, long j3, long j4, long j5, long j6, long j7);

    private static native long CvBoost_2(long j, int i, long j2);

    private static native void clear_0(long j);

    private static native void delete(long j);

    private static native float predict_0(long j, long j2, long j3, int i, int i2, boolean z, boolean z2);

    private static native float predict_1(long j, long j2);

    private static native void prune_0(long j, int i, int i2);

    private static native boolean train_0(long j, long j2, int i, long j3, long j4, long j5, long j6, long j7, long j8, boolean z);

    private static native boolean train_1(long j, long j2, int i, long j3);

    protected CvBoost(long addr) {
        super(addr);
    }

    public CvBoost() {
        super(CvBoost_0());
    }

    public CvBoost(Mat trainData, int tflag, Mat responses, Mat varIdx, Mat sampleIdx, Mat varType, Mat missingDataMask, CvBoostParams params) {
        super(CvBoost_1(trainData.nativeObj, tflag, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, varType.nativeObj, missingDataMask.nativeObj, params.nativeObj));
    }

    public CvBoost(Mat trainData, int tflag, Mat responses) {
        super(CvBoost_2(trainData.nativeObj, tflag, responses.nativeObj));
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public float predict(Mat sample, Mat missing, Range slice, boolean rawMode, boolean returnSum) {
        return predict_0(this.nativeObj, sample.nativeObj, missing.nativeObj, slice.start, slice.end, rawMode, returnSum);
    }

    public float predict(Mat sample) {
        return predict_1(this.nativeObj, sample.nativeObj);
    }

    public void prune(Range slice) {
        prune_0(this.nativeObj, slice.start, slice.end);
    }

    public boolean train(Mat trainData, int tflag, Mat responses, Mat varIdx, Mat sampleIdx, Mat varType, Mat missingDataMask, CvBoostParams params, boolean update) {
        return train_0(this.nativeObj, trainData.nativeObj, tflag, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, varType.nativeObj, missingDataMask.nativeObj, params.nativeObj, update);
    }

    public boolean train(Mat trainData, int tflag, Mat responses) {
        return train_1(this.nativeObj, trainData.nativeObj, tflag, responses.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
