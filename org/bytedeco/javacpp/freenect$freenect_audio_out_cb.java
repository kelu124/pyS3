package org.bytedeco.javacpp;

import org.bytedeco.javacpp.freenect.freenect_device;

public class freenect$freenect_audio_out_cb extends FunctionPointer {
    private native void allocate();

    public native void call(freenect_device org_bytedeco_javacpp_freenect_freenect_device, freenect$freenect_sample_51 org_bytedeco_javacpp_freenect_freenect_sample_51, IntPointer intPointer);

    static {
        Loader.load();
    }

    public freenect$freenect_audio_out_cb(Pointer p) {
        super(p);
    }

    protected freenect$freenect_audio_out_cb() {
        allocate();
    }
}
