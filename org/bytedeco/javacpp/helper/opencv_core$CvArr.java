package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.opencv_core.IplROI;

@Opaque
public class opencv_core$CvArr extends opencv_core$AbstractArray {
    public opencv_core$CvArr(Pointer p) {
        super(p);
    }

    public int arrayChannels() {
        throw new UnsupportedOperationException();
    }

    public int arrayDepth() {
        throw new UnsupportedOperationException();
    }

    public int arrayOrigin() {
        throw new UnsupportedOperationException();
    }

    public void arrayOrigin(int origin) {
        throw new UnsupportedOperationException();
    }

    public int arrayWidth() {
        throw new UnsupportedOperationException();
    }

    public int arrayHeight() {
        throw new UnsupportedOperationException();
    }

    public IplROI arrayROI() {
        throw new UnsupportedOperationException();
    }

    public int arraySize() {
        throw new UnsupportedOperationException();
    }

    public BytePointer arrayData() {
        throw new UnsupportedOperationException();
    }

    public int arrayStep() {
        throw new UnsupportedOperationException();
    }
}
