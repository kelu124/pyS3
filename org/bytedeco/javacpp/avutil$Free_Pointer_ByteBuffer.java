package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import org.bytedeco.javacpp.annotation.Cast;

public class avutil$Free_Pointer_ByteBuffer extends FunctionPointer {
    private native void allocate();

    public native void call(Pointer pointer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer);

    static {
        Loader.load();
    }

    public avutil$Free_Pointer_ByteBuffer(Pointer p) {
        super(p);
    }

    protected avutil$Free_Pointer_ByteBuffer() {
        allocate();
    }
}
