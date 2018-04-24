package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avcodec.AVPacket;

public class avcodec$AVCodec$Receive_packet_AVCodecContext_AVPacket extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, AVPacket aVPacket);

    static {
        Loader.load();
    }

    public avcodec$AVCodec$Receive_packet_AVCodecContext_AVPacket(Pointer p) {
        super(p);
    }

    protected avcodec$AVCodec$Receive_packet_AVCodecContext_AVPacket() {
        allocate();
    }
}
