package org.bytedeco.javacv;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.RealSense;
import org.bytedeco.javacpp.RealSense.context;
import org.bytedeco.javacpp.RealSense.device;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameGrabber.ImageMode;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

public class RealSenseFrameGrabber extends FrameGrabber {
    public static int DEFAULT_COLOR_FRAMERATE = 30;
    public static int DEFAULT_COLOR_HEIGHT = 720;
    public static int DEFAULT_COLOR_WIDTH = 1280;
    public static int DEFAULT_DEPTH_HEIGHT = 480;
    public static int DEFAULT_DEPTH_WIDTH = 640;
    private static context context = null;
    private static Exception loadingException = null;
    private boolean IREnabled = false;
    private int IRFrameRate = 30;
    private int IRImageHeight = DEFAULT_DEPTH_HEIGHT;
    private int IRImageWidth = DEFAULT_DEPTH_WIDTH;
    private boolean behaveAsColorFrameGrabber = false;
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    private boolean colorEnabled = false;
    private FrameConverter converter = new ToIplImage();
    private boolean depth = false;
    private boolean depthEnabled = false;
    private int depthFrameRate = 30;
    private int depthImageHeight = DEFAULT_DEPTH_HEIGHT;
    private int depthImageWidth = DEFAULT_DEPTH_WIDTH;
    private device device = null;
    private int deviceNumber = 0;
    private IplImage rawDepthImage = null;
    private Pointer rawDepthImageData = new Pointer(null);
    private IplImage rawIRImage = null;
    private Pointer rawIRImageData = new Pointer(null);
    private IplImage rawVideoImage = null;
    private Pointer rawVideoImageData = new Pointer(null);
    private IplImage returnImage = null;
    private boolean startedOnce = false;

    public static String[] getDeviceDescriptions() throws Exception {
        tryLoad();
        String[] desc = new String[context.get_device_count()];
        for (int i = 0; i < desc.length; i++) {
            desc[i] = context.get_device(i).get_name().getString();
        }
        return desc;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public void setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    public static RealSenseFrameGrabber createDefault(int deviceNumber) throws Exception {
        return new RealSenseFrameGrabber(deviceNumber);
    }

    public static RealSenseFrameGrabber createDefault(File deviceFile) throws Exception {
        throw new Exception(RealSenseFrameGrabber.class + " does not support File devices.");
    }

    public static RealSenseFrameGrabber createDefault(String devicePath) throws Exception {
        throw new Exception(RealSenseFrameGrabber.class + " does not support path.");
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            loadingException.printStackTrace();
            throw loadingException;
        }
        try {
            if (context == null) {
                Loader.load(RealSense.class);
                context = new context();
                System.out.println("RealSense devices found: " + context.get_device_count());
            }
        } catch (Throwable t) {
            loadingException = new Exception("Failed to load " + RealSenseFrameGrabber.class, t);
        }
    }

    public RealSenseFrameGrabber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public static void main(String[] args) {
        context context = new context();
        System.out.println("Devices found: " + context.get_device_count());
        device device = context.get_device(0);
        System.out.println("Using device 0, an " + device.get_name());
        System.out.println(" Serial number: " + device.get_serial());
    }

    public void enableColorStream() {
        if (!this.colorEnabled) {
            if (this.imageWidth == 0) {
                this.imageWidth = DEFAULT_COLOR_WIDTH;
            }
            if (this.imageHeight == 0) {
                this.imageHeight = DEFAULT_COLOR_HEIGHT;
            }
            if (this.frameRate == 0.0d) {
                this.frameRate = (double) DEFAULT_COLOR_FRAMERATE;
            }
            this.device.enable_stream(1, this.imageWidth, this.imageHeight, 5, (int) this.frameRate);
            this.colorEnabled = true;
        }
    }

    public void disableColorStream() {
        if (this.colorEnabled) {
            this.device.disable_stream(1);
            this.colorEnabled = false;
        }
    }

    public void enableDepthStream() {
        if (!this.depthEnabled) {
            this.device.enable_stream(0, this.depthImageWidth, this.depthImageHeight, 1, this.depthFrameRate);
            this.depthEnabled = true;
        }
    }

    public void disableDepthStream() {
        if (this.depthEnabled) {
            this.device.disable_stream(0);
            this.depthEnabled = false;
        }
    }

    public void enableIRStream() {
        if (!this.IREnabled) {
            this.device.enable_stream(2, this.IRImageWidth, this.IRImageHeight, 9, this.IRFrameRate);
            this.IREnabled = true;
        }
    }

    public void disableIRStream() {
        if (this.IREnabled) {
            this.device.disable_stream(2);
            this.IREnabled = false;
        }
    }

    public void release() throws Exception {
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public device getRealSenseDevice() {
        return this.device;
    }

    public float getDepthScale() {
        return this.device.get_depth_scale();
    }

    public double getFrameRate() {
        return super.getFrameRate();
    }

    public void start() throws Exception {
        if (context == null || context.get_device_count() <= this.deviceNumber) {
            throw new Exception("FATAL error: Realsense camera: " + this.deviceNumber + " not connected/found");
        }
        this.device = context.get_device(this.deviceNumber);
        if (!(this.colorEnabled || this.IREnabled || this.depthEnabled || this.startedOnce)) {
            enableColorStream();
            this.behaveAsColorFrameGrabber = true;
            setImageMode(ImageMode.GRAY);
        }
        this.startedOnce = true;
        this.device.start();
    }

    public void stop() throws Exception {
        this.device.stop();
        this.colorEnabled = false;
        this.IREnabled = false;
        this.depthEnabled = false;
        this.frameNumber = 0;
    }

    public IplImage grabDepth() {
        if (this.depthEnabled) {
            this.rawDepthImageData = this.device.get_frame_data(0);
            int deviceWidth = this.device.get_stream_width(0);
            int deviceHeight = this.device.get_stream_height(0);
            if (!(this.rawDepthImage != null && this.rawDepthImage.width() == deviceWidth && this.rawDepthImage.height() == deviceHeight)) {
                this.rawDepthImage = IplImage.createHeader(deviceWidth, deviceHeight, opencv_core.IPL_DEPTH_16S, 1);
            }
            opencv_core.cvSetData(this.rawDepthImage, this.rawDepthImageData, ((deviceWidth * 1) * opencv_core.IPL_DEPTH_16S) / 8);
            if (-2147483632 > 8 && !ByteOrder.nativeOrder().equals(this.byteOrder)) {
                ByteBuffer bb = this.rawDepthImage.getByteBuffer();
                bb.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer());
            }
            return this.rawDepthImage;
        }
        System.out.println("Depth stream not enabled, impossible to get the image.");
        return null;
    }

    public IplImage grabVideo() {
        if (this.colorEnabled) {
            this.rawVideoImageData = this.device.get_frame_data(1);
            int deviceWidth = this.device.get_stream_width(1);
            int deviceHeight = this.device.get_stream_height(1);
            if (!(this.rawVideoImage != null && this.rawVideoImage.width() == deviceWidth && this.rawVideoImage.height() == deviceHeight)) {
                this.rawVideoImage = IplImage.createHeader(deviceWidth, deviceHeight, 8, 3);
            }
            opencv_core.cvSetData(this.rawVideoImage, this.rawVideoImageData, ((deviceWidth * 3) * 8) / 8);
            return this.rawVideoImage;
        }
        System.out.println("Color stream not enabled, impossible to get the image.");
        return null;
    }

    public IplImage grabIR() {
        if (this.IREnabled) {
            this.rawIRImageData = this.device.get_frame_data(2);
            int deviceWidth = this.device.get_stream_width(2);
            int deviceHeight = this.device.get_stream_height(2);
            if (!(this.rawIRImage != null && this.rawIRImage.width() == deviceWidth && this.rawIRImage.height() == deviceHeight)) {
                this.rawIRImage = IplImage.createHeader(deviceWidth, deviceHeight, 8, 1);
            }
            opencv_core.cvSetData(this.rawIRImage, this.rawIRImageData, ((deviceWidth * 1) * 8) / 8);
            return this.rawIRImage;
        }
        System.out.println("IR stream not enabled, impossible to get the image.");
        return null;
    }

    public Frame grab() throws Exception {
        this.device.wait_for_frames();
        if (!this.colorEnabled || !this.behaveAsColorFrameGrabber) {
            return null;
        }
        IplImage image = grabVideo();
        if (this.returnImage == null) {
            this.returnImage = IplImage.create(this.device.get_stream_width(1), this.device.get_stream_height(1), 8, 1);
        }
        opencv_imgproc.cvCvtColor(image, this.returnImage, 6);
        return this.converter.convert(this.returnImage);
    }

    public void trigger() throws Exception {
        this.device.wait_for_frames();
    }

    public int getDepthImageWidth() {
        return this.depthImageWidth;
    }

    public void setDepthImageWidth(int depthImageWidth) {
        this.depthImageWidth = depthImageWidth;
    }

    public int getDepthImageHeight() {
        return this.depthImageHeight;
    }

    public void setDepthImageHeight(int depthImageHeight) {
        this.depthImageHeight = depthImageHeight;
    }

    public int getIRImageWidth() {
        return this.IRImageWidth;
    }

    public void setIRImageWidth(int IRImageWidth) {
        this.IRImageWidth = IRImageWidth;
    }

    public int getIRImageHeight() {
        return this.IRImageHeight;
    }

    public void setIRImageHeight(int IRImageHeight) {
        this.IRImageHeight = IRImageHeight;
    }

    public int getDepthFrameRate() {
        return this.depthFrameRate;
    }

    public void setDepthFrameRate(int frameRate) {
        this.depthFrameRate = frameRate;
    }

    public int getIRFrameRate() {
        return this.IRFrameRate;
    }

    public void setIRFrameRate(int IRFrameRate) {
        this.IRFrameRate = IRFrameRate;
    }

    public double getGamma() {
        if (this.gamma == 0.0d) {
            return 2.2d;
        }
        return this.gamma;
    }

    public void setPreset(int preset) {
        RealSense.apply_ivcam_preset(this.device, preset);
    }

    public void setShortRange() {
        setPreset(0);
    }

    public void setLongRange() {
        setPreset(1);
    }

    public void setMidRange() {
        setPreset(9);
    }

    public void setDefaultPreset() {
        setPreset(8);
    }

    public void setObjectScanningPreset() {
        setPreset(4);
    }

    public void setCursorPreset() {
        setPreset(7);
    }

    public void setGestureRecognitionPreset() {
        setPreset(3);
    }

    public void setBackgroundSegmentationPreset() {
        setPreset(2);
    }

    public void setIROnlyPreset() {
        setPreset(10);
    }

    public void setOption(int option, int value) {
        this.device.set_option(option, (double) value);
    }

    public void set(int value) {
        setOption(0, value);
    }

    public void setColorBrightness(int value) {
        setOption(1, value);
    }

    public void setColorContrast(int value) {
        setOption(2, value);
    }

    public void setColorExposure(int value) {
        setOption(3, value);
    }

    public void setColorGain(int value) {
        setOption(4, value);
    }

    public void setColorGamma(int value) {
        setOption(5, value);
    }

    public void setColorHue(int value) {
        setOption(6, value);
    }

    public void setColorSaturation(int value) {
        setOption(7, value);
    }

    public void setColorSharpness(int value) {
        setOption(8, value);
    }

    public void setColorWhiteBalance(int value) {
        setOption(9, value);
    }

    public void setColorEnableAutoExposure(int value) {
        setOption(10, value);
    }

    public void setColorEnableAutoWhiteBalance(int value) {
        setOption(11, value);
    }

    public void setLaserPower(int value) {
        setOption(12, value);
    }

    public void setAccuracy(int value) {
        setOption(13, value);
    }

    public void setMotionRange(int value) {
        setOption(14, value);
    }

    public void setFilterOption(int value) {
        setOption(15, value);
    }

    public void setConfidenceThreshold(int value) {
        setOption(16, value);
    }

    public void setDynamicFPS(int value) {
        setOption(17, value);
    }

    public void setLR_AutoExposureEnabled(int value) {
        setOption(28, value);
    }

    public void setLR_Gain(int value) {
        setOption(29, value);
    }

    public void setLR_Exposure(int value) {
        setOption(30, value);
    }

    public void setEmitterEnabled(int value) {
        setOption(31, value);
    }

    public void setDepthUnits(int value) {
        setOption(32, value);
    }

    public void setDepthClampMin(int value) {
        setOption(33, value);
    }

    public void setDepthClampMax(int value) {
        setOption(34, value);
    }

    public void setDisparityMultiplier(int value) {
        setOption(35, value);
    }

    public void setDisparityShift(int value) {
        setOption(36, value);
    }

    public void setAutoExposureMeanIntensitySetPoint(int value) {
        setOption(37, value);
    }

    public void setAutoExposureBrightRatioSetPoint(int value) {
        setOption(38, value);
    }

    public void setAutoExposureKpGain(int value) {
        setOption(39, value);
    }

    public void setAutoExposureKpExposure(int value) {
        setOption(40, value);
    }

    public void setAutoExposureKpDarkThreshold(int value) {
        setOption(41, value);
    }

    public void setAutoExposureTopEdge(int value) {
        setOption(42, value);
    }

    public void setAutoExposureBottomEdge(int value) {
        setOption(43, value);
    }

    public void setAutoExposureLeftEdge(int value) {
        setOption(44, value);
    }

    public void setAutoExposureRightEdge(int value) {
        setOption(45, value);
    }

    public void setDepthControlEstimateMedianDecrement(int value) {
        setOption(46, value);
    }

    public void setDepthControlEstimateMedianIncrement(int value) {
        setOption(47, value);
    }

    public void setDepthControlMedianThreshold(int value) {
        setOption(48, value);
    }

    public void setDepthControlMinimumThreshold(int value) {
        setOption(49, value);
    }

    public void setDepthControlScoreMaximumThreshold(int value) {
        setOption(50, value);
    }

    public void setDepthControlTextureCountThreshold(int value) {
        setOption(51, value);
    }

    public void setDepthControlTextureDifference(int value) {
        setOption(52, value);
    }

    public void setDepthControlSecondPeakThreshold(int value) {
        setOption(53, value);
    }

    public void setDepthControlNeighborThreshold(int value) {
        setOption(54, value);
    }

    public void setDepthControlLRThreshold(int value) {
        setOption(55, value);
    }

    public void setFisheyeExposure(int value) {
        setOption(56, value);
    }

    public void setFisheyeGain(int value) {
        setOption(57, value);
    }

    public void setFisheyeStobe(int value) {
        setOption(58, value);
    }

    public void setFisheyeExternalTrigger(int value) {
        setOption(59, value);
    }

    public void setFisheyeEnableAutoExposure(int value) {
        setOption(60, value);
    }

    public void setFisheyeAutoExposureMode(int value) {
        setOption(61, value);
    }

    public void setFisheyeAutoExposureAntiflickerRate(int value) {
        setOption(62, value);
    }

    public void setFisheyeAutoExposurePixelSampleRate(int value) {
        setOption(63, value);
    }

    public void setFisheyeAutoExposureSkipFrames(int value) {
        setOption(64, value);
    }

    public void setFramesQueueSize(int value) {
        setOption(65, value);
    }

    public void setHardwareLoggerEnabled(int value) {
        setOption(66, value);
    }
}
