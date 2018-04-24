package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class RealSense$rs_log_callback extends Pointer {
    public native void on_event(@Cast({"rs_log_severity"}) int i, String str);

    public native void on_event(@Cast({"rs_log_severity"}) int i, @Cast({"const char*"}) BytePointer bytePointer);

    public native void release();

    static {
        Loader.load();
    }

    public RealSense$rs_log_callback(Pointer p) {
        super(p);
    }
}
