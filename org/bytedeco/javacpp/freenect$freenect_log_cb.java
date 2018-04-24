package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.freenect.freenect_context;

public class freenect$freenect_log_cb extends FunctionPointer {
    private native void allocate();

    public native void call(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"freenect_loglevel"}) int i, @Cast({"const char*"}) BytePointer bytePointer);

    static {
        Loader.load();
    }

    public freenect$freenect_log_cb(Pointer p) {
        super(p);
    }

    protected freenect$freenect_log_cb() {
        allocate();
    }
}
