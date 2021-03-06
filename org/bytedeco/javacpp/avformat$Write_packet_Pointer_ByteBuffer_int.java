package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import org.bytedeco.javacpp.annotation.Cast;

public class avformat$Write_packet_Pointer_ByteBuffer_int extends FunctionPointer {
    private native void allocate();

    public native int call(Pointer pointer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer, int i);

    static {
        Loader.load();
    }

    public avformat$Write_packet_Pointer_ByteBuffer_int(Pointer p) {
        super(p);
    }

    protected avformat$Write_packet_Pointer_ByteBuffer_int() {
        allocate();
    }
}
