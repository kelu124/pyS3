package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;

@Namespace("cv::xfeatures2d")
public class opencv_xfeatures2d$MSDDetector extends Feature2D {
    private native void allocate();

    private native void allocateArray(long j);

    @Ptr
    public static native opencv_xfeatures2d$MSDDetector create();

    @Ptr
    public static native opencv_xfeatures2d$MSDDetector create(int i, int i2, int i3, int i4, float f, int i5, float f2, int i6, @Cast({"bool"}) boolean z);

    static {
        Loader.load();
    }

    public opencv_xfeatures2d$MSDDetector() {
        super((Pointer) null);
        allocate();
    }

    public opencv_xfeatures2d$MSDDetector(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_xfeatures2d$MSDDetector(Pointer p) {
        super(p);
    }

    public opencv_xfeatures2d$MSDDetector position(long position) {
        return (opencv_xfeatures2d$MSDDetector) super.position(position);
    }
}
