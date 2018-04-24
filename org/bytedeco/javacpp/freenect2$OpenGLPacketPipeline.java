package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;

@Namespace("libfreenect2")
@NoOffset
public class freenect2$OpenGLPacketPipeline extends freenect2$PacketPipeline {
    private native void allocate();

    private native void allocate(Pointer pointer, @Cast({"bool"}) boolean z);

    private native void allocateArray(long j);

    static {
        Loader.load();
    }

    public freenect2$OpenGLPacketPipeline(Pointer p) {
        super(p);
    }

    public freenect2$OpenGLPacketPipeline(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public freenect2$OpenGLPacketPipeline position(long position) {
        return (freenect2$OpenGLPacketPipeline) super.position(position);
    }

    public freenect2$OpenGLPacketPipeline(Pointer parent_opengl_context, @Cast({"bool"}) boolean debug) {
        super((Pointer) null);
        allocate(parent_opengl_context, debug);
    }

    public freenect2$OpenGLPacketPipeline() {
        super((Pointer) null);
        allocate();
    }
}
