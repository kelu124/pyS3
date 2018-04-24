package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.ValueGetter;
import org.bytedeco.javacpp.opencv_core.IplImage;

@Name({"IplImage*"})
public class opencv_core$IplImageArray extends opencv_core$CvArrArray {
    private native void allocateArray(long j);

    @ValueGetter
    public native IplImage get();

    public opencv_core$IplImageArray(IplImage... array) {
        this((long) array.length);
        put((opencv_core$CvArr[]) array);
        position(0);
    }

    public opencv_core$IplImageArray(long size) {
        super(new opencv_core$CvArr[0]);
        allocateArray(size);
    }

    public opencv_core$IplImageArray(Pointer p) {
        super(p);
    }

    public opencv_core$IplImageArray position(long position) {
        return (opencv_core$IplImageArray) super.position(position);
    }

    public opencv_core$IplImageArray put(opencv_core$CvArr... array) {
        return (opencv_core$IplImageArray) super.put(array);
    }

    public opencv_core$IplImageArray put(opencv_core$CvArr p) {
        if (p instanceof IplImage) {
            return (opencv_core$IplImageArray) super.put(p);
        }
        throw new ArrayStoreException(p.getClass().getName());
    }
}
