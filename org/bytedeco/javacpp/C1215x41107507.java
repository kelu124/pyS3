package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avformat.AVDeviceCapabilitiesQuery;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1215x41107507 extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, AVDeviceCapabilitiesQuery aVDeviceCapabilitiesQuery);

    static {
        Loader.load();
    }

    public C1215x41107507(Pointer p) {
        super(p);
    }

    protected C1215x41107507() {
        allocate();
    }
}
