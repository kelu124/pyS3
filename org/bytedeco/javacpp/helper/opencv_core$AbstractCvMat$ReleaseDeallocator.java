package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;

protected class opencv_core$AbstractCvMat$ReleaseDeallocator extends CvMat implements Pointer$Deallocator {
    opencv_core$AbstractCvMat$ReleaseDeallocator(CvMat m) {
        super(m);
    }

    public void deallocate() {
        opencv_core.cvReleaseMat(this);
    }
}
