package org.bytedeco.javacv;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;

public class ProCamGeometricCalibrator {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static ThreadLocal<CvMat> tempWarp3x3 = CvMat.createThreadLocal(3, 3);
    private final int LSB_IMAGE_SHIFT;
    private final int MSB_IMAGE_SHIFT;
    LinkedList<Marker[]>[] allImagedBoardMarkers;
    private final MarkedPlane boardPlane;
    private CvMat[] boardWarp;
    private final CvMat boardWarpSrcPts;
    private GeometricCalibrator[] cameraCalibrators;
    private org.bytedeco.javacv.MarkerDetector.Settings detectorSettings;
    private IplImage[] grayscaleImage;
    private CvMat[] lastBoardWarp;
    private Marker[][] lastDetectedMarkers1;
    private Marker[][] lastDetectedMarkers2;
    private MarkerDetector[] markerDetectors;
    private CvMat[] prevBoardWarp;
    private CvMat[] projWarp;
    private final GeometricCalibrator projectorCalibrator;
    private final MarkedPlane projectorPlane;
    private double[] rmseBoardWarp;
    private double[] rmseProjWarp;
    private Settings settings;
    private IplImage[] tempImage1;
    private IplImage[] tempImage2;
    private CvMat[] tempPts1;
    private CvMat[] tempPts2;
    private boolean updatePrewarp;

    public static class Settings extends org.bytedeco.javacv.GeometricCalibrator.Settings {
        double detectedProjectorMin = 0.5d;
        double prewarpUpdateErrorMax = 0.01d;
        boolean useOnlyIntersection = true;

        public double getDetectedProjectorMin() {
            return this.detectedProjectorMin;
        }

        public void setDetectedProjectorMin(double detectedProjectorMin) {
            this.detectedProjectorMin = detectedProjectorMin;
        }

        public boolean isUseOnlyIntersection() {
            return this.useOnlyIntersection;
        }

        public void setUseOnlyIntersection(boolean useOnlyIntersection) {
            this.useOnlyIntersection = useOnlyIntersection;
        }

        public double getPrewarpUpdateErrorMax() {
            return this.prewarpUpdateErrorMax;
        }

        public void setPrewarpUpdateErrorMax(double prewarpUpdateErrorMax) {
            this.prewarpUpdateErrorMax = prewarpUpdateErrorMax;
        }
    }

    static {
        boolean z;
        if (ProCamGeometricCalibrator.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public ProCamGeometricCalibrator(Settings settings, org.bytedeco.javacv.MarkerDetector.Settings detectorSettings, MarkedPlane boardPlane, MarkedPlane projectorPlane, ProjectiveDevice camera, ProjectiveDevice projector) {
        GeometricCalibrator[] geometricCalibratorArr = new GeometricCalibrator[]{new GeometricCalibrator(settings, detectorSettings, boardPlane, camera)};
        this(settings, detectorSettings, boardPlane, projectorPlane, geometricCalibratorArr, new GeometricCalibrator(settings, detectorSettings, projectorPlane, projector));
    }

    public ProCamGeometricCalibrator(Settings settings, org.bytedeco.javacv.MarkerDetector.Settings detectorSettings, MarkedPlane boardPlane, MarkedPlane projectorPlane, GeometricCalibrator[] cameraCalibrators, GeometricCalibrator projectorCalibrator) {
        this.MSB_IMAGE_SHIFT = 8;
        this.LSB_IMAGE_SHIFT = 7;
        this.updatePrewarp = false;
        this.settings = settings;
        this.detectorSettings = detectorSettings;
        this.boardPlane = boardPlane;
        this.projectorPlane = projectorPlane;
        this.cameraCalibrators = cameraCalibrators;
        int n = cameraCalibrators.length;
        this.markerDetectors = new MarkerDetector[n];
        this.allImagedBoardMarkers = new LinkedList[n];
        this.grayscaleImage = new IplImage[n];
        this.tempImage1 = new IplImage[n];
        this.tempImage2 = new IplImage[n];
        this.lastDetectedMarkers1 = new Marker[n][];
        this.lastDetectedMarkers2 = new Marker[n][];
        this.rmseBoardWarp = new double[n];
        this.rmseProjWarp = new double[n];
        this.boardWarp = new CvMat[n];
        this.projWarp = new CvMat[n];
        this.prevBoardWarp = new CvMat[n];
        this.lastBoardWarp = new CvMat[n];
        this.tempPts1 = new CvMat[n];
        this.tempPts2 = new CvMat[n];
        for (int i = 0; i < n; i++) {
            this.markerDetectors[i] = new MarkerDetector(detectorSettings);
            this.allImagedBoardMarkers[i] = new LinkedList();
            this.grayscaleImage[i] = null;
            this.tempImage1[i] = null;
            this.tempImage2[i] = null;
            this.lastDetectedMarkers1[i] = null;
            this.lastDetectedMarkers2[i] = null;
            this.rmseBoardWarp[i] = Double.POSITIVE_INFINITY;
            this.rmseProjWarp[i] = Double.POSITIVE_INFINITY;
            this.boardWarp[i] = CvMat.create(3, 3);
            this.projWarp[i] = CvMat.create(3, 3);
            this.prevBoardWarp[i] = CvMat.create(3, 3);
            this.lastBoardWarp[i] = CvMat.create(3, 3);
            opencv_core.cvSetIdentity(this.prevBoardWarp[i]);
            opencv_core.cvSetIdentity(this.lastBoardWarp[i]);
            this.tempPts1[i] = CvMat.create(1, 4, 6, 2);
            this.tempPts2[i] = CvMat.create(1, 4, 6, 2);
        }
        this.projectorCalibrator = projectorCalibrator;
        this.boardWarpSrcPts = CvMat.create(1, 4, 6, 2);
        if (boardPlane != null) {
            int w = boardPlane.getImage().width();
            int h = boardPlane.getImage().height();
            this.boardWarpSrcPts.put(new double[]{0.0d, 0.0d, (double) w, 0.0d, (double) w, (double) h, 0.0d, (double) h});
        }
        if (projectorPlane != null) {
            w = projectorPlane.getImage().width();
            h = projectorPlane.getImage().height();
            projectorCalibrator.getProjectiveDevice().imageWidth = w;
            projectorCalibrator.getProjectiveDevice().imageHeight = h;
        }
    }

    public MarkedPlane getBoardPlane() {
        return this.boardPlane;
    }

    public MarkedPlane getProjectorPlane() {
        return this.projectorPlane;
    }

    public GeometricCalibrator[] getCameraCalibrators() {
        return this.cameraCalibrators;
    }

    public GeometricCalibrator getProjectorCalibrator() {
        return this.projectorCalibrator;
    }

    public int getImageCount() {
        int n = this.projectorCalibrator.getImageCount() / this.cameraCalibrators.length;
        GeometricCalibrator[] geometricCalibratorArr = this.cameraCalibrators;
        int length = geometricCalibratorArr.length;
        int i = 0;
        while (i < length) {
            GeometricCalibrator c = geometricCalibratorArr[i];
            if ($assertionsDisabled || c.getImageCount() == n) {
                i++;
            } else {
                throw new AssertionError();
            }
        }
        return n;
    }

    public Marker[][] processCameraImage(IplImage cameraImage) {
        return processCameraImage(cameraImage, 0);
    }

    public Marker[][] processCameraImage(IplImage cameraImage, final int cameraNumber) {
        boolean boardWhiteMarkers;
        boolean projWhiteMarkers;
        this.cameraCalibrators[cameraNumber].getProjectiveDevice().imageWidth = cameraImage.width();
        this.cameraCalibrators[cameraNumber].getProjectiveDevice().imageHeight = cameraImage.height();
        if (cameraImage.nChannels() > 1) {
            if (!(this.grayscaleImage[cameraNumber] != null && this.grayscaleImage[cameraNumber].width() == cameraImage.width() && this.grayscaleImage[cameraNumber].height() == cameraImage.height() && this.grayscaleImage[cameraNumber].depth() == cameraImage.depth())) {
                this.grayscaleImage[cameraNumber] = IplImage.create(cameraImage.width(), cameraImage.height(), cameraImage.depth(), 1, cameraImage.origin());
            }
            opencv_imgproc.cvCvtColor(cameraImage, this.grayscaleImage[cameraNumber], 6);
        } else {
            this.grayscaleImage[cameraNumber] = cameraImage;
        }
        if (this.boardPlane.getForegroundColor().magnitude() > this.boardPlane.getBackgroundColor().magnitude()) {
            boardWhiteMarkers = true;
        } else {
            boardWhiteMarkers = false;
        }
        if (this.projectorPlane.getForegroundColor().magnitude() > this.projectorPlane.getBackgroundColor().magnitude()) {
            projWhiteMarkers = true;
        } else {
            projWhiteMarkers = false;
        }
        if (this.grayscaleImage[cameraNumber].depth() > 8) {
            if (!(this.tempImage1[cameraNumber] != null && this.tempImage1[cameraNumber].width() == this.grayscaleImage[cameraNumber].width() && this.tempImage1[cameraNumber].height() == this.grayscaleImage[cameraNumber].height())) {
                this.tempImage1[cameraNumber] = IplImage.create(this.grayscaleImage[cameraNumber].width(), this.grayscaleImage[cameraNumber].height(), 8, 1, this.grayscaleImage[cameraNumber].origin());
                this.tempImage2[cameraNumber] = IplImage.create(this.grayscaleImage[cameraNumber].width(), this.grayscaleImage[cameraNumber].height(), 8, 1, this.grayscaleImage[cameraNumber].origin());
            }
            Parallel.run(new Runnable() {
                public void run() {
                    opencv_core.cvConvertScale(ProCamGeometricCalibrator.this.grayscaleImage[cameraNumber], ProCamGeometricCalibrator.this.tempImage1[cameraNumber], 0.0078125d, 0.0d);
                    ProCamGeometricCalibrator.this.lastDetectedMarkers1[cameraNumber] = ProCamGeometricCalibrator.this.cameraCalibrators[cameraNumber].markerDetector.detect(ProCamGeometricCalibrator.this.tempImage1[cameraNumber], boardWhiteMarkers);
                }
            }, new Runnable() {
                public void run() {
                    opencv_core.cvConvertScale(ProCamGeometricCalibrator.this.grayscaleImage[cameraNumber], ProCamGeometricCalibrator.this.tempImage2[cameraNumber], 0.00390625d, 0.0d);
                    ProCamGeometricCalibrator.this.lastDetectedMarkers2[cameraNumber] = ProCamGeometricCalibrator.this.markerDetectors[cameraNumber].detect(ProCamGeometricCalibrator.this.tempImage2[cameraNumber], projWhiteMarkers);
                }
            });
        } else {
            Parallel.run(new Runnable() {
                public void run() {
                    ProCamGeometricCalibrator.this.lastDetectedMarkers1[cameraNumber] = ProCamGeometricCalibrator.this.cameraCalibrators[cameraNumber].markerDetector.detect(ProCamGeometricCalibrator.this.grayscaleImage[cameraNumber], boardWhiteMarkers);
                }
            }, new Runnable() {
                public void run() {
                    ProCamGeometricCalibrator.this.lastDetectedMarkers2[cameraNumber] = ProCamGeometricCalibrator.this.markerDetectors[cameraNumber].detect(ProCamGeometricCalibrator.this.grayscaleImage[cameraNumber], projWhiteMarkers);
                }
            });
        }
        if (!processMarkers(cameraNumber)) {
            return (Marker[][]) null;
        }
        return new Marker[][]{this.lastDetectedMarkers1[cameraNumber], this.lastDetectedMarkers2[cameraNumber]};
    }

    public void drawMarkers(IplImage image) {
        drawMarkers(image, 0);
    }

    public void drawMarkers(IplImage image, int cameraNumber) {
        this.cameraCalibrators[cameraNumber].markerDetector.draw(image, this.lastDetectedMarkers1[cameraNumber]);
        this.projectorCalibrator.markerDetector.draw(image, this.lastDetectedMarkers2[cameraNumber]);
    }

    public boolean processMarkers() {
        return processMarkers(0);
    }

    public boolean processMarkers(int cameraNumber) {
        return processMarkers(this.lastDetectedMarkers1[cameraNumber], this.lastDetectedMarkers2[cameraNumber], cameraNumber);
    }

    public boolean processMarkers(Marker[] imagedBoardMarkers, Marker[] imagedProjectorMarkers) {
        return processMarkers(imagedBoardMarkers, imagedProjectorMarkers, 0);
    }

    public boolean processMarkers(Marker[] imagedBoardMarkers, Marker[] imagedProjectorMarkers, int cameraNumber) {
        this.rmseBoardWarp[cameraNumber] = this.boardPlane.getTotalWarp(imagedBoardMarkers, this.boardWarp[cameraNumber]);
        this.rmseProjWarp[cameraNumber] = this.projectorPlane.getTotalWarp(imagedProjectorMarkers, this.projWarp[cameraNumber]);
        int imageSize = (this.cameraCalibrators[cameraNumber].getProjectiveDevice().imageWidth + this.cameraCalibrators[cameraNumber].getProjectiveDevice().imageHeight) / 2;
        if (this.rmseBoardWarp[cameraNumber] > this.settings.prewarpUpdateErrorMax * ((double) imageSize) || this.rmseProjWarp[cameraNumber] > this.settings.prewarpUpdateErrorMax * ((double) imageSize)) {
            return false;
        }
        this.updatePrewarp = true;
        if (((double) imagedBoardMarkers.length) < ((double) this.boardPlane.getMarkers().length) * this.settings.detectedBoardMin || ((double) imagedProjectorMarkers.length) < ((double) this.projectorPlane.getMarkers().length) * this.settings.detectedProjectorMin) {
            return false;
        }
        opencv_core.cvPerspectiveTransform(this.boardWarpSrcPts, this.tempPts1[cameraNumber], this.boardWarp[cameraNumber]);
        opencv_core.cvPerspectiveTransform(this.boardWarpSrcPts, this.tempPts2[cameraNumber], this.prevBoardWarp[cameraNumber]);
        double rmsePrev = opencv_core.cvNorm(this.tempPts1[cameraNumber], this.tempPts2[cameraNumber]);
        opencv_core.cvPerspectiveTransform(this.boardWarpSrcPts, this.tempPts2[cameraNumber], this.lastBoardWarp[cameraNumber]);
        double rmseLast = opencv_core.cvNorm(this.tempPts1[cameraNumber], this.tempPts2[cameraNumber]);
        opencv_core.cvCopy(this.boardWarp[cameraNumber], this.prevBoardWarp[cameraNumber]);
        if (rmsePrev >= this.settings.patternSteadySize * ((double) imageSize) || rmseLast <= this.settings.patternMovedSize * ((double) imageSize)) {
            return false;
        }
        return true;
    }

    public void addMarkers() throws InterruptedException {
        addMarkers(0);
    }

    public void addMarkers(int cameraNumber) throws InterruptedException {
        addMarkers(this.lastDetectedMarkers1[cameraNumber], this.lastDetectedMarkers2[cameraNumber], cameraNumber);
    }

    public void addMarkers(Marker[] imagedBoardMarkers, Marker[] imagedProjectorMarkers) throws InterruptedException {
        addMarkers(imagedBoardMarkers, imagedProjectorMarkers, 0);
    }

    public void addMarkers(Marker[] imagedBoardMarkers, Marker[] imagedProjectorMarkers, int cameraNumber) throws InterruptedException {
        int i;
        CvMat tempWarp = (CvMat) tempWarp3x3.get();
        if (this.settings.useOnlyIntersection) {
            Marker[] inProjectorBoardMarkers = new Marker[imagedBoardMarkers.length];
            for (i = 0; i < inProjectorBoardMarkers.length; i++) {
                inProjectorBoardMarkers[i] = imagedBoardMarkers[i].clone();
            }
            opencv_core.cvInvert(this.projWarp[cameraNumber], tempWarp);
            Marker.applyWarp(inProjectorBoardMarkers, tempWarp);
            int w = this.projectorPlane.getImage().width();
            int h = this.projectorPlane.getImage().height();
            Marker[] boardMarkersToAdd = new Marker[imagedBoardMarkers.length];
            int totalToAdd = 0;
            for (i = 0; i < inProjectorBoardMarkers.length; i++) {
                double[] c = inProjectorBoardMarkers[i].corners;
                boolean outside = false;
                int j = 0;
                while (j < 4) {
                    int margin = this.detectorSettings.subPixelWindow / 2;
                    if (c[j * 2] < ((double) margin) || c[j * 2] >= ((double) (w - margin)) || c[(j * 2) + 1] < ((double) margin) || c[(j * 2) + 1] >= ((double) (h - margin))) {
                        outside = true;
                        break;
                    }
                    j++;
                }
                if (!outside) {
                    int totalToAdd2 = totalToAdd + 1;
                    boardMarkersToAdd[totalToAdd] = imagedBoardMarkers[i];
                    totalToAdd = totalToAdd2;
                }
            }
            Marker[] a = (Marker[]) Arrays.copyOf(boardMarkersToAdd, totalToAdd);
            this.cameraCalibrators[cameraNumber].addMarkers(this.boardPlane.getMarkers(), a);
            this.allImagedBoardMarkers[cameraNumber].add(a);
        } else {
            this.cameraCalibrators[cameraNumber].addMarkers(this.boardPlane.getMarkers(), imagedBoardMarkers);
            this.allImagedBoardMarkers[cameraNumber].add(imagedBoardMarkers);
        }
        Marker[] prewrappedProjMarkers = new Marker[this.projectorPlane.getMarkers().length];
        for (i = 0; i < prewrappedProjMarkers.length; i++) {
            prewrappedProjMarkers[i] = this.projectorPlane.getMarkers()[i].clone();
        }
        Marker.applyWarp(prewrappedProjMarkers, this.projectorPlane.getPrewarp());
        synchronized (this.projectorCalibrator) {
            while (this.projectorCalibrator.getImageCount() % this.cameraCalibrators.length < cameraNumber) {
                this.projectorCalibrator.wait();
            }
            this.projectorCalibrator.addMarkers(imagedProjectorMarkers, prewrappedProjMarkers);
            this.projectorCalibrator.notify();
        }
        opencv_core.cvCopy(this.boardWarp[cameraNumber], this.lastBoardWarp[cameraNumber]);
    }

    public IplImage getProjectorImage() {
        if (this.updatePrewarp) {
            double minRmse = Double.MAX_VALUE;
            int minCameraNumber = 0;
            for (int i = 0; i < this.cameraCalibrators.length; i++) {
                double rmse = this.rmseBoardWarp[i] + this.rmseProjWarp[i];
                if (rmse < minRmse) {
                    minRmse = rmse;
                    minCameraNumber = i;
                }
            }
            CvMat prewarp = this.projectorPlane.getPrewarp();
            opencv_core.cvInvert(this.projWarp[minCameraNumber], prewarp);
            opencv_core.cvMatMul(prewarp, this.boardWarp[minCameraNumber], prewarp);
            this.projectorPlane.setPrewarp(prewarp);
        }
        return this.projectorPlane.getImage();
    }

    public double[] calibrate(boolean useCenters, boolean calibrateCameras) {
        return calibrate(useCenters, calibrateCameras);
    }

    public double[] calibrate(boolean useCenters, boolean calibrateCameras, int cameraAtOrigin) {
        int cameraNumber;
        GeometricCalibrator calibratorAtOrigin = this.cameraCalibrators[cameraAtOrigin];
        if (calibrateCameras) {
            for (cameraNumber = 0; cameraNumber < this.cameraCalibrators.length; cameraNumber++) {
                this.cameraCalibrators[cameraNumber].calibrate(useCenters);
                if (this.cameraCalibrators[cameraNumber] != calibratorAtOrigin) {
                    calibratorAtOrigin.calibrateStereo(useCenters, this.cameraCalibrators[cameraNumber]);
                }
            }
        }
        LinkedList<Marker[]> allDistortedProjectorMarkers = this.projectorCalibrator.getAllObjectMarkers();
        LinkedList<Marker[]> distortedProjectorMarkersAtOrigin = new LinkedList();
        LinkedList<Marker[]> allUndistortedProjectorMarkers = new LinkedList();
        LinkedList<Marker[]> undistortedProjectorMarkersAtOrigin = new LinkedList();
        Iterator<Marker[]> ip = allDistortedProjectorMarkers.iterator();
        Iterator<Marker[]>[] ib = new Iterator[this.cameraCalibrators.length];
        for (cameraNumber = 0; cameraNumber < this.cameraCalibrators.length; cameraNumber++) {
            ib[cameraNumber] = this.allImagedBoardMarkers[cameraNumber].iterator();
        }
        while (ip.hasNext()) {
            cameraNumber = 0;
            while (cameraNumber < this.cameraCalibrators.length) {
                int i;
                double maxError = (this.settings.prewarpUpdateErrorMax * ((double) (this.cameraCalibrators[cameraNumber].getProjectiveDevice().imageWidth + this.cameraCalibrators[cameraNumber].getProjectiveDevice().imageHeight))) / 2.0d;
                Marker[] distortedBoardMarkers = (Marker[]) ib[cameraNumber].next();
                Marker[] distortedProjectorMarkers = (Marker[]) ip.next();
                Marker[] undistortedBoardMarkers = new Marker[distortedBoardMarkers.length];
                Object undistortedProjectorMarkers = new Marker[distortedProjectorMarkers.length];
                for (i = 0; i < distortedBoardMarkers.length; i++) {
                    Marker m = distortedBoardMarkers[i].clone();
                    undistortedBoardMarkers[i] = m;
                    m.corners = this.cameraCalibrators[cameraNumber].getProjectiveDevice().undistort(m.corners);
                }
                for (i = 0; i < distortedProjectorMarkers.length; i++) {
                    m = distortedProjectorMarkers[i].clone();
                    undistortedProjectorMarkers[i] = m;
                    m.corners = this.cameraCalibrators[cameraNumber].getProjectiveDevice().undistort(m.corners);
                }
                if (this.boardPlane.getTotalWarp(undistortedBoardMarkers, this.boardWarp[cameraNumber]) <= maxError || $assertionsDisabled) {
                    opencv_core.cvInvert(this.boardWarp[cameraNumber], this.boardWarp[cameraNumber]);
                    Marker.applyWarp(undistortedProjectorMarkers, this.boardWarp[cameraNumber]);
                    allUndistortedProjectorMarkers.add(undistortedProjectorMarkers);
                    if (this.cameraCalibrators[cameraNumber] == calibratorAtOrigin) {
                        undistortedProjectorMarkersAtOrigin.add(undistortedProjectorMarkers);
                        distortedProjectorMarkersAtOrigin.add(distortedProjectorMarkers);
                    } else {
                        undistortedProjectorMarkersAtOrigin.add(new Marker[0]);
                        distortedProjectorMarkersAtOrigin.add(new Marker[0]);
                    }
                    cameraNumber++;
                } else {
                    throw new AssertionError();
                }
            }
        }
        this.projectorCalibrator.setAllObjectMarkers(allUndistortedProjectorMarkers);
        double[] reprojErr = this.projectorCalibrator.calibrate(useCenters);
        LinkedList<Marker[]> om = calibratorAtOrigin.getAllObjectMarkers();
        LinkedList<Marker[]> im = calibratorAtOrigin.getAllImageMarkers();
        calibratorAtOrigin.setAllObjectMarkers(undistortedProjectorMarkersAtOrigin);
        calibratorAtOrigin.setAllImageMarkers(distortedProjectorMarkersAtOrigin);
        double[] epipolarErr = calibratorAtOrigin.calibrateStereo(useCenters, this.projectorCalibrator);
        this.projectorCalibrator.setAllObjectMarkers(allDistortedProjectorMarkers);
        calibratorAtOrigin.setAllObjectMarkers(om);
        calibratorAtOrigin.setAllImageMarkers(im);
        return new double[]{reprojErr[0], reprojErr[1], epipolarErr[0], epipolarErr[1]};
    }
}
