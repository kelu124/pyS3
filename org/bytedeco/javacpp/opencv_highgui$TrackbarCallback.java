package org.bytedeco.javacpp;

public class opencv_highgui$TrackbarCallback extends FunctionPointer {
    private native void allocate();

    public native void call(int i, Pointer pointer);

    static {
        Loader.load();
    }

    public opencv_highgui$TrackbarCallback(Pointer p) {
        super(p);
    }

    protected opencv_highgui$TrackbarCallback() {
        allocate();
    }
}
