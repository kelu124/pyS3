package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_core$CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;

public class opencv_objdetect extends org.bytedeco.javacpp.presets.opencv_objdetect {

    public static abstract class AbstractCvHaarClassifierCascade extends Pointer {

        static class ReleaseDeallocator extends CvHaarClassifierCascade implements Pointer$Deallocator {
            ReleaseDeallocator(CvHaarClassifierCascade p) {
                super(p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_objdetect.cvReleaseHaarClassifierCascade(this);
            }
        }

        public AbstractCvHaarClassifierCascade(Pointer p) {
            super(p);
        }

        public static CvHaarClassifierCascade load(String directory, CvSize orig_window_size) {
            CvHaarClassifierCascade h = org.bytedeco.javacpp.opencv_objdetect.cvLoadHaarClassifierCascade(directory, orig_window_size);
            if (h != null) {
                h.deallocator(new ReleaseDeallocator(h));
            }
            return h;
        }

        public void release() {
            deallocate();
        }
    }

    public static CvSeq cvHaarDetectObjects(CvArr image, CvHaarClassifierCascade cascade, opencv_core$CvMemStorage storage, double scale_factor, int min_neighbors, int flags) {
        return org.bytedeco.javacpp.opencv_objdetect.cvHaarDetectObjects(image, cascade, storage, scale_factor, min_neighbors, flags, CvSize.ZERO, CvSize.ZERO);
    }
}
