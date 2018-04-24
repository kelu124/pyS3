package org.opencv.ml;

import org.opencv.core.Mat;

public class CvANN_MLP extends CvStatModel {
    public static final int GAUSSIAN = 2;
    public static final int IDENTITY = 0;
    public static final int NO_INPUT_SCALE = 2;
    public static final int NO_OUTPUT_SCALE = 4;
    public static final int SIGMOID_SYM = 1;
    public static final int UPDATE_WEIGHTS = 1;

    private static native long CvANN_MLP_0();

    private static native long CvANN_MLP_1(long j, int i, double d, double d2);

    private static native long CvANN_MLP_2(long j);

    private static native void clear_0(long j);

    private static native void create_0(long j, long j2, int i, double d, double d2);

    private static native void create_1(long j, long j2);

    private static native void delete(long j);

    private static native float predict_0(long j, long j2, long j3);

    private static native int train_0(long j, long j2, long j3, long j4, long j5, long j6, int i);

    private static native int train_1(long j, long j2, long j3, long j4);

    protected CvANN_MLP(long addr) {
        super(addr);
    }

    public CvANN_MLP() {
        super(CvANN_MLP_0());
    }

    public CvANN_MLP(Mat layerSizes, int activateFunc, double fparam1, double fparam2) {
        super(CvANN_MLP_1(layerSizes.nativeObj, activateFunc, fparam1, fparam2));
    }

    public CvANN_MLP(Mat layerSizes) {
        super(CvANN_MLP_2(layerSizes.nativeObj));
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public void create(Mat layerSizes, int activateFunc, double fparam1, double fparam2) {
        create_0(this.nativeObj, layerSizes.nativeObj, activateFunc, fparam1, fparam2);
    }

    public void create(Mat layerSizes) {
        create_1(this.nativeObj, layerSizes.nativeObj);
    }

    public float predict(Mat inputs, Mat outputs) {
        return predict_0(this.nativeObj, inputs.nativeObj, outputs.nativeObj);
    }

    public int train(Mat inputs, Mat outputs, Mat sampleWeights, Mat sampleIdx, CvANN_MLP_TrainParams params, int flags) {
        return train_0(this.nativeObj, inputs.nativeObj, outputs.nativeObj, sampleWeights.nativeObj, sampleIdx.nativeObj, params.nativeObj, flags);
    }

    public int train(Mat inputs, Mat outputs, Mat sampleWeights) {
        return train_1(this.nativeObj, inputs.nativeObj, outputs.nativeObj, sampleWeights.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
