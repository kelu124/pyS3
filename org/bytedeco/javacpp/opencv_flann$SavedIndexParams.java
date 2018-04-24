package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.presets.opencv_core.Str;

@Namespace("cv::flann")
public class opencv_flann$SavedIndexParams extends opencv_flann$IndexParams {
    private native void allocate(@Str String str);

    private native void allocate(@Str BytePointer bytePointer);

    static {
        Loader.load();
    }

    public opencv_flann$SavedIndexParams(Pointer p) {
        super(p);
    }

    public opencv_flann$SavedIndexParams(@Str BytePointer filename) {
        super((Pointer) null);
        allocate(filename);
    }

    public opencv_flann$SavedIndexParams(@Str String filename) {
        super((Pointer) null);
        allocate(filename);
    }
}
