package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avcodec.AVPacket;

public class avcodec$AVCodec$Send_packet_AVCodecContext_AVPacket extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, @Const AVPacket aVPacket);

    static {
        Loader.load();
    }

    public avcodec$AVCodec$Send_packet_AVCodecContext_AVPacket(Pointer p) {
        super(p);
    }

    protected avcodec$AVCodec$Send_packet_AVCodecContext_AVPacket() {
        allocate();
    }
}
