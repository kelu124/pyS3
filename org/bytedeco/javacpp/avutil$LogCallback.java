package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class avutil$LogCallback extends FunctionPointer {
    private native void allocate();

    public native void call(int i, @Cast({"const char*"}) BytePointer bytePointer);

    static {
        Loader.load();
    }

    public avutil$LogCallback(Pointer p) {
        super(p);
    }

    protected avutil$LogCallback() {
        allocate();
    }
}
