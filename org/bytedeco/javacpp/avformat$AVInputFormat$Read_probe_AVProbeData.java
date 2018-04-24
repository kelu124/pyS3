package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avformat.AVProbeData;

public class avformat$AVInputFormat$Read_probe_AVProbeData extends FunctionPointer {
    private native void allocate();

    public native int call(AVProbeData aVProbeData);

    static {
        Loader.load();
    }

    public avformat$AVInputFormat$Read_probe_AVProbeData(Pointer p) {
        super(p);
    }

    protected avformat$AVInputFormat$Read_probe_AVProbeData() {
        allocate();
    }
}
