package org.bytedeco.javacpp;

import org.bytedeco.javacpp.FlyCapture2_C.fc2Image;

public class FlyCapture2_C$fc2ImageEventCallback extends FunctionPointer {
    private native void allocate();

    public native void call(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, Pointer pointer);

    static {
        Loader.load();
    }

    public FlyCapture2_C$fc2ImageEventCallback(Pointer p) {
        super(p);
    }

    protected FlyCapture2_C$fc2ImageEventCallback() {
        allocate();
    }
}
