package org.bytedeco.javacpp;

import org.bytedeco.javacpp.dc1394.dc1394camera_t;

public class dc1394$dc1394capture_callback_t extends FunctionPointer {
    private native void allocate();

    public native void call(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, Pointer pointer);

    static {
        Loader.load();
    }

    public dc1394$dc1394capture_callback_t(Pointer p) {
        super(p);
    }

    protected dc1394$dc1394capture_callback_t() {
        allocate();
    }
}
