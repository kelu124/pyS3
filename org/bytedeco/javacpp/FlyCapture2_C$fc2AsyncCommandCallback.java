package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class FlyCapture2_C$fc2AsyncCommandCallback extends FunctionPointer {
    private native void allocate();

    public native void call(@Cast({"fc2Error"}) int i, Pointer pointer);

    static {
        Loader.load();
    }

    public FlyCapture2_C$fc2AsyncCommandCallback(Pointer p) {
        super(p);
    }

    protected FlyCapture2_C$fc2AsyncCommandCallback() {
        allocate();
    }
}
