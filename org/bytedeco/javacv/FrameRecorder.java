package org.bytedeco.javacv;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class FrameRecorder implements Closeable {
    public static final List<String> list = new LinkedList(Arrays.asList(new String[]{"FFmpeg", "OpenCV"}));
    protected double aspectRatio;
    protected int audioBitrate;
    protected int audioChannels;
    protected int audioCodec;
    protected String audioCodecName;
    protected HashMap<String, String> audioMetadata = new HashMap();
    protected HashMap<String, String> audioOptions = new HashMap();
    protected double audioQuality = -1.0d;
    protected String format;
    protected int frameNumber = 0;
    protected double frameRate;
    protected int gopSize = -1;
    protected int imageHeight;
    protected int imageWidth;
    protected boolean interleaved;
    protected HashMap<String, String> metadata = new HashMap();
    protected HashMap<String, String> options = new HashMap();
    protected int pixelFormat;
    protected int sampleFormat;
    protected int sampleRate;
    protected long timestamp = 0;
    protected int videoBitrate;
    protected int videoCodec;
    protected String videoCodecName;
    protected HashMap<String, String> videoMetadata = new HashMap();
    protected HashMap<String, String> videoOptions = new HashMap();
    protected double videoQuality = -1.0d;

    public static class Exception extends IOException {
        public Exception(String message) {
            super(message);
        }

        public Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public abstract void record(Frame frame) throws Exception;

    public abstract void release() throws Exception;

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;

    public static void init() {
        for (String name : list) {
            try {
                get(name).getMethod("tryLoad", new Class[0]).invoke(null, new Object[0]);
            } catch (Throwable th) {
            }
        }
    }

    public static Class<? extends FrameRecorder> getDefault() {
        for (String name : list) {
            try {
                Class<? extends FrameRecorder> c = get(name);
                c.getMethod("tryLoad", new Class[0]).invoke(null, new Object[0]);
                return c;
            } catch (Throwable th) {
            }
        }
        return null;
    }

    public static Class<? extends FrameRecorder> get(String className) throws Exception {
        Class<? extends FrameRecorder> asSubclass;
        className = FrameRecorder.class.getPackage().getName() + "." + className;
        try {
            asSubclass = Class.forName(className).asSubclass(FrameRecorder.class);
        } catch (ClassNotFoundException e) {
            String className2 = className + "FrameRecorder";
            try {
                asSubclass = Class.forName(className2).asSubclass(FrameRecorder.class);
            } catch (ClassNotFoundException e2) {
                throw new Exception("Could not get FrameRecorder class for " + className + " or " + className2, e);
            }
        }
        return asSubclass;
    }

    public static FrameRecorder create(Class<? extends FrameRecorder> c, Class p, Object o, int w, int h) throws Exception {
        Throwable cause;
        try {
            return (FrameRecorder) c.getConstructor(new Class[]{p, Integer.TYPE, Integer.TYPE}).newInstance(new Object[]{o, Integer.valueOf(w), Integer.valueOf(h)});
        } catch (Throwable ex) {
            cause = ex;
            throw new Exception("Could not create new " + c.getSimpleName() + "(" + o + ", " + w + ", " + h + ")", cause);
        } catch (Throwable ex2) {
            cause = ex2;
            throw new Exception("Could not create new " + c.getSimpleName() + "(" + o + ", " + w + ", " + h + ")", cause);
        } catch (Throwable ex22) {
            cause = ex22;
            throw new Exception("Could not create new " + c.getSimpleName() + "(" + o + ", " + w + ", " + h + ")", cause);
        } catch (Throwable ex222) {
            cause = ex222;
            throw new Exception("Could not create new " + c.getSimpleName() + "(" + o + ", " + w + ", " + h + ")", cause);
        } catch (InvocationTargetException ex3) {
            cause = ex3.getCause();
            throw new Exception("Could not create new " + c.getSimpleName() + "(" + o + ", " + w + ", " + h + ")", cause);
        }
    }

    public static FrameRecorder createDefault(File file, int width, int height) throws Exception {
        return create(getDefault(), File.class, file, width, height);
    }

    public static FrameRecorder createDefault(String filename, int width, int height) throws Exception {
        return create(getDefault(), String.class, filename, width, height);
    }

    public static FrameRecorder create(String className, File file, int width, int height) throws Exception {
        return create(get(className), File.class, file, width, height);
    }

    public static FrameRecorder create(String className, String filename, int width, int height) throws Exception {
        return create(get(className), String.class, filename, width, height);
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getVideoCodecName() {
        return this.videoCodecName;
    }

    public void setVideoCodecName(String videoCodecName) {
        this.videoCodecName = videoCodecName;
    }

    public String getAudioCodecName() {
        return this.audioCodecName;
    }

    public void setAudioCodecName(String audioCodecName) {
        this.audioCodecName = audioCodecName;
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

    public int getGopSize() {
        return this.gopSize;
    }

    public void setGopSize(int gopSize) {
        this.gopSize = gopSize;
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

    public double getVideoQuality() {
        return this.videoQuality;
    }

    public void setVideoQuality(double videoQuality) {
        this.videoQuality = videoQuality;
    }

    public int getSampleFormat() {
        return this.sampleFormat;
    }

    public void setSampleFormat(int sampleFormat) {
        this.sampleFormat = sampleFormat;
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

    public int getSampleRate() {
        return this.sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public double getAudioQuality() {
        return this.audioQuality;
    }

    public void setAudioQuality(double audioQuality) {
        this.audioQuality = audioQuality;
    }

    public boolean isInterleaved() {
        return this.interleaved;
    }

    public void setInterleaved(boolean interleaved) {
        this.interleaved = interleaved;
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

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void close() throws Exception {
        stop();
        release();
    }
}
