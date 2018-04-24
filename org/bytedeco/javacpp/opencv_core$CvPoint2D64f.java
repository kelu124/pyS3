package org.bytedeco.javacpp;

import org.bytedeco.javacpp.helper.opencv_core.AbstractCvPoint2D64f;

public class opencv_core$CvPoint2D64f extends AbstractCvPoint2D64f {
    private native void allocate();

    private native void allocateArray(long j);

    public native double mo4835x();

    public native opencv_core$CvPoint2D64f mo4836x(double d);

    public native double mo4837y();

    public native opencv_core$CvPoint2D64f mo4838y(double d);

    static {
        Loader.load();
    }

    public opencv_core$CvPoint2D64f() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$CvPoint2D64f(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$CvPoint2D64f(Pointer p) {
        super(p);
    }

    public opencv_core$CvPoint2D64f position(long position) {
        return (opencv_core$CvPoint2D64f) super.position(position);
    }
}
