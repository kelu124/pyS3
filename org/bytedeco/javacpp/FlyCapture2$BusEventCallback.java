package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class FlyCapture2$BusEventCallback extends FunctionPointer {
    private native void allocate();

    public native void call(Pointer pointer, @Cast({"unsigned int"}) int i);

    static {
        Loader.load();
    }

    public FlyCapture2$BusEventCallback(Pointer p) {
        super(p);
    }

    protected FlyCapture2$BusEventCallback() {
        allocate();
    }
}
