package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avutil.AVBufferRef;

public class avutil$Alloc_Pointer_int extends FunctionPointer {
    private native void allocate();

    public native AVBufferRef call(Pointer pointer, int i);

    static {
        Loader.load();
    }

    public avutil$Alloc_Pointer_int(Pointer p) {
        super(p);
    }

    protected avutil$Alloc_Pointer_int() {
        allocate();
    }
}
