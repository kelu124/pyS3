package org.opencv.ml;

import org.opencv.core.Mat;

public class CvKNearest extends CvStatModel {
    private static native long CvKNearest_0();

    private static native long CvKNearest_1(long j, long j2, long j3, boolean z, int i);

    private static native long CvKNearest_2(long j, long j2);

    private static native void delete(long j);

    private static native float find_nearest_0(long j, long j2, int i, long j3, long j4, long j5);

    private static native boolean train_0(long j, long j2, long j3, long j4, boolean z, int i, boolean z2);

    private static native boolean train_1(long j, long j2, long j3);

    protected CvKNearest(long addr) {
        super(addr);
    }

    public CvKNearest() {
        super(CvKNearest_0());
    }

    public CvKNearest(Mat trainData, Mat responses, Mat sampleIdx, boolean isRegression, int max_k) {
        super(CvKNearest_1(trainData.nativeObj, responses.nativeObj, sampleIdx.nativeObj, isRegression, max_k));
    }

    public CvKNearest(Mat trainData, Mat responses) {
        super(CvKNearest_2(trainData.nativeObj, responses.nativeObj));
    }

    public float find_nearest(Mat samples, int k, Mat results, Mat neighborResponses, Mat dists) {
        return find_nearest_0(this.nativeObj, samples.nativeObj, k, results.nativeObj, neighborResponses.nativeObj, dists.nativeObj);
    }

    public boolean train(Mat trainData, Mat responses, Mat sampleIdx, boolean isRegression, int maxK, boolean updateBase) {
        return train_0(this.nativeObj, trainData.nativeObj, responses.nativeObj, sampleIdx.nativeObj, isRegression, maxK, updateBase);
    }

    public boolean train(Mat trainData, Mat responses) {
        return train_1(this.nativeObj, trainData.nativeObj, responses.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
