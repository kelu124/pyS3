package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;

public class avcodec$AVHWAccel$Uninit_AVCodecContext extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext);

    static {
        Loader.load();
    }

    public avcodec$AVHWAccel$Uninit_AVCodecContext(Pointer p) {
        super(p);
    }

    protected avcodec$AVHWAccel$Uninit_AVCodecContext() {
        allocate();
    }
}
