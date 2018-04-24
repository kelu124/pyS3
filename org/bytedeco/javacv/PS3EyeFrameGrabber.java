package org.bytedeco.javacv;

import cl.eye.CLCamera;
import java.io.File;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameGrabber.ImageMode;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

public class PS3EyeFrameGrabber extends FrameGrabber {
    private static Exception loadingException = null;
    CLCamera camera;
    int cameraIndex;
    FrameConverter converter;
    IplImage image_1ch;
    IplImage image_4ch;
    byte[] ipl_frame;
    int[] ps3_frame;
    String stat;
    protected Triggered triggered;
    String uuid;

    protected enum Triggered {
        NO_TRIGGER,
        HAS_FRAME,
        NO_FRAME
    }

    public static String[] getDeviceDescriptions() throws Exception {
        tryLoad();
        String[] descriptions = new String[CLCamera.cameraCount()];
        for (int i = 0; i < descriptions.length; i++) {
            descriptions[i] = CLCamera.cameraUUID(i);
        }
        return descriptions;
    }

    public static PS3EyeFrameGrabber createDefault(File deviceFile) throws Exception {
        throw new Exception(PS3EyeFrameGrabber.class + " does not support device files.");
    }

    public static PS3EyeFrameGrabber createDefault(String devicePath) throws Exception {
        throw new Exception(PS3EyeFrameGrabber.class + " does not support device paths.");
    }

    public static PS3EyeFrameGrabber createDefault(int deviceNumber) throws Exception {
        return new PS3EyeFrameGrabber(deviceNumber);
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            CLCamera.IsLibraryLoaded();
        } catch (Throwable t) {
            loadingException = new Exception("Failed to load " + PS3EyeFrameGrabber.class, t);
        }
    }

    public PS3EyeFrameGrabber() throws Exception {
        this(0);
    }

    public PS3EyeFrameGrabber(int cameraIndex) throws Exception {
        this(cameraIndex, 640, 480, 60);
    }

    public PS3EyeFrameGrabber(int cameraIndex, int imageWidth, int imageHeight, int framerate) throws Exception {
        this(cameraIndex, 640, 480, 60, null);
    }

    public PS3EyeFrameGrabber(int cameraIndex, int imageWidth, int imageHeight, int framerate, Object applet) throws Exception {
        this.cameraIndex = 0;
        this.ps3_frame = null;
        this.ipl_frame = null;
        this.image_4ch = null;
        this.image_1ch = null;
        this.converter = new ToIplImage();
        this.triggered = Triggered.NO_TRIGGER;
        this.camera = null;
        if (CLCamera.IsLibraryLoaded()) {
            try {
                this.camera = (CLCamera) CLCamera.class.newInstance();
            } catch (Throwable t) {
                Exception exception = new Exception("Failed to construct " + PS3EyeFrameGrabber.class, t);
            }
            this.cameraIndex = cameraIndex;
            this.stat = "created";
            this.uuid = CLCamera.cameraUUID(cameraIndex);
            if ((imageWidth == 640 && imageHeight == 480) || (imageWidth == 320 && imageHeight == 240)) {
                setImageWidth(imageWidth);
                setImageHeight(imageHeight);
                setImageMode(ImageMode.COLOR);
                setFrameRate((double) framerate);
                setTimeout((1000 / framerate) + 1);
                setBitsPerPixel(8);
                setTriggerMode(false);
                setNumBuffers(4);
                return;
            }
            throw new Exception("Only 640x480 or 320x240 images supported");
        }
        throw new Exception("CLEye multicam dll not loaded");
    }

    public static int getCameraCount() {
        return CLCamera.cameraCount();
    }

    public static String[] listPS3Cameras() {
        int no = getCameraCount();
        if (no <= 0) {
            return null;
        }
        String[] strArr = new String[no];
        for (no--; no >= 0; no--) {
            strArr[no] = CLCamera.cameraUUID(no);
        }
        return strArr;
    }

    public IplImage makeImage(int[] frame) {
        this.image_4ch.getIntBuffer().put(this.ps3_frame);
        return this.image_4ch;
    }

    public int[] grab_raw() {
        if (this.camera.getCameraFrame(this.ps3_frame, this.timeout)) {
            return this.ps3_frame;
        }
        return null;
    }

    public void trigger() throws Exception {
        for (int i = 0; i < this.numBuffers + 1; i++) {
            grab_raw();
        }
        int[] grab_raw = grab_raw();
        this.ps3_frame = grab_raw;
        if (grab_raw != null) {
            this.triggered = Triggered.HAS_FRAME;
            this.timestamp = System.nanoTime() / 1000;
            return;
        }
        this.triggered = Triggered.NO_FRAME;
    }

    public IplImage grab_RGB4() {
        if (!this.camera.getCameraFrame(this.ps3_frame, this.timeout)) {
            return null;
        }
        this.timestamp = System.nanoTime() / 1000;
        this.image_4ch.getIntBuffer().put(this.ps3_frame);
        return this.image_4ch;
    }

    public Frame grab() throws Exception {
        Object img;
        switch (this.triggered) {
            case NO_TRIGGER:
                img = grab_RGB4();
                break;
            case HAS_FRAME:
                this.triggered = Triggered.NO_TRIGGER;
                img = makeImage(this.ps3_frame);
                break;
            case NO_FRAME:
                this.triggered = Triggered.NO_TRIGGER;
                return null;
            default:
                throw new Exception("Int. error - unknown triggering state");
        }
        if (img != null && this.imageMode == ImageMode.GRAY) {
            opencv_imgproc.cvCvtColor(img, this.image_1ch, 7);
            img = this.image_1ch;
        }
        return this.converter.convert(img);
    }

    public void start() throws Exception {
        if (this.ps3_frame == null) {
            this.ps3_frame = new int[(this.imageWidth * this.imageHeight)];
            this.image_4ch = IplImage.create(this.imageWidth, this.imageHeight, 8, 4);
            this.image_1ch = IplImage.create(this.imageWidth, this.imageHeight, 8, 1);
        }
        CLCamera cLCamera = this.camera;
        int i = this.cameraIndex;
        int i2 = this.imageMode == ImageMode.GRAY ? CLCamera.CLEYE_MONO_PROCESSED : CLCamera.CLEYE_COLOR_PROCESSED;
        int i3 = (this.imageWidth == 320 && this.imageHeight == 240) ? CLCamera.CLEYE_QVGA : CLCamera.CLEYE_VGA;
        if (!cLCamera.createCamera(i, i2, i3, (int) this.frameRate)) {
            throw new Exception("Low level createCamera() failed");
        } else if (this.camera.startCamera()) {
            this.stat = "started";
        } else {
            throw new Exception("Camera start() failed");
        }
    }

    public void stop() throws Exception {
        if (this.camera.stopCamera()) {
            this.stat = "stopped";
            return;
        }
        throw new Exception("Camera stop() failed");
    }

    public void release() {
        if (this.camera != null) {
            this.camera.dispose();
            this.camera = null;
        }
        if (this.image_4ch != null) {
            this.image_4ch.release();
            this.image_4ch = null;
        }
        if (this.image_1ch != null) {
            this.image_1ch.release();
            this.image_1ch = null;
        }
        if (this.ipl_frame != null) {
            this.ipl_frame = null;
        }
        if (this.ps3_frame != null) {
            this.ps3_frame = null;
        }
        this.stat = "released";
    }

    public void dispose() {
        release();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public CLCamera getCamera() {
        return this.camera;
    }

    public String getUUID() {
        return this.uuid;
    }

    public String toString() {
        return "UUID=" + this.uuid + "; status=" + this.stat + "; timeout=" + this.timeout + "; " + (this.camera != null ? this.camera.toString() : "<no camera>");
    }

    public static void main(String[] argv) {
        String[] uuids = listPS3Cameras();
        for (int i = 0; i < uuids.length; i++) {
            System.out.println(i + ": " + uuids[i]);
        }
    }
}
