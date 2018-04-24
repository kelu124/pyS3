package org.bytedeco.javacv;

import java.io.File;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.videoInputLib;
import org.bytedeco.javacpp.videoInputLib.videoInput;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameGrabber.ImageMode;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

public class VideoInputFrameGrabber extends FrameGrabber {
    private static Exception loadingException = null;
    private IplImage bgrImage = null;
    private BytePointer bgrImageData = null;
    private FrameConverter converter = new ToIplImage();
    private int deviceNumber = 0;
    private IplImage grayImage = null;
    private videoInput myVideoInput = null;

    public static String[] getDeviceDescriptions() throws Exception {
        tryLoad();
        String[] descriptions = new String[videoInput.listDevices()];
        for (int i = 0; i < descriptions.length; i++) {
            descriptions[i] = videoInput.getDeviceName(i).getString();
        }
        return descriptions;
    }

    public static VideoInputFrameGrabber createDefault(File deviceFile) throws Exception {
        throw new Exception(VideoInputFrameGrabber.class + " does not support device files.");
    }

    public static VideoInputFrameGrabber createDefault(String devicePath) throws Exception {
        throw new Exception(VideoInputFrameGrabber.class + " does not support device paths.");
    }

    public static VideoInputFrameGrabber createDefault(int deviceNumber) throws Exception {
        return new VideoInputFrameGrabber(deviceNumber);
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            Loader.load(videoInputLib.class);
        } catch (Throwable t) {
            loadingException = new Exception("Failed to load " + VideoInputFrameGrabber.class, t);
        }
    }

    public VideoInputFrameGrabber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
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

    public int getImageWidth() {
        return this.myVideoInput == null ? super.getImageWidth() : this.myVideoInput.getWidth(this.deviceNumber);
    }

    public int getImageHeight() {
        return this.myVideoInput == null ? super.getImageHeight() : this.myVideoInput.getHeight(this.deviceNumber);
    }

    public void start() throws Exception {
        start(-1);
    }

    public void start(int connection) throws Exception {
        this.myVideoInput = new videoInput();
        if (this.frameRate > 0.0d) {
            this.myVideoInput.setIdealFramerate(this.deviceNumber, (int) this.frameRate);
        }
        if (!this.myVideoInput.setupDevice(this.deviceNumber, this.imageWidth > 0 ? this.imageWidth : 640, this.imageHeight > 0 ? this.imageHeight : 480, connection)) {
            this.myVideoInput = null;
            throw new Exception("videoInput.setupDevice() Error: Could not setup device.");
        } else if (this.format != null && this.format.length() > 0) {
            int f = this.format.equals("VI_NTSC_M") ? 0 : this.format.equals("VI_PAL_B") ? 1 : this.format.equals("VI_PAL_D") ? 2 : this.format.equals("VI_PAL_G") ? 3 : this.format.equals("VI_PAL_H") ? 4 : this.format.equals("VI_PAL_I") ? 5 : this.format.equals("VI_PAL_M") ? 6 : this.format.equals("VI_PAL_N") ? 7 : this.format.equals("VI_PAL_NC") ? 8 : this.format.equals("VI_SECAM_B") ? 9 : this.format.equals("VI_SECAM_D") ? 10 : this.format.equals("VI_SECAM_G") ? 11 : this.format.equals("VI_SECAM_H") ? 12 : this.format.equals("VI_SECAM_K") ? 13 : this.format.equals("VI_SECAM_K1") ? 14 : this.format.equals("VI_SECAM_L") ? 15 : this.format.equals("VI_NTSC_M_J") ? 16 : this.format.equals("VI_NTSC_433") ? 17 : -1;
            if (f >= 0 && !this.myVideoInput.setFormat(this.deviceNumber, f)) {
                throw new Exception("videoInput.setFormat() Error: Could not set format " + this.format + ".");
            }
        }
    }

    public void stop() throws Exception {
        if (this.myVideoInput != null) {
            this.myVideoInput.stopDevice(this.deviceNumber);
            this.myVideoInput = null;
        }
    }

    public void trigger() throws Exception {
        if (this.myVideoInput == null) {
            throw new Exception("videoInput is null. (Has start() been called?)");
        }
        int w = this.myVideoInput.getWidth(this.deviceNumber);
        int h = this.myVideoInput.getHeight(this.deviceNumber);
        if (!(this.bgrImage != null && this.bgrImage.width() == w && this.bgrImage.height() == h)) {
            this.bgrImage = IplImage.create(w, h, 8, 3);
            this.bgrImageData = this.bgrImage.imageData();
        }
        for (int i = 0; i < this.numBuffers + 1; i++) {
            this.myVideoInput.getPixels(this.deviceNumber, this.bgrImageData, false, true);
        }
    }

    public Frame grab() throws Exception {
        if (this.myVideoInput == null) {
            throw new Exception("videoInput is null. (Has start() been called?)");
        }
        int w = this.myVideoInput.getWidth(this.deviceNumber);
        int h = this.myVideoInput.getHeight(this.deviceNumber);
        if (!(this.bgrImage != null && this.bgrImage.width() == w && this.bgrImage.height() == h)) {
            this.bgrImage = IplImage.create(w, h, 8, 3);
            this.bgrImageData = this.bgrImage.imageData();
        }
        if (this.myVideoInput.getPixels(this.deviceNumber, this.bgrImageData, false, true)) {
            this.timestamp = System.nanoTime() / 1000;
            if (this.imageMode != ImageMode.GRAY) {
                return this.converter.convert(this.bgrImage);
            }
            if (this.grayImage == null) {
                this.grayImage = IplImage.create(w, h, 8, 1);
            }
            opencv_imgproc.cvCvtColor(this.bgrImage, this.grayImage, 6);
            return this.converter.convert(this.grayImage);
        }
        throw new Exception("videoInput.getPixels() Error: Could not get pixels.");
    }
}
