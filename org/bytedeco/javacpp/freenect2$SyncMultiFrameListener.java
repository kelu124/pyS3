package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;

@Namespace("libfreenect2")
@NoOffset
public class freenect2$SyncMultiFrameListener extends freenect2$FrameListener {
    private native void allocate(@Cast({"unsigned int"}) int i);

    @Cast({"bool"})
    public native boolean hasNewFrame();

    @Cast({"bool"})
    public native boolean onNewFrame(@Cast({"libfreenect2::Frame::Type"}) int i, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame);

    public native void release(@ByRef freenect2$FrameMap org_bytedeco_javacpp_freenect2_FrameMap);

    public native void waitForNewFrame(@ByRef freenect2$FrameMap org_bytedeco_javacpp_freenect2_FrameMap);

    @Cast({"bool"})
    public native boolean waitForNewFrame(@ByRef freenect2$FrameMap org_bytedeco_javacpp_freenect2_FrameMap, int i);

    static {
        Loader.load();
    }

    public freenect2$SyncMultiFrameListener(Pointer p) {
        super(p);
    }

    public freenect2$SyncMultiFrameListener(@Cast({"unsigned int"}) int frame_types) {
        super((Pointer) null);
        allocate(frame_types);
    }
}
