package org.bytedeco.javacpp;

public class opencv_core$CvSetElem extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native int flags();

    public native opencv_core$CvSetElem flags(int i);

    public native opencv_core$CvSetElem next_free();

    public native opencv_core$CvSetElem next_free(opencv_core$CvSetElem org_bytedeco_javacpp_opencv_core_CvSetElem);

    static {
        Loader.load();
    }

    public opencv_core$CvSetElem() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$CvSetElem(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$CvSetElem(Pointer p) {
        super(p);
    }

    public opencv_core$CvSetElem position(long position) {
        return (opencv_core$CvSetElem) super.position(position);
    }
}
