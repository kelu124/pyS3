package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;

public class C1205xb3bed0dc extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, avcodec$AVCodecContext$Func_AVCodecContext_Pointer org_bytedeco_javacpp_avcodec_AVCodecContext_Func_AVCodecContext_Pointer, Pointer pointer, IntPointer intPointer, int i, int i2);

    static {
        Loader.load();
    }

    public C1205xb3bed0dc(Pointer p) {
        super(p);
    }

    protected C1205xb3bed0dc() {
        allocate();
    }
}
