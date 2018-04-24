package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.helper.opencv_core.AbstractCvMat;
import org.bytedeco.javacpp.opencv_core.CvMat;

class opencv_core$AbstractCvMat$2 extends ThreadLocal<CvMat> {
    final /* synthetic */ int val$cols;
    final /* synthetic */ int val$rows;
    final /* synthetic */ int val$type;

    opencv_core$AbstractCvMat$2(int i, int i2, int i3) {
        this.val$rows = i;
        this.val$cols = i2;
        this.val$type = i3;
    }

    protected CvMat initialValue() {
        return AbstractCvMat.createHeader(this.val$rows, this.val$cols, this.val$type);
    }
}
