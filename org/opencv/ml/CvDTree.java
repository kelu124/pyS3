package org.opencv.ml;

import org.opencv.core.Mat;

public class CvDTree extends CvStatModel {
    private static native long CvDTree_0();

    private static native void clear_0(long j);

    private static native void delete(long j);

    private static native long getVarImportance_0(long j);

    private static native boolean train_0(long j, long j2, int i, long j3, long j4, long j5, long j6, long j7, long j8);

    private static native boolean train_1(long j, long j2, int i, long j3);

    protected CvDTree(long addr) {
        super(addr);
    }

    public CvDTree() {
        super(CvDTree_0());
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public Mat getVarImportance() {
        return new Mat(getVarImportance_0(this.nativeObj));
    }

    public boolean train(Mat trainData, int tflag, Mat responses, Mat varIdx, Mat sampleIdx, Mat varType, Mat missingDataMask, CvDTreeParams params) {
        return train_0(this.nativeObj, trainData.nativeObj, tflag, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, varType.nativeObj, missingDataMask.nativeObj, params.nativeObj);
    }

    public boolean train(Mat trainData, int tflag, Mat responses) {
        return train_1(this.nativeObj, trainData.nativeObj, tflag, responses.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
