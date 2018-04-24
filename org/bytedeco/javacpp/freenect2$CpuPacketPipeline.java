package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;

@Namespace("libfreenect2")
public class freenect2$CpuPacketPipeline extends freenect2$PacketPipeline {
    private native void allocate();

    private native void allocateArray(long j);

    static {
        Loader.load();
    }

    public freenect2$CpuPacketPipeline(Pointer p) {
        super(p);
    }

    public freenect2$CpuPacketPipeline(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public freenect2$CpuPacketPipeline position(long position) {
        return (freenect2$CpuPacketPipeline) super.position(position);
    }

    public freenect2$CpuPacketPipeline() {
        super((Pointer) null);
        allocate();
    }
}
