package org.bytedeco.javacpp;

import org.bytedeco.javacpp.opencv_core.CvPoint;

public class opencv_imgproc$CvConvexityDefect extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native float depth();

    public native opencv_imgproc$CvConvexityDefect depth(float f);

    public native CvPoint depth_point();

    public native opencv_imgproc$CvConvexityDefect depth_point(CvPoint cvPoint);

    public native CvPoint end();

    public native opencv_imgproc$CvConvexityDefect end(CvPoint cvPoint);

    public native CvPoint start();

    public native opencv_imgproc$CvConvexityDefect start(CvPoint cvPoint);

    static {
        Loader.load();
    }

    public opencv_imgproc$CvConvexityDefect() {
        super((Pointer) null);
        allocate();
    }

    public opencv_imgproc$CvConvexityDefect(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_imgproc$CvConvexityDefect(Pointer p) {
        super(p);
    }

    public opencv_imgproc$CvConvexityDefect position(long position) {
        return (opencv_imgproc$CvConvexityDefect) super.position(position);
    }
}
