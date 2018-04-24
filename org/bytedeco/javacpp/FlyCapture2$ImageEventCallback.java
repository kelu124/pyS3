package org.bytedeco.javacpp;

import org.bytedeco.javacpp.FlyCapture2.Image;
import org.bytedeco.javacpp.annotation.Const;

public class FlyCapture2$ImageEventCallback extends FunctionPointer {
    private native void allocate();

    public native void call(Image image, @Const Pointer pointer);

    static {
        Loader.load();
    }

    public FlyCapture2$ImageEventCallback(Pointer p) {
        super(p);
    }

    protected FlyCapture2$ImageEventCallback() {
        allocate();
    }
}
