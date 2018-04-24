package org.opencv.contrib;

import java.util.List;
import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;

public class FaceRecognizer extends Algorithm {
    private static native void delete(long j);

    private static native void load_0(long j, String str);

    private static native void predict_0(long j, long j2, double[] dArr, double[] dArr2);

    private static native void save_0(long j, String str);

    private static native void train_0(long j, long j2, long j3);

    private static native void update_0(long j, long j2, long j3);

    protected FaceRecognizer(long addr) {
        super(addr);
    }

    public void load(String filename) {
        load_0(this.nativeObj, filename);
    }

    public void predict(Mat src, int[] label, double[] confidence) {
        double[] label_out = new double[1];
        double[] confidence_out = new double[1];
        predict_0(this.nativeObj, src.nativeObj, label_out, confidence_out);
        if (label != null) {
            label[0] = (int) label_out[0];
        }
        if (confidence != null) {
            confidence[0] = confidence_out[0];
        }
    }

    public void save(String filename) {
        save_0(this.nativeObj, filename);
    }

    public void train(List<Mat> src, Mat labels) {
        train_0(this.nativeObj, Converters.vector_Mat_to_Mat(src).nativeObj, labels.nativeObj);
    }

    public void update(List<Mat> src, Mat labels) {
        update_0(this.nativeObj, Converters.vector_Mat_to_Mat(src).nativeObj, labels.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
