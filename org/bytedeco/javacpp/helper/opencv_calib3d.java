package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_calib3d.CvPOSITObject;
import org.bytedeco.javacpp.opencv_calib3d.CvStereoBMState;
import org.bytedeco.javacpp.opencv_core.CvPoint3D32f;

public class opencv_calib3d extends org.bytedeco.javacpp.presets.opencv_calib3d {

    public static abstract class AbstractCvPOSITObject extends Pointer {

        static class ReleaseDeallocator extends CvPOSITObject implements Pointer$Deallocator {
            ReleaseDeallocator(CvPOSITObject p) {
                super(p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_calib3d.cvReleasePOSITObject(this);
            }
        }

        public AbstractCvPOSITObject(Pointer p) {
            super(p);
        }

        public static CvPOSITObject create(CvPoint3D32f points, int point_count) {
            CvPOSITObject p = org.bytedeco.javacpp.opencv_calib3d.cvCreatePOSITObject(points, point_count);
            if (p != null) {
                p.deallocator(new ReleaseDeallocator(p));
            }
            return p;
        }

        public void release() {
            deallocate();
        }
    }

    public static abstract class AbstractCvStereoBMState extends Pointer {

        static class ReleaseDeallocator extends CvStereoBMState implements Pointer$Deallocator {
            ReleaseDeallocator(CvStereoBMState p) {
                super(p);
            }

            public void deallocate() {
                org.bytedeco.javacpp.opencv_calib3d.cvReleaseStereoBMState(this);
            }
        }

        public AbstractCvStereoBMState(Pointer p) {
            super(p);
        }

        public CvStereoBMState position(long position) {
            return (CvStereoBMState) super.position(position);
        }

        public static CvStereoBMState create(int preset, int numberOfDisparities) {
            CvStereoBMState p = org.bytedeco.javacpp.opencv_calib3d.cvCreateStereoBMState(preset, numberOfDisparities);
            if (p != null) {
                p.deallocator(new ReleaseDeallocator(p));
            }
            return p;
        }

        public void release() {
            deallocate();
        }
    }
}
