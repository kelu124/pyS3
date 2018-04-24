package org.bytedeco.javacpp;

import java.nio.DoubleBuffer;
import java.util.Arrays;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.MemberSetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;

@Properties(inherit = {opencv_core.class}, value = {@Platform(compiler = {"fastfpu"}, define = {"MAX_SIZE 16", "CV_INLINE static inline"}, include = {"cvkernels.h"})})
public class cvkernels {

    public static class KernelData extends Pointer {
        private DoubleBuffer[] dstDstDotBuffers;

        private native void allocate();

        private native void allocateArray(long j);

        @ByRef
        @Name({"operator="})
        private native KernelData put(@ByRef KernelData kernelData);

        @MemberSetter
        @Name({"dstDstDot"})
        private native KernelData setDstDstDot(DoubleBuffer doubleBuffer);

        public native KernelData H1(CvMat cvMat);

        public native CvMat H1();

        public native KernelData H2(CvMat cvMat);

        public native CvMat H2();

        public native KernelData m53X(CvMat cvMat);

        public native CvMat m54X();

        public native int dstCount();

        public native KernelData dstCount(int i);

        public native int dstCountOutlier();

        public native KernelData dstCountOutlier(int i);

        public native int dstCountZero();

        public native KernelData dstCountZero(int i);

        public native KernelData dstImg(IplImage iplImage);

        public native IplImage dstImg();

        public native KernelData mask(IplImage iplImage);

        public native IplImage mask();

        public native double outlierThreshold();

        public native KernelData outlierThreshold(double d);

        public native KernelData srcDotImg(IplImage iplImage);

        public native IplImage srcDotImg();

        public native double srcDstDot();

        public native KernelData srcDstDot(double d);

        public native KernelData srcImg(IplImage iplImage);

        public native IplImage srcImg();

        public native KernelData srcImg2(IplImage iplImage);

        public native IplImage srcImg2();

        public native KernelData subImg(IplImage iplImage);

        public native IplImage subImg();

        public native KernelData transImg(IplImage iplImage);

        public native IplImage transImg();

        public native double zeroThreshold();

        public native KernelData zeroThreshold(double d);

        static {
            Loader.load();
        }

        public KernelData() {
            this.dstDstDotBuffers = new DoubleBuffer[1];
            allocate();
        }

        public KernelData(long size) {
            this.dstDstDotBuffers = new DoubleBuffer[1];
            allocateArray(size);
        }

        public KernelData(Pointer p) {
            super(p);
            this.dstDstDotBuffers = new DoubleBuffer[1];
        }

        public KernelData position(long position) {
            return (KernelData) super.position(position);
        }

        public DoubleBuffer dstDstDot() {
            return this.dstDstDotBuffers[(int) this.position];
        }

        public KernelData dstDstDot(DoubleBuffer dstDstDot) {
            if (((long) this.dstDstDotBuffers.length) < this.capacity) {
                this.dstDstDotBuffers = (DoubleBuffer[]) Arrays.copyOf(this.dstDstDotBuffers, (int) this.capacity);
            }
            this.dstDstDotBuffers[(int) this.position] = dstDstDot;
            return setDstDstDot(dstDstDot);
        }
    }

    public static native void multiWarpColorTransform32F(KernelData kernelData, int i, CvRect cvRect, CvScalar cvScalar);

    public static native void multiWarpColorTransform8U(KernelData kernelData, int i, CvRect cvRect, CvScalar cvScalar);

    static {
        Loader.load();
    }
}
