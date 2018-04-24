package org.bytedeco.javacpp;

public class freenect2$Freenect2Device$IrCameraParams extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native float cx();

    public native freenect2$Freenect2Device$IrCameraParams cx(float f);

    public native float cy();

    public native freenect2$Freenect2Device$IrCameraParams cy(float f);

    public native float fx();

    public native freenect2$Freenect2Device$IrCameraParams fx(float f);

    public native float fy();

    public native freenect2$Freenect2Device$IrCameraParams fy(float f);

    public native float k1();

    public native freenect2$Freenect2Device$IrCameraParams k1(float f);

    public native float k2();

    public native freenect2$Freenect2Device$IrCameraParams k2(float f);

    public native float k3();

    public native freenect2$Freenect2Device$IrCameraParams k3(float f);

    public native float p1();

    public native freenect2$Freenect2Device$IrCameraParams p1(float f);

    public native float p2();

    public native freenect2$Freenect2Device$IrCameraParams p2(float f);

    static {
        Loader.load();
    }

    public freenect2$Freenect2Device$IrCameraParams() {
        super((Pointer) null);
        allocate();
    }

    public freenect2$Freenect2Device$IrCameraParams(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public freenect2$Freenect2Device$IrCameraParams(Pointer p) {
        super(p);
    }

    public freenect2$Freenect2Device$IrCameraParams position(long position) {
        return (freenect2$Freenect2Device$IrCameraParams) super.position(position);
    }
}
