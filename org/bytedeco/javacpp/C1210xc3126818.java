package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avformat.AVDeviceCapabilitiesQuery;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1210xc3126818 extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, AVDeviceCapabilitiesQuery aVDeviceCapabilitiesQuery);

    static {
        Loader.load();
    }

    public C1210xc3126818(Pointer p) {
        super(p);
    }

    protected C1210xc3126818() {
        allocate();
    }
}
