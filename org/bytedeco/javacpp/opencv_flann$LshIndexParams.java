package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;

@Namespace("cv::flann")
public class opencv_flann$LshIndexParams extends opencv_flann$IndexParams {
    private native void allocate(int i, int i2, int i3);

    static {
        Loader.load();
    }

    public opencv_flann$LshIndexParams(Pointer p) {
        super(p);
    }

    public opencv_flann$LshIndexParams(int table_number, int key_size, int multi_probe_level) {
        super((Pointer) null);
        allocate(table_number, key_size, multi_probe_level);
    }
}
