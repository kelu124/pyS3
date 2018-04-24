package org.bytedeco.javacpp;

import org.bytedeco.javacpp.helper.opencv_core.AbstractCvMemStorage;

public class opencv_core$CvMemStorage extends AbstractCvMemStorage {
    private native void allocate();

    private native void allocateArray(long j);

    public native int block_size();

    public native opencv_core$CvMemStorage block_size(int i);

    public native opencv_core$CvMemBlock bottom();

    public native opencv_core$CvMemStorage bottom(opencv_core$CvMemBlock org_bytedeco_javacpp_opencv_core_CvMemBlock);

    public native int free_space();

    public native opencv_core$CvMemStorage free_space(int i);

    public native opencv_core$CvMemStorage parent();

    public native opencv_core$CvMemStorage parent(opencv_core$CvMemStorage org_bytedeco_javacpp_opencv_core_CvMemStorage);

    public native int signature();

    public native opencv_core$CvMemStorage signature(int i);

    public native opencv_core$CvMemBlock top();

    public native opencv_core$CvMemStorage top(opencv_core$CvMemBlock org_bytedeco_javacpp_opencv_core_CvMemBlock);

    static {
        Loader.load();
    }

    public opencv_core$CvMemStorage() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$CvMemStorage(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$CvMemStorage(Pointer p) {
        super(p);
    }

    public opencv_core$CvMemStorage position(long position) {
        return (opencv_core$CvMemStorage) super.position(position);
    }
}
