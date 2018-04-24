package org.bytedeco.javacpp;

public class opencv_core$CvMemBlock extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native opencv_core$CvMemBlock next();

    public native opencv_core$CvMemBlock next(opencv_core$CvMemBlock org_bytedeco_javacpp_opencv_core_CvMemBlock);

    public native opencv_core$CvMemBlock prev();

    public native opencv_core$CvMemBlock prev(opencv_core$CvMemBlock org_bytedeco_javacpp_opencv_core_CvMemBlock);

    static {
        Loader.load();
    }

    public opencv_core$CvMemBlock() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$CvMemBlock(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$CvMemBlock(Pointer p) {
        super(p);
    }

    public opencv_core$CvMemBlock position(long position) {
        return (opencv_core$CvMemBlock) super.position(position);
    }
}
