package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;

public class avcodec$Func_AVCodecContext_Pointer_int_int extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, Pointer pointer, int i, int i2);

    static {
        Loader.load();
    }

    public avcodec$Func_AVCodecContext_Pointer_int_int(Pointer p) {
        super(p);
    }

    protected avcodec$Func_AVCodecContext_Pointer_int_int() {
        allocate();
    }
}
