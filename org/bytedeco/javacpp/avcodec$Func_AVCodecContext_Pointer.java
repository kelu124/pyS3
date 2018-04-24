package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;

public class avcodec$Func_AVCodecContext_Pointer extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, Pointer pointer);

    static {
        Loader.load();
    }

    public avcodec$Func_AVCodecContext_Pointer(Pointer p) {
        super(p);
    }

    protected avcodec$Func_AVCodecContext_Pointer() {
        allocate();
    }
}
