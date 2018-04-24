package org.bytedeco.javacpp;

public class flandmark$FLANDMARK_PSIG extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native int COLS();

    public native flandmark$FLANDMARK_PSIG COLS(int i);

    public native int ROWS();

    public native flandmark$FLANDMARK_PSIG ROWS(int i);

    public native IntPointer disp();

    public native flandmark$FLANDMARK_PSIG disp(IntPointer intPointer);

    static {
        Loader.load();
    }

    public flandmark$FLANDMARK_PSIG() {
        super((Pointer) null);
        allocate();
    }

    public flandmark$FLANDMARK_PSIG(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public flandmark$FLANDMARK_PSIG(Pointer p) {
        super(p);
    }

    public flandmark$FLANDMARK_PSIG position(long position) {
        return (flandmark$FLANDMARK_PSIG) super.position(position);
    }
}
