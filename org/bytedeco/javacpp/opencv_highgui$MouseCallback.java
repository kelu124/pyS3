package org.bytedeco.javacpp;

public class opencv_highgui$MouseCallback extends FunctionPointer {
    private native void allocate();

    public native void call(int i, int i2, int i3, int i4, Pointer pointer);

    static {
        Loader.load();
    }

    public opencv_highgui$MouseCallback(Pointer p) {
        super(p);
    }

    protected opencv_highgui$MouseCallback() {
        allocate();
    }
}
