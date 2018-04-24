package org.bytedeco.javacpp;

public class avdevice$AVDeviceRect extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native int height();

    public native avdevice$AVDeviceRect height(int i);

    public native int width();

    public native avdevice$AVDeviceRect width(int i);

    public native int m198x();

    public native avdevice$AVDeviceRect m199x(int i);

    public native int m200y();

    public native avdevice$AVDeviceRect m201y(int i);

    static {
        Loader.load();
    }

    public avdevice$AVDeviceRect() {
        super((Pointer) null);
        allocate();
    }

    public avdevice$AVDeviceRect(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public avdevice$AVDeviceRect(Pointer p) {
        super(p);
    }

    public avdevice$AVDeviceRect position(long position) {
        return (avdevice$AVDeviceRect) super.position(position);
    }
}
