package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;

public class C1204xc596acba extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, avcodec$AVCodecContext$Func_AVCodecContext_Pointer_int_int org_bytedeco_javacpp_avcodec_AVCodecContext_Func_AVCodecContext_Pointer_int_int, Pointer pointer, IntPointer intPointer, int i);

    static {
        Loader.load();
    }

    public C1204xc596acba(Pointer p) {
        super(p);
    }

    protected C1204xc596acba() {
        allocate();
    }
}
