package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.ValueGetter;
import org.bytedeco.javacpp.opencv_core.CvMat;

@Name({"CvMat*"})
public class opencv_core$CvMatArray extends opencv_core$CvArrArray {
    private native void allocateArray(long j);

    @ValueGetter
    public native CvMat get();

    public opencv_core$CvMatArray(CvMat... array) {
        this((long) array.length);
        put((opencv_core$CvArr[]) array);
        position(0);
    }

    public opencv_core$CvMatArray(long size) {
        super(new opencv_core$CvArr[0]);
        allocateArray(size);
    }

    public opencv_core$CvMatArray(Pointer p) {
        super(p);
    }

    public opencv_core$CvMatArray position(long position) {
        return (opencv_core$CvMatArray) super.position(position);
    }

    public opencv_core$CvMatArray put(opencv_core$CvArr... array) {
        return (opencv_core$CvMatArray) super.put(array);
    }

    public opencv_core$CvMatArray put(opencv_core$CvArr p) {
        if (p instanceof CvMat) {
            return (opencv_core$CvMatArray) super.put(p);
        }
        throw new ArrayStoreException(p.getClass().getName());
    }
}
