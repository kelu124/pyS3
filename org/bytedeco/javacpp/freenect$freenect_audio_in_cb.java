package org.bytedeco.javacpp;

import org.bytedeco.javacpp.freenect.freenect_device;

public class freenect$freenect_audio_in_cb extends FunctionPointer {
    private native void allocate();

    public native void call(freenect_device org_bytedeco_javacpp_freenect_freenect_device, int i, IntPointer intPointer, IntPointer intPointer2, IntPointer intPointer3, IntPointer intPointer4, ShortPointer shortPointer, Pointer pointer);

    static {
        Loader.load();
    }

    public freenect$freenect_audio_in_cb(Pointer p) {
        super(p);
    }

    protected freenect$freenect_audio_in_cb() {
        allocate();
    }
}
