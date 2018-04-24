package org.bytedeco.javacv;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.dc1394;
import org.bytedeco.javacpp.dc1394.dc1394_t;
import org.bytedeco.javacpp.dc1394.dc1394camera_id_t;
import org.bytedeco.javacpp.dc1394.dc1394camera_list_t;
import org.bytedeco.javacpp.dc1394.dc1394camera_t;
import org.bytedeco.javacpp.dc1394.dc1394video_frame_t;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.presets.dc1394.pollfd;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameGrabber.ImageMode;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

public class DC1394FrameGrabber extends FrameGrabber {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final boolean linux = Loader.getPlatform().startsWith("linux");
    private static Exception loadingException = null;
    private dc1394camera_t camera = null;
    private dc1394video_frame_t conv_image;
    private FrameConverter converter;
    private dc1394_t f184d = null;
    private dc1394video_frame_t enqueue_image;
    private pollfd fds;
    private dc1394video_frame_t frame;
    private final float[] gammaOut;
    private boolean oneShotMode;
    private final int[] out;
    private final float[] outFloat;
    private dc1394video_frame_t[] raw_image;
    private boolean resetDone;
    private IplImage return_image;
    private IplImage temp_image;

    static {
        boolean z;
        if (DC1394FrameGrabber.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public static String[] getDeviceDescriptions() throws Exception {
        tryLoad();
        dc1394_t d = dc1394.dc1394_new();
        if (d == null) {
            throw new Exception("dc1394_new() Error: Failed to initialize libdc1394.");
        }
        dc1394camera_list_t list = new dc1394camera_list_t(null);
        int err = dc1394.dc1394_camera_enumerate(d, list);
        if (err != 0) {
            throw new Exception("dc1394_camera_enumerate() Error " + err + ": Failed to enumerate cameras.");
        }
        int num = list.num();
        String[] descriptions = new String[num];
        if (num > 0) {
            dc1394camera_id_t ids = list.ids();
            for (int i = 0; i < num; i++) {
                ids.position((long) i);
                dc1394camera_t camera = dc1394.dc1394_camera_new_unit(d, ids.guid(), ids.unit());
                if (camera == null) {
                    throw new Exception("dc1394_camera_new_unit() Error: Failed to initialize camera with GUID 0x" + Long.toHexString(ids.guid()) + " / " + camera.unit() + ".");
                }
                descriptions[i] = camera.vendor().getString() + " " + camera.model().getString() + " 0x" + Long.toHexString(camera.guid()) + " / " + camera.unit();
                dc1394.dc1394_camera_free(camera);
            }
        }
        dc1394.dc1394_camera_free_list(list);
        dc1394.dc1394_free(d);
        return descriptions;
    }

    public static DC1394FrameGrabber createDefault(File deviceFile) throws Exception {
        throw new Exception(DC1394FrameGrabber.class + " does not support device files.");
    }

    public static DC1394FrameGrabber createDefault(String devicePath) throws Exception {
        throw new Exception(DC1394FrameGrabber.class + " does not support device paths.");
    }

    public static DC1394FrameGrabber createDefault(int deviceNumber) throws Exception {
        return new DC1394FrameGrabber(deviceNumber);
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            Loader.load(dc1394.class);
        } catch (Throwable t) {
            loadingException = new Exception("Failed to load " + DC1394FrameGrabber.class, t);
        }
    }

    public DC1394FrameGrabber(int deviceNumber) throws Exception {
        pollfd org_bytedeco_javacpp_presets_dc1394_pollfd;
        if (linux) {
            org_bytedeco_javacpp_presets_dc1394_pollfd = new pollfd();
        } else {
            org_bytedeco_javacpp_presets_dc1394_pollfd = null;
        }
        this.fds = org_bytedeco_javacpp_presets_dc1394_pollfd;
        this.oneShotMode = false;
        this.resetDone = false;
        this.raw_image = new dc1394video_frame_t[]{new dc1394video_frame_t(null), new dc1394video_frame_t(null)};
        this.conv_image = new dc1394video_frame_t();
        this.frame = null;
        this.enqueue_image = null;
        this.return_image = null;
        this.converter = new ToIplImage();
        this.out = new int[1];
        this.outFloat = new float[1];
        this.gammaOut = new float[1];
        this.f184d = dc1394.dc1394_new();
        dc1394camera_list_t list = new dc1394camera_list_t(null);
        int err = dc1394.dc1394_camera_enumerate(this.f184d, list);
        if (err != 0) {
            throw new Exception("dc1394_camera_enumerate() Error " + err + ": Failed to enumerate cameras.");
        }
        int num = list.num();
        if (num <= deviceNumber) {
            throw new Exception("DC1394Grabber() Error: Camera number " + deviceNumber + " not found. There are only " + num + " devices.");
        }
        dc1394camera_id_t ids = list.ids().position((long) deviceNumber);
        this.camera = dc1394.dc1394_camera_new_unit(this.f184d, ids.guid(), ids.unit());
        if (this.camera == null) {
            throw new Exception("dc1394_camera_new_unit() Error: Failed to initialize camera with GUID 0x" + Long.toHexString(ids.guid()) + " / " + this.camera.unit() + ".");
        }
        dc1394.dc1394_camera_free_list(list);
    }

    public void release() throws Exception {
        if (this.camera != null) {
            stop();
            dc1394.dc1394_camera_free(this.camera);
            this.camera = null;
        }
        if (this.f184d != null) {
            dc1394.dc1394_free(this.f184d);
            this.f184d = null;
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
        if (this.camera == null) {
            return super.getFrameRate();
        }
        if (dc1394.dc1394_feature_get_absolute_value(this.camera, 431, this.outFloat) != 0) {
            dc1394.dc1394_video_get_framerate(this.camera, this.out);
            dc1394.dc1394_framerate_as_float(this.out[0], this.outFloat);
        }
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
        start(true, true);
    }

    public void start(boolean tryReset, boolean try1394b) throws Exception {
        int c = -1;
        if (this.imageMode == ImageMode.COLOR || this.imageMode == ImageMode.RAW) {
            if (this.imageWidth <= 0 || this.imageHeight <= 0) {
                c = -1;
            } else if (this.imageWidth <= 640 && this.imageHeight <= 480) {
                c = 68;
            } else if (this.imageWidth <= 800 && this.imageHeight <= 600) {
                c = 72;
            } else if (this.imageWidth <= 1024 && this.imageHeight <= 768) {
                c = 75;
            } else if (this.imageWidth <= 1280 && this.imageHeight <= 960) {
                c = 80;
            } else if (this.imageWidth <= 1600 && this.imageHeight <= 1200) {
                c = 83;
            }
        } else if (this.imageMode == ImageMode.GRAY) {
            if (this.imageWidth <= 0 || this.imageHeight <= 0) {
                c = -1;
            } else if (this.imageWidth <= 640 && this.imageHeight <= 480) {
                c = this.bpp > 8 ? 70 : 69;
            } else if (this.imageWidth <= 800 && this.imageHeight <= 600) {
                c = this.bpp > 8 ? 77 : 73;
            } else if (this.imageWidth <= 1024 && this.imageHeight <= 768) {
                c = this.bpp > 8 ? 78 : 76;
            } else if (this.imageWidth <= 1280 && this.imageHeight <= 960) {
                c = this.bpp > 8 ? 85 : 81;
            } else if (this.imageWidth <= 1600 && this.imageHeight <= 1200) {
                c = this.bpp > 8 ? 86 : 84;
            }
        }
        if (c == -1) {
            dc1394.dc1394_video_get_mode(this.camera, this.out);
            c = this.out[0];
        }
        int f = -1;
        if (this.frameRate <= 0.0d) {
            f = -1;
        } else if (this.frameRate <= 1.876d) {
            f = 32;
        } else if (this.frameRate <= 3.76d) {
            f = 33;
        } else if (this.frameRate <= 7.51d) {
            f = 34;
        } else if (this.frameRate <= 15.01d) {
            f = 35;
        } else if (this.frameRate <= 30.01d) {
            f = 36;
        } else if (this.frameRate <= 60.01d) {
            f = 37;
        } else if (this.frameRate <= 120.01d) {
            f = 38;
        } else if (this.frameRate <= 240.01d) {
            f = 39;
        }
        if (f == -1) {
            dc1394.dc1394_video_get_framerate(this.camera, this.out);
            f = this.out[0];
        }
        try {
            int err;
            this.oneShotMode = false;
            if (this.triggerMode) {
                if (dc1394.dc1394_external_trigger_set_power(this.camera, 1) != 0) {
                    this.oneShotMode = true;
                } else {
                    if (dc1394.dc1394_external_trigger_set_mode(this.camera, dc1394.DC1394_TRIGGER_MODE_14) != 0) {
                        err = dc1394.dc1394_external_trigger_set_mode(this.camera, 384);
                    }
                    if (dc1394.dc1394_external_trigger_set_source(this.camera, 580) != 0) {
                        this.oneShotMode = true;
                        dc1394.dc1394_external_trigger_set_power(this.camera, 0);
                    }
                }
            }
            err = dc1394.dc1394_video_set_operation_mode(this.camera, 480);
            if (try1394b) {
                err = dc1394.dc1394_video_set_operation_mode(this.camera, 481);
                if (err == 0) {
                    err = dc1394.dc1394_video_set_iso_speed(this.camera, 3);
                }
            }
            if (!(err == 0 && try1394b)) {
                err = dc1394.dc1394_video_set_iso_speed(this.camera, 2);
                if (err != 0) {
                    throw new Exception("dc1394_video_set_iso_speed() Error " + err + ": Could not set maximum iso speed.");
                }
            }
            err = dc1394.dc1394_video_set_mode(this.camera, c);
            if (err != 0) {
                throw new Exception("dc1394_video_set_mode() Error " + err + ": Could not set video mode.");
            }
            if (dc1394.dc1394_is_video_mode_scalable(c) == 1) {
                err = dc1394.dc1394_format7_set_roi(this.camera, c, -1, -1, -1, -1, -1, -1);
                if (err != 0) {
                    throw new Exception("dc1394_format7_set_roi() Error " + err + ": Could not set format7 mode.");
                }
            }
            err = dc1394.dc1394_video_set_framerate(this.camera, f);
            if (err != 0) {
                throw new Exception("dc1394_video_set_framerate() Error " + err + ": Could not set framerate.");
            }
            err = dc1394.dc1394_capture_setup(this.camera, this.numBuffers, 4);
            if (err != 0) {
                throw new Exception("dc1394_capture_setup() Error " + err + ": Could not setup camera-\nmake sure that the video mode and framerate are\nsupported by your camera.");
            }
            if (this.gamma != 0.0d) {
                err = dc1394.dc1394_feature_set_absolute_value(this.camera, 422, (float) this.gamma);
                if (err != 0) {
                    throw new Exception("dc1394_feature_set_absolute_value() Error " + err + ": Could not set gamma.");
                }
            }
            if (dc1394.dc1394_feature_get_absolute_value(this.camera, 422, this.gammaOut) != 0) {
                this.gammaOut[0] = 2.2f;
            }
            if (linux) {
                this.fds.fd(dc1394.dc1394_capture_get_fileno(this.camera));
            }
            if (!this.oneShotMode) {
                err = dc1394.dc1394_video_set_transmission(this.camera, 1);
                if (err != 0) {
                    throw new Exception("dc1394_video_set_transmission() Error " + err + ": Could not start camera iso transmission.");
                }
            }
            this.resetDone = false;
            if (linux && try1394b) {
                if (this.triggerMode) {
                    trigger();
                }
                this.fds.events((short) 1);
                if (dc1394.poll(this.fds, 1, this.timeout) == 0) {
                    stop();
                    start(tryReset, false);
                } else if (this.triggerMode) {
                    grab();
                    enqueue();
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new Exception("dc1394_reset_bus() Error: Could not reset bus and try to start again.", ex);
        } catch (Exception e) {
            if (tryReset) {
                if (!this.resetDone) {
                    dc1394.dc1394_reset_bus(this.camera);
                    Thread.sleep(100);
                    this.resetDone = true;
                    start(false, try1394b);
                    this.resetDone = false;
                }
            }
            throw e;
        } catch (Throwable th) {
            this.resetDone = false;
        }
    }

    public void stop() throws Exception {
        this.enqueue_image = null;
        this.temp_image = null;
        this.return_image = null;
        this.timestamp = 0;
        this.frameNumber = 0;
        int err = dc1394.dc1394_video_set_transmission(this.camera, 0);
        if (err != 0) {
            throw new Exception("dc1394_video_set_transmission() Error " + err + ": Could not stop the camera?");
        }
        err = dc1394.dc1394_capture_stop(this.camera);
        if (err != 0 && err != -10) {
            throw new Exception("dc1394_capture_stop() Error " + err + ": Could not stop the camera?");
        } else if (dc1394.dc1394_external_trigger_get_mode(this.camera, this.out) == 0 && this.out[0] >= 384) {
            err = dc1394.dc1394_external_trigger_set_power(this.camera, 0);
            if (err != 0) {
                throw new Exception("dc1394_external_trigger_set_power() Error " + err + ": Could not switch off external trigger.");
            }
        }
    }

    private void enqueue() throws Exception {
        enqueue(this.enqueue_image);
        this.enqueue_image = null;
    }

    private void enqueue(dc1394video_frame_t image) throws Exception {
        if (image != null) {
            int err = dc1394.dc1394_capture_enqueue(this.camera, image);
            if (err != 0) {
                throw new Exception("dc1394_capture_enqueue() Error " + err + ": Could not release a frame.");
            }
        }
    }

    public void trigger() throws Exception {
        enqueue();
        int err;
        if (this.oneShotMode) {
            err = dc1394.dc1394_video_set_one_shot(this.camera, 1);
            if (err != 0) {
                throw new Exception("dc1394_video_set_one_shot() Error " + err + ": Could not set camera into one-shot mode.");
            }
            return;
        }
        long time = System.currentTimeMillis();
        do {
            dc1394.dc1394_software_trigger_get_power(this.camera, this.out);
            if (System.currentTimeMillis() - time > ((long) this.timeout)) {
                break;
            }
        } while (this.out[0] == 1);
        err = dc1394.dc1394_software_trigger_set_power(this.camera, 1);
        if (err != 0) {
            throw new Exception("dc1394_software_trigger_set_power() Error " + err + ": Could not trigger camera.");
        }
    }

    public Frame grab() throws Exception {
        enqueue();
        if (linux) {
            this.fds.events((short) 1);
            if (dc1394.poll(this.fds, 1, this.timeout) == 0) {
                throw new Exception("poll() Error: Timeout occured. (Has start() been called?)");
            }
        }
        int i = 0;
        int err = dc1394.dc1394_capture_dequeue(this.camera, 672, this.raw_image[0]);
        if (err != 0) {
            throw new Exception("dc1394_capture_dequeue(WAIT) Error " + err + ": Could not capture a frame. (Has start() been called?)");
        }
        int numDequeued = 0;
        while (!this.raw_image[i].isNull()) {
            enqueue();
            this.enqueue_image = this.raw_image[i];
            i = (i + 1) % 2;
            numDequeued++;
            err = dc1394.dc1394_capture_dequeue(this.camera, 673, this.raw_image[i]);
            if (err != 0) {
                throw new Exception("dc1394_capture_dequeue(POLL) Error " + err + ": Could not capture a frame.");
            }
        }
        this.frame = this.raw_image[(i + 1) % 2];
        int w = this.frame.size(0);
        int h = this.frame.size(1);
        int depth = this.frame.data_depth();
        int iplDepth = 0;
        switch (depth) {
            case 8:
                iplDepth = 8;
                break;
            case 16:
                iplDepth = 16;
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        int stride = this.frame.stride();
        int size = this.frame.image_bytes();
        int numChannels = ((stride / w) * 8) / depth;
        ByteOrder frameEndian = this.frame.little_endian() != 0 ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
        boolean alreadySwapped = false;
        int color_coding = this.frame.color_coding();
        boolean colorbayer = color_coding == 361 || color_coding == 362;
        boolean colorrgb = color_coding == 356 || color_coding == 358;
        boolean coloryuv = color_coding == 353 || color_coding == 354 || color_coding == 355;
        BytePointer imageData = this.frame.image();
        if ((depth <= 8 || frameEndian.equals(ByteOrder.nativeOrder())) && !coloryuv && (this.imageMode == ImageMode.RAW || ((this.imageMode == ImageMode.COLOR && numChannels == 3) || (this.imageMode == ImageMode.GRAY && numChannels == 1 && !colorbayer)))) {
            if (this.return_image == null) {
                this.return_image = IplImage.createHeader(w, h, iplDepth, numChannels);
            }
            this.return_image.widthStep(stride);
            this.return_image.imageSize(size);
            this.return_image.imageData(imageData);
        } else {
            int c;
            int padding_bytes = this.frame.padding_bytes();
            int padding1 = (int) Math.ceil(((double) padding_bytes) / ((double) ((w * depth) / 8)));
            int padding3 = (int) Math.ceil(((double) padding_bytes) / ((double) (((w * 3) * depth) / 8)));
            if (this.return_image == null) {
                int padding;
                c = this.imageMode == ImageMode.COLOR ? 3 : 1;
                if (this.imageMode == ImageMode.COLOR) {
                    padding = padding3;
                } else {
                    padding = padding1;
                }
                this.return_image = IplImage.create(w, h + padding, iplDepth, c);
                this.return_image.height(this.return_image.height() - padding);
            }
            if (this.temp_image == null) {
                if (this.imageMode != ImageMode.COLOR || ((numChannels <= 1 && depth <= 8) || coloryuv || colorbayer)) {
                    if (this.imageMode == ImageMode.GRAY && (coloryuv || colorbayer || (colorrgb && depth > 8))) {
                        this.temp_image = IplImage.create(w, h + padding3, iplDepth, 3);
                        this.temp_image.height(this.temp_image.height() - padding3);
                    } else {
                        if (this.imageMode == ImageMode.GRAY && colorrgb) {
                            this.temp_image = IplImage.createHeader(w, h, iplDepth, 3);
                        } else {
                            if (this.imageMode != ImageMode.COLOR || numChannels != 1 || coloryuv || colorbayer) {
                                this.temp_image = this.return_image;
                            } else {
                                this.temp_image = IplImage.createHeader(w, h, iplDepth, 1);
                            }
                        }
                    }
                } else {
                    this.temp_image = IplImage.create(w, h + padding1, iplDepth, numChannels);
                    this.temp_image.height(this.temp_image.height() - padding1);
                }
            }
            this.conv_image.size(0, this.temp_image.width());
            this.conv_image.size(1, this.temp_image.height());
            dc1394video_frame_t org_bytedeco_javacpp_dc1394_dc1394video_frame_t;
            int i2;
            if (depth > 8) {
                org_bytedeco_javacpp_dc1394_dc1394video_frame_t = this.conv_image;
                i2 = this.imageMode == ImageMode.RAW ? 362 : this.temp_image.nChannels() == 1 ? dc1394.DC1394_COLOR_CODING_MONO16 : dc1394.DC1394_COLOR_CODING_RGB16;
                org_bytedeco_javacpp_dc1394_dc1394video_frame_t.color_coding(i2);
                this.conv_image.data_depth(16);
            } else {
                org_bytedeco_javacpp_dc1394_dc1394video_frame_t = this.conv_image;
                i2 = this.imageMode == ImageMode.RAW ? dc1394.DC1394_COLOR_CODING_RAW8 : this.temp_image.nChannels() == 1 ? 352 : dc1394.DC1394_COLOR_CODING_RGB8;
                org_bytedeco_javacpp_dc1394_dc1394video_frame_t.color_coding(i2);
                this.conv_image.data_depth(8);
            }
            this.conv_image.stride(this.temp_image.widthStep());
            int temp_size = this.temp_image.imageSize();
            this.conv_image.allocated_image_bytes((long) temp_size).total_bytes((long) temp_size).image_bytes(temp_size);
            this.conv_image.image(this.temp_image.imageData());
            if (colorbayer) {
                c = this.frame.color_filter();
                if (c == 512) {
                    this.frame.color_filter(515);
                } else if (c == 513) {
                    this.frame.color_filter(514);
                } else if (c == 514) {
                    this.frame.color_filter(513);
                } else if (c == 515) {
                    this.frame.color_filter(512);
                } else if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                err = dc1394.dc1394_debayer_frames(this.frame, this.conv_image, 1);
                this.frame.color_filter(c);
                if (err != 0) {
                    throw new Exception("dc1394_debayer_frames() Error " + err + ": Could not debayer frame.");
                }
            } else if (depth > 8 && this.frame.data_depth() == this.conv_image.data_depth() && this.frame.color_coding() == this.conv_image.color_coding() && this.frame.stride() == this.conv_image.stride()) {
                this.temp_image.getByteBuffer().order(ByteOrder.nativeOrder()).asShortBuffer().put(this.frame.getByteBuffer().order(frameEndian).asShortBuffer());
                alreadySwapped = true;
            } else {
                if ((this.imageMode == ImageMode.GRAY && colorrgb) || (this.imageMode == ImageMode.COLOR && numChannels == 1 && !coloryuv && !colorbayer)) {
                    this.temp_image.widthStep(stride);
                    this.temp_image.imageSize(size);
                    this.temp_image.imageData(imageData);
                } else if (!colorrgb && (colorbayer || coloryuv || numChannels > 1)) {
                    err = dc1394.dc1394_convert_frames(this.frame, this.conv_image);
                    if (err != 0) {
                        throw new Exception("dc1394_convert_frames() Error " + err + ": Could not convert frame.");
                    }
                }
            }
            if (!(alreadySwapped || depth <= 8 || frameEndian.equals(ByteOrder.nativeOrder()))) {
                ByteBuffer bb = this.temp_image.getByteBuffer();
                bb.order(ByteOrder.nativeOrder()).asShortBuffer().put(bb.order(frameEndian).asShortBuffer());
            }
            if (this.imageMode != ImageMode.COLOR || numChannels != 1 || coloryuv || colorbayer) {
                if (this.imageMode == ImageMode.GRAY && (colorbayer || colorrgb || coloryuv)) {
                    opencv_imgproc.cvCvtColor(this.temp_image, this.return_image, 6);
                }
            } else {
                opencv_imgproc.cvCvtColor(this.temp_image, this.return_image, 8);
            }
        }
        switch (this.frame.color_filter()) {
            case 512:
                this.sensorPattern = 0;
                break;
            case 513:
                this.sensorPattern = 4294967296L;
                break;
            case 514:
                this.sensorPattern = 1;
                break;
            case 515:
                this.sensorPattern = FrameGrabber.SENSOR_PATTERN_BGGR;
                break;
            default:
                this.sensorPattern = -1;
                break;
        }
        this.enqueue_image = this.frame;
        this.timestamp = this.frame.timestamp();
        this.frameNumber += numDequeued;
        return this.converter.convert((Object) this.return_image);
    }
}
