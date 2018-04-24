package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;

@Namespace("libfreenect2")
public class freenect2$FrameListener extends Pointer {
    @Cast({"bool"})
    public native boolean onNewFrame(@Cast({"libfreenect2::Frame::Type"}) int i, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame);

    static {
        Loader.load();
    }

    public freenect2$FrameListener(Pointer p) {
        super(p);
    }
}
