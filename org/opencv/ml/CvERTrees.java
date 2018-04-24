package org.opencv.ml;

import org.opencv.core.Mat;

public class CvERTrees extends CvRTrees {
    private static native long CvERTrees_0();

    private static native void delete(long j);

    private static native boolean train_0(long j, long j2, int i, long j3, long j4, long j5, long j6, long j7, long j8);

    private static native boolean train_1(long j, long j2, int i, long j3);

    protected CvERTrees(long addr) {
        super(addr);
    }

    public CvERTrees() {
        super(CvERTrees_0());
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
