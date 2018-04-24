package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVBSFContext;
import org.bytedeco.javacpp.avcodec.AVPacket;

public class avcodec$AVBitStreamFilter$Filter_AVBSFContext_AVPacket extends FunctionPointer {
    private native void allocate();

    public native int call(AVBSFContext aVBSFContext, AVPacket aVPacket);

    static {
        Loader.load();
    }

    public avcodec$AVBitStreamFilter$Filter_AVBSFContext_AVPacket(Pointer p) {
        super(p);
    }

    protected avcodec$AVBitStreamFilter$Filter_AVBSFContext_AVPacket() {
        allocate();
    }
}
