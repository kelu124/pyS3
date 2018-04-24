package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.freenect.freenect_device;

public class freenect$freenect_video_cb extends FunctionPointer {
    private native void allocate();

    public native void call(freenect_device org_bytedeco_javacpp_freenect_freenect_device, Pointer pointer, @Cast({"uint32_t"}) int i);

    static {
        Loader.load();
    }

    public freenect$freenect_video_cb(Pointer p) {
        super(p);
    }

    protected freenect$freenect_video_cb() {
        allocate();
    }
}
