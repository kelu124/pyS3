package org.bytedeco.javacpp;

public class opencv_imgproc$CvHuMoments extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native double hu1();

    public native opencv_imgproc$CvHuMoments hu1(double d);

    public native double hu2();

    public native opencv_imgproc$CvHuMoments hu2(double d);

    public native double hu3();

    public native opencv_imgproc$CvHuMoments hu3(double d);

    public native double hu4();

    public native opencv_imgproc$CvHuMoments hu4(double d);

    public native double hu5();

    public native opencv_imgproc$CvHuMoments hu5(double d);

    public native double hu6();

    public native opencv_imgproc$CvHuMoments hu6(double d);

    public native double hu7();

    public native opencv_imgproc$CvHuMoments hu7(double d);

    static {
        Loader.load();
    }

    public opencv_imgproc$CvHuMoments() {
        super((Pointer) null);
        allocate();
    }

    public opencv_imgproc$CvHuMoments(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_imgproc$CvHuMoments(Pointer p) {
        super(p);
    }

    public opencv_imgproc$CvHuMoments position(long position) {
        return (opencv_imgproc$CvHuMoments) super.position(position);
    }
}
