package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.ValueGetter;
import org.bytedeco.javacpp.opencv_core.CvMatND;

@Name({"CvMatND*"})
public class opencv_core$CvMatNDArray extends opencv_core$CvArrArray {
    private native void allocateArray(long j);

    @ValueGetter
    public native CvMatND get();

    public opencv_core$CvMatNDArray(CvMatND... array) {
        this((long) array.length);
        put((opencv_core$CvArr[]) array);
        position(0);
    }

    public opencv_core$CvMatNDArray(long size) {
        super(new opencv_core$CvArr[0]);
        allocateArray(size);
    }

    public opencv_core$CvMatNDArray(Pointer p) {
        super(p);
    }

    public opencv_core$CvMatNDArray position(long position) {
        return (opencv_core$CvMatNDArray) super.position(position);
    }

    public opencv_core$CvMatNDArray put(opencv_core$CvArr... array) {
        return (opencv_core$CvMatNDArray) super.put(array);
    }

    public opencv_core$CvMatNDArray put(opencv_core$CvArr p) {
        if (p instanceof CvMatND) {
            return (opencv_core$CvMatNDArray) super.put(p);
        }
        throw new ArrayStoreException(p.getClass().getName());
    }
}
