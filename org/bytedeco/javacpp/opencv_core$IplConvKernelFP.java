package org.bytedeco.javacpp;

public class opencv_core$IplConvKernelFP extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native int anchorX();

    public native opencv_core$IplConvKernelFP anchorX(int i);

    public native int anchorY();

    public native opencv_core$IplConvKernelFP anchorY(int i);

    public native int nCols();

    public native opencv_core$IplConvKernelFP nCols(int i);

    public native int nRows();

    public native opencv_core$IplConvKernelFP nRows(int i);

    public native FloatPointer values();

    public native opencv_core$IplConvKernelFP values(FloatPointer floatPointer);

    static {
        Loader.load();
    }

    public opencv_core$IplConvKernelFP() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$IplConvKernelFP(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$IplConvKernelFP(Pointer p) {
        super(p);
    }

    public opencv_core$IplConvKernelFP position(long position) {
        return (opencv_core$IplConvKernelFP) super.position(position);
    }
}
