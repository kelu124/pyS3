package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avcodec.AVPacket;

public class C1199x1f13ca66 extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, Pointer pointer, IntPointer intPointer, AVPacket aVPacket);

    static {
        Loader.load();
    }

    public C1199x1f13ca66(Pointer p) {
        super(p);
    }

    protected C1199x1f13ca66() {
        allocate();
    }
}
