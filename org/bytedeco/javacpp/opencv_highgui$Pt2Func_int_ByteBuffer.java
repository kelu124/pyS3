package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.Cast;

public class opencv_highgui$Pt2Func_int_ByteBuffer extends FunctionPointer {
    private native void allocate();

    public native int call(int i, @ByPtrPtr @Cast({"char**"}) ByteBuffer byteBuffer);

    static {
        Loader.load();
    }

    public opencv_highgui$Pt2Func_int_ByteBuffer(Pointer p) {
        super(p);
    }

    protected opencv_highgui$Pt2Func_int_ByteBuffer() {
        allocate();
    }
}
