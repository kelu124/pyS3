package org.bytedeco.javacpp;

import org.bytedeco.javacpp.RealSense.rs_device;
import org.bytedeco.javacpp.RealSense.rs_motion_data;
import org.bytedeco.javacpp.annotation.ByVal;

public class RealSense$rs_device$On_event_rs_device_rs_motion_data_Pointer extends FunctionPointer {
    private native void allocate();

    public native void call(rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByVal rs_motion_data org_bytedeco_javacpp_RealSense_rs_motion_data, Pointer pointer);

    static {
        Loader.load();
    }

    public RealSense$rs_device$On_event_rs_device_rs_motion_data_Pointer(Pointer p) {
        super(p);
    }

    protected RealSense$rs_device$On_event_rs_device_rs_motion_data_Pointer() {
        allocate();
    }
}
