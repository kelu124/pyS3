package org.bytedeco.javacpp;

public class opencv_objdetect$CvHaarStageClassifier extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native int child();

    public native opencv_objdetect$CvHaarStageClassifier child(int i);

    public native opencv_objdetect$CvHaarClassifier classifier();

    public native opencv_objdetect$CvHaarStageClassifier classifier(opencv_objdetect$CvHaarClassifier org_bytedeco_javacpp_opencv_objdetect_CvHaarClassifier);

    public native int count();

    public native opencv_objdetect$CvHaarStageClassifier count(int i);

    public native int next();

    public native opencv_objdetect$CvHaarStageClassifier next(int i);

    public native int parent();

    public native opencv_objdetect$CvHaarStageClassifier parent(int i);

    public native float threshold();

    public native opencv_objdetect$CvHaarStageClassifier threshold(float f);

    static {
        Loader.load();
    }

    public opencv_objdetect$CvHaarStageClassifier() {
        super((Pointer) null);
        allocate();
    }

    public opencv_objdetect$CvHaarStageClassifier(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_objdetect$CvHaarStageClassifier(Pointer p) {
        super(p);
    }

    public opencv_objdetect$CvHaarStageClassifier position(long position) {
        return (opencv_objdetect$CvHaarStageClassifier) super.position(position);
    }
}
