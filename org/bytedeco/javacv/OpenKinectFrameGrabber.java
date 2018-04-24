package org.bytedeco.javacv;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.freenect;
import org.bytedeco.javacpp.freenect.freenect_context;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameGrabber.ImageMode;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

public class OpenKinectFrameGrabber extends FrameGrabber {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static Exception loadingException = null;
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    private FrameConverter converter = new ToIplImage();
    private boolean depth = false;
    private int depthFormat = -1;
    private int deviceNumber = 0;
    private IplImage rawDepthImage = null;
    private BytePointer rawDepthImageData = new BytePointer(null);
    private IplImage rawIRImage = null;
    private BytePointer rawIRImageData = new BytePointer(null);
    private IplImage rawVideoImage = null;
    private BytePointer rawVideoImageData = new BytePointer(null);
    private IplImage returnImage = null;
    private int[] timestamp = new int[]{0};
    private int videoFormat = -1;

    static {
        boolean z;
        if (OpenKinectFrameGrabber.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public static String[] getDeviceDescriptions() throws Exception {
        tryLoad();
        freenect_context ctx = new freenect_context(null);
        int err = freenect.freenect_init(ctx, null);
        if (err < 0) {
            throw new Exception("freenect_init() Error " + err + ": Failed to init context.");
        }
        int count = freenect.freenect_num_devices(ctx);
        if (count < 0) {
            throw new Exception("freenect_num_devices() Error " + err + ": Failed to get number of devices.");
        }
        String[] descriptions = new String[count];
        for (int i = 0; i < descriptions.length; i++) {
            descriptions[i] = "Kinect #" + i;
        }
        err = freenect.freenect_shutdown(ctx);
        if (err >= 0) {
            return descriptions;
        }
        throw new Exception("freenect_shutdown() Error " + err + ": Failed to shutdown context.");
    }

    public static OpenKinectFrameGrabber createDefault(File deviceFile) throws Exception {
        throw new Exception(OpenKinectFrameGrabber.class + " does not support device files.");
    }

    public static OpenKinectFrameGrabber createDefault(String devicePath) throws Exception {
        throw new Exception(OpenKinectFrameGrabber.class + " does not support device paths.");
    }

    public static OpenKinectFrameGrabber createDefault(int deviceNumber) throws Exception {
        return new OpenKinectFrameGrabber(deviceNumber);
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            Loader.load(freenect.class);
        } catch (Throwable t) {
            loadingException = new Exception("Failed to load " + OpenKinectFrameGrabber.class, t);
        }
    }

    public OpenKinectFrameGrabber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public void release() throws Exception {
        stop();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public void setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    public int getDepthFormat() {
        return this.depthFormat;
    }

    public void setDepthFormat(int depthFormat) {
        this.depthFormat = depthFormat;
    }

    public int getVideoFormat() {
        return this.videoFormat;
    }

    public void setVideoFormat(int videoFormat) {
        this.videoFormat = videoFormat;
    }

    public double getGamma() {
        if (this.gamma == 0.0d) {
            return 2.2d;
        }
        return this.gamma;
    }

    public void setImageMode(ImageMode imageMode) {
        if (imageMode != this.imageMode) {
            this.returnImage = null;
        }
        super.setImageMode(imageMode);
    }

    public void start() throws Exception {
        this.depth = "depth".equalsIgnoreCase(this.format);
    }

    public void stop() throws Exception {
        freenect.freenect_sync_stop();
    }

    public void trigger() throws Exception {
        for (int i = 0; i < this.numBuffers + 1; i++) {
            int err;
            if (this.depth) {
                err = freenect.freenect_sync_get_depth(this.rawDepthImageData, this.timestamp, this.deviceNumber, this.depthFormat < 0 ? this.bpp : this.depthFormat);
                if (err != 0) {
                    throw new Exception("freenect_sync_get_depth() Error " + err + ": Failed to get depth synchronously.");
                }
            } else {
                err = freenect.freenect_sync_get_video(this.rawVideoImageData, this.timestamp, this.deviceNumber, this.videoFormat < 0 ? this.bpp : this.videoFormat);
                if (err != 0) {
                    throw new Exception("freenect_sync_get_video() Error " + err + ": Failed to get video synchronously.");
                }
            }
        }
    }

    public IplImage grabDepth() throws Exception {
        int fmt = this.depthFormat < 0 ? this.bpp : this.depthFormat;
        int iplDepth = 16;
        int channels = 1;
        switch (fmt) {
            case 0:
            case 1:
            case 4:
            case 5:
                iplDepth = 16;
                channels = 1;
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        int err = freenect.freenect_sync_get_depth(this.rawDepthImageData, this.timestamp, this.deviceNumber, fmt);
        if (err != 0) {
            throw new Exception("freenect_sync_get_depth() Error " + err + ": Failed to get depth synchronously.");
        }
        if (!(this.rawDepthImage != null && this.rawDepthImage.width() == 640 && this.rawDepthImage.height() == 480)) {
            this.rawDepthImage = IplImage.createHeader(640, 480, iplDepth, channels);
        }
        opencv_core.cvSetData(this.rawDepthImage, this.rawDepthImageData, 1280);
        if (iplDepth > 8 && !ByteOrder.nativeOrder().equals(this.byteOrder)) {
            ByteBuffer bb = this.rawDepthImage.getByteBuffer();
            bb.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer());
        }
        this.timestamp = (long) this.timestamp[0];
        return this.rawDepthImage;
    }

    public IplImage grabVideo() throws Exception {
        int fmt = this.videoFormat < 0 ? this.bpp : this.videoFormat;
        int iplDepth = 8;
        int channels = 3;
        switch (fmt) {
            case 0:
                iplDepth = 8;
                channels = 3;
                break;
            case 1:
            case 2:
                iplDepth = 8;
                channels = 1;
                break;
            case 3:
                iplDepth = 16;
                channels = 1;
                break;
            case 5:
                iplDepth = 8;
                channels = 3;
                break;
            case 6:
                iplDepth = 8;
                channels = 2;
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        int err = freenect.freenect_sync_get_video(this.rawVideoImageData, this.timestamp, this.deviceNumber, fmt);
        if (err != 0) {
            throw new Exception("freenect_sync_get_video() Error " + err + ": Failed to get video synchronously.");
        }
        if (!(this.rawVideoImage != null && this.rawVideoImage.width() == 640 && this.rawVideoImage.height() == 480)) {
            this.rawVideoImage = IplImage.createHeader(640, 480, iplDepth, channels);
        }
        opencv_core.cvSetData(this.rawVideoImage, this.rawVideoImageData, ((640 * channels) * iplDepth) / 8);
        if (iplDepth > 8 && !ByteOrder.nativeOrder().equals(this.byteOrder)) {
            ByteBuffer bb = this.rawVideoImage.getByteBuffer();
            bb.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer());
        }
        if (channels == 3) {
            opencv_imgproc.cvCvtColor(this.rawVideoImage, this.rawVideoImage, 4);
        }
        this.timestamp = (long) this.timestamp[0];
        return this.rawVideoImage;
    }

    public IplImage grabIR() throws Exception {
        int err = freenect.freenect_sync_get_video(this.rawIRImageData, this.timestamp, this.deviceNumber, 2);
        if (err != 0) {
            throw new Exception("freenect_sync_get_video() Error " + err + ": Failed to get video synchronously.");
        }
        if (!(this.rawIRImage != null && this.rawIRImage.width() == 640 && this.rawIRImage.height() == 480)) {
            this.rawIRImage = IplImage.createHeader(640, 480, 8, 1);
        }
        opencv_core.cvSetData(this.rawIRImage, this.rawIRImageData, 640);
        this.timestamp = (long) this.timestamp[0];
        return this.rawIRImage;
    }

    public Frame grab() throws Exception {
        Object image = this.depth ? grabDepth() : grabVideo();
        int w = image.width();
        int h = image.height();
        int iplDepth = image.depth();
        int channels = image.nChannels();
        if (this.imageMode == ImageMode.COLOR && channels == 1) {
            if (this.returnImage == null) {
                this.returnImage = IplImage.create(w, h, iplDepth, 3);
            }
            opencv_imgproc.cvCvtColor(image, this.returnImage, 8);
            return this.converter.convert(this.returnImage);
        } else if (this.imageMode != ImageMode.GRAY || channels != 3) {
            return this.converter.convert(image);
        } else {
            if (this.returnImage == null) {
                this.returnImage = IplImage.create(w, h, iplDepth, 1);
            }
            opencv_imgproc.cvCvtColor(image, this.returnImage, 6);
            return this.converter.convert(this.returnImage);
        }
    }
}
