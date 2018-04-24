package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.Cast;

public class opencv_highgui$Pt2Func_int_BytePointer extends FunctionPointer {
    private native void allocate();

    public native int call(int i, @ByPtrPtr @Cast({"char**"}) BytePointer bytePointer);

    static {
        Loader.load();
    }

    public opencv_highgui$Pt2Func_int_BytePointer(Pointer p) {
        super(p);
    }

    protected opencv_highgui$Pt2Func_int_BytePointer() {
        allocate();
    }
}
