package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class avformat$Read_packet_Pointer_BytePointer_int extends FunctionPointer {
    private native void allocate();

    public native int call(Pointer pointer, @Cast({"uint8_t*"}) BytePointer bytePointer, int i);

    static {
        Loader.load();
    }

    public avformat$Read_packet_Pointer_BytePointer_int(Pointer p) {
        super(p);
    }

    protected avformat$Read_packet_Pointer_BytePointer_int() {
        allocate();
    }
}
