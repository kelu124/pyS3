package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;

@Namespace("cv::xfeatures2d")
public class opencv_xfeatures2d$StarDetector extends Feature2D {
    private native void allocate();

    private native void allocateArray(long j);

    @Ptr
    public static native opencv_xfeatures2d$StarDetector create();

    @Ptr
    public static native opencv_xfeatures2d$StarDetector create(int i, int i2, int i3, int i4, int i5);

    static {
        Loader.load();
    }

    public opencv_xfeatures2d$StarDetector() {
        super((Pointer) null);
        allocate();
    }

    public opencv_xfeatures2d$StarDetector(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_xfeatures2d$StarDetector(Pointer p) {
        super(p);
    }

    public opencv_xfeatures2d$StarDetector position(long position) {
        return (opencv_xfeatures2d$StarDetector) super.position(position);
    }
}
