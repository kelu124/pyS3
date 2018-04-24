package org.bytedeco.javacpp;

public class opencv_core$CvMemStoragePos extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native int free_space();

    public native opencv_core$CvMemStoragePos free_space(int i);

    public native opencv_core$CvMemBlock top();

    public native opencv_core$CvMemStoragePos top(opencv_core$CvMemBlock org_bytedeco_javacpp_opencv_core_CvMemBlock);

    static {
        Loader.load();
    }

    public opencv_core$CvMemStoragePos() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$CvMemStoragePos(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$CvMemStoragePos(Pointer p) {
        super(p);
    }

    public opencv_core$CvMemStoragePos position(long position) {
        return (opencv_core$CvMemStoragePos) super.position(position);
    }
}
