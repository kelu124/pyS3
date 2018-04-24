package org.bytedeco.javacpp.presets;

import java.nio.ByteBuffer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;

public abstract class PGRFlyCapture$AbstractFlyCaptureImage extends Pointer {
    public abstract int iCols();

    public abstract int iRowInc();

    public abstract int iRows();

    public abstract BytePointer pData();

    public PGRFlyCapture$AbstractFlyCaptureImage(Pointer p) {
        super(p);
    }

    public ByteBuffer getByteBuffer() {
        return pData().capacity((long) (iRowInc() * iRows())).asByteBuffer();
    }
}
