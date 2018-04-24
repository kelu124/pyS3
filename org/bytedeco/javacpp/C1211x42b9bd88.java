package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avformat.AVDeviceCapabilitiesQuery;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1211x42b9bd88 extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, AVDeviceCapabilitiesQuery aVDeviceCapabilitiesQuery);

    static {
        Loader.load();
    }

    public C1211x42b9bd88(Pointer p) {
        super(p);
    }

    protected C1211x42b9bd88() {
        allocate();
    }
}
