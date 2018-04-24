package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.Cast;

public class avcodec$Cb_Pointer_int extends FunctionPointer {
    private native void allocate();

    public native int call(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"AVLockOp"}) int i);

    static {
        Loader.load();
    }

    public avcodec$Cb_Pointer_int(Pointer p) {
        super(p);
    }

    protected avcodec$Cb_Pointer_int() {
        allocate();
    }
}
