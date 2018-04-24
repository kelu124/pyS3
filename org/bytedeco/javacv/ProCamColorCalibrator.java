package org.bytedeco.javacv;

import java.awt.Color;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;

public class ProCamColorCalibrator {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static ThreadLocal<CvMat> H3x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> R3x3 = CvMat.createThreadLocal(3, 3);
    private static ThreadLocal<CvMat> n3x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> t3x1 = CvMat.createThreadLocal(3, 1);
    private static ThreadLocal<CvMat> z3x1 = CvMat.createThreadLocal(3, 1);
    private CvMat boardDstPts;
    private MarkedPlane boardPlane = null;
    private CvMat boardSrcPts;
    private CvMat camKinv;
    private CameraDevice camera = null;
    private Color[] cameraColors = null;
    private int counter = 0;
    private MarkerDetector markerDetector = null;
    private IplImage mask;
    private IplImage mask2;
    private CvMat projDstPts;
    private CvMat projSrcPts;
    private ProjectorDevice projector = null;
    private Color[] projectorColors = null;
    private Settings settings;
    private IplImage undistImage;

    public static class Settings extends BaseChildSettings {
        double detectedBoardMin = 0.5d;
        int samplesPerChannel = 4;
        double trimmingFraction = 0.01d;

        public int getSamplesPerChannel() {
            return this.samplesPerChannel;
        }

        public void setSamplesPerChannel(int samplesPerChannel) {
            this.samplesPerChannel = samplesPerChannel;
        }

        public double getDetectedBoardMin() {
            return this.detectedBoardMin;
        }

        public void setDetectedBoardMin(double detectedBoardMin) {
            this.detectedBoardMin = detectedBoardMin;
        }
    }

    static {
        boolean z;
        if (ProCamColorCalibrator.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public ProCamColorCalibrator(Settings settings, org.bytedeco.javacv.MarkerDetector.Settings detectorSettings, MarkedPlane boardPlane, CameraDevice camera, ProjectorDevice projector) {
        this.settings = settings;
        this.markerDetector = new MarkerDetector(detectorSettings);
        this.boardPlane = boardPlane;
        this.camera = camera;
        this.projector = projector;
        Marker[] boardMarkers = boardPlane.getMarkers();
        this.boardSrcPts = CvMat.create((boardMarkers.length * 4) + 4, 1, 6, 2);
        this.boardDstPts = CvMat.create((boardMarkers.length * 4) + 4, 1, 6, 2);
        this.boardSrcPts.put(new double[]{0.0d, 0.0d, (double) boardPlane.getWidth(), 0.0d, (double) boardPlane.getWidth(), (double) boardPlane.getHeight(), 0.0d, (double) boardPlane.getHeight()});
        for (int i = 0; i < boardMarkers.length; i++) {
            this.boardSrcPts.put((i * 8) + 8, boardMarkers[i].corners);
        }
        this.projSrcPts = CvMat.create(4, 1, 6, 2);
        this.projDstPts = CvMat.create(4, 1, 6, 2);
        this.projSrcPts.put(new double[]{0.0d, 0.0d, (double) (projector.imageWidth - 1), 0.0d, (double) (projector.imageWidth - 1), (double) (projector.imageHeight - 1), 0.0d, (double) (projector.imageHeight - 1)});
        this.camKinv = CvMat.create(3, 3);
        opencv_core.cvInvert(camera.cameraMatrix, this.camKinv);
    }

    public int getColorCount() {
        return this.counter;
    }

    public Color[] getProjectorColors() {
        double invgamma = 1.0d / this.projector.getSettings().getResponseGamma();
        int s = this.settings.samplesPerChannel;
        if (this.projectorColors == null) {
            this.projectorColors = new Color[((s * s) * s)];
            this.cameraColors = new Color[((s * s) * s)];
            for (int i = 0; i < this.projectorColors.length; i++) {
                int j = i / s;
                int k = j / s;
                this.projectorColors[i] = new Color((float) Math.pow(((double) (i % s)) / ((double) (s - 1)), invgamma), (float) Math.pow(((double) (j % s)) / ((double) (s - 1)), invgamma), (float) Math.pow(((double) (k % s)) / ((double) (s - 1)), invgamma));
            }
        }
        return this.projectorColors;
    }

    public Color getProjectorColor() {
        return getProjectorColors()[this.counter];
    }

    public Color[] getCameraColors() {
        return this.cameraColors;
    }

    public Color getCameraColor() {
        return getCameraColors()[this.counter];
    }

    public void addCameraColor() {
        this.counter++;
    }

    public void addCameraColor(Color color) {
        Color[] colorArr = this.cameraColors;
        int i = this.counter;
        this.counter = i + 1;
        colorArr[i] = color;
    }

    public IplImage getMaskImage() {
        return this.mask;
    }

    public IplImage getUndistortedCameraImage() {
        return this.undistImage;
    }

    public boolean processCameraImage(IplImage cameraImage) {
        if (!(this.undistImage != null && this.undistImage.width() == cameraImage.width() && this.undistImage.height() == cameraImage.height() && this.undistImage.depth() == cameraImage.depth())) {
            this.undistImage = cameraImage.clone();
        }
        if (!(this.mask != null && this.mask2 != null && this.mask.width() == cameraImage.width() && this.mask2.width() == cameraImage.width() && this.mask.height() == cameraImage.height() && this.mask2.height() == cameraImage.width())) {
            this.mask = IplImage.create(cameraImage.width(), cameraImage.height(), 8, 1, cameraImage.origin());
            this.mask2 = IplImage.create(cameraImage.width(), cameraImage.height(), 8, 1, cameraImage.origin());
        }
        CvArr H = (CvMat) H3x3.get();
        CvArr R = (CvMat) R3x3.get();
        CvArr t = (CvMat) t3x1.get();
        CvMat n = (CvMat) n3x1.get();
        CvArr z = (CvMat) z3x1.get();
        z.put(new double[]{0.0d, 0.0d, 1.0d});
        this.camera.undistort(cameraImage, this.undistImage);
        Marker[] detectedBoardMarkers = this.markerDetector.detect(this.undistImage, false);
        if (((double) detectedBoardMarkers.length) < ((double) this.boardPlane.getMarkers().length) * this.settings.detectedBoardMin) {
            return false;
        }
        int j;
        this.boardPlane.getTotalWarp(detectedBoardMarkers, H);
        opencv_core.cvPerspectiveTransform(this.boardSrcPts, this.boardDstPts, H);
        double[] boardPts = this.boardDstPts.get();
        opencv_core.cvMatMul(this.camKinv, H, R);
        double error = JavaCV.HnToRt(R, z, R, t);
        opencv_core.cvMatMul(R, z, n);
        opencv_core.cvGEMM(this.projector.T, n, -1.0d / opencv_core.cvDotProduct(t, z), this.projector.R, 1.0d, H, 2);
        opencv_core.cvMatMul(this.projector.cameraMatrix, H, H);
        opencv_core.cvMatMul(H, this.camKinv, H);
        opencv_core.cvConvertScale(H, H, 1.0d / H.get(8), 0.0d);
        opencv_core.cvInvert(H, H);
        opencv_core.cvConvertScale(H, H, 1.0d / H.get(8), 0.0d);
        opencv_core.cvPerspectiveTransform(this.projSrcPts, this.projDstPts, H);
        double[] projPts = this.projDstPts.get();
        opencv_core.cvSetZero(this.mask);
        double cx = 0.0d;
        double cy = 0.0d;
        for (j = 0; j < 4; j++) {
            cx += boardPts[j * 2];
            cy += boardPts[(j * 2) + 1];
        }
        cx /= 4.0d;
        cy /= 4.0d;
        for (j = 0; j < 4; j++) {
            int i = j * 2;
            boardPts[i] = boardPts[i] - ((boardPts[j * 2] - cx) * this.settings.trimmingFraction);
            i = (j * 2) + 1;
            boardPts[i] = boardPts[i] - ((boardPts[(j * 2) + 1] - cy) * this.settings.trimmingFraction);
        }
        opencv_imgproc.cvFillConvexPoly(this.mask, new CvPoint(4).put((byte) 16, boardPts, 0, 8), 4, CvScalar.WHITE, 8, 16);
        for (j = 0; j < (boardPts.length - 8) / 8; j++) {
            opencv_imgproc.cvFillConvexPoly(this.mask, new CvPoint(4).put((byte) 16, boardPts, (j * 8) + 8, 8), 4, CvScalar.BLACK, 8, 16);
        }
        opencv_core.cvSetZero(this.mask2);
        cx = 0.0d;
        cy = 0.0d;
        for (j = 0; j < 4; j++) {
            cx += projPts[j * 2];
            cy += projPts[(j * 2) + 1];
        }
        cx /= 4.0d;
        cy /= 4.0d;
        for (j = 0; j < 4; j++) {
            i = j * 2;
            projPts[i] = projPts[i] - ((projPts[j * 2] - cx) * this.settings.trimmingFraction);
            i = (j * 2) + 1;
            projPts[i] = projPts[i] - ((projPts[(j * 2) + 1] - cy) * this.settings.trimmingFraction);
        }
        opencv_imgproc.cvFillConvexPoly(this.mask2, new CvPoint(4).put((byte) 16, projPts, 0, 8), 4, CvScalar.WHITE, 8, 16);
        opencv_core.cvAnd(this.mask, this.mask2, this.mask, null);
        opencv_imgproc.cvErode(this.mask, this.mask, null, 1);
        CvScalar c = opencv_core.cvAvg(this.undistImage, this.mask);
        int[] o = this.camera.getRGBColorOrder();
        double s = cameraImage.highValue();
        this.cameraColors[this.counter] = new Color((float) (c.val(o[0]) / s), (float) (c.val(o[1]) / s), (float) (c.val(o[2]) / s));
        return true;
    }

    public double calibrate() {
        Color[] cc = getCameraColors();
        Color[] pc = getProjectorColors();
        if ($assertionsDisabled || this.counter == pc.length) {
            ColorCalibrator calibrator = new ColorCalibrator(this.projector);
            this.projector.avgColorErr = calibrator.calibrate(cc, pc);
            this.camera.colorMixingMatrix = CvMat.create(3, 3);
            this.camera.additiveLight = CvMat.create(3, 1);
            opencv_core.cvSetIdentity(this.camera.colorMixingMatrix);
            opencv_core.cvSetZero(this.camera.additiveLight);
            this.counter = 0;
            return this.projector.avgColorErr;
        }
        throw new AssertionError();
    }
}
