package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;

public class avutil$Callback_Pointer_int_String_Pointer extends FunctionPointer {
    private native void allocate();

    public native void call(Pointer pointer, int i, String str, @Cast({"va_list*"}) @ByVal Pointer pointer2);

    static {
        Loader.load();
    }

    public avutil$Callback_Pointer_int_String_Pointer(Pointer p) {
        super(p);
    }

    protected avutil$Callback_Pointer_int_String_Pointer() {
        allocate();
    }
}
