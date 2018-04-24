package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class PGRFlyCapture$FlyCaptureCallback extends FunctionPointer {
    private native void allocate();

    public native void call(Pointer pointer, int i, @Cast({"unsigned long"}) int i2);

    static {
        Loader.load();
    }

    public PGRFlyCapture$FlyCaptureCallback(Pointer p) {
        super(p);
    }

    protected PGRFlyCapture$FlyCaptureCallback() {
        allocate();
    }
}
