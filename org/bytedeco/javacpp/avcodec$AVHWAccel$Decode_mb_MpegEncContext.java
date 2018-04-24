package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.MpegEncContext;

public class avcodec$AVHWAccel$Decode_mb_MpegEncContext extends FunctionPointer {
    private native void allocate();

    public native void call(MpegEncContext mpegEncContext);

    static {
        Loader.load();
    }

    public avcodec$AVHWAccel$Decode_mb_MpegEncContext(Pointer p) {
        super(p);
    }

    protected avcodec$AVHWAccel$Decode_mb_MpegEncContext() {
        allocate();
    }
}
