package org.bytedeco.javacpp;

public class opencv_core$IplROI extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native int coi();

    public native opencv_core$IplROI coi(int i);

    public native int height();

    public native opencv_core$IplROI height(int i);

    public native int width();

    public native opencv_core$IplROI width(int i);

    public native int xOffset();

    public native opencv_core$IplROI xOffset(int i);

    public native int yOffset();

    public native opencv_core$IplROI yOffset(int i);

    static {
        Loader.load();
    }

    public opencv_core$IplROI() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$IplROI(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$IplROI(Pointer p) {
        super(p);
    }

    public opencv_core$IplROI position(long position) {
        return (opencv_core$IplROI) super.position(position);
    }
}
