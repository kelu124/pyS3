package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class opencv_highgui$Pt2Func_int_PointerPointer extends FunctionPointer {
    private native void allocate();

    public native int call(int i, @Cast({"char**"}) PointerPointer pointerPointer);

    static {
        Loader.load();
    }

    public opencv_highgui$Pt2Func_int_PointerPointer(Pointer p) {
        super(p);
    }

    protected opencv_highgui$Pt2Func_int_PointerPointer() {
        allocate();
    }
}
