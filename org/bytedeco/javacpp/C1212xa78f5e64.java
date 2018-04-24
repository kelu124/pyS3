package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avformat.AVDeviceInfoList;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1212xa78f5e64 extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, AVDeviceInfoList aVDeviceInfoList);

    static {
        Loader.load();
    }

    public C1212xa78f5e64(Pointer p) {
        super(p);
    }

    protected C1212xa78f5e64() {
        allocate();
    }
}
