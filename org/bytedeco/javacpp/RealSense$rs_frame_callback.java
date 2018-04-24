package org.bytedeco.javacpp;

import org.bytedeco.javacpp.RealSense.rs_device;
import org.bytedeco.javacpp.RealSense.rs_frame_ref;

public class RealSense$rs_frame_callback extends Pointer {
    public native void on_frame(rs_device org_bytedeco_javacpp_RealSense_rs_device, rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref);

    public native void release();

    static {
        Loader.load();
    }

    public RealSense$rs_frame_callback(Pointer p) {
        super(p);
    }
}
