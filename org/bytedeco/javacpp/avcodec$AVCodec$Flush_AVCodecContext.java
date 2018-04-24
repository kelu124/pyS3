package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;

public class avcodec$AVCodec$Flush_AVCodecContext extends FunctionPointer {
    private native void allocate();

    public native void call(AVCodecContext aVCodecContext);

    static {
        Loader.load();
    }

    public avcodec$AVCodec$Flush_AVCodecContext(Pointer p) {
        super(p);
    }

    protected avcodec$AVCodec$Flush_AVCodecContext() {
        allocate();
    }
}
