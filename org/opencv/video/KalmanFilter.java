package org.opencv.video;

import org.opencv.core.Mat;

public class KalmanFilter {
    protected final long nativeObj;

    private static native long KalmanFilter_0();

    private static native long KalmanFilter_1(int i, int i2, int i3, int i4);

    private static native long KalmanFilter_2(int i, int i2);

    private static native long correct_0(long j, long j2);

    private static native void delete(long j);

    private static native long predict_0(long j, long j2);

    private static native long predict_1(long j);

    protected KalmanFilter(long addr) {
        this.nativeObj = addr;
    }

    public KalmanFilter() {
        this.nativeObj = KalmanFilter_0();
    }

    public KalmanFilter(int dynamParams, int measureParams, int controlParams, int type) {
        this.nativeObj = KalmanFilter_1(dynamParams, measureParams, controlParams, type);
    }

    public KalmanFilter(int dynamParams, int measureParams) {
        this.nativeObj = KalmanFilter_2(dynamParams, measureParams);
    }

    public Mat correct(Mat measurement) {
        return new Mat(correct_0(this.nativeObj, measurement.nativeObj));
    }

    public Mat predict(Mat control) {
        return new Mat(predict_0(this.nativeObj, control.nativeObj));
    }

    public Mat predict() {
        return new Mat(predict_1(this.nativeObj));
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
