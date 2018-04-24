package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class RealSense$On_log_int_String_Pointer extends FunctionPointer {
    private native void allocate();

    public native void call(@Cast({"rs_log_severity"}) int i, String str, Pointer pointer);

    static {
        Loader.load();
    }

    public RealSense$On_log_int_String_Pointer(Pointer p) {
        super(p);
    }

    protected RealSense$On_log_int_String_Pointer() {
        allocate();
    }
}
