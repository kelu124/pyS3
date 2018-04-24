package org.opencv.highgui;

import java.util.LinkedList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Size;

public class VideoCapture {
    protected final long nativeObj;

    private static native long n_VideoCapture();

    private static native long n_VideoCapture(int i);

    private static native long n_VideoCapture(String str);

    private static native void n_delete(long j);

    private static native double n_get(long j, int i);

    private static native String n_getSupportedPreviewSizes(long j);

    private static native boolean n_grab(long j);

    private static native boolean n_isOpened(long j);

    private static native boolean n_open(long j, int i);

    private static native boolean n_open(long j, String str);

    private static native boolean n_read(long j, long j2);

    private static native void n_release(long j);

    private static native boolean n_retrieve(long j, long j2);

    private static native boolean n_retrieve(long j, long j2, int i);

    private static native boolean n_set(long j, int i, double d);

    protected VideoCapture(long addr) {
        this.nativeObj = addr;
    }

    public VideoCapture() {
        this.nativeObj = n_VideoCapture();
    }

    public VideoCapture(int device) {
        this.nativeObj = n_VideoCapture(device);
    }

    public double get(int propId) {
        return n_get(this.nativeObj, propId);
    }

    public List<Size> getSupportedPreviewSizes() {
        String[] sizes_str = n_getSupportedPreviewSizes(this.nativeObj).split(",");
        List<Size> sizes = new LinkedList();
        for (String str : sizes_str) {
            String[] wh = str.split("x");
            sizes.add(new Size(Double.parseDouble(wh[0]), Double.parseDouble(wh[1])));
        }
        return sizes;
    }

    public boolean grab() {
        return n_grab(this.nativeObj);
    }

    public boolean isOpened() {
        return n_isOpened(this.nativeObj);
    }

    public boolean open(int device) {
        return n_open(this.nativeObj, device);
    }

    public boolean read(Mat image) {
        return n_read(this.nativeObj, image.nativeObj);
    }

    public void release() {
        n_release(this.nativeObj);
    }

    public boolean retrieve(Mat image, int channel) {
        return n_retrieve(this.nativeObj, image.nativeObj, channel);
    }

    public boolean retrieve(Mat image) {
        return n_retrieve(this.nativeObj, image.nativeObj);
    }

    public boolean set(int propId, double value) {
        return n_set(this.nativeObj, propId, value);
    }

    protected void finalize() throws Throwable {
        n_delete(this.nativeObj);
        super.finalize();
    }
}
