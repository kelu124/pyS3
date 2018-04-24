package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_video$CvKalman;

public class opencv_video extends org.bytedeco.javacpp.presets.opencv_video {

    public static abstract class AbstractCvKalman extends Pointer {

        static class ReleaseDeallocator extends opencv_video$CvKalman implements Pointer$Deallocator {
            ReleaseDeallocator(opencv_video$CvKalman p) {
                super((Pointer) p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_video.cvReleaseKalman(this);
            }
        }

        public AbstractCvKalman(Pointer p) {
            super(p);
        }

        public static opencv_video$CvKalman create(int dynam_params, int measure_params, int control_params) {
            opencv_video$CvKalman k = org.bytedeco.javacpp.opencv_video.cvCreateKalman(dynam_params, measure_params, control_params);
            if (k != null) {
                k.deallocator(new ReleaseDeallocator(k));
            }
            return k;
        }

        public void release() {
            deallocate();
        }
    }
}
