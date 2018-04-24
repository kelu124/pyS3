package org.bytedeco.javacv;

import java.beans.PropertyEditorSupport;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class FrameGrabber implements Closeable {
    public static final long SENSOR_PATTERN_BGGR = 4294967297L;
    public static final long SENSOR_PATTERN_GBRG = 4294967296L;
    public static final long SENSOR_PATTERN_GRBG = 1;
    public static final long SENSOR_PATTERN_RGGB = 0;
    public static final List<String> list = new LinkedList(Arrays.asList(new String[]{"DC1394", "FlyCapture", "FlyCapture2", "OpenKinect", "OpenKinect2", "RealSense", "PS3Eye", "VideoInput", "OpenCV", "FFmpeg", "IPCamera"}));
    protected double aspectRatio = 0.0d;
    protected int audioBitrate = 0;
    protected int audioChannels = 0;
    protected int audioCodec;
    protected HashMap<String, String> audioMetadata = new HashMap();
    protected HashMap<String, String> audioOptions = new HashMap();
    protected int audioStream = -1;
    protected int bpp = 0;
    protected boolean deinterlace = false;
    private Frame delayedFrame = null;
    private long delayedTime = 0;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    protected String format = null;
    protected int frameNumber = 0;
    protected double frameRate = 0.0d;
    private Future<Void> future = null;
    protected double gamma = 0.0d;
    protected int imageHeight = 0;
    protected ImageMode imageMode = ImageMode.COLOR;
    protected int imageWidth = 0;
    protected HashMap<String, String> metadata = new HashMap();
    protected int numBuffers = 4;
    protected HashMap<String, String> options = new HashMap();
    protected int pixelFormat = -1;
    protected int sampleFormat = 0;
    protected int sampleRate = 0;
    protected long sensorPattern = -1;
    protected int timeout = 10000;
    protected long timestamp = 0;
    protected boolean triggerMode = false;
    protected int videoBitrate = 0;
    protected int videoCodec;
    protected HashMap<String, String> videoMetadata = new HashMap();
    protected HashMap<String, String> videoOptions = new HashMap();
    protected int videoStream = -1;

    public static class Array {
        private long bestInterval = Long.MAX_VALUE;
        private long[] bestLatencies = null;
        protected FrameGrabber[] frameGrabbers = null;
        private Frame[] grabbedFrames = null;
        private long lastNewestTimestamp = 0;
        private long[] latencies = null;

        protected Array(FrameGrabber[] frameGrabbers) {
            setFrameGrabbers(frameGrabbers);
        }

        public FrameGrabber[] getFrameGrabbers() {
            return this.frameGrabbers;
        }

        public void setFrameGrabbers(FrameGrabber[] frameGrabbers) {
            this.frameGrabbers = frameGrabbers;
            this.grabbedFrames = new Frame[frameGrabbers.length];
            this.latencies = new long[frameGrabbers.length];
            this.bestLatencies = null;
            this.lastNewestTimestamp = 0;
        }

        public int size() {
            return this.frameGrabbers.length;
        }

        public void start() throws Exception {
            for (FrameGrabber f : this.frameGrabbers) {
                f.start();
            }
        }

        public void stop() throws Exception {
            for (FrameGrabber f : this.frameGrabbers) {
                f.stop();
            }
        }

        public void trigger() throws Exception {
            for (FrameGrabber f : this.frameGrabbers) {
                if (f.isTriggerMode()) {
                    f.trigger();
                }
            }
        }

        public Frame[] grab() throws Exception {
            if (this.frameGrabbers.length == 1) {
                this.grabbedFrames[0] = this.frameGrabbers[0].grab();
                return this.grabbedFrames;
            }
            int i;
            long newestTimestamp = 0;
            boolean unsynchronized = false;
            for (i = 0; i < this.frameGrabbers.length; i++) {
                this.grabbedFrames[i] = this.frameGrabbers[i].grab();
                if (this.grabbedFrames[i] != null) {
                    newestTimestamp = Math.max(newestTimestamp, this.frameGrabbers[i].getTimestamp());
                }
                if (this.frameGrabbers[i].getClass() != this.frameGrabbers[(i + 1) % this.frameGrabbers.length].getClass()) {
                    unsynchronized = true;
                }
            }
            if (unsynchronized) {
                return this.grabbedFrames;
            }
            for (i = 0; i < this.frameGrabbers.length; i++) {
                if (this.grabbedFrames[i] != null) {
                    this.latencies[i] = newestTimestamp - Math.max(0, this.frameGrabbers[i].getTimestamp());
                }
            }
            if (this.bestLatencies == null) {
                this.bestLatencies = Arrays.copyOf(this.latencies, this.latencies.length);
            } else {
                int sum1 = 0;
                int sum2 = 0;
                for (i = 0; i < this.frameGrabbers.length; i++) {
                    sum1 = (int) (((long) sum1) + this.latencies[i]);
                    sum2 = (int) (((long) sum2) + this.bestLatencies[i]);
                }
                if (sum1 < sum2) {
                    this.bestLatencies = Arrays.copyOf(this.latencies, this.latencies.length);
                }
            }
            this.bestInterval = Math.min(this.bestInterval, newestTimestamp - this.lastNewestTimestamp);
            for (i = 0; i < this.bestLatencies.length; i++) {
                this.bestLatencies[i] = Math.min(this.bestLatencies[i], (this.bestInterval * 9) / 10);
            }
            for (int j = 0; j < 2; j++) {
                i = 0;
                while (i < this.frameGrabbers.length) {
                    if (!this.frameGrabbers[i].isTriggerMode() && this.grabbedFrames[i] != null) {
                        int latency = (int) (newestTimestamp - Math.max(0, this.frameGrabbers[i].getTimestamp()));
                        while (((double) (((long) latency) - this.bestLatencies[i])) > 0.1d * ((double) this.bestLatencies[i])) {
                            this.grabbedFrames[i] = this.frameGrabbers[i].grab();
                            if (this.grabbedFrames[i] == null) {
                                break;
                            }
                            latency = (int) (newestTimestamp - Math.max(0, this.frameGrabbers[i].getTimestamp()));
                            if (latency < 0) {
                                newestTimestamp = Math.max(0, this.frameGrabbers[i].getTimestamp());
                                break;
                            }
                        }
                    }
                    i++;
                }
            }
            this.lastNewestTimestamp = newestTimestamp;
            return this.grabbedFrames;
        }

        public void release() throws Exception {
            for (FrameGrabber f : this.frameGrabbers) {
                f.release();
            }
        }
    }

    public static class Exception extends IOException {
        public Exception(String message) {
            super(message);
        }

        public Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public enum ImageMode {
        COLOR,
        GRAY,
        RAW
    }

    public static class PropertyEditor extends PropertyEditorSupport {
        public String getAsText() {
            Class c = (Class) getValue();
            return c == null ? "null" : c.getSimpleName().split("FrameGrabber")[0];
        }

        public void setAsText(String s) {
            if (s == null) {
                setValue(null);
            }
            try {
                setValue(FrameGrabber.get(s));
            } catch (Exception ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        public String[] getTags() {
            return (String[]) FrameGrabber.list.toArray(new String[FrameGrabber.list.size()]);
        }
    }

    public abstract Frame grab() throws Exception;

    public abstract void release() throws Exception;

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;

    public abstract void trigger() throws Exception;

    public static void init() {
        for (String name : list) {
            try {
                get(name).getMethod("tryLoad", new Class[0]).invoke(null, new Object[0]);
            } catch (Throwable th) {
            }
        }
    }

    public static Class<? extends FrameGrabber> getDefault() {
        for (String name : list) {
            boolean mayContainCameras;
            try {
                Class<? extends FrameGrabber> c;
                c = get(name);
                c.getMethod("tryLoad", new Class[0]).invoke(null, new Object[0]);
                mayContainCameras = false;
                if (((String[]) c.getMethod("getDeviceDescriptions", new Class[0]).invoke(null, new Object[0])).length > 0) {
                    mayContainCameras = true;
                }
            } catch (Throwable th) {
            }
            if (mayContainCameras) {
                return c;
            }
        }
        return null;
    }

    public static Class<? extends FrameGrabber> get(String className) throws Exception {
        Class<? extends FrameGrabber> asSubclass;
        className = FrameGrabber.class.getPackage().getName() + "." + className;
        try {
            asSubclass = Class.forName(className).asSubclass(FrameGrabber.class);
        } catch (ClassNotFoundException e) {
            String className2 = className + "FrameGrabber";
            try {
                asSubclass = Class.forName(className2).asSubclass(FrameGrabber.class);
            } catch (ClassNotFoundException e2) {
                throw new Exception("Could not get FrameGrabber class for " + className + " or " + className2, e);
            }
        }
        return asSubclass;
    }

    public static FrameGrabber create(Class<? extends FrameGrabber> c, Class p, Object o) throws Exception {
        Throwable cause;
        try {
            return (FrameGrabber) c.getConstructor(new Class[]{p}).newInstance(new Object[]{o});
        } catch (Throwable ex) {
            cause = ex;
            throw new Exception("Could not create new " + c.getSimpleName() + "(" + o + ")", cause);
        } catch (Throwable ex2) {
            cause = ex2;
            throw new Exception("Could not create new " + c.getSimpleName() + "(" + o + ")", cause);
        } catch (Throwable ex22) {
            cause = ex22;
            throw new Exception("Could not create new " + c.getSimpleName() + "(" + o + ")", cause);
        } catch (Throwable ex222) {
            cause = ex222;
            throw new Exception("Could not create new " + c.getSimpleName() + "(" + o + ")", cause);
        } catch (InvocationTargetException ex3) {
            cause = ex3.getCause();
            throw new Exception("Could not create new " + c.getSimpleName() + "(" + o + ")", cause);
        }
    }

    public static FrameGrabber createDefault(File deviceFile) throws Exception {
        return create(getDefault(), File.class, deviceFile);
    }

    public static FrameGrabber createDefault(String devicePath) throws Exception {
        return create(getDefault(), String.class, devicePath);
    }

    public static FrameGrabber createDefault(int deviceNumber) throws Exception {
        try {
            return create(getDefault(), Integer.TYPE, Integer.valueOf(deviceNumber));
        } catch (Exception e) {
            return create(getDefault(), Integer.class, Integer.valueOf(deviceNumber));
        }
    }

    public static FrameGrabber create(String className, File deviceFile) throws Exception {
        return create(get(className), File.class, deviceFile);
    }

    public static FrameGrabber create(String className, String devicePath) throws Exception {
        return create(get(className), String.class, devicePath);
    }

    public static FrameGrabber create(String className, int deviceNumber) throws Exception {
        try {
            return create(get(className), Integer.TYPE, Integer.valueOf(deviceNumber));
        } catch (Exception e) {
            return create(get(className), Integer.class, Integer.valueOf(deviceNumber));
        }
    }

    public int getVideoStream() {
        return this.videoStream;
    }

    public void setVideoStream(int videoStream) {
        this.videoStream = videoStream;
    }

    public int getAudioStream() {
        return this.audioStream;
    }

    public void setAudioStream(int audioStream) {
        this.audioStream = audioStream;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getImageWidth() {
        return this.imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return this.imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getAudioChannels() {
        return this.audioChannels;
    }

    public void setAudioChannels(int audioChannels) {
        this.audioChannels = audioChannels;
    }

    public ImageMode getImageMode() {
        return this.imageMode;
    }

    public void setImageMode(ImageMode imageMode) {
        this.imageMode = imageMode;
    }

    public long getSensorPattern() {
        return this.sensorPattern;
    }

    public void setSensorPattern(long sensorPattern) {
        this.sensorPattern = sensorPattern;
    }

    public int getPixelFormat() {
        return this.pixelFormat;
    }

    public void setPixelFormat(int pixelFormat) {
        this.pixelFormat = pixelFormat;
    }

    public int getVideoCodec() {
        return this.videoCodec;
    }

    public void setVideoCodec(int videoCodec) {
        this.videoCodec = videoCodec;
    }

    public int getVideoBitrate() {
        return this.videoBitrate;
    }

    public void setVideoBitrate(int videoBitrate) {
        this.videoBitrate = videoBitrate;
    }

    public double getAspectRatio() {
        return this.aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public double getFrameRate() {
        return this.frameRate;
    }

    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    public int getAudioCodec() {
        return this.audioCodec;
    }

    public void setAudioCodec(int audioCodec) {
        this.audioCodec = audioCodec;
    }

    public int getAudioBitrate() {
        return this.audioBitrate;
    }

    public void setAudioBitrate(int audioBitrate) {
        this.audioBitrate = audioBitrate;
    }

    public int getSampleFormat() {
        return this.sampleFormat;
    }

    public void setSampleFormat(int sampleFormat) {
        this.sampleFormat = sampleFormat;
    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public boolean isTriggerMode() {
        return this.triggerMode;
    }

    public void setTriggerMode(boolean triggerMode) {
        this.triggerMode = triggerMode;
    }

    public int getBitsPerPixel() {
        return this.bpp;
    }

    public void setBitsPerPixel(int bitsPerPixel) {
        this.bpp = bitsPerPixel;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getNumBuffers() {
        return this.numBuffers;
    }

    public void setNumBuffers(int numBuffers) {
        this.numBuffers = numBuffers;
    }

    public double getGamma() {
        return this.gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public boolean isDeinterlace() {
        return this.deinterlace;
    }

    public void setDeinterlace(boolean deinterlace) {
        this.deinterlace = deinterlace;
    }

    public String getOption(String key) {
        return (String) this.options.get(key);
    }

    public void setOption(String key, String value) {
        this.options.put(key, value);
    }

    public String getVideoOption(String key) {
        return (String) this.videoOptions.get(key);
    }

    public void setVideoOption(String key, String value) {
        this.videoOptions.put(key, value);
    }

    public String getAudioOption(String key) {
        return (String) this.audioOptions.get(key);
    }

    public void setAudioOption(String key, String value) {
        this.audioOptions.put(key, value);
    }

    public String getMetadata(String key) {
        return (String) this.metadata.get(key);
    }

    public void setMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    public String getVideoMetadata(String key) {
        return (String) this.videoMetadata.get(key);
    }

    public void setVideoMetadata(String key, String value) {
        this.videoMetadata.put(key, value);
    }

    public String getAudioMetadata(String key) {
        return (String) this.audioMetadata.get(key);
    }

    public void setAudioMetadata(String key, String value) {
        this.audioMetadata.put(key, value);
    }

    public int getFrameNumber() {
        return this.frameNumber;
    }

    public void setFrameNumber(int frameNumber) throws Exception {
        this.frameNumber = frameNumber;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) throws Exception {
        this.timestamp = timestamp;
    }

    public int getLengthInFrames() {
        return 0;
    }

    public long getLengthInTime() {
        return 0;
    }

    public void close() throws Exception {
        stop();
        release();
    }

    public Frame grabFrame() throws Exception {
        return grab();
    }

    public void restart() throws Exception {
        stop();
        start();
    }

    public void flush() throws Exception {
        for (int i = 0; i < this.numBuffers + 1; i++) {
            grab();
        }
    }

    public void delayedGrab(long delayTime) {
        this.delayedFrame = null;
        this.delayedTime = 0;
        final long start = System.nanoTime() / 1000;
        if (this.future == null || this.future.isDone()) {
            final long j = delayTime;
            this.future = this.executor.submit(new Callable<Void>() {
                public Void call() throws Exception {
                    do {
                        FrameGrabber.this.delayedFrame = FrameGrabber.this.grab();
                        FrameGrabber.this.delayedTime = (System.nanoTime() / 1000) - start;
                    } while (FrameGrabber.this.delayedTime < j);
                    return null;
                }
            });
        }
    }

    public long getDelayedTime() throws InterruptedException, ExecutionException {
        if (this.future == null) {
            return 0;
        }
        this.future.get();
        return this.delayedTime;
    }

    public Frame getDelayedFrame() throws InterruptedException, ExecutionException {
        if (this.future == null) {
            return null;
        }
        this.future.get();
        return this.delayedFrame;
    }

    public Array createArray(FrameGrabber[] frameGrabbers) {
        return new Array(frameGrabbers);
    }
}
