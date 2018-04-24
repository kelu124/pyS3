package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.avcodec.AVCodecContext;

public class avcodec$AVCodecParser$Split_AVCodecContext_BytePointer_int extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, @Cast({"const uint8_t*"}) BytePointer bytePointer, int i);

    static {
        Loader.load();
    }

    public avcodec$AVCodecParser$Split_AVCodecContext_BytePointer_int(Pointer p) {
        super(p);
    }

    protected avcodec$AVCodecParser$Split_AVCodecContext_BytePointer_int() {
        allocate();
    }
}
