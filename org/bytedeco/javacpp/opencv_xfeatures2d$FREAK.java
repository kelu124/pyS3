package org.bytedeco.javacpp;

import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.StdVector;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;

@Namespace("cv::xfeatures2d")
public class opencv_xfeatures2d$FREAK extends Feature2D {
    public static final int NB_ORIENPAIRS = 45;
    public static final int NB_PAIRS = 512;
    public static final int NB_SCALES = 64;

    private native void allocate();

    private native void allocateArray(long j);

    @Ptr
    public static native opencv_xfeatures2d$FREAK create();

    @Ptr
    public static native opencv_xfeatures2d$FREAK create(@Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2, float f, int i, @StdVector IntBuffer intBuffer);

    @Ptr
    public static native opencv_xfeatures2d$FREAK create(@Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2, float f, int i, @StdVector IntPointer intPointer);

    @Ptr
    public static native opencv_xfeatures2d$FREAK create(@Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2, float f, int i, @StdVector int[] iArr);

    static {
        Loader.load();
    }

    public opencv_xfeatures2d$FREAK() {
        super((Pointer) null);
        allocate();
    }

    public opencv_xfeatures2d$FREAK(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_xfeatures2d$FREAK(Pointer p) {
        super(p);
    }

    public opencv_xfeatures2d$FREAK position(long position) {
        return (opencv_xfeatures2d$FREAK) super.position(position);
    }
}
