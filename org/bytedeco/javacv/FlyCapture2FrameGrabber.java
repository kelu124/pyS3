package org.bytedeco.javacv;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.FlyCapture2;
import org.bytedeco.javacpp.FlyCapture2.BusManager;
import org.bytedeco.javacpp.FlyCapture2.Camera;
import org.bytedeco.javacpp.FlyCapture2.CameraInfo;
import org.bytedeco.javacpp.FlyCapture2.Error;
import org.bytedeco.javacpp.FlyCapture2.Image;
import org.bytedeco.javacpp.FlyCapture2.PGRGuid;
import org.bytedeco.javacpp.FlyCapture2.TimeStamp;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameGrabber.ImageMode;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

public class FlyCapture2FrameGrabber extends FrameGrabber {
    public static final int CAMERA_POWER = 1552;
    public static final int IMAGE_DATA_FORMAT = 4168;
    public static final int INITIALIZE = 0;
    public static final int IS_CAMERA_POWER = 1024;
    public static final int SOFTWARE_TRIGGER = 1580;
    public static final int SOFT_ASYNC_TRIGGER = 4140;
    public static final int TRIGGER_INQ = 1328;
    static final int VIDEOMODE_ANY = -1;
    private static Exception loadingException = null;
    private BusManager busMgr = new BusManager();
    private Camera camera;
    private CameraInfo cameraInfo;
    private Image conv_image = new Image();
    private FrameConverter converter = new ToIplImage();
    private final float[] gammaOut = new float[1];
    private final float[] outFloat = new float[1];
    private Image raw_image = new Image();
    private final int[] regOut = new int[1];
    private IplImage return_image = null;
    private IplImage temp_image;

    public static String[] getDeviceDescriptions() throws Exception {
        tryLoad();
        BusManager busMgr = new BusManager();
        int[] numCameras = new int[1];
        busMgr.GetNumOfCameras(numCameras);
        String[] descriptions = new String[numCameras[0]];
        for (int i = 0; i < numCameras[0]; i++) {
            PGRGuid guid = new PGRGuid();
            Error error = busMgr.GetCameraFromIndex(i, guid);
            if (error.notEquals(0)) {
                PrintError(error);
                System.exit(-1);
            }
            Camera cam = new Camera();
            error = cam.Connect(guid);
            if (error.notEquals(0)) {
                PrintError(error);
            }
            CameraInfo camInfo = new CameraInfo();
            error = cam.GetCameraInfo(camInfo);
            if (error.notEquals(0)) {
                PrintError(error);
            }
            descriptions[i] = CameraInfo(camInfo);
        }
        return descriptions;
    }

    static void PrintError(Error error) {
        error.PrintErrorTrace();
    }

    static String CameraInfo(CameraInfo pCamInfo) {
        return "\n*** CAMERA INFORMATION ***\nSerial number - " + pCamInfo.serialNumber() + "\nCamera model - " + pCamInfo.modelName().getString() + "\nCamera vendor - " + pCamInfo.vendorName().getString() + "\nSensor - " + pCamInfo.sensorInfo().getString() + "\nResolution - " + pCamInfo.sensorResolution().getString() + "\nFirmware version - " + pCamInfo.firmwareVersion().getString() + "\nFirmware build time - " + pCamInfo.firmwareBuildTime().getString() + "\n";
    }

    public static FlyCaptureFrameGrabber createDefault(File deviceFile) throws Exception {
        return null;
    }

    public static FlyCaptureFrameGrabber createDefault(String devicePath) throws Exception {
        return null;
    }

    public static FlyCaptureFrameGrabber createDefault(int deviceNumber) throws Exception {
        return new FlyCaptureFrameGrabber(deviceNumber);
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            loadingException.printStackTrace();
            throw loadingException;
        }
        try {
            Loader.load(FlyCapture2.class);
        } catch (Throwable t) {
            loadingException = new Exception("Failed to load " + FlyCapture2FrameGrabber.class, t);
        }
    }

    public FlyCapture2FrameGrabber(int deviceNumber) throws Exception {
        this.busMgr.GetNumOfCameras(new int[1]);
        PGRGuid guid = new PGRGuid();
        Error error = this.busMgr.GetCameraFromIndex(deviceNumber, guid);
        if (error.notEquals(0)) {
            PrintError(error);
            System.exit(-1);
        }
        this.camera = new Camera();
        error = this.camera.Connect(guid);
        if (error.notEquals(0)) {
            PrintError(error);
        }
        this.cameraInfo = new CameraInfo();
        error = this.camera.GetCameraInfo(this.cameraInfo);
        if (error.notEquals(0)) {
            PrintError(error);
        }
    }

    public void release() throws Exception {
        if (this.camera != null) {
            stop();
            this.camera.Disconnect();
            this.camera = null;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public double getGamma() {
        return (Float.isNaN(this.gammaOut[0]) || Float.isInfinite(this.gammaOut[0]) || this.gammaOut[0] == 0.0f) ? 2.2d : (double) this.gammaOut[0];
    }

    public int getImageWidth() {
        return this.return_image == null ? super.getImageWidth() : this.return_image.width();
    }

    public int getImageHeight() {
        return this.return_image == null ? super.getImageHeight() : this.return_image.height();
    }

    public double getFrameRate() {
        return super.getFrameRate();
    }

    public void setImageMode(ImageMode imageMode) {
        if (imageMode != this.imageMode) {
            this.temp_image = null;
            this.return_image = null;
        }
        super.setImageMode(imageMode);
    }

    public void start() throws Exception {
        if (this.frameRate > 0.0d) {
            if (this.frameRate > 1.876d) {
                if (this.frameRate > 3.76d) {
                    if (this.frameRate > 7.51d) {
                        if (this.frameRate > 15.01d) {
                            if (this.frameRate > 30.01d) {
                                if (this.frameRate > 60.01d) {
                                    if (this.frameRate > 120.01d) {
                                        if (this.frameRate <= 240.01d) {
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.imageMode == ImageMode.COLOR || this.imageMode == ImageMode.RAW) {
            if (this.imageWidth > 0 && this.imageHeight > 0) {
                if (this.imageWidth > 640 || this.imageHeight > 480) {
                    if (this.imageWidth > 800 || this.imageHeight > 600) {
                        if (this.imageWidth > 1024 || this.imageHeight > 768) {
                            if (this.imageWidth > 1280 || this.imageHeight > 960) {
                                if (this.imageWidth <= 1600 && this.imageHeight <= 1200) {
                                }
                            }
                        }
                    }
                }
            }
        } else if (this.imageMode == ImageMode.GRAY) {
            if (this.imageWidth > 0 && this.imageHeight > 0) {
                if (this.imageWidth > 640 || this.imageHeight > 480) {
                    if (this.imageWidth > 800 || this.imageHeight > 600) {
                        if (this.imageWidth > 1024 || this.imageHeight > 768) {
                            if (this.imageWidth > 1280 || this.imageHeight > 960) {
                                if (this.imageWidth <= 1600 && this.imageHeight <= 1200) {
                                    if (this.bpp > 8) {
                                    }
                                }
                            } else if (this.bpp > 8) {
                            }
                        } else if (this.bpp > 8) {
                        }
                    } else if (this.bpp > 8) {
                    }
                } else if (this.bpp > 8) {
                }
            }
        }
        Error error = this.camera.StartCapture();
        if (error.notEquals(0)) {
            PrintError(error);
        }
    }

    public void stop() throws Exception {
        Error error = this.camera.StopCapture();
        if (error.notEquals(0)) {
            throw new Exception("flycapture camera StopCapture() Error " + error);
        }
        this.temp_image = null;
        this.return_image = null;
        this.timestamp = 0;
        this.frameNumber = 0;
    }

    public void trigger() throws Exception {
        Error error = this.camera.FireSoftwareTrigger();
        if (error.notEquals(0)) {
            throw new Exception("flycaptureSetCameraRegister() Error " + error);
        }
    }

    private int getNumChannels(int pixelFormat) {
        switch (pixelFormat) {
            case Integer.MIN_VALUE:
            case 2097152:
            case 4194304:
            case 16777216:
            case 67108864:
                return 1;
            case -2147483640:
            case 8388608:
            case 33554432:
            case 134217728:
                return 3;
            case 1073741832:
                return 4;
            default:
                return -1;
        }
    }

    private int getDepth(int pixelFormat) {
        switch (pixelFormat) {
            case 2097152:
            case 33554432:
            case 67108864:
                return 16;
            case 8388608:
            case 16777216:
                return opencv_core.IPL_DEPTH_16S;
            default:
                return 8;
        }
    }

    private void setPixelFormat(Image image, int pixelFormat) {
        image.SetDimensions(image.GetRows(), image.GetCols(), image.GetStride(), pixelFormat, image.GetBayerTileFormat());
    }

    private void setStride(Image image, int stride) {
        image.SetDimensions(image.GetRows(), image.GetCols(), stride, image.GetPixelFormat(), image.GetBayerTileFormat());
    }

    public Frame grab() throws Exception {
        Error error = this.camera.RetrieveBuffer(this.raw_image);
        if (error.notEquals(0)) {
            throw new Exception("flycaptureGrabImage2() Error " + error + " (Has start() been called?)");
        }
        int w = this.raw_image.GetCols();
        int h = this.raw_image.GetRows();
        int format = this.raw_image.GetPixelFormat();
        int depth = getDepth(format);
        int stride = this.raw_image.GetStride();
        int size = h * stride;
        int numChannels = getNumChannels(format);
        error = this.camera.ReadRegister(4168, this.regOut);
        if (error.notEquals(0)) {
            throw new Exception("flycaptureGetCameraRegister() Error " + error);
        }
        ByteOrder frameEndian = (this.regOut[0] & 1) != 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        boolean alreadySwapped = false;
        boolean colorrgb = format == 134217728 || format == 33554432 || format == -2147483640 || format == 1073741832;
        boolean coloryuv = format == 1073741824 || format == 536870912 || format == 268435456;
        BytePointer imageData = this.raw_image.GetData();
        if ((depth == 8 || frameEndian.equals(ByteOrder.nativeOrder())) && (this.imageMode == ImageMode.RAW || ((this.imageMode == ImageMode.COLOR && numChannels == 3) || (this.imageMode == ImageMode.GRAY && numChannels == 1 && null == null)))) {
            if (this.return_image == null) {
                this.return_image = IplImage.createHeader(w, h, depth, numChannels);
            }
            this.return_image.widthStep(stride);
            this.return_image.imageSize(size);
            this.return_image.imageData(imageData);
        } else {
            if (this.return_image == null) {
                this.return_image = IplImage.create(w, h, depth, this.imageMode == ImageMode.COLOR ? 3 : 1);
            }
            if (this.temp_image == null) {
                if (this.imageMode != ImageMode.COLOR || ((numChannels <= 1 && depth <= 8) || coloryuv || null != null)) {
                    if (this.imageMode != ImageMode.GRAY || null == null) {
                        if (this.imageMode == ImageMode.GRAY && colorrgb) {
                            this.temp_image = IplImage.createHeader(w, h, depth, 3);
                        } else {
                            if (this.imageMode == ImageMode.COLOR && numChannels == 1 && !coloryuv && null == null) {
                                this.temp_image = IplImage.createHeader(w, h, depth, 1);
                            } else {
                                this.temp_image = this.return_image;
                            }
                        }
                    } else {
                        this.temp_image = IplImage.create(w, h, depth, 3);
                    }
                } else {
                    this.temp_image = IplImage.create(w, h, depth, numChannels);
                }
            }
            setStride(this.conv_image, this.temp_image.widthStep());
            this.conv_image.SetData(this.temp_image.imageData(), (this.temp_image.width() * this.temp_image.height()) * this.temp_image.depth());
            Image image;
            int i;
            if (depth == 8) {
                image = this.conv_image;
                i = this.imageMode == ImageMode.RAW ? 4194304 : this.temp_image.nChannels() == 1 ? Integer.MIN_VALUE : -2147483640;
                setPixelFormat(image, i);
            } else {
                image = this.conv_image;
                i = this.imageMode == ImageMode.RAW ? 2097152 : this.temp_image.nChannels() == 1 ? 67108864 : 33554432;
                setPixelFormat(image, i);
            }
            if (depth != 8 && this.conv_image.GetPixelFormat() == format && this.conv_image.GetStride() == stride) {
                this.temp_image.getByteBuffer().order(ByteOrder.nativeOrder()).asShortBuffer().put(this.raw_image.GetData().asByteBuffer().order(frameEndian).asShortBuffer());
                alreadySwapped = true;
            } else {
                if ((this.imageMode == ImageMode.GRAY && colorrgb) || (this.imageMode == ImageMode.COLOR && numChannels == 1 && !coloryuv && null == null)) {
                    this.temp_image.widthStep(stride);
                    this.temp_image.imageSize(size);
                    this.temp_image.imageData(imageData);
                } else if (!colorrgb && (null != null || coloryuv || numChannels > 1)) {
                    error = this.raw_image.Convert(this.conv_image);
                    if (error.notEquals(0)) {
                        throw new Exception("flycaptureConvertImage() Error " + error);
                    }
                }
            }
            if (!(alreadySwapped || depth == 8 || frameEndian.equals(ByteOrder.nativeOrder()))) {
                ByteBuffer bb = this.temp_image.getByteBuffer();
                bb.order(ByteOrder.nativeOrder()).asShortBuffer().put(bb.order(frameEndian).asShortBuffer());
            }
            if (this.imageMode == ImageMode.COLOR && numChannels == 1 && !coloryuv && null == null) {
                opencv_imgproc.cvCvtColor(this.temp_image, this.return_image, 8);
            } else {
                if (this.imageMode == ImageMode.GRAY && (null != null || colorrgb)) {
                    opencv_imgproc.cvCvtColor(this.temp_image, this.return_image, 6);
                }
            }
        }
        switch (this.cameraInfo.bayerTileFormat()) {
            case 1:
                this.sensorPattern = 0;
                break;
            case 2:
                this.sensorPattern = 1;
                break;
            case 3:
                this.sensorPattern = 4294967296L;
                break;
            case 4:
                this.sensorPattern = FrameGrabber.SENSOR_PATTERN_BGGR;
                break;
            default:
                this.sensorPattern = -1;
                break;
        }
        TimeStamp timeStamp = this.raw_image.GetTimeStamp();
        this.timestamp = (timeStamp.seconds() * 1000000) + ((long) timeStamp.microSeconds());
        return this.converter.convert((Object) this.return_image);
    }
}
