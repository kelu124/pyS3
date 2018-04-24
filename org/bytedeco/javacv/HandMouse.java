package org.bytedeco.javacv;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.apache.poi.hssf.record.DeltaRecord;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvMemStorage;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core$CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_imgproc.CvMoments;

public class HandMouse {
    private IplImage binaryImage;
    private double centerX;
    private double centerY;
    private CvPoint contourPoints;
    private IntBuffer contourPointsBuffer;
    private int contourPointsSize;
    private double edgeX;
    private double edgeY;
    private double imageTipX;
    private double imageTipY;
    private boolean imageUpdateNeeded;
    private IntPointer intPointer;
    private CvMoments moments;
    private long prevTipTime;
    private double prevTipX;
    private double prevTipY;
    private CvPoint pt1;
    private CvPoint pt2;
    private IplImage relativeResidual;
    private CvRect roi;
    private Settings settings;
    private opencv_core$CvMemStorage storage;
    private long tipTime;
    private double tipX;
    private double tipY;

    public static class Settings extends BaseChildSettings {
        double brightnessMin = 0.1d;
        double clickSteadySize = 0.05d;
        long clickSteadyTime = 250;
        double edgeAreaMax = 0.1d;
        double edgeAreaMin = DeltaRecord.DEFAULT_VALUE;
        int mopIterations = 1;
        double thresholdHigh = 0.5d;
        double thresholdLow = 0.25d;
        double updateAlpha = 0.5d;

        public Settings(Settings s) {
            s.mopIterations = this.mopIterations;
            s.clickSteadySize = this.clickSteadySize;
            s.clickSteadyTime = this.clickSteadyTime;
            s.edgeAreaMin = this.edgeAreaMin;
            s.edgeAreaMax = this.edgeAreaMax;
            s.thresholdHigh = this.thresholdHigh;
            s.thresholdLow = this.thresholdLow;
            s.brightnessMin = this.brightnessMin;
            s.updateAlpha = this.updateAlpha;
        }

        public int getMopIterations() {
            return this.mopIterations;
        }

        public void setMopIterations(int mopIterations) {
            this.mopIterations = mopIterations;
        }

        public double getClickSteadySize() {
            return this.clickSteadySize;
        }

        public void setClickSteadySize(double clickSteadySize) {
            this.clickSteadySize = clickSteadySize;
        }

        public long getClickSteadyTime() {
            return this.clickSteadyTime;
        }

        public void setClickSteadyTime(long clickSteadyTime) {
            this.clickSteadyTime = clickSteadyTime;
        }

        public double getEdgeAreaMin() {
            return this.edgeAreaMin;
        }

        public void setEdgeAreaMin(double edgeAreaMin) {
            this.edgeAreaMin = edgeAreaMin;
        }

        public double getEdgeAreaMax() {
            return this.edgeAreaMax;
        }

        public void setEdgeAreaMax(double edgeAreaMax) {
            this.edgeAreaMax = edgeAreaMax;
        }

        public double getThresholdHigh() {
            return this.thresholdHigh;
        }

        public void setThresholdHigh(double thresholdHigh) {
            this.thresholdHigh = thresholdHigh;
        }

        public double getThresholdLow() {
            return this.thresholdLow;
        }

        public void setThresholdLow(double thresholdLow) {
            this.thresholdLow = thresholdLow;
        }

        public double getBrightnessMin() {
            return this.brightnessMin;
        }

        public void setBrightnessMin(double brightnessMin) {
            this.brightnessMin = brightnessMin;
        }

        public double getUpdateAlpha() {
            return this.updateAlpha;
        }

        public void setUpdateAlpha(double updateAlpha) {
            this.updateAlpha = updateAlpha;
        }
    }

    public HandMouse() {
        this(new Settings());
    }

    public HandMouse(Settings settings) {
        this.relativeResidual = null;
        this.binaryImage = null;
        this.roi = null;
        this.storage = AbstractCvMemStorage.create();
        this.contourPointsSize = 0;
        this.intPointer = new IntPointer(1);
        this.contourPoints = null;
        this.contourPointsBuffer = null;
        this.moments = new CvMoments();
        this.edgeX = 0.0d;
        this.edgeY = 0.0d;
        this.centerX = 0.0d;
        this.centerY = 0.0d;
        this.imageTipX = -1.0d;
        this.tipX = -1.0d;
        this.prevTipX = -1.0d;
        this.imageTipY = -1.0d;
        this.tipY = -1.0d;
        this.prevTipY = -1.0d;
        this.tipTime = 0;
        this.prevTipTime = 0;
        this.pt1 = new CvPoint();
        this.pt2 = new CvPoint();
        this.imageUpdateNeeded = false;
        setSettings(settings);
    }

    public Settings getSettings() {
        return this.settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void reset() {
        this.prevTipY = -1.0d;
        this.prevTipX = -1.0d;
        this.tipY = -1.0d;
        this.tipX = -1.0d;
    }

    public void update(IplImage[] images, int pyramidLevel, CvRect roi, double[] roiPts) {
        int i;
        this.roi = roi;
        IplImage target = images[1];
        IplImage transformed = images[2];
        IplImage residual = images[3];
        IplImage mask = images[4];
        int width = roi.width();
        int height = roi.height();
        int channels = residual.nChannels();
        this.relativeResidual = IplImage.createIfNotCompatible(this.relativeResidual, mask);
        this.binaryImage = IplImage.createIfNotCompatible(this.binaryImage, mask);
        opencv_core.cvResetImageROI(this.relativeResidual);
        opencv_core.cvResetImageROI(this.binaryImage);
        if (channels > 3) {
            i = 3;
        } else {
            i = channels;
        }
        double brightnessMin = ((double) i) * this.settings.brightnessMin;
        double contourEdgeAreaMax = ((double) ((((width + height) / 2) * width) * height)) * this.settings.edgeAreaMax;
        double contourEdgeAreaMin = ((double) ((((width + height) / 2) * width) * height)) * this.settings.edgeAreaMin;
        ByteBuffer maskBuf = mask.getByteBuffer();
        FloatBuffer residualBuf = residual.getFloatBuffer();
        FloatBuffer targetBuf = target.getFloatBuffer();
        FloatBuffer transformedBuf = transformed.getFloatBuffer();
        ByteBuffer relResBuf = this.relativeResidual.getByteBuffer();
        while (maskBuf.hasRemaining() && residualBuf.hasRemaining() && targetBuf.hasRemaining() && transformedBuf.hasRemaining() && relResBuf.hasRemaining()) {
            if (maskBuf.get() == (byte) 0) {
                residualBuf.position(residualBuf.position() + channels);
                targetBuf.position(targetBuf.position() + channels);
                transformedBuf.position(transformedBuf.position() + channels);
                relResBuf.put((byte) 0);
            } else {
                double relativeNorm = 0.0d;
                double brightness = 0.0d;
                for (int z = 0; z < channels; z++) {
                    float r = Math.abs(residualBuf.get());
                    float c = targetBuf.get();
                    float t = transformedBuf.get();
                    if (z < 3) {
                        float maxct = Math.max(c, t);
                        brightness += (double) maxct;
                        relativeNorm = Math.max((double) (r / maxct), relativeNorm);
                    }
                }
                if (brightness < brightnessMin) {
                    relResBuf.put((byte) 0);
                } else {
                    relResBuf.put((byte) ((int) Math.round((255.0d / this.settings.thresholdHigh) * Math.min(relativeNorm, this.settings.thresholdHigh))));
                }
            }
        }
        JavaCV.hysteresisThreshold(this.relativeResidual, this.binaryImage, 255.0d, (255.0d * this.settings.thresholdLow) / this.settings.thresholdHigh, 255.0d);
        int roiX = roi.x();
        int roiY = roi.y();
        opencv_core.cvSetImageROI(this.binaryImage, roi);
        if (this.settings.mopIterations > 0) {
            opencv_imgproc.cvMorphologyEx(this.binaryImage, this.binaryImage, null, null, 2, this.settings.mopIterations);
            opencv_imgproc.cvMorphologyEx(this.binaryImage, this.binaryImage, null, null, 3, this.settings.mopIterations);
        }
        CvArr contour = new CvContour(null);
        opencv_imgproc.cvFindContours(this.binaryImage, this.storage, contour, Loader.sizeof(CvContour.class), 0, 1);
        double largestContourEdgeArea = 0.0d;
        CvArr largestContour = null;
        while (contour != null && !contour.isNull()) {
            int i2;
            this.contourPointsSize = contour.total();
            if (this.contourPoints == null || this.contourPoints.capacity() < ((long) this.contourPointsSize)) {
                this.contourPoints = new CvPoint((long) this.contourPointsSize);
                this.contourPointsBuffer = this.contourPoints.asByteBuffer().asIntBuffer();
            }
            opencv_core.cvCvtSeqToArray(contour, this.contourPoints.position(0));
            double[] edgePts = new double[roiPts.length];
            for (i2 = 0; i2 < roiPts.length / 2; i2++) {
                edgePts[i2 * 2] = (roiPts[i2 * 2] / ((double) (1 << pyramidLevel))) - ((double) roiX);
                edgePts[(i2 * 2) + 1] = (roiPts[(i2 * 2) + 1] / ((double) (1 << pyramidLevel))) - ((double) roiY);
            }
            double m00 = 0.0d;
            double m10 = 0.0d;
            double m01 = 0.0d;
            for (i2 = 0; i2 < this.contourPointsSize; i2++) {
                int x = this.contourPointsBuffer.get(i2 * 2);
                int y = this.contourPointsBuffer.get((i2 * 2) + 1);
                for (int j = 0; j < roiPts.length / 2; j++) {
                    double x1 = edgePts[j * 2];
                    double y1 = edgePts[(j * 2) + 1];
                    double dx = edgePts[((j * 2) + 2) % edgePts.length] - x1;
                    double dy = edgePts[((j * 2) + 3) % edgePts.length] - y1;
                    double u = (((((double) x) - x1) * dx) + ((((double) y) - y1) * dy)) / ((dx * dx) + (dy * dy));
                    dx = (x1 + (u * dx)) - ((double) x);
                    dy = (y1 + (u * dy)) - ((double) y);
                    if ((dx * dx) + (dy * dy) < 2.0d) {
                        m00 += 1.0d;
                        m10 += (double) x;
                        m01 += (double) y;
                        break;
                    }
                }
            }
            double contourEdgeArea = m00 * Math.abs(opencv_imgproc.cvContourArea(contour, opencv_core.CV_WHOLE_SEQ, 0));
            if (contourEdgeArea > contourEdgeAreaMin && contourEdgeArea < contourEdgeAreaMax && contourEdgeArea > largestContourEdgeArea) {
                largestContourEdgeArea = contourEdgeArea;
                largestContour = contour;
                double inv_m00 = 1.0d / m00;
                this.edgeX = m10 * inv_m00;
                this.edgeY = m01 * inv_m00;
            }
            contour = contour.h_next();
        }
        if (isClick()) {
            this.prevTipX = -1.0d;
            this.prevTipY = -1.0d;
            this.prevTipTime = 0;
        } else if (!isSteady()) {
            this.prevTipX = this.tipX;
            this.prevTipY = this.tipY;
            this.prevTipTime = System.currentTimeMillis();
        }
        if (largestContour == null) {
            this.tipX = -1.0d;
            this.tipY = -1.0d;
            this.tipTime = 0;
            this.imageUpdateNeeded = false;
        } else {
            opencv_imgproc.cvMoments(largestContour, this.moments, 0);
            inv_m00 = 1.0d / this.moments.m00();
            this.centerX = this.moments.m10() * inv_m00;
            this.centerY = this.moments.m01() * inv_m00;
            this.contourPointsSize = largestContour.total();
            opencv_core.cvCvtSeqToArray(largestContour, this.contourPoints.position(0));
            double tipDist2 = 0.0d;
            int tipIndex = 0;
            for (i2 = 0; i2 < this.contourPointsSize; i2++) {
                dx = this.centerX - this.edgeX;
                dy = this.centerY - this.edgeY;
                u = (((((double) this.contourPointsBuffer.get(i2 * 2)) - this.edgeX) * dx) + ((((double) this.contourPointsBuffer.get((i2 * 2) + 1)) - this.edgeY) * dy)) / ((dx * dx) + (dy * dy));
                dx = (this.edgeX + (u * dx)) - this.edgeX;
                dy = (this.edgeY + (u * dy)) - this.edgeY;
                double d2 = (dx * dx) + (dy * dy);
                if (d2 > tipDist2) {
                    tipIndex = i2;
                    tipDist2 = d2;
                }
            }
            double a = (this.imageTipX < 0.0d || this.imageTipY < 0.0d) ? 1.0d : this.settings.updateAlpha;
            this.imageTipX = (((double) this.contourPointsBuffer.get(tipIndex * 2)) * a) + ((1.0d - a) * this.imageTipX);
            this.imageTipY = (((double) this.contourPointsBuffer.get((tipIndex * 2) + 1)) * a) + ((1.0d - a) * this.imageTipY);
            this.tipX = (this.imageTipX + ((double) roiX)) * ((double) (1 << pyramidLevel));
            this.tipY = (this.imageTipY + ((double) roiY)) * ((double) (1 << pyramidLevel));
            this.tipTime = System.currentTimeMillis();
            this.imageUpdateNeeded = true;
        }
        opencv_core.cvClearMemStorage(this.storage);
    }

    public IplImage getRelativeResidual() {
        return this.relativeResidual;
    }

    public IplImage getResultImage() {
        if (this.imageUpdateNeeded) {
            opencv_core.cvSetZero(this.binaryImage);
            opencv_imgproc.cvFillPoly(this.binaryImage, this.contourPoints, this.intPointer.put(this.contourPointsSize), 1, CvScalar.WHITE, 8, 0);
            this.pt1.put((byte) 16, new double[]{this.edgeX, this.edgeY});
            opencv_imgproc.cvCircle(this.binaryImage, this.pt1, 327680, CvScalar.GRAY, 2, 8, 16);
            this.pt1.put((byte) 16, new double[]{this.centerX - 5.0d, this.centerY - 5.0d});
            this.pt2.put((byte) 16, new double[]{this.centerX + 5.0d, this.centerY + 5.0d});
            opencv_imgproc.cvRectangle(this.binaryImage, this.pt1, this.pt2, CvScalar.GRAY, 2, 8, 16);
            this.pt1.put((byte) 16, new double[]{this.imageTipX - 5.0d, this.imageTipY - 5.0d});
            this.pt2.put((byte) 16, new double[]{this.imageTipX + 5.0d, this.imageTipY + 5.0d});
            opencv_imgproc.cvLine(this.binaryImage, this.pt1, this.pt2, CvScalar.GRAY, 2, 8, 16);
            this.pt1.put((byte) 16, new double[]{this.imageTipX - 5.0d, this.imageTipY + 5.0d});
            this.pt2.put((byte) 16, new double[]{this.imageTipX + 5.0d, this.imageTipY - 5.0d});
            opencv_imgproc.cvLine(this.binaryImage, this.pt1, this.pt2, CvScalar.GRAY, 2, 8, 16);
            opencv_core.cvResetImageROI(this.binaryImage);
            this.imageUpdateNeeded = false;
        }
        return this.binaryImage;
    }

    public double getX() {
        return this.tipX;
    }

    public double getY() {
        return this.tipY;
    }

    public boolean isSteady() {
        if (this.tipX < 0.0d || this.tipY < 0.0d || this.prevTipX < 0.0d || this.prevTipY < 0.0d) {
            return false;
        }
        double dx = this.tipX - this.prevTipX;
        double dy = this.tipY - this.prevTipY;
        double steadySize = this.settings.clickSteadySize * ((double) ((this.roi.width() + this.roi.height()) / 2));
        if ((dx * dx) + (dy * dy) < steadySize * steadySize) {
            return true;
        }
        return false;
    }

    public boolean isClick() {
        return isSteady() && this.tipTime - this.prevTipTime > this.settings.clickSteadyTime;
    }
}
