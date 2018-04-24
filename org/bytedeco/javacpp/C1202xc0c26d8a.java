package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.avcodec.AVCodecContext;

public class C1202xc0c26d8a extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, @Const AVCodecContext aVCodecContext2);

    static {
        Loader.load();
    }

    public C1202xc0c26d8a(Pointer p) {
        super(p);
    }

    protected C1202xc0c26d8a() {
        allocate();
    }
}
