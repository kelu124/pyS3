package org.bytedeco.javacpp;

import org.bytedeco.javacpp.opencv_core.CvGraphEdge;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;

public class opencv_core$CvGraphVtx2D extends opencv_core$CvGraphVtx {
    private native void allocate();

    private native void allocateArray(long j);

    public native CvGraphEdge first();

    public native opencv_core$CvGraphVtx2D first(CvGraphEdge cvGraphEdge);

    public native int flags();

    public native opencv_core$CvGraphVtx2D flags(int i);

    public native opencv_core$CvGraphVtx2D ptr(CvPoint2D32f cvPoint2D32f);

    public native CvPoint2D32f ptr();

    static {
        Loader.load();
    }

    public opencv_core$CvGraphVtx2D() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$CvGraphVtx2D(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$CvGraphVtx2D(Pointer p) {
        super(p);
    }

    public opencv_core$CvGraphVtx2D position(long position) {
        return (opencv_core$CvGraphVtx2D) super.position(position);
    }
}
