package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Namespace;

@Namespace("libfreenect2")
public class freenect2$DumpPacketPipeline extends freenect2$PacketPipeline {
    private native void allocate();

    private native void allocateArray(long j);

    @Const
    public native ShortPointer getDepthLookupTable(@Cast({"size_t*"}) SizeTPointer sizeTPointer);

    @Cast({"const unsigned char*"})
    public native BytePointer getDepthP0Tables(@Cast({"size_t*"}) SizeTPointer sizeTPointer);

    @Const
    public native FloatPointer getDepthXTable(@Cast({"size_t*"}) SizeTPointer sizeTPointer);

    @Const
    public native FloatPointer getDepthZTable(@Cast({"size_t*"}) SizeTPointer sizeTPointer);

    static {
        Loader.load();
    }

    public freenect2$DumpPacketPipeline(Pointer p) {
        super(p);
    }

    public freenect2$DumpPacketPipeline(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public freenect2$DumpPacketPipeline position(long position) {
        return (freenect2$DumpPacketPipeline) super.position(position);
    }

    public freenect2$DumpPacketPipeline() {
        super((Pointer) null);
        allocate();
    }
}
