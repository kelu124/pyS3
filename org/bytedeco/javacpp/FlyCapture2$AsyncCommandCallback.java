package org.bytedeco.javacpp;

import org.bytedeco.javacpp.FlyCapture2.Error;
import org.bytedeco.javacpp.annotation.ByVal;

public class FlyCapture2$AsyncCommandCallback extends FunctionPointer {
    private native void allocate();

    public native void call(@ByVal Error error, Pointer pointer);

    static {
        Loader.load();
    }

    public FlyCapture2$AsyncCommandCallback(Pointer p) {
        super(p);
    }

    protected FlyCapture2$AsyncCommandCallback() {
        allocate();
    }
}
