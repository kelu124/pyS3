package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.Name;

@Name({"CvArr*"})
public class opencv_core$CvArrArray extends PointerPointer<opencv_core$CvArr> {
    private native void allocateArray(long j);

    public native opencv_core$CvArr get();

    public native opencv_core$CvArrArray put(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    static {
        Loader.load();
    }

    public opencv_core$CvArrArray(opencv_core$CvArr... array) {
        this((long) array.length);
        put(array);
        position(0);
    }

    public opencv_core$CvArrArray(long size) {
        super(size);
        allocateArray(size);
    }

    public opencv_core$CvArrArray(Pointer p) {
        super(p);
    }

    public opencv_core$CvArrArray position(long position) {
        return (opencv_core$CvArrArray) super.position(position);
    }

    public opencv_core$CvArrArray put(opencv_core$CvArr... array) {
        for (int i = 0; i < array.length; i++) {
            position((long) i).put(array[i]);
        }
        return this;
    }
}
