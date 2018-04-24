package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avformat.AVDeviceCapabilitiesQuery;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1216xc1643a37 extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, AVDeviceCapabilitiesQuery aVDeviceCapabilitiesQuery);

    static {
        Loader.load();
    }

    public C1216xc1643a37(Pointer p) {
        super(p);
    }

    protected C1216xc1643a37() {
        allocate();
    }
}
