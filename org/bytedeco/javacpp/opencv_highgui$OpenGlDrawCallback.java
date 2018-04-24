package org.bytedeco.javacpp;

public class opencv_highgui$OpenGlDrawCallback extends FunctionPointer {
    private native void allocate();

    public native void call(Pointer pointer);

    static {
        Loader.load();
    }

    public opencv_highgui$OpenGlDrawCallback(Pointer p) {
        super(p);
    }

    protected opencv_highgui$OpenGlDrawCallback() {
        allocate();
    }
}
