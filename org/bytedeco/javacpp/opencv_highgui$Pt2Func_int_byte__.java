package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.Cast;

public class opencv_highgui$Pt2Func_int_byte__ extends FunctionPointer {
    private native void allocate();

    public native int call(int i, @ByPtrPtr @Cast({"char**"}) byte[] bArr);

    static {
        Loader.load();
    }

    public opencv_highgui$Pt2Func_int_byte__(Pointer p) {
        super(p);
    }

    protected opencv_highgui$Pt2Func_int_byte__() {
        allocate();
    }
}
