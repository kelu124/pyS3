package org.bytedeco.javacv;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.PGRFlyCapture;
import org.bytedeco.javacpp.PGRFlyCapture.FlyCaptureContext;
import org.bytedeco.javacpp.PGRFlyCapture.FlyCaptureImage;
import org.bytedeco.javacpp.PGRFlyCapture.FlyCaptureInfoEx;
import org.bytedeco.javacpp.PGRFlyCapture.FlyCaptureTimestamp;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameGrabber.ImageMode;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

public class FlyCaptureFrameGrabber extends FrameGrabber {
    public static final int CAMERA_POWER = 1552;
    public static final int IMAGE_DATA_FORMAT = 4168;
    public static final int INITIALIZE = 0;
    public static final int IS_CAMERA_POWER = 1024;
    public static final int SOFTWARE_TRIGGER = 1580;
    public static final int SOFT_ASYNC_TRIGGER = 4140;
    public static final int TRIGGER_INQ = 1328;
    private static Exception loadingException = null;
    private FlyCaptureContext context = new FlyCaptureContext(null);
    private FlyCaptureImage conv_image = new FlyCaptureImage();
    private FrameConverter converter = new ToIplImage();
    private final float[] gammaOut = new float[1];
    private final float[] outFloat = new float[1];
    private FlyCaptureImage raw_image = new FlyCaptureImage();
    private final int[] regOut = new int[1];
    private IplImage return_image = null;
    private IplImage temp_image;

    public static String[] getDeviceDescriptions() throws Exception {
        tryLoad();
        int[] count = new int[1];
        int error = PGRFlyCapture.flycaptureBusCameraCount(count);
        if (error != 0) {
            throw new Exception("flycaptureBusCameraCount() Error " + error);
        }
        int c = count[0];
        String[] descriptions = new String[c];
        if (c > 0) {
            FlyCaptureInfoEx info = new FlyCaptureInfoEx((long) c);
            error = PGRFlyCapture.flycaptureBusEnumerateCamerasEx(info, count);
            if (error != 0) {
                throw new Exception("flycaptureBusEnumerateCamerasEx() Error " + error);
            }
            for (int i = 0; i < descriptions.length; i++) {
                info.position((long) i);
                descriptions[i] = info.pszVendorName() + " " + info.pszModelName() + " " + info.SerialNumber();
            }
        }
        return descriptions;
    }

    public static FlyCaptureFrameGrabber createDefault(File deviceFile) throws Exception {
        throw new Exception(FlyCaptureFrameGrabber.class + " does not support device files.");
    }

    public static FlyCaptureFrameGrabber createDefault(String devicePath) throws Exception {
        throw new Exception(FlyCaptureFrameGrabber.class + " does not support device paths.");
    }

    public static FlyCaptureFrameGrabber createDefault(int deviceNumber) throws Exception {
        return new FlyCaptureFrameGrabber(deviceNumber);
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            Loader.load(PGRFlyCapture.class);
        } catch (Throwable t) {
            loadingException = new Exception("Failed to load " + FlyCaptureFrameGrabber.class, t);
        }
    }

    public FlyCaptureFrameGrabber(int deviceNumber) throws Exception {
        int error = PGRFlyCapture.flycaptureCreateContext(this.context);
        if (error != 0) {
            throw new Exception("flycaptureCreateContext() Error " + error);
        }
        error = PGRFlyCapture.flycaptureInitializePlus(this.context, deviceNumber, this.numBuffers, (BytePointer) null);
        if (error != 0) {
            throw new Exception("flycaptureInitialize() Error " + error);
        }
    }

    public void release() throws Exception {
        if (this.context != null) {
            stop();
            int error = PGRFlyCapture.flycaptureDestroyContext(this.context);
            this.context = null;
            if (error != 0) {
                throw new Exception("flycaptureDestroyContext() Error " + error);
            }
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
        if (this.context == null || this.context.isNull()) {
            return super.getFrameRate();
        }
        PGRFlyCapture.flycaptureGetCameraAbsProperty(this.context, 15, this.outFloat);
        return (double) this.outFloat[0];
    }

    public void setImageMode(ImageMode imageMode) {
        if (imageMode != this.imageMode) {
            this.temp_image = null;
            this.return_image = null;
        }
        super.setImageMode(imageMode);
    }

    public void start() throws Exception {
        int f = 11;
        if (this.frameRate <= 0.0d) {
            f = 11;
        } else if (this.frameRate <= 1.876d) {
            f = 0;
        } else if (this.frameRate <= 3.76d) {
            f = 1;
        } else if (this.frameRate <= 7.51d) {
            f = 2;
        } else if (this.frameRate <= 15.01d) {
            f = 3;
        } else if (this.frameRate <= 30.01d) {
            f = 4;
        } else if (this.frameRate <= 60.01d) {
            f = 6;
        } else if (this.frameRate <= 120.01d) {
            f = 7;
        } else if (this.frameRate <= 240.01d) {
            f = 8;
        }
        int c = 16;
        if (this.imageMode == ImageMode.COLOR || this.imageMode == ImageMode.RAW) {
            if (this.imageWidth <= 0 || this.imageHeight <= 0) {
                c = 16;
            } else if (this.imageWidth <= 640 && this.imageHeight <= 480) {
                c = 4;
            } else if (this.imageWidth <= 800 && this.imageHeight <= 600) {
                c = 18;
            } else if (this.imageWidth <= 1024 && this.imageHeight <= 768) {
                c = 21;
            } else if (this.imageWidth <= 1280 && this.imageHeight <= 960) {
                c = 23;
            } else if (this.imageWidth <= 1600 && this.imageHeight <= 1200) {
                c = 51;
            }
        } else if (this.imageMode == ImageMode.GRAY) {
            if (this.imageWidth <= 0 || this.imageHeight <= 0) {
                c = 16;
            } else if (this.imageWidth <= 640 && this.imageHeight <= 480) {
                c = this.bpp > 8 ? 6 : 5;
            } else if (this.imageWidth <= 800 && this.imageHeight <= 600) {
                c = this.bpp > 8 ? 19 : 7;
            } else if (this.imageWidth <= 1024 && this.imageHeight <= 768) {
                c = this.bpp > 8 ? 9 : 8;
            } else if (this.imageWidth <= 1280 && this.imageHeight <= 960) {
                c = this.bpp > 8 ? 24 : 10;
            } else if (this.imageWidth <= 1600 && this.imageHeight <= 1200) {
                c = this.bpp > 8 ? 52 : 11;
            }
        }
        int[] iPolarity = new int[1];
        int error = PGRFlyCapture.flycaptureGetTrigger(this.context, (boolean[]) null, iPolarity, new int[1], new int[1], new int[1], null);
        if (error != 0) {
            throw new Exception("flycaptureGetTrigger() Error " + error);
        }
        error = PGRFlyCapture.flycaptureSetTrigger(this.context, this.triggerMode, iPolarity[0], 7, 14, 0);
        if (error != 0) {
            error = PGRFlyCapture.flycaptureSetTrigger(this.context, true, iPolarity[0], 7, 0, 0);
        }
        if (error != 0) {
            throw new Exception("flycaptureSetTrigger() Error " + error);
        }
        if (this.triggerMode) {
            waitForTriggerReady();
        }
        error = PGRFlyCapture.flycaptureGetCameraRegister(this.context, 4168, this.regOut);
        if (error != 0) {
            throw new Exception("flycaptureGetCameraRegister() Error " + error);
        }
        int reg;
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            reg = this.regOut[0] | 1;
        } else {
            reg = this.regOut[0] & -2;
        }
        error = PGRFlyCapture.flycaptureSetCameraRegister(this.context, 4168, reg);
        if (error != 0) {
            throw new Exception("flycaptureSetCameraRegister() Error " + error);
        }
        if (PGRFlyCapture.flycaptureSetBusSpeed(this.context, 7, 7) != 0) {
            error = PGRFlyCapture.flycaptureSetBusSpeed(this.context, 8, 8);
            if (error != 0) {
                throw new Exception("flycaptureSetBusSpeed() Error " + error);
            }
        }
        if (this.gamma != 0.0d) {
            error = PGRFlyCapture.flycaptureSetCameraAbsProperty(this.context, 6, (float) this.gamma);
            if (error != 0) {
                throw new Exception("flycaptureSetCameraAbsProperty() Error " + error + ": Could not set gamma.");
            }
        }
        if (PGRFlyCapture.flycaptureGetCameraAbsProperty(this.context, 6, this.gammaOut) != 0) {
            this.gammaOut[0] = 2.2f;
        }
        error = PGRFlyCapture.flycaptureStart(this.context, c, f);
        if (error != 0) {
            throw new Exception("flycaptureStart() Error " + error);
        }
        error = PGRFlyCapture.flycaptureSetGrabTimeoutEx(this.context, this.timeout);
        if (error != 0) {
            throw new Exception("flycaptureSetGrabTimeoutEx() Error " + error);
        }
    }

    private void waitForTriggerReady() throws Exception {
        long time = System.currentTimeMillis();
        do {
            int error = PGRFlyCapture.flycaptureGetCameraRegister(this.context, 1580, this.regOut);
            if (error != 0) {
                throw new Exception("flycaptureGetCameraRegister() Error " + error);
            } else if (System.currentTimeMillis() - time > ((long) this.timeout)) {
                return;
            }
        } while ((this.regOut[0] >>> 31) != 0);
    }

    public void stop() throws Exception {
        int error = PGRFlyCapture.flycaptureStop(this.context);
        if (error == 0 || error == 1) {
            this.temp_image = null;
            this.return_image = null;
            this.timestamp = 0;
            this.frameNumber = 0;
            return;
        }
        throw new Exception("flycaptureStop() Error " + error);
    }

    public void trigger() throws Exception {
        waitForTriggerReady();
        int error = PGRFlyCapture.flycaptureSetCameraRegister(this.context, 4140, Integer.MIN_VALUE);
        if (error != 0) {
            throw new Exception("flycaptureSetCameraRegister() Error " + error);
        }
    }

    private int getNumChannels(int pixelFormat) {
        switch (pixelFormat) {
            case 1:
            case 32:
            case 128:
            case 512:
            case 1024:
                return 1;
            case 16:
            case 64:
            case 256:
            case PGRFlyCapture.FLYCAPTURE_BGR /*268435457*/:
                return 3;
            case PGRFlyCapture.FLYCAPTURE_BGRU /*268435458*/:
                return 4;
            default:
                return -1;
        }
    }

    private int getDepth(int pixelFormat) {
        switch (pixelFormat) {
            case 32:
            case 64:
            case 1024:
                return 16;
            case 128:
            case 256:
                return opencv_core.IPL_DEPTH_16S;
            default:
                return 8;
        }
    }

    public Frame grab() throws Exception {
        int error = PGRFlyCapture.flycaptureGrabImage2(this.context, this.raw_image);
        if (error != 0) {
            throw new Exception("flycaptureGrabImage2() Error " + error + " (Has start() been called?)");
        }
        int w = this.raw_image.iCols();
        int h = this.raw_image.iRows();
        int format = this.raw_image.pixelFormat();
        int depth = getDepth(format);
        int stride = this.raw_image.iRowInc();
        int size = h * stride;
        int numChannels = getNumChannels(format);
        error = PGRFlyCapture.flycaptureGetCameraRegister(this.context, 4168, this.regOut);
        if (error != 0) {
            throw new Exception("flycaptureGetCameraRegister() Error " + error);
        }
        ByteOrder frameEndian = (this.regOut[0] & 1) != 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        boolean alreadySwapped = false;
        boolean colorbayer = this.raw_image.bStippled();
        boolean colorrgb = format == 16 || format == 64 || format == 268435457 || format == 268435458;
        boolean coloryuv = format == 2 || format == 4 || format == 8;
        BytePointer imageData = this.raw_image.pData();
        if ((depth == 8 || frameEndian.equals(ByteOrder.nativeOrder())) && (this.imageMode == ImageMode.RAW || ((this.imageMode == ImageMode.COLOR && numChannels == 3) || (this.imageMode == ImageMode.GRAY && numChannels == 1 && !colorbayer)))) {
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
                if (this.imageMode != ImageMode.COLOR || ((numChannels <= 1 && depth <= 8) || coloryuv || colorbayer)) {
                    if (this.imageMode == ImageMode.GRAY && colorbayer) {
                        this.temp_image = IplImage.create(w, h, depth, 3);
                    } else {
                        if (this.imageMode == ImageMode.GRAY && colorrgb) {
                            this.temp_image = IplImage.createHeader(w, h, depth, 3);
                        } else {
                            if (this.imageMode != ImageMode.COLOR || numChannels != 1 || coloryuv || colorbayer) {
                                this.temp_image = this.return_image;
                            } else {
                                this.temp_image = IplImage.createHeader(w, h, depth, 1);
                            }
                        }
                    }
                } else {
                    this.temp_image = IplImage.create(w, h, depth, numChannels);
                }
            }
            this.conv_image.iRowInc(this.temp_image.widthStep());
            this.conv_image.pData(this.temp_image.imageData());
            FlyCaptureImage flyCaptureImage;
            int i;
            if (depth == 8) {
                flyCaptureImage = this.conv_image;
                i = this.imageMode == ImageMode.RAW ? 512 : this.temp_image.nChannels() == 1 ? 1 : PGRFlyCapture.FLYCAPTURE_BGR;
                flyCaptureImage.pixelFormat(i);
            } else {
                flyCaptureImage = this.conv_image;
                i = this.imageMode == ImageMode.RAW ? 1024 : this.temp_image.nChannels() == 1 ? 32 : 64;
                flyCaptureImage.pixelFormat(i);
            }
            if (depth != 8 && this.conv_image.pixelFormat() == format && this.conv_image.iRowInc() == stride) {
                this.temp_image.getByteBuffer().order(ByteOrder.nativeOrder()).asShortBuffer().put(this.raw_image.getByteBuffer().order(frameEndian).asShortBuffer());
                alreadySwapped = true;
            } else {
                if ((this.imageMode == ImageMode.GRAY && colorrgb) || (this.imageMode == ImageMode.COLOR && numChannels == 1 && !coloryuv && !colorbayer)) {
                    this.temp_image.widthStep(stride);
                    this.temp_image.imageSize(size);
                    this.temp_image.imageData(imageData);
                } else if (!colorrgb && (colorbayer || coloryuv || numChannels > 1)) {
                    error = PGRFlyCapture.flycaptureConvertImage(this.context, this.raw_image, this.conv_image);
                    if (error != 0) {
                        throw new Exception("flycaptureConvertImage() Error " + error);
                    }
                }
            }
            if (!(alreadySwapped || depth == 8 || frameEndian.equals(ByteOrder.nativeOrder()))) {
                ByteBuffer bb = this.temp_image.getByteBuffer();
                bb.order(ByteOrder.nativeOrder()).asShortBuffer().put(bb.order(frameEndian).asShortBuffer());
            }
            if (this.imageMode != ImageMode.COLOR || numChannels != 1 || coloryuv || colorbayer) {
                if (this.imageMode == ImageMode.GRAY && (colorbayer || colorrgb)) {
                    opencv_imgproc.cvCvtColor(this.temp_image, this.return_image, 6);
                }
            } else {
                opencv_imgproc.cvCvtColor(this.temp_image, this.return_image, 8);
            }
        }
        if (PGRFlyCapture.flycaptureGetColorTileFormat(this.context, this.regOut) == 0) {
            switch (this.regOut[0]) {
                case 0:
                    this.sensorPattern = FrameGrabber.SENSOR_PATTERN_BGGR;
                    break;
                case 1:
                    this.sensorPattern = 4294967296L;
                    break;
                case 2:
                    this.sensorPattern = 1;
                    break;
                case 3:
                    this.sensorPattern = 0;
                    break;
                default:
                    this.sensorPattern = -1;
                    break;
            }
        }
        this.sensorPattern = -1;
        FlyCaptureTimestamp timeStamp = this.raw_image.timeStamp();
        this.timestamp = (((long) timeStamp.ulSeconds()) * 1000000) + ((long) timeStamp.ulMicroSeconds());
        return this.converter.convert((Object) this.return_image);
    }
}
