package org.bytedeco.javacv;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.bytedeco.javacpp.cvkernels.KernelData;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Parallel.Looper;

public class cvkernels extends org.bytedeco.javacpp.cvkernels {
    private static ThreadLocal<ParallelData[]> parallelData = new C12671();

    static class C12671 extends ThreadLocal<ParallelData[]> {
        C12671() {
        }

        protected ParallelData[] initialValue() {
            ParallelData[] pd = new ParallelData[Parallel.getNumThreads()];
            for (int i = 0; i < pd.length; i++) {
                pd[i] = new ParallelData();
            }
            return pd;
        }
    }

    private static class ParallelData {
        KernelData data;
        CvRect roi;

        private ParallelData() {
            this.data = null;
            this.roi = new CvRect();
        }
    }

    public static void multiWarpColorTransform(KernelData data, CvRect roi, CvScalar fillColor) {
        KernelData d;
        int x;
        int y;
        int w;
        int h;
        final int size = (int) data.capacity();
        final ParallelData[] pd = (ParallelData[]) parallelData.get();
        int i = 0;
        while (i < pd.length) {
            int j;
            if (pd[i].data == null || pd[i].data.capacity() < ((long) size)) {
                pd[i].data = new KernelData((long) size);
                for (j = 0; j < size; j++) {
                    d = pd[i].data.position((long) j);
                    data.position((long) j);
                    if (data.dstDstDot() != null) {
                        d.dstDstDot(ByteBuffer.allocateDirect(data.dstDstDot().capacity() * 8).order(ByteOrder.nativeOrder()).asDoubleBuffer());
                    }
                }
            }
            for (j = 0; j < size; j++) {
                d = pd[i].data.position((long) j);
                d.put(data.position((long) j));
                d.dstDstDot(d.dstDstDot());
            }
            i++;
        }
        IplImage img = data.position(0).srcImg();
        final int depth = img.depth();
        if (roi != null) {
            x = roi.x();
            y = roi.y();
            w = roi.width();
            h = roi.height();
        } else {
            x = 0;
            y = 0;
            w = img.width();
            h = img.height();
        }
        final CvScalar cvScalar = fillColor;
        Parallel.loop(y, y + h, pd.length, new Looper() {
            static final /* synthetic */ boolean $assertionsDisabled = (!cvkernels.class.desiredAssertionStatus());

            public void loop(int from, int to, int looperID) {
                CvRect r = pd[looperID].roi.x(x).y(from).width(w).height(to - from);
                if (depth == 32) {
                    org.bytedeco.javacpp.cvkernels.multiWarpColorTransform32F(pd[looperID].data.position(0), size, r, cvScalar);
                } else if (depth == 8) {
                    org.bytedeco.javacpp.cvkernels.multiWarpColorTransform8U(pd[looperID].data.position(0), size, r, cvScalar);
                } else if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
        });
        for (i = 0; i < size; i++) {
            int dstCount = 0;
            int dstCountZero = 0;
            int dstCountOutlier = 0;
            double srcDstDot = 0.0d;
            double[] dstDstDot = null;
            if (data.dstDstDot() != null) {
                dstDstDot = new double[data.dstDstDot().capacity()];
            }
            for (ParallelData parallelData : pd) {
                d = parallelData.data.position((long) i);
                dstCount += d.dstCount();
                dstCountZero += d.dstCountZero();
                dstCountOutlier += d.dstCountOutlier();
                srcDstDot += d.srcDstDot();
                if (!(dstDstDot == null || d.dstDstDot() == null)) {
                    for (int k = 0; k < dstDstDot.length; k++) {
                        dstDstDot[k] = dstDstDot[k] + d.dstDstDot().get(k);
                    }
                }
            }
            data.position((long) i);
            data.dstCount(dstCount);
            data.dstCountZero(dstCountZero);
            data.dstCountOutlier(dstCountOutlier);
            data.srcDstDot(srcDstDot);
            if (!(dstDstDot == null || data.dstDstDot() == null)) {
                data.dstDstDot().position(0);
                data.dstDstDot().put(dstDstDot);
            }
        }
    }
}
