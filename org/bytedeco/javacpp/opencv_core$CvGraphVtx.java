package org.bytedeco.javacpp;

import org.bytedeco.javacpp.opencv_core.CvGraphEdge;

public class opencv_core$CvGraphVtx extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native CvGraphEdge first();

    public native opencv_core$CvGraphVtx first(CvGraphEdge cvGraphEdge);

    public native int flags();

    public native opencv_core$CvGraphVtx flags(int i);

    static {
        Loader.load();
    }

    public opencv_core$CvGraphVtx() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$CvGraphVtx(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$CvGraphVtx(Pointer p) {
        super(p);
    }

    public opencv_core$CvGraphVtx position(long position) {
        return (opencv_core$CvGraphVtx) super.position(position);
    }
}
