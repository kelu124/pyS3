package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avutil.AVBufferRef;

public class avutil$Alloc_int extends FunctionPointer {
    private native void allocate();

    public native AVBufferRef call(int i);

    static {
        Loader.load();
    }

    public avutil$Alloc_int(Pointer p) {
        super(p);
    }

    protected avutil$Alloc_int() {
        allocate();
    }
}
