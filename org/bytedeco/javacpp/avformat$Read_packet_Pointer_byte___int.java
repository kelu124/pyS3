package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class avformat$Read_packet_Pointer_byte___int extends FunctionPointer {
    private native void allocate();

    public native int call(Pointer pointer, @Cast({"uint8_t*"}) byte[] bArr, int i);

    static {
        Loader.load();
    }

    public avformat$Read_packet_Pointer_byte___int(Pointer p) {
        super(p);
    }

    protected avformat$Read_packet_Pointer_byte___int() {
        allocate();
    }
}
