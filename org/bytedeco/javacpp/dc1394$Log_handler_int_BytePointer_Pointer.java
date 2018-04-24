package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class dc1394$Log_handler_int_BytePointer_Pointer extends FunctionPointer {
    private native void allocate();

    public native void call(@Cast({"dc1394log_t"}) int i, @Cast({"const char*"}) BytePointer bytePointer, Pointer pointer);

    static {
        Loader.load();
    }

    public dc1394$Log_handler_int_BytePointer_Pointer(Pointer p) {
        super(p);
    }

    protected dc1394$Log_handler_int_BytePointer_Pointer() {
        allocate();
    }
}
