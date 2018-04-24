package org.bytedeco.javacpp;

import org.bytedeco.javacpp.helper.opencv_core.AbstractCvGraphScanner;
import org.bytedeco.javacpp.opencv_core.CvGraph;
import org.bytedeco.javacpp.opencv_core.CvGraphEdge;
import org.bytedeco.javacpp.opencv_core.CvSeq;

public class opencv_core$CvGraphScanner extends AbstractCvGraphScanner {
    private native void allocate();

    private native void allocateArray(long j);

    public native opencv_core$CvGraphScanner dst(opencv_core$CvGraphVtx org_bytedeco_javacpp_opencv_core_CvGraphVtx);

    public native opencv_core$CvGraphVtx dst();

    public native CvGraphEdge edge();

    public native opencv_core$CvGraphScanner edge(CvGraphEdge cvGraphEdge);

    public native CvGraph graph();

    public native opencv_core$CvGraphScanner graph(CvGraph cvGraph);

    public native int index();

    public native opencv_core$CvGraphScanner index(int i);

    public native int mask();

    public native opencv_core$CvGraphScanner mask(int i);

    public native opencv_core$CvGraphScanner stack(CvSeq cvSeq);

    public native CvSeq stack();

    public native opencv_core$CvGraphScanner vtx(opencv_core$CvGraphVtx org_bytedeco_javacpp_opencv_core_CvGraphVtx);

    public native opencv_core$CvGraphVtx vtx();

    static {
        Loader.load();
    }

    public opencv_core$CvGraphScanner() {
        super((Pointer) null);
        allocate();
    }

    public opencv_core$CvGraphScanner(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_core$CvGraphScanner(Pointer p) {
        super(p);
    }

    public opencv_core$CvGraphScanner position(long position) {
        return (opencv_core$CvGraphScanner) super.position(position);
    }
}
