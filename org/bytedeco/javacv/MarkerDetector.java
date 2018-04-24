package org.bytedeco.javacv;

import java.util.Arrays;
import org.apache.poi.hssf.record.DeltaRecord;
import org.bytedeco.javacpp.ARToolKitPlus.ARMarkerInfo;
import org.bytedeco.javacpp.ARToolKitPlus.MultiTracker;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvMemStorage;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core$CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvBox2D;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.CvTermCriteria;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_imgproc.CvFont;

public class MarkerDetector {
    private int channels;
    private CvPoint2D32f corners;
    private int depth;
    private CvFont font;
    private int height;
    private IntPointer markerNum;
    private opencv_core$CvMemStorage memory;
    private CvMat points;
    private Settings settings;
    private IplImage sqSumImage;
    private CvSize subPixelSize;
    private CvTermCriteria subPixelTermCriteria;
    private CvSize subPixelZeroZone;
    private IplImage sumImage;
    private IplImage tempImage;
    private IplImage tempImage2;
    private CvSize textSize;
    private IplImage thresholdedImage;
    private MultiTracker tracker;
    private int width;

    public static class Settings extends BaseChildSettings {
        int subPixelWindow = 11;
        double thresholdKBlackMarkers = 0.6d;
        double thresholdKWhiteMarkers = 1.0d;
        double thresholdVarMultiplier = 1.0d;
        int thresholdWindowMax = 63;
        int thresholdWindowMin = 5;

        public int getThresholdWindowMin() {
            return this.thresholdWindowMin;
        }

        public void setThresholdWindowMin(int thresholdWindowMin) {
            this.thresholdWindowMin = thresholdWindowMin;
        }

        public int getThresholdWindowMax() {
            return this.thresholdWindowMax;
        }

        public void setThresholdWindowMax(int thresholdWindowMax) {
            this.thresholdWindowMax = thresholdWindowMax;
        }

        public double getThresholdVarMultiplier() {
            return this.thresholdVarMultiplier;
        }

        public void setThresholdVarMultiplier(double thresholdVarMultiplier) {
            this.thresholdVarMultiplier = thresholdVarMultiplier;
        }

        public double getThresholdKBlackMarkers() {
            return this.thresholdKBlackMarkers;
        }

        public void setThresholdKBlackMarkers(double thresholdKBlackMarkers) {
            this.thresholdKBlackMarkers = thresholdKBlackMarkers;
        }

        public double getThresholdKWhiteMarkers() {
            return this.thresholdKWhiteMarkers;
        }

        public void setThresholdKWhiteMarkers(double thresholdKWhiteMarkers) {
            this.thresholdKWhiteMarkers = thresholdKWhiteMarkers;
        }

        public int getSubPixelWindow() {
            return this.subPixelWindow;
        }

        public void setSubPixelWindow(int subPixelWindow) {
            this.subPixelWindow = subPixelWindow;
        }
    }

    public MarkerDetector(Settings settings) {
        this.tracker = null;
        this.markerNum = new IntPointer(1);
        this.width = 0;
        this.height = 0;
        this.depth = 0;
        this.channels = 0;
        this.points = CvMat.create(1, 4, 5, 2);
        this.corners = new CvPoint2D32f(4);
        this.memory = AbstractCvMemStorage.create();
        this.subPixelSize = null;
        this.subPixelZeroZone = null;
        this.subPixelTermCriteria = null;
        this.font = opencv_imgproc.cvFont(1.0d, 1);
        this.textSize = new CvSize();
        setSettings(settings);
    }

    public MarkerDetector() {
        this(new Settings());
    }

    public Settings getSettings() {
        return this.settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        this.subPixelSize = opencv_core.cvSize(settings.subPixelWindow / 2, settings.subPixelWindow / 2);
        this.subPixelZeroZone = opencv_core.cvSize(-1, -1);
        this.subPixelTermCriteria = opencv_core.cvTermCriteria(2, 100, DeltaRecord.DEFAULT_VALUE);
    }

    public IplImage getThresholdedImage() {
        return this.thresholdedImage;
    }

    private void init(IplImage image) {
        if (this.tracker == null || image.width() != this.width || image.height() != this.height || image.depth() != this.depth || image.nChannels() != this.channels) {
            this.width = image.width();
            this.height = image.height();
            this.depth = image.depth();
            this.channels = image.nChannels();
            if (this.depth != 8 || this.channels > 1) {
                this.tempImage = IplImage.create(this.width, this.height, 8, 1);
            }
            if (this.depth != 8 && this.channels > 1) {
                this.tempImage2 = IplImage.create(this.width, this.height, 8, 3);
            }
            this.sumImage = IplImage.create(this.width + 1, this.height + 1, 64, 1);
            this.sqSumImage = IplImage.create(this.width + 1, this.height + 1, 64, 1);
            this.thresholdedImage = IplImage.create(this.width, this.height, 8, 1);
            this.tracker = new MultiTracker(this.thresholdedImage.widthStep(), this.thresholdedImage.height());
            this.tracker.setPixelFormat(7);
            this.tracker.setBorderWidth(0.125f);
            this.tracker.setUndistortionMode(0);
            this.tracker.setMarkerMode(2);
            this.tracker.setImageProcessingMode(1);
        }
    }

    public Marker[] detect(IplImage image, boolean whiteMarkers) {
        CvArr image2;
        double d;
        init(image);
        if (this.depth != 8 && this.channels > 1) {
            opencv_core.cvConvertScale(image, this.tempImage2, 255.0d / image.highValue(), 0.0d);
            opencv_imgproc.cvCvtColor(this.tempImage2, this.tempImage, this.channels > 3 ? 11 : 6);
            image2 = this.tempImage;
        } else if (this.depth != 8) {
            opencv_core.cvConvertScale(image, this.tempImage, 255.0d / image.highValue(), 0.0d);
            image2 = this.tempImage;
        } else if (this.channels > 1) {
            opencv_imgproc.cvCvtColor(image, this.tempImage, this.channels > 3 ? 11 : 6);
            image2 = this.tempImage;
        }
        IplImage iplImage = this.sumImage;
        IplImage iplImage2 = this.sqSumImage;
        IplImage iplImage3 = this.thresholdedImage;
        int i = this.settings.thresholdWindowMax;
        int i2 = this.settings.thresholdWindowMin;
        double d2 = this.settings.thresholdVarMultiplier;
        if (whiteMarkers) {
            d = this.settings.thresholdKWhiteMarkers;
        } else {
            d = this.settings.thresholdKBlackMarkers;
        }
        JavaCV.adaptiveThreshold(image2, iplImage, iplImage2, iplImage3, whiteMarkers, i, i2, d2, d);
        int n = 0;
        ARMarkerInfo aRMarkerInfo = new ARMarkerInfo(null);
        this.tracker.arDetectMarkerLite(this.thresholdedImage.imageData(), 128, aRMarkerInfo, this.markerNum);
        Marker[] markers2 = new Marker[this.markerNum.get(0)];
        for (int i3 = 0; i3 < markers2.length && !aRMarkerInfo.isNull(); i3++) {
            aRMarkerInfo.position((long) i3);
            int id = aRMarkerInfo.id();
            if (id >= 0) {
                int dir = aRMarkerInfo.dir();
                float confidence = aRMarkerInfo.cf();
                float[] vertex = new float[8];
                aRMarkerInfo.vertex().get(vertex);
                int w = (this.settings.subPixelWindow / 2) + 1;
                if (vertex[0] - ((float) w) >= 0.0f && vertex[0] + ((float) w) < ((float) this.width) && vertex[1] - ((float) w) >= 0.0f && vertex[1] + ((float) w) < ((float) this.height) && vertex[2] - ((float) w) >= 0.0f && vertex[2] + ((float) w) < ((float) this.width) && vertex[3] - ((float) w) >= 0.0f && vertex[3] + ((float) w) < ((float) this.height) && vertex[4] - ((float) w) >= 0.0f && vertex[4] + ((float) w) < ((float) this.width) && vertex[5] - ((float) w) >= 0.0f && vertex[5] + ((float) w) < ((float) this.height) && vertex[6] - ((float) w) >= 0.0f && vertex[6] + ((float) w) < ((float) this.width) && vertex[7] - ((float) w) >= 0.0f && vertex[7] + ((float) w) < ((float) this.height)) {
                    this.points.getFloatBuffer().put(vertex);
                    CvBox2D box = opencv_imgproc.cvMinAreaRect2(this.points, this.memory);
                    float bw = box.size().width();
                    float bh = box.size().height();
                    opencv_core.cvClearMemStorage(this.memory);
                    if (bw > 0.0f && bh > 0.0f && ((double) (bw / bh)) >= 0.1d && bw / bh <= 10.0f) {
                        for (int j = 0; j < 4; j++) {
                            this.corners.position((long) j).put((double) vertex[j * 2], (double) vertex[(j * 2) + 1]);
                        }
                        opencv_imgproc.cvFindCornerSubPix(image2, this.corners.position(0), 4, this.subPixelSize, this.subPixelZeroZone, this.subPixelTermCriteria);
                        int n2 = n + 1;
                        markers2[n] = new Marker(id, new double[]{(double) this.corners.position((long) ((4 - dir) % 4)).x(), (double) this.corners.position((long) ((4 - dir) % 4)).y(), (double) this.corners.position((long) ((5 - dir) % 4)).x(), (double) this.corners.position((long) ((5 - dir) % 4)).y(), (double) this.corners.position((long) ((6 - dir) % 4)).x(), (double) this.corners.position((long) ((6 - dir) % 4)).y(), (double) this.corners.position((long) ((7 - dir) % 4)).x(), (double) this.corners.position((long) ((7 - dir) % 4)).y()}, (double) confidence);
                        n = n2;
                    }
                }
            }
        }
        return (Marker[]) Arrays.copyOf(markers2, n);
    }

    public void draw(IplImage image, Marker[] markers) {
        for (Marker m : markers) {
            int cx = 0;
            int cy = 0;
            int[] pts = new int[8];
            for (int i = 0; i < 4; i++) {
                int x = (int) Math.round(m.corners[i * 2] * 65536.0d);
                int y = (int) Math.round(m.corners[(i * 2) + 1] * 65536.0d);
                pts[i * 2] = x;
                pts[(i * 2) + 1] = y;
                cx += x;
                cy += y;
            }
            cx /= 4;
            cy /= 4;
            opencv_imgproc.cvPolyLine(image, pts, new int[]{pts.length / 2}, 1, 1, opencv_core.CV_RGB(0.0d, 0.0d, image.highValue()), 1, 16, 16);
            String text = Integer.toString(m.id);
            opencv_imgproc.cvGetTextSize(text, this.font, this.textSize, new int[1]);
            opencv_imgproc.cvRectangle(image, new int[]{cx - ((((this.textSize.width() * 3) / 2) << 16) / 2), ((((this.textSize.height() * 3) / 2) << 16) / 2) + cy}, new int[]{((((this.textSize.width() * 3) / 2) << 16) / 2) + cx, cy - ((((this.textSize.height() * 3) / 2) << 16) / 2)}, opencv_core.CV_RGB(0.0d, image.highValue(), 0.0d), -1, 16, 16);
            opencv_imgproc.cvPutText(image, text, new int[]{(int) Math.round((((double) cx) / 65536.0d) - ((double) (this.textSize.width() / 2))), ((int) Math.round((((double) cy) / 65536.0d) + ((double) (this.textSize.height() / 2)))) + 1}, this.font, CvScalar.BLACK);
        }
    }
}
