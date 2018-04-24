package org.bytedeco.javacpp;

import org.bytedeco.javacpp.opencv_objdetect.CvHaarFeature;

public class opencv_objdetect$CvHaarClassifier extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native FloatPointer alpha();

    public native opencv_objdetect$CvHaarClassifier alpha(FloatPointer floatPointer);

    public native int count();

    public native opencv_objdetect$CvHaarClassifier count(int i);

    public native opencv_objdetect$CvHaarClassifier haar_feature(CvHaarFeature cvHaarFeature);

    public native CvHaarFeature haar_feature();

    public native IntPointer left();

    public native opencv_objdetect$CvHaarClassifier left(IntPointer intPointer);

    public native IntPointer right();

    public native opencv_objdetect$CvHaarClassifier right(IntPointer intPointer);

    public native FloatPointer threshold();

    public native opencv_objdetect$CvHaarClassifier threshold(FloatPointer floatPointer);

    static {
        Loader.load();
    }

    public opencv_objdetect$CvHaarClassifier() {
        super((Pointer) null);
        allocate();
    }

    public opencv_objdetect$CvHaarClassifier(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_objdetect$CvHaarClassifier(Pointer p) {
        super(p);
    }

    public opencv_objdetect$CvHaarClassifier position(long position) {
        return (opencv_objdetect$CvHaarClassifier) super.position(position);
    }
}
