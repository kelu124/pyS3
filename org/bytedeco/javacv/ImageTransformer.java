package org.bytedeco.javacv;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;

public interface ImageTransformer {

    public static class Data {
        public int dstCount;
        public int dstCountOutlier;
        public int dstCountZero;
        public DoubleBuffer dstDstDot;
        public IplImage dstImg;
        public IplImage mask;
        public double outlierThreshold;
        public int pyramidLevel;
        public IplImage srcDotImg;
        public double srcDstDot;
        public IplImage srcImg;
        public IplImage subImg;
        public IplImage transImg;
        public double zeroThreshold;

        public Data() {
            this(null, null, null, null, 0.0d, 0.0d, 0, null, null, 0);
        }

        public Data(IplImage srcImg, IplImage subImg, IplImage srcDotImg, IplImage mask, double zeroThreshold, double outlierThreshold, int pyramidLevel, IplImage transImg, IplImage dstImg, int dstDstDotLength) {
            DoubleBuffer doubleBuffer;
            this.srcImg = srcImg;
            this.subImg = subImg;
            this.srcDotImg = srcDotImg;
            this.mask = mask;
            this.zeroThreshold = zeroThreshold;
            this.outlierThreshold = outlierThreshold;
            this.pyramidLevel = pyramidLevel;
            this.transImg = transImg;
            this.dstImg = dstImg;
            if (dstDstDotLength == 0) {
                doubleBuffer = null;
            } else {
                doubleBuffer = ByteBuffer.allocateDirect(dstDstDotLength * 8).order(ByteOrder.nativeOrder()).asDoubleBuffer();
            }
            this.dstDstDot = doubleBuffer;
        }
    }

    public interface Parameters extends Cloneable {
        Parameters clone();

        void compose(Parameters parameters, boolean z, Parameters parameters2, boolean z2);

        double get(int i);

        double[] get();

        double getConstraintError();

        double[] getSubspace();

        boolean preoptimize();

        void reset(boolean z);

        void set(int i, double d);

        void set(Parameters parameters);

        void set(double... dArr);

        void setSubspace(double... dArr);

        int size();
    }

    Parameters createParameters();

    void transform(CvMat cvMat, CvMat cvMat2, Parameters parameters, boolean z);

    void transform(Data[] dataArr, CvRect cvRect, Parameters[] parametersArr, boolean[] zArr);
}
