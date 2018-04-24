package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class avutil$Free_Pointer_BytePointer extends FunctionPointer {
    private native void allocate();

    public native void call(Pointer pointer, @Cast({"uint8_t*"}) BytePointer bytePointer);

    static {
        Loader.load();
    }

    public avutil$Free_Pointer_BytePointer(Pointer p) {
        super(p);
    }

    protected avutil$Free_Pointer_BytePointer() {
        allocate();
    }
}
