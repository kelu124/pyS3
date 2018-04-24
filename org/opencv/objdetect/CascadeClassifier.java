package org.opencv.objdetect;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;

public class CascadeClassifier {
    protected final long nativeObj;

    private static native long CascadeClassifier_0();

    private static native long CascadeClassifier_1(String str);

    private static native void delete(long j);

    private static native void detectMultiScale_0(long j, long j2, long j3, double d, int i, int i2, double d2, double d3, double d4, double d5);

    private static native void detectMultiScale_1(long j, long j2, long j3);

    private static native void detectMultiScale_2(long j, long j2, long j3, long j4, long j5, double d, int i, int i2, double d2, double d3, double d4, double d5, boolean z);

    private static native void detectMultiScale_3(long j, long j2, long j3, long j4, long j5);

    private static native boolean empty_0(long j);

    private static native boolean load_0(long j, String str);

    protected CascadeClassifier(long addr) {
        this.nativeObj = addr;
    }

    public CascadeClassifier() {
        this.nativeObj = CascadeClassifier_0();
    }

    public CascadeClassifier(String filename) {
        this.nativeObj = CascadeClassifier_1(filename);
    }

    public void detectMultiScale(Mat image, MatOfRect objects, double scaleFactor, int minNeighbors, int flags, Size minSize, Size maxSize) {
        double d = scaleFactor;
        int i = minNeighbors;
        int i2 = flags;
        detectMultiScale_0(this.nativeObj, image.nativeObj, objects.nativeObj, d, i, i2, minSize.width, minSize.height, maxSize.width, maxSize.height);
    }

    public void detectMultiScale(Mat image, MatOfRect objects) {
        detectMultiScale_1(this.nativeObj, image.nativeObj, objects.nativeObj);
    }

    public void detectMultiScale(Mat image, MatOfRect objects, MatOfInt rejectLevels, MatOfDouble levelWeights, double scaleFactor, int minNeighbors, int flags, Size minSize, Size maxSize, boolean outputRejectLevels) {
        double d = scaleFactor;
        int i = minNeighbors;
        int i2 = flags;
        detectMultiScale_2(this.nativeObj, image.nativeObj, objects.nativeObj, rejectLevels.nativeObj, levelWeights.nativeObj, d, i, i2, minSize.width, minSize.height, maxSize.width, maxSize.height, outputRejectLevels);
    }

    public void detectMultiScale(Mat image, MatOfRect objects, MatOfInt rejectLevels, MatOfDouble levelWeights) {
        detectMultiScale_3(this.nativeObj, image.nativeObj, objects.nativeObj, rejectLevels.nativeObj, levelWeights.nativeObj);
    }

    public boolean empty() {
        return empty_0(this.nativeObj);
    }

    public boolean load(String filename) {
        return load_0(this.nativeObj, filename);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
