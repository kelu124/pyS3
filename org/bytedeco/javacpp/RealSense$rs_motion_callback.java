package org.bytedeco.javacpp;

import org.bytedeco.javacpp.RealSense.rs_motion_data;
import org.bytedeco.javacpp.annotation.ByVal;

public class RealSense$rs_motion_callback extends Pointer {
    public native void on_event(@ByVal rs_motion_data org_bytedeco_javacpp_RealSense_rs_motion_data);

    public native void release();

    static {
        Loader.load();
    }

    public RealSense$rs_motion_callback(Pointer p) {
        super(p);
    }
}
