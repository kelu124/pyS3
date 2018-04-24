package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;

@Namespace("cv::xfeatures2d")
public class opencv_xfeatures2d$LUCID extends Feature2D {
    private native void allocate();

    private native void allocateArray(long j);

    @Ptr
    public static native opencv_xfeatures2d$LUCID create(int i, int i2);

    static {
        Loader.load();
    }

    public opencv_xfeatures2d$LUCID() {
        super((Pointer) null);
        allocate();
    }

    public opencv_xfeatures2d$LUCID(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_xfeatures2d$LUCID(Pointer p) {
        super(p);
    }

    public opencv_xfeatures2d$LUCID position(long position) {
        return (opencv_xfeatures2d$LUCID) super.position(position);
    }
}
