package org.bytedeco.javacpp;

import org.bytedeco.javacpp.RealSense.rs_device;
import org.bytedeco.javacpp.RealSense.rs_frame_ref;

public class RealSense$rs_device$On_frame_rs_device_rs_frame_ref_Pointer extends FunctionPointer {
    private native void allocate();

    public native void call(rs_device org_bytedeco_javacpp_RealSense_rs_device, rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, Pointer pointer);

    static {
        Loader.load();
    }

    public RealSense$rs_device$On_frame_rs_device_rs_frame_ref_Pointer(Pointer p) {
        super(p);
    }

    protected RealSense$rs_device$On_frame_rs_device_rs_frame_ref_Pointer() {
        allocate();
    }
}
