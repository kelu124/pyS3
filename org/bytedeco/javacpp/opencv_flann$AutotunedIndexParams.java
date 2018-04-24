package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;

@Namespace("cv::flann")
public class opencv_flann$AutotunedIndexParams extends opencv_flann$IndexParams {
    private native void allocate();

    private native void allocate(float f, float f2, float f3, float f4);

    private native void allocateArray(long j);

    static {
        Loader.load();
    }

    public opencv_flann$AutotunedIndexParams(Pointer p) {
        super(p);
    }

    public opencv_flann$AutotunedIndexParams(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_flann$AutotunedIndexParams position(long position) {
        return (opencv_flann$AutotunedIndexParams) super.position(position);
    }

    public opencv_flann$AutotunedIndexParams(float target_precision, float build_weight, float memory_weight, float sample_fraction) {
        super((Pointer) null);
        allocate(target_precision, build_weight, memory_weight, sample_fraction);
    }

    public opencv_flann$AutotunedIndexParams() {
        super((Pointer) null);
        allocate();
    }
}
