package org.bytedeco.javacv;

import java.io.File;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_videoio.VideoCapture;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameGrabber.ImageMode;
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat;

public class OpenCVFrameGrabber extends FrameGrabber {
    private static Exception loadingException = null;
    private VideoCapture capture;
    private final OpenCVFrameConverter converter;
    private int deviceNumber;
    private String filename;
    private final Mat mat;
    private Mat returnMatrix;

    public static String[] getDeviceDescriptions() throws Exception {
        tryLoad();
        throw new UnsupportedOperationException("Device enumeration not support by OpenCV.");
    }

    public static OpenCVFrameGrabber createDefault(File deviceFile) throws Exception {
        return new OpenCVFrameGrabber(deviceFile);
    }

    public static OpenCVFrameGrabber createDefault(String devicePath) throws Exception {
        return new OpenCVFrameGrabber(devicePath);
    }

    public static OpenCVFrameGrabber createDefault(int deviceNumber) throws Exception {
        return new OpenCVFrameGrabber(deviceNumber);
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            Loader.load(opencv_highgui.class);
        } catch (Throwable t) {
            loadingException = new Exception("Failed to load " + OpenCVFrameGrabber.class, t);
        }
    }

    public OpenCVFrameGrabber(int deviceNumber) {
        this.deviceNumber = 0;
        this.filename = null;
        this.capture = null;
        this.returnMatrix = null;
        this.converter = new ToMat();
        this.mat = new Mat();
        this.deviceNumber = deviceNumber;
    }

    public OpenCVFrameGrabber(File file) {
        this(file.getAbsolutePath());
    }

    public OpenCVFrameGrabber(String filename) {
        this.deviceNumber = 0;
        this.filename = null;
        this.capture = null;
        this.returnMatrix = null;
        this.converter = new ToMat();
        this.mat = new Mat();
        this.filename = filename;
    }

    public void release() throws Exception {
        stop();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public double getGamma() {
        if (this.gamma == 0.0d) {
            return 2.2d;
        }
        return this.gamma;
    }

    public String getFormat() {
        if (this.capture == null) {
            return super.getFormat();
        }
        int fourcc = (int) this.capture.get(6);
        return "" + ((char) (fourcc & 255)) + ((char) ((fourcc >> 8) & 255)) + ((char) ((fourcc >> 16) & 255)) + ((char) ((fourcc >> 24) & 255));
    }

    public int getImageWidth() {
        if (this.returnMatrix != null) {
            return this.returnMatrix.cols();
        }
        return this.capture == null ? super.getImageWidth() : (int) this.capture.get(3);
    }

    public int getImageHeight() {
        if (this.returnMatrix != null) {
            return this.returnMatrix.rows();
        }
        return this.capture == null ? super.getImageHeight() : (int) this.capture.get(4);
    }

    public int getPixelFormat() {
        return this.capture == null ? super.getPixelFormat() : (int) this.capture.get(16);
    }

    public double getFrameRate() {
        return this.capture == null ? super.getFrameRate() : (double) ((int) this.capture.get(5));
    }

    public void setImageMode(ImageMode imageMode) {
        if (imageMode != this.imageMode) {
            this.returnMatrix = null;
        }
        super.setImageMode(imageMode);
    }

    public int getFrameNumber() {
        if (this.capture == null) {
            return super.getFrameNumber();
        }
        return (int) this.capture.get(1);
    }

    public void setFrameNumber(int frameNumber) throws Exception {
        if (this.capture == null) {
            super.setFrameNumber(frameNumber);
        } else if (!this.capture.set(1, (double) frameNumber)) {
            throw new Exception("set() Error: Could not set CV_CAP_PROP_POS_FRAMES to " + frameNumber + ".");
        }
    }

    public long getTimestamp() {
        if (this.capture == null) {
            return super.getTimestamp();
        }
        return Math.round(this.capture.get(0) * 1000.0d);
    }

    public void setTimestamp(long timestamp) throws Exception {
        if (this.capture == null) {
            super.setTimestamp(timestamp);
        } else if (!this.capture.set(0, ((double) timestamp) / 1000.0d)) {
            throw new Exception("set() Error: Could not set CV_CAP_PROP_POS_MSEC to " + (((double) timestamp) / 1000.0d) + ".");
        }
    }

    public int getLengthInFrames() {
        if (this.capture == null) {
            return super.getLengthInFrames();
        }
        return (int) this.capture.get(7);
    }

    public long getLengthInTime() {
        return Math.round(((double) (((long) getLengthInFrames()) * 1000000)) / getFrameRate());
    }

    public void start() throws Exception {
        double d = 0.0d;
        if (this.filename == null || this.filename.length() <= 0) {
            this.capture = new VideoCapture(this.deviceNumber);
        } else {
            this.capture = new VideoCapture(this.filename);
        }
        if (this.imageWidth > 0 && !this.capture.set(3, (double) this.imageWidth)) {
            this.capture.set(9, (double) this.imageWidth);
        }
        if (this.imageHeight > 0 && !this.capture.set(4, (double) this.imageHeight)) {
            this.capture.set(9, (double) this.imageHeight);
        }
        if (this.frameRate > 0.0d) {
            this.capture.set(5, this.frameRate);
        }
        if (this.bpp > 0) {
            this.capture.set(8, (double) this.bpp);
        }
        VideoCapture videoCapture = this.capture;
        if (this.imageMode == ImageMode.COLOR) {
            d = 1.0d;
        }
        videoCapture.set(16, d);
        Mat mat = new Mat();
        int i = 0;
        while (true) {
            int count = i + 1;
            if (i >= 100) {
                break;
            }
            try {
                if (this.capture.read(mat)) {
                    break;
                }
                Thread.sleep(100);
                i = count;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (!this.capture.read(mat)) {
            throw new Exception("read() Error: Could not read frame in start().");
        } else if (!this.triggerMode && !this.capture.grab()) {
            throw new Exception("grab() Error: Could not grab frame. (Has start() been called?)");
        }
    }

    public void stop() throws Exception {
        if (this.capture != null) {
            this.capture.release();
            this.capture = null;
        }
    }

    public void trigger() throws Exception {
        Mat mat = new Mat();
        for (int i = 0; i < this.numBuffers + 1; i++) {
            this.capture.read(mat);
        }
        if (!this.capture.grab()) {
            throw new Exception("grab() Error: Could not grab frame. (Has start() been called?)");
        }
    }

    public Frame grab() throws Exception {
        if (!this.capture.retrieve(this.mat)) {
            throw new Exception("retrieve() Error: Could not retrieve frame. (Has start() been called?)");
        } else if (this.triggerMode || this.capture.grab()) {
            if (this.imageMode == ImageMode.GRAY && this.mat.channels() > 1) {
                if (this.returnMatrix == null) {
                    this.returnMatrix = new Mat(new int[]{this.mat.rows(), this.mat.cols(), this.mat.depth(), 1});
                }
                opencv_imgproc.cvtColor(this.mat, this.returnMatrix, 6);
            } else if (this.imageMode == ImageMode.COLOR && this.mat.channels() == 1) {
                if (this.returnMatrix == null) {
                    this.returnMatrix = new Mat(new int[]{this.mat.rows(), this.mat.cols(), this.mat.depth(), 3});
                }
                opencv_imgproc.cvtColor(this.mat, this.returnMatrix, 8);
            } else {
                this.returnMatrix = this.mat;
            }
            return this.converter.convert(this.returnMatrix);
        } else {
            throw new Exception("grab() Error: Could not grab frame. (Has start() been called?)");
        }
    }
}
