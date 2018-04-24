package org.bytedeco.javacpp;

import org.bytedeco.javacpp.helper.opencv_core.AbstractCvPoint3D64f;

public class opencv_core$CvPoint3D64f extends AbstractCvPoint3D64f {
    private native void allocate();

    private native void allocateArray(long j);

    public native double mo4839x();

    public native opencv_core$CvPoint3D64f mo4840x(double d);

    public native double mo4841y();

    public native opencv_core$CvPoint3D64f mo4842y(double d);

    public native double mo4843z();

    public native opencv_core$CvPoint3D64f mo4844z(double d);

    static {
        Loader.load();
    }

    public opencv_core$CvPoint3D64f() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$CvPoint3D64f(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$CvPoint3D64f(Pointer p) {
        super(p);
    }

    public opencv_core$CvPoint3D64f position(long position) {
        return (opencv_core$CvPoint3D64f) super.position(position);
    }
}
