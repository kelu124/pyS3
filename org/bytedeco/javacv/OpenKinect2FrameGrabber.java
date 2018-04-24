package org.bytedeco.javacv;

import java.io.File;
import java.nio.ByteOrder;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.freenect2;
import org.bytedeco.javacpp.freenect2.CpuPacketPipeline;
import org.bytedeco.javacpp.freenect2.Frame;
import org.bytedeco.javacpp.freenect2.FrameMap;
import org.bytedeco.javacpp.freenect2.Freenect2;
import org.bytedeco.javacpp.freenect2.Freenect2Device;
import org.bytedeco.javacpp.freenect2.PacketPipeline;
import org.bytedeco.javacpp.freenect2.SyncMultiFrameListener;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.FrameGrabber.Exception;

public class OpenKinect2FrameGrabber extends FrameGrabber {
    public static int DEFAULT_COLOR_HEIGHT = 480;
    public static int DEFAULT_COLOR_WIDTH = 640;
    public static int DEFAULT_DEPTH_HEIGHT = 480;
    public static int DEFAULT_DEPTH_WIDTH = 640;
    private static Freenect2 freenect2Context = null;
    private static Exception loadingException = null;
    private boolean IREnabled = false;
    private int IRFrameRate = 60;
    private int IRImageHeight = DEFAULT_DEPTH_HEIGHT;
    private int IRImageWidth = DEFAULT_DEPTH_WIDTH;
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    private boolean colorEnabled = false;
    private boolean depthEnabled = false;
    private int depthFrameRate = 60;
    private int depthImageHeight = DEFAULT_DEPTH_HEIGHT;
    private int depthImageWidth = DEFAULT_DEPTH_WIDTH;
    private Freenect2Device device = null;
    private int deviceNumber = 0;
    private SyncMultiFrameListener frameListener;
    private int frameTypes = 0;
    private FrameMap frames = new FrameMap();
    private boolean hasFirstGoodColorImage = false;
    private IplImage rawDepthImage = null;
    private IplImage rawIRImage = null;
    private IplImage rawVideoImage = null;
    private String serial = null;
    private BytePointer videoBuffer = null;
    private IplImage videoImageRGBA = null;

    public static String[] getDeviceDescriptions() throws Exception {
        tryLoad();
        String[] desc = new String[freenect2Context.enumerateDevices()];
        for (int i = 0; i < desc.length; i++) {
            desc[i] = freenect2Context.getDeviceSerialNumber(i).getString();
        }
        return desc;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public void setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    public static OpenKinect2FrameGrabber createDefault(int deviceNumber) throws Exception {
        return new OpenKinect2FrameGrabber(deviceNumber);
    }

    public static OpenKinect2FrameGrabber createDefault(File deviceFile) throws Exception {
        throw new Exception(OpenKinect2FrameGrabber.class + " does not support File devices.");
    }

    public static OpenKinect2FrameGrabber createDefault(String devicePath) throws Exception {
        throw new Exception(OpenKinect2FrameGrabber.class + " does not support path.");
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            loadingException.printStackTrace();
            throw loadingException;
        }
        try {
            if (freenect2Context == null) {
                Loader.load(freenect2.class);
                freenect2Context = new Freenect2();
            }
        } catch (Throwable t) {
            loadingException = new Exception("Failed to load " + OpenKinect2FrameGrabber.class, t);
        }
    }

    public OpenKinect2FrameGrabber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public void enableColorStream() {
        if (!this.colorEnabled) {
            this.frameTypes |= 1;
            this.colorEnabled = true;
        }
    }

    public void enableDepthStream() {
        if (!this.depthEnabled) {
            this.frameTypes |= 4;
            this.depthEnabled = true;
        }
    }

    public void enableIRStream() {
        if (!this.IREnabled) {
            this.frameTypes |= 2;
            this.IREnabled = true;
        }
    }

    public void release() throws Exception {
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public void start() throws Exception {
        if (freenect2Context == null) {
            try {
                tryLoad();
            } catch (Exception e) {
                System.out.println("Exception in the TryLoad !" + e);
                e.printStackTrace();
            }
        }
        if (freenect2Context == null) {
            throw new Exception("FATAL error: OpenKinect2 camera: driver could not load.");
        } else if (freenect2Context.enumerateDevices() == 0) {
            throw new Exception("FATAL error: OpenKinect2: no device connected!");
        } else {
            this.device = null;
            PacketPipeline pipeline = new CpuPacketPipeline();
            this.serial = freenect2Context.getDeviceSerialNumber(this.deviceNumber).getString();
            this.device = freenect2Context.openDevice(this.serial, pipeline);
            this.frameListener = new SyncMultiFrameListener(this.frameTypes);
            if (this.colorEnabled) {
                this.device.setColorFrameListener(this.frameListener);
            }
            if (this.depthEnabled || this.IREnabled) {
                this.device.setIrAndDepthFrameListener(this.frameListener);
            }
            this.rawVideoImage = IplImage.createHeader(1920, 1080, 8, 4);
            this.device.start();
            System.out.println("OpenKinect2 device started.");
            System.out.println("Serial: " + this.device.getSerialNumber().getString());
            System.out.println("Firmware: " + this.device.getFirmwareVersion().getString());
        }
    }

    public void stop() throws Exception {
        this.device.stop();
        this.frameNumber = 0;
    }

    protected void grabVideo() {
        Frame rgb = this.frames.get(1);
        int channels = (int) rgb.bytes_per_pixel();
        int deviceWidth = (int) rgb.width();
        int deviceHeight = (int) rgb.height();
        BytePointer rawVideoImageData = rgb.data();
        if (this.rawVideoImage == null) {
            this.rawVideoImage = IplImage.createHeader(deviceWidth, deviceHeight, 8, channels);
        }
        opencv_core.cvSetData(this.rawVideoImage, rawVideoImageData, ((deviceWidth * channels) * 8) / 8);
        if (this.videoImageRGBA == null) {
            this.videoImageRGBA = this.rawVideoImage.clone();
        }
        opencv_imgproc.cvCvtColor(this.rawVideoImage, this.videoImageRGBA, 5);
    }

    protected void grabIR() {
        Frame IRImage = this.frames.get(2);
        int bpp = (int) IRImage.bytes_per_pixel();
        int deviceWidth = (int) IRImage.width();
        int deviceHeight = (int) IRImage.height();
        Pointer rawIRData = IRImage.data();
        if (this.rawIRImage == null) {
            this.rawIRImage = IplImage.createHeader(deviceWidth, deviceHeight, 32, 1);
        }
        opencv_core.cvSetData(this.rawIRImage, rawIRData, ((deviceWidth * 1) * 32) / 8);
    }

    protected void grabDepth() {
        Frame depthImage = this.frames.get(4);
        int bpp = (int) depthImage.bytes_per_pixel();
        int deviceWidth = (int) depthImage.width();
        int deviceHeight = (int) depthImage.height();
        Pointer rawDepthData = depthImage.data();
        if (this.rawDepthImage == null) {
            this.rawDepthImage = IplImage.createHeader(deviceWidth, deviceHeight, 32, 1);
        }
        opencv_core.cvSetData(this.rawDepthImage, rawDepthData, ((deviceWidth * 1) * 32) / 8);
    }

    public Frame grab() throws Exception {
        if (!this.frameListener.waitForNewFrame(this.frames, 10000)) {
            System.out.println("Openkinect2: timeout!");
        }
        this.frameNumber++;
        if (this.colorEnabled) {
            grabVideo();
        }
        if (this.IREnabled) {
            grabIR();
        }
        if (this.depthEnabled) {
            grabDepth();
        }
        this.frameListener.release(this.frames);
        return null;
    }

    public IplImage getVideoImage() {
        return this.videoImageRGBA;
    }

    public IplImage getIRImage() {
        return this.rawIRImage;
    }

    public IplImage getDepthImage() {
        return this.rawDepthImage;
    }

    public void trigger() throws Exception {
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
}
