package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;

public class avcodec$AVCodec$Init_AVCodecContext extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext);

    static {
        Loader.load();
    }

    public avcodec$AVCodec$Init_AVCodecContext(Pointer p) {
        super(p);
    }

    protected avcodec$AVCodec$Init_AVCodecContext() {
        allocate();
    }
}
