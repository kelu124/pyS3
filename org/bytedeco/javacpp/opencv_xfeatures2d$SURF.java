package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;

@Namespace("cv::xfeatures2d")
public class opencv_xfeatures2d$SURF extends Feature2D {
    @Ptr
    public static native opencv_xfeatures2d$SURF create();

    @Ptr
    public static native opencv_xfeatures2d$SURF create(double d, int i, int i2, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2);

    @Cast({"bool"})
    public native boolean getExtended();

    public native double getHessianThreshold();

    public native int getNOctaveLayers();

    public native int getNOctaves();

    @Cast({"bool"})
    public native boolean getUpright();

    public native void setExtended(@Cast({"bool"}) boolean z);

    public native void setHessianThreshold(double d);

    public native void setNOctaveLayers(int i);

    public native void setNOctaves(int i);

    public native void setUpright(@Cast({"bool"}) boolean z);

    static {
        Loader.load();
    }

    public opencv_xfeatures2d$SURF(Pointer p) {
        super(p);
    }
}
