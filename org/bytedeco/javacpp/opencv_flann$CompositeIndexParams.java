package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;

@Namespace("cv::flann")
public class opencv_flann$CompositeIndexParams extends opencv_flann$IndexParams {
    private native void allocate();

    private native void allocate(int i, int i2, int i3, @Cast({"cvflann::flann_centers_init_t"}) int i4, float f);

    private native void allocateArray(long j);

    static {
        Loader.load();
    }

    public opencv_flann$CompositeIndexParams(Pointer p) {
        super(p);
    }

    public opencv_flann$CompositeIndexParams(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_flann$CompositeIndexParams position(long position) {
        return (opencv_flann$CompositeIndexParams) super.position(position);
    }

    public opencv_flann$CompositeIndexParams(int trees, int branching, int iterations, @Cast({"cvflann::flann_centers_init_t"}) int centers_init, float cb_index) {
        super((Pointer) null);
        allocate(trees, branching, iterations, centers_init, cb_index);
    }

    public opencv_flann$CompositeIndexParams() {
        super((Pointer) null);
        allocate();
    }
}
