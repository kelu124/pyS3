package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avformat.AVDeviceInfoList;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1217x818bf8d3 extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, AVDeviceInfoList aVDeviceInfoList);

    static {
        Loader.load();
    }

    public C1217x818bf8d3(Pointer p) {
        super(p);
    }

    protected C1217x818bf8d3() {
        allocate();
    }
}
