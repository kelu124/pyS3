package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class opencv_core$ErrorCallback extends FunctionPointer {
    private native void allocate();

    public native int call(int i, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, int i2, Pointer pointer);

    static {
        Loader.load();
    }

    public opencv_core$ErrorCallback(Pointer p) {
        super(p);
    }

    protected opencv_core$ErrorCallback() {
        allocate();
    }
}
