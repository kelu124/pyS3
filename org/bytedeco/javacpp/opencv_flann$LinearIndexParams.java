package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;

@Namespace("cv::flann")
public class opencv_flann$LinearIndexParams extends opencv_flann$IndexParams {
    private native void allocate();

    private native void allocateArray(long j);

    static {
        Loader.load();
    }

    public opencv_flann$LinearIndexParams(Pointer p) {
        super(p);
    }

    public opencv_flann$LinearIndexParams(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_flann$LinearIndexParams position(long position) {
        return (opencv_flann$LinearIndexParams) super.position(position);
    }

    public opencv_flann$LinearIndexParams() {
        super((Pointer) null);
        allocate();
    }
}
