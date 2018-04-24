package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class avutil$Free_Pointer_byte__ extends FunctionPointer {
    private native void allocate();

    public native void call(Pointer pointer, @Cast({"uint8_t*"}) byte[] bArr);

    static {
        Loader.load();
    }

    public avutil$Free_Pointer_byte__(Pointer p) {
        super(p);
    }

    protected avutil$Free_Pointer_byte__() {
        allocate();
    }
}
