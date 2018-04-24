package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;

@Namespace("cv::flann")
public class opencv_flann$SearchParams extends opencv_flann$IndexParams {
    private native void allocate();

    private native void allocate(int i, float f, @Cast({"bool"}) boolean z);

    private native void allocateArray(long j);

    static {
        Loader.load();
    }

    public opencv_flann$SearchParams(Pointer p) {
        super(p);
    }

    public opencv_flann$SearchParams(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_flann$SearchParams position(long position) {
        return (opencv_flann$SearchParams) super.position(position);
    }

    public opencv_flann$SearchParams(int checks, float eps, @Cast({"bool"}) boolean sorted) {
        super((Pointer) null);
        allocate(checks, eps, sorted);
    }

    public opencv_flann$SearchParams() {
        super((Pointer) null);
        allocate();
    }
}
