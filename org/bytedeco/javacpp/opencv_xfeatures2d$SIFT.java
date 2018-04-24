package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;

@Namespace("cv::xfeatures2d")
public class opencv_xfeatures2d$SIFT extends Feature2D {
    private native void allocate();

    private native void allocateArray(long j);

    @Ptr
    public static native opencv_xfeatures2d$SIFT create();

    @Ptr
    public static native opencv_xfeatures2d$SIFT create(int i, int i2, double d, double d2, double d3);

    static {
        Loader.load();
    }

    public opencv_xfeatures2d$SIFT() {
        super((Pointer) null);
        allocate();
    }

    public opencv_xfeatures2d$SIFT(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_xfeatures2d$SIFT(Pointer p) {
        super(p);
    }

    public opencv_xfeatures2d$SIFT position(long position) {
        return (opencv_xfeatures2d$SIFT) super.position(position);
    }
}
