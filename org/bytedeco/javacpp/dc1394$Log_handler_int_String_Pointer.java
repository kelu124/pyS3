package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class dc1394$Log_handler_int_String_Pointer extends FunctionPointer {
    private native void allocate();

    public native void call(@Cast({"dc1394log_t"}) int i, String str, Pointer pointer);

    static {
        Loader.load();
    }

    public dc1394$Log_handler_int_String_Pointer(Pointer p) {
        super(p);
    }

    protected dc1394$Log_handler_int_String_Pointer() {
        allocate();
    }
}
