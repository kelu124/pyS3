package org.bytedeco.javacv;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_calib3d;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.ProjectiveDevice.CalibrationSettings;

public class GeometricCalibrator {
    static final /* synthetic */ boolean $assertionsDisabled = (!GeometricCalibrator.class.desiredAssertionStatus());
    private LinkedList<Marker[]> allImageMarkers = new LinkedList();
    private LinkedList<Marker[]> allObjectMarkers = new LinkedList();
    private Marker[] lastDetectedMarkers = null;
    private CvMat lastWarp = CvMat.create(3, 3);
    private MarkedPlane markedPlane;
    MarkerDetector markerDetector;
    private CvMat prevWarp = CvMat.create(3, 3);
    private ProjectiveDevice projectiveDevice;
    private Settings settings;
    private IplImage tempImage = null;
    private CvMat tempPts = CvMat.create(1, 4, 6, 2);
    private CvMat warp = CvMat.create(3, 3);
    private CvMat warpDstPts = CvMat.create(1, 4, 6, 2);
    private CvMat warpSrcPts = CvMat.create(1, 4, 6, 2);

    public static class Settings extends BaseChildSettings {
        double detectedBoardMin = 0.5d;
        double patternMovedSize = 0.05d;
        double patternSteadySize = 0.005d;

        public double getDetectedBoardMin() {
            return this.detectedBoardMin;
        }

        public void setDetectedBoardMin(double detectedBoardMin) {
            this.detectedBoardMin = detectedBoardMin;
        }

        public double getPatternSteadySize() {
            return this.patternSteadySize;
        }

        public void setPatternSteadySize(double patternSteadySize) {
            this.patternSteadySize = patternSteadySize;
        }

        public double getPatternMovedSize() {
            return this.patternMovedSize;
        }

        public void setPatternMovedSize(double patternMovedSize) {
            this.patternMovedSize = patternMovedSize;
        }
    }

    public GeometricCalibrator(Settings settings, org.bytedeco.javacv.MarkerDetector.Settings detectorSettings, MarkedPlane markedPlane, ProjectiveDevice projectiveDevice) {
        this.settings = settings;
        this.markerDetector = new MarkerDetector(detectorSettings);
        this.markedPlane = markedPlane;
        this.projectiveDevice = projectiveDevice;
        opencv_core.cvSetIdentity(this.prevWarp);
        opencv_core.cvSetIdentity(this.lastWarp);
        if (markedPlane != null) {
            int w = markedPlane.getImage().width();
            int h = markedPlane.getImage().height();
            this.warpSrcPts.put(new double[]{0.0d, 0.0d, (double) w, 0.0d, (double) w, (double) h, 0.0d, (double) h});
        }
    }

    public MarkerDetector getMarkerDetector() {
        return this.markerDetector;
    }

    public MarkedPlane getMarkedPlane() {
        return this.markedPlane;
    }

    public ProjectiveDevice getProjectiveDevice() {
        return this.projectiveDevice;
    }

    public LinkedList<Marker[]> getAllObjectMarkers() {
        return this.allObjectMarkers;
    }

    public void setAllObjectMarkers(LinkedList<Marker[]> allObjectMarkers) {
        this.allObjectMarkers = allObjectMarkers;
    }

    public LinkedList<Marker[]> getAllImageMarkers() {
        return this.allImageMarkers;
    }

    public void setAllImageMarkers(LinkedList<Marker[]> allImageMarkers) {
        this.allImageMarkers = allImageMarkers;
    }

    public Marker[] processImage(IplImage image) {
        this.projectiveDevice.imageWidth = image.width();
        this.projectiveDevice.imageHeight = image.height();
        boolean whiteMarkers = this.markedPlane.getForegroundColor().magnitude() > this.markedPlane.getBackgroundColor().magnitude();
        if (image.depth() > 8) {
            if (!(this.tempImage != null && this.tempImage.width() == image.width() && this.tempImage.height() == image.height())) {
                this.tempImage = IplImage.create(image.width(), image.height(), 8, 1, image.origin());
            }
            opencv_core.cvConvertScale(image, this.tempImage, 0.00390625d, 0.0d);
            this.lastDetectedMarkers = this.markerDetector.detect(this.tempImage, whiteMarkers);
        } else {
            this.lastDetectedMarkers = this.markerDetector.detect(image, whiteMarkers);
        }
        if (((double) this.lastDetectedMarkers.length) < ((double) this.markedPlane.getMarkers().length) * this.settings.detectedBoardMin) {
            return null;
        }
        this.markedPlane.getTotalWarp(this.lastDetectedMarkers, this.warp);
        opencv_core.cvPerspectiveTransform(this.warpSrcPts, this.warpDstPts, this.warp);
        opencv_core.cvPerspectiveTransform(this.warpSrcPts, this.tempPts, this.prevWarp);
        double rmsePrev = opencv_core.cvNorm(this.warpDstPts, this.tempPts);
        opencv_core.cvPerspectiveTransform(this.warpSrcPts, this.tempPts, this.lastWarp);
        double rmseLast = opencv_core.cvNorm(this.warpDstPts, this.tempPts);
        opencv_core.cvCopy(this.warp, this.prevWarp);
        int imageSize = (image.width() + image.height()) / 2;
        return (rmsePrev >= this.settings.patternSteadySize * ((double) imageSize) || rmseLast <= this.settings.patternMovedSize * ((double) imageSize)) ? null : this.lastDetectedMarkers;
    }

    public void drawMarkers(IplImage image) {
        this.markerDetector.draw(image, this.lastDetectedMarkers);
    }

    public void addMarkers() {
        addMarkers(this.markedPlane.getMarkers(), this.lastDetectedMarkers);
    }

    public void addMarkers(Marker[] imageMarkers) {
        addMarkers(this.markedPlane.getMarkers(), imageMarkers);
    }

    public void addMarkers(Marker[] objectMarkers, Marker[] imageMarkers) {
        int maxLength = Math.min(objectMarkers.length, imageMarkers.length);
        Marker[] om = new Marker[maxLength];
        Marker[] im = new Marker[maxLength];
        int i = 0;
        for (Marker m1 : objectMarkers) {
            for (Marker m2 : imageMarkers) {
                if (m1.id == m2.id) {
                    om[i] = m1;
                    im[i] = m2;
                    i++;
                    break;
                }
            }
        }
        if (i < maxLength) {
            om = (Marker[]) Arrays.copyOf(om, i);
            im = (Marker[]) Arrays.copyOf(im, i);
        }
        this.allObjectMarkers.add(om);
        this.allImageMarkers.add(im);
        opencv_core.cvCopy(this.prevWarp, this.lastWarp);
    }

    public int getImageCount() {
        if ($assertionsDisabled || this.allObjectMarkers.size() == this.allImageMarkers.size()) {
            return this.allObjectMarkers.size();
        }
        throw new AssertionError();
    }

    private CvMat[] getPoints(boolean useCenters) {
        if ($assertionsDisabled || this.allObjectMarkers.size() == this.allImageMarkers.size()) {
            Marker[] m1;
            Marker[] m2;
            Iterator<Marker[]> i1 = this.allObjectMarkers.iterator();
            Iterator<Marker[]> i2 = this.allImageMarkers.iterator();
            IntBuffer pointCountsBuf = CvMat.create(1, this.allImageMarkers.size(), 4, 1).getIntBuffer();
            int totalPointCount = 0;
            while (i1.hasNext() && i2.hasNext()) {
                m1 = (Marker[]) i1.next();
                m2 = (Marker[]) i2.next();
                if ($assertionsDisabled || m1.length == m2.length) {
                    int n = m1.length * (useCenters ? 1 : 4);
                    pointCountsBuf.put(n);
                    totalPointCount += n;
                } else {
                    throw new AssertionError();
                }
            }
            i1 = this.allObjectMarkers.iterator();
            i2 = this.allImageMarkers.iterator();
            CvMat objectPoints = CvMat.create(1, totalPointCount, 5, 3);
            CvMat imagePoints = CvMat.create(1, totalPointCount, 5, 2);
            FloatBuffer objectPointsBuf = objectPoints.getFloatBuffer();
            FloatBuffer imagePointsBuf = imagePoints.getFloatBuffer();
            while (i1.hasNext() && i2.hasNext()) {
                m1 = (Marker[]) i1.next();
                m2 = (Marker[]) i2.next();
                for (int j = 0; j < m1.length; j++) {
                    if (useCenters) {
                        double[] c1 = m1[j].getCenter();
                        objectPointsBuf.put((float) c1[0]);
                        objectPointsBuf.put((float) c1[1]);
                        objectPointsBuf.put(0.0f);
                        double[] c2 = m2[j].getCenter();
                        imagePointsBuf.put((float) c2[0]);
                        imagePointsBuf.put((float) c2[1]);
                    } else {
                        for (int k = 0; k < 4; k++) {
                            objectPointsBuf.put((float) m1[j].corners[k * 2]);
                            objectPointsBuf.put((float) m1[j].corners[(k * 2) + 1]);
                            objectPointsBuf.put(0.0f);
                            imagePointsBuf.put((float) m2[j].corners[k * 2]);
                            imagePointsBuf.put((float) m2[j].corners[(k * 2) + 1]);
                        }
                    }
                }
            }
            return new CvMat[]{objectPoints, imagePoints, pointCounts};
        }
        throw new AssertionError();
    }

    public static double[] computeReprojectionError(CvMat object_points, CvMat image_points, CvMat point_counts, CvMat camera_matrix, CvMat dist_coeffs, CvMat rot_vects, CvMat trans_vects, CvMat per_view_errors) {
        CvArr image_points2 = CvMat.create(image_points.rows(), image_points.cols(), image_points.type());
        int image_count = rot_vects.rows();
        int points_so_far = 0;
        double total_err = 0.0d;
        double max_err = 0.0d;
        CvMat object_points_i = new CvMat();
        CvArr image_points_i = new CvMat();
        CvMat image_points2_i = new CvMat();
        IntBuffer point_counts_buf = point_counts.getIntBuffer();
        CvMat rot_vect = new CvMat();
        CvMat trans_vect = new CvMat();
        for (int i = 0; i < image_count; i++) {
            object_points_i.reset();
            image_points_i.reset();
            image_points2_i.reset();
            int point_count = point_counts_buf.get(i);
            opencv_core.cvGetCols(object_points, object_points_i, points_so_far, points_so_far + point_count);
            opencv_core.cvGetCols(image_points, image_points_i, points_so_far, points_so_far + point_count);
            opencv_core.cvGetCols(image_points2, image_points2_i, points_so_far, points_so_far + point_count);
            points_so_far += point_count;
            opencv_core.cvGetRows(rot_vects, rot_vect, i, i + 1, 1);
            opencv_core.cvGetRows(trans_vects, trans_vect, i, i + 1, 1);
            opencv_calib3d.cvProjectPoints2(object_points_i, rot_vect, trans_vect, camera_matrix, dist_coeffs, image_points2_i);
            double err = opencv_core.cvNorm(image_points_i, image_points2_i);
            err *= err;
            if (per_view_errors != null) {
                per_view_errors.put(i, Math.sqrt(err / ((double) point_count)));
            }
            total_err += err;
            for (int j = 0; j < point_count; j++) {
                double x1 = image_points_i.get(0, j, 0);
                double y1 = image_points_i.get(0, j, 1);
                double dx = x1 - image_points2_i.get(0, j, 0);
                double dy = y1 - image_points2_i.get(0, j, 1);
                err = Math.sqrt((dx * dx) + (dy * dy));
                if (err > max_err) {
                    max_err = err;
                }
            }
        }
        return new double[]{Math.sqrt(total_err / ((double) points_so_far)), max_err};
    }

    public double[] calibrate(boolean useCenters) {
        ProjectiveDevice d = this.projectiveDevice;
        CalibrationSettings dsettings = (CalibrationSettings) d.getSettings();
        if (d.cameraMatrix == null) {
            d.cameraMatrix = CvMat.create(3, 3);
            opencv_core.cvSetZero(d.cameraMatrix);
            if ((dsettings.flags & 2) != 0) {
                d.cameraMatrix.put(0, dsettings.initAspectRatio);
                d.cameraMatrix.put(4, 1.0d);
            }
        }
        int kn = dsettings.isFixK3() ? 4 : 5;
        if (!(!dsettings.isRationalModel() || dsettings.isFixK4() || dsettings.isFixK4() || dsettings.isFixK5())) {
            kn = 8;
        }
        if (d.distortionCoeffs == null || d.distortionCoeffs.cols() != kn) {
            d.distortionCoeffs = CvMat.create(1, kn);
            opencv_core.cvSetZero(d.distortionCoeffs);
        }
        CvMat rotVects = new CvMat();
        CvMat transVects = new CvMat();
        d.extrParams = CvMat.create(this.allImageMarkers.size(), 6);
        opencv_core.cvGetCols(d.extrParams, rotVects, 0, 3);
        opencv_core.cvGetCols(d.extrParams, transVects, 3, 6);
        CvMat[] points = getPoints(useCenters);
        opencv_calib3d.cvCalibrateCamera2(points[0], points[1], points[2], opencv_core.cvSize(d.imageWidth, d.imageHeight), d.cameraMatrix, d.distortionCoeffs, rotVects, transVects, dsettings.flags, opencv_core.cvTermCriteria(3, 30, JavaCV.DBL_EPSILON));
        if (opencv_core.cvCheckArr(d.cameraMatrix, 2, 0.0d, 0.0d) == 0 || opencv_core.cvCheckArr(d.distortionCoeffs, 2, 0.0d, 0.0d) == 0 || opencv_core.cvCheckArr(d.extrParams, 2, 0.0d, 0.0d) == 0) {
            d.cameraMatrix = null;
            d.avgReprojErr = -1.0d;
            d.maxReprojErr = -1.0d;
            return null;
        }
        d.reprojErrs = CvMat.create(1, this.allImageMarkers.size());
        double[] err = computeReprojectionError(points[0], points[1], points[2], d.cameraMatrix, d.distortionCoeffs, rotVects, transVects, d.reprojErrs);
        d.avgReprojErr = err[0];
        d.maxReprojErr = err[1];
        return err;
    }

    public static double[] computeStereoError(CvMat imagePoints1, CvMat imagePoints2, CvMat M1, CvMat D1, CvMat M2, CvMat D2, CvMat F) {
        int N = imagePoints1.cols();
        CvMat L1 = CvMat.create(1, N, 5, 3);
        CvMat L2 = CvMat.create(1, N, 5, 3);
        opencv_imgproc.cvUndistortPoints(imagePoints1, imagePoints1, M1, D1, null, M1);
        opencv_imgproc.cvUndistortPoints(imagePoints2, imagePoints2, M2, D2, null, M2);
        opencv_calib3d.cvComputeCorrespondEpilines(imagePoints1, 1, F, L1);
        opencv_calib3d.cvComputeCorrespondEpilines(imagePoints2, 2, F, L2);
        double avgErr = 0.0d;
        double maxErr = 0.0d;
        CvMat p1 = imagePoints1;
        CvMat p2 = imagePoints2;
        for (int i = 0; i < N; i++) {
            double e1 = ((p1.get(0, i, 0) * L2.get(0, i, 0)) + (p1.get(0, i, 1) * L2.get(0, i, 1))) + L2.get(0, i, 2);
            double e2 = ((p2.get(0, i, 0) * L1.get(0, i, 0)) + (p2.get(0, i, 1) * L1.get(0, i, 1))) + L1.get(0, i, 2);
            double err = (e1 * e1) + (e2 * e2);
            avgErr += err;
            err = Math.sqrt(err);
            if (err > maxErr) {
                maxErr = err;
            }
        }
        return new double[]{Math.sqrt(avgErr / ((double) N)), maxErr};
    }

    public double[] calibrateStereo(boolean useCenters, GeometricCalibrator peer) {
        ProjectiveDevice d = this.projectiveDevice;
        ProjectiveDevice dp = peer.projectiveDevice;
        CalibrationSettings dsettings = (CalibrationSettings) d.getSettings();
        CalibrationSettings dpsettings = (CalibrationSettings) dp.getSettings();
        CvMat[] points1 = getPoints(useCenters);
        CvMat[] points2 = peer.getPoints(useCenters);
        FloatBuffer objPts1 = points1[0].getFloatBuffer();
        FloatBuffer imgPts1 = points1[1].getFloatBuffer();
        IntBuffer imgCount1 = points1[2].getIntBuffer();
        FloatBuffer objPts2 = points2[0].getFloatBuffer();
        FloatBuffer imgPts2 = points2[1].getFloatBuffer();
        IntBuffer imgCount2 = points2[2].getIntBuffer();
        if ($assertionsDisabled || imgCount1.capacity() == imgCount2.capacity()) {
            CvMat objectPointsMat = CvMat.create(1, Math.min(objPts1.capacity(), objPts2.capacity()), 5, 3);
            CvMat imagePoints1Mat = CvMat.create(1, Math.min(imgPts1.capacity(), imgPts2.capacity()), 5, 2);
            CvMat imagePoints2Mat = CvMat.create(1, Math.min(imgPts1.capacity(), imgPts2.capacity()), 5, 2);
            CvMat pointCountsMat = CvMat.create(1, imgCount1.capacity(), 4, 1);
            FloatBuffer objectPoints = objectPointsMat.getFloatBuffer();
            FloatBuffer imagePoints1 = imagePoints1Mat.getFloatBuffer();
            FloatBuffer imagePoints2 = imagePoints2Mat.getFloatBuffer();
            IntBuffer pointCounts = pointCountsMat.getIntBuffer();
            int end1 = 0;
            int end2 = 0;
            for (int i = 0; i < imgCount1.capacity(); i++) {
                int start1 = end1;
                int start2 = end2;
                end1 = start1 + imgCount1.get(i);
                end2 = start2 + imgCount2.get(i);
                int count = 0;
                for (int j = start1; j < end1; j++) {
                    float x1 = objPts1.get(j * 3);
                    float y1 = objPts1.get((j * 3) + 1);
                    float z1 = objPts1.get((j * 3) + 2);
                    for (int k = start2; k < end2; k++) {
                        float x2 = objPts2.get(k * 3);
                        float y2 = objPts2.get((k * 3) + 1);
                        float z2 = objPts2.get((k * 3) + 2);
                        if (x1 == x2 && y1 == y2 && z1 == z2) {
                            objectPoints.put(x1);
                            objectPoints.put(y1);
                            objectPoints.put(z1);
                            imagePoints1.put(imgPts1.get(j * 2));
                            imagePoints1.put(imgPts1.get((j * 2) + 1));
                            imagePoints2.put(imgPts2.get(k * 2));
                            imagePoints2.put(imgPts2.get((k * 2) + 1));
                            count++;
                            break;
                        }
                    }
                }
                if (count > 0) {
                    pointCounts.put(count);
                }
            }
            objectPointsMat.cols(objectPoints.position() / 3);
            imagePoints1Mat.cols(imagePoints1.position() / 2);
            imagePoints2Mat.cols(imagePoints2.position() / 2);
            pointCountsMat.cols(pointCounts.position());
            d.f182R = CvMat.create(3, 3);
            d.f182R.put(new double[]{1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 1.0d});
            d.f183T = CvMat.create(3, 1);
            d.f183T.put(new double[]{0.0d, 0.0d, 0.0d});
            d.f180E = CvMat.create(3, 3);
            opencv_core.cvSetZero(d.f180E);
            d.f181F = CvMat.create(3, 3);
            opencv_core.cvSetZero(d.f181F);
            dp.f182R = CvMat.create(3, 3);
            dp.f183T = CvMat.create(3, 1);
            dp.f180E = CvMat.create(3, 3);
            dp.f181F = CvMat.create(3, 3);
            opencv_calib3d.cvStereoCalibrate(objectPointsMat, imagePoints1Mat, imagePoints2Mat, pointCountsMat, d.cameraMatrix, d.distortionCoeffs, dp.cameraMatrix, dp.distortionCoeffs, opencv_core.cvSize(d.imageWidth, d.imageHeight), dp.f182R, dp.f183T, dp.f180E, dp.f181F, dpsettings.flags, opencv_core.cvTermCriteria(3, 100, 1.0E-6d));
            d.avgEpipolarErr = 0.0d;
            d.maxEpipolarErr = 0.0d;
            double[] err = computeStereoError(imagePoints1Mat, imagePoints2Mat, d.cameraMatrix, d.distortionCoeffs, dp.cameraMatrix, dp.distortionCoeffs, dp.f181F);
            dp.avgEpipolarErr = err[0];
            dp.maxEpipolarErr = err[1];
            return err;
        }
        throw new AssertionError();
    }
}
