package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;

@Namespace("libfreenect2")
@NoOffset
public class freenect2$PacketPipeline extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native freenect2$DepthPacketProcessor getDepthPacketProcessor();

    @Cast({"libfreenect2::PacketPipeline::PacketParser*"})
    public native freenect2$DataCallback getIrPacketParser();

    @Cast({"libfreenect2::PacketPipeline::PacketParser*"})
    public native freenect2$DataCallback getRgbPacketParser();

    public native freenect2$RgbPacketProcessor getRgbPacketProcessor();

    static {
        Loader.load();
    }

    public freenect2$PacketPipeline(Pointer p) {
        super(p);
    }

    public freenect2$PacketPipeline(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public freenect2$PacketPipeline position(long position) {
        return (freenect2$PacketPipeline) super.position(position);
    }

    public freenect2$PacketPipeline() {
        super((Pointer) null);
        allocate();
    }
}
