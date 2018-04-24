package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;

@Namespace("cv::flann")
public class opencv_flann$KDTreeIndexParams extends opencv_flann$IndexParams {
    private native void allocate();

    private native void allocate(int i);

    private native void allocateArray(long j);

    static {
        Loader.load();
    }

    public opencv_flann$KDTreeIndexParams(Pointer p) {
        super(p);
    }

    public opencv_flann$KDTreeIndexParams(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_flann$KDTreeIndexParams position(long position) {
        return (opencv_flann$KDTreeIndexParams) super.position(position);
    }

    public opencv_flann$KDTreeIndexParams(int trees) {
        super((Pointer) null);
        allocate(trees);
    }

    public opencv_flann$KDTreeIndexParams() {
        super((Pointer) null);
        allocate();
    }
}
