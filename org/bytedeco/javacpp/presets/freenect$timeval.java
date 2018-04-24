package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;

public class freenect$timeval extends Pointer {
    private native void allocate();

    private native void allocateArray(int i);

    public native long tv_sec();

    public native freenect$timeval tv_sec(long j);

    public native long tv_usec();

    public native freenect$timeval tv_usec(long j);

    static {
        Loader.load();
    }

    public freenect$timeval() {
        allocate();
    }

    public freenect$timeval(int size) {
        allocateArray(size);
    }

    public freenect$timeval(Pointer p) {
        super(p);
    }
}
