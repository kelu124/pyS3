package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;

public class avcodec$AVHWAccel$End_frame_AVCodecContext extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext);

    static {
        Loader.load();
    }

    public avcodec$AVHWAccel$End_frame_AVCodecContext(Pointer p) {
        super(p);
    }

    protected avcodec$AVHWAccel$End_frame_AVCodecContext() {
        allocate();
    }
}
