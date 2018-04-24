package org.bytedeco.javacpp;

public class avformat$AVIOInterruptCB extends Pointer {

    public static class Callback_Pointer extends FunctionPointer {
        private native void allocate();

        public native int call(Pointer pointer);

        static {
            Loader.load();
        }

        public Callback_Pointer(Pointer p) {
            super(p);
        }

        protected Callback_Pointer() {
            allocate();
        }
    }

    private native void allocate();

    private native void allocateArray(long j);

    public native Callback_Pointer callback();

    public native avformat$AVIOInterruptCB callback(Callback_Pointer callback_Pointer);

    public native Pointer opaque();

    public native avformat$AVIOInterruptCB opaque(Pointer pointer);

    static {
        Loader.load();
    }

    public avformat$AVIOInterruptCB() {
        super((Pointer) null);
        allocate();
    }

    public avformat$AVIOInterruptCB(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public avformat$AVIOInterruptCB(Pointer p) {
        super(p);
    }

    public avformat$AVIOInterruptCB position(long position) {
        return (avformat$AVIOInterruptCB) super.position(position);
    }
}
