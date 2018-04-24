package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class avcodec$Cb_PointerPointer_int extends FunctionPointer {
    private native void allocate();

    public native int call(@Cast({"void**"}) PointerPointer pointerPointer, @Cast({"AVLockOp"}) int i);

    static {
        Loader.load();
    }

    public avcodec$Cb_PointerPointer_int(Pointer p) {
        super(p);
    }

    protected avcodec$Cb_PointerPointer_int() {
        allocate();
    }
}
