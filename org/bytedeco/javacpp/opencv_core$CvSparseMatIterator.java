package org.bytedeco.javacpp;

import org.bytedeco.javacpp.opencv_core.CvSparseMat;
import org.bytedeco.javacpp.opencv_core.CvSparseNode;

public class opencv_core$CvSparseMatIterator extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native int curidx();

    public native opencv_core$CvSparseMatIterator curidx(int i);

    public native CvSparseMat mat();

    public native opencv_core$CvSparseMatIterator mat(CvSparseMat cvSparseMat);

    public native opencv_core$CvSparseMatIterator node(CvSparseNode cvSparseNode);

    public native CvSparseNode node();

    static {
        Loader.load();
    }

    public opencv_core$CvSparseMatIterator() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$CvSparseMatIterator(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$CvSparseMatIterator(Pointer p) {
        super(p);
    }

    public opencv_core$CvSparseMatIterator position(long position) {
        return (opencv_core$CvSparseMatIterator) super.position(position);
    }
}
