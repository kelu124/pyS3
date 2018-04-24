package org.bytedeco.javacpp;

import org.bytedeco.javacpp.helper.opencv_imgproc.AbstractIplConvKernel;

public class opencv_core$IplConvKernel extends AbstractIplConvKernel {
    private native void allocate();

    private native void allocateArray(long j);

    public native int anchorX();

    public native opencv_core$IplConvKernel anchorX(int i);

    public native int anchorY();

    public native opencv_core$IplConvKernel anchorY(int i);

    public native int nCols();

    public native opencv_core$IplConvKernel nCols(int i);

    public native int nRows();

    public native opencv_core$IplConvKernel nRows(int i);

    public native int nShiftR();

    public native opencv_core$IplConvKernel nShiftR(int i);

    public native IntPointer values();

    public native opencv_core$IplConvKernel values(IntPointer intPointer);

    static {
        Loader.load();
    }

    public opencv_core$IplConvKernel() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$IplConvKernel(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$IplConvKernel(Pointer p) {
        super(p);
    }

    public opencv_core$IplConvKernel position(long position) {
        return (opencv_core$IplConvKernel) super.position(position);
    }
}
