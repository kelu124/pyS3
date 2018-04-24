package org.bytedeco.javacpp;

public class freenect2$Freenect2Device$ColorCameraParams extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native float cx();

    public native freenect2$Freenect2Device$ColorCameraParams cx(float f);

    public native float cy();

    public native freenect2$Freenect2Device$ColorCameraParams cy(float f);

    public native float fx();

    public native freenect2$Freenect2Device$ColorCameraParams fx(float f);

    public native float fy();

    public native freenect2$Freenect2Device$ColorCameraParams fy(float f);

    public native float mx_x0y0();

    public native freenect2$Freenect2Device$ColorCameraParams mx_x0y0(float f);

    public native float mx_x0y1();

    public native freenect2$Freenect2Device$ColorCameraParams mx_x0y1(float f);

    public native float mx_x0y2();

    public native freenect2$Freenect2Device$ColorCameraParams mx_x0y2(float f);

    public native float mx_x0y3();

    public native freenect2$Freenect2Device$ColorCameraParams mx_x0y3(float f);

    public native float mx_x1y0();

    public native freenect2$Freenect2Device$ColorCameraParams mx_x1y0(float f);

    public native float mx_x1y1();

    public native freenect2$Freenect2Device$ColorCameraParams mx_x1y1(float f);

    public native float mx_x1y2();

    public native freenect2$Freenect2Device$ColorCameraParams mx_x1y2(float f);

    public native float mx_x2y0();

    public native freenect2$Freenect2Device$ColorCameraParams mx_x2y0(float f);

    public native float mx_x2y1();

    public native freenect2$Freenect2Device$ColorCameraParams mx_x2y1(float f);

    public native float mx_x3y0();

    public native freenect2$Freenect2Device$ColorCameraParams mx_x3y0(float f);

    public native float my_x0y0();

    public native freenect2$Freenect2Device$ColorCameraParams my_x0y0(float f);

    public native float my_x0y1();

    public native freenect2$Freenect2Device$ColorCameraParams my_x0y1(float f);

    public native float my_x0y2();

    public native freenect2$Freenect2Device$ColorCameraParams my_x0y2(float f);

    public native float my_x0y3();

    public native freenect2$Freenect2Device$ColorCameraParams my_x0y3(float f);

    public native float my_x1y0();

    public native freenect2$Freenect2Device$ColorCameraParams my_x1y0(float f);

    public native float my_x1y1();

    public native freenect2$Freenect2Device$ColorCameraParams my_x1y1(float f);

    public native float my_x1y2();

    public native freenect2$Freenect2Device$ColorCameraParams my_x1y2(float f);

    public native float my_x2y0();

    public native freenect2$Freenect2Device$ColorCameraParams my_x2y0(float f);

    public native float my_x2y1();

    public native freenect2$Freenect2Device$ColorCameraParams my_x2y1(float f);

    public native float my_x3y0();

    public native freenect2$Freenect2Device$ColorCameraParams my_x3y0(float f);

    public native float shift_d();

    public native freenect2$Freenect2Device$ColorCameraParams shift_d(float f);

    public native float shift_m();

    public native freenect2$Freenect2Device$ColorCameraParams shift_m(float f);

    static {
        Loader.load();
    }

    public freenect2$Freenect2Device$ColorCameraParams() {
        super((Pointer) null);
        allocate();
    }

    public freenect2$Freenect2Device$ColorCameraParams(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public freenect2$Freenect2Device$ColorCameraParams(Pointer p) {
        super(p);
    }

    public freenect2$Freenect2Device$ColorCameraParams position(long position) {
        return (freenect2$Freenect2Device$ColorCameraParams) super.position(position);
    }
}
