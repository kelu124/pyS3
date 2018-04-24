package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;

@Namespace("cv::xfeatures2d")
public class opencv_xfeatures2d$BriefDescriptorExtractor extends Feature2D {
    private native void allocate();

    private native void allocateArray(long j);

    @Ptr
    public static native opencv_xfeatures2d$BriefDescriptorExtractor create();

    @Ptr
    public static native opencv_xfeatures2d$BriefDescriptorExtractor create(int i, @Cast({"bool"}) boolean z);

    static {
        Loader.load();
    }

    public opencv_xfeatures2d$BriefDescriptorExtractor() {
        super((Pointer) null);
        allocate();
    }

    public opencv_xfeatures2d$BriefDescriptorExtractor(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_xfeatures2d$BriefDescriptorExtractor(Pointer p) {
        super(p);
    }

    public opencv_xfeatures2d$BriefDescriptorExtractor position(long position) {
        return (opencv_xfeatures2d$BriefDescriptorExtractor) super.position(position);
    }
}
