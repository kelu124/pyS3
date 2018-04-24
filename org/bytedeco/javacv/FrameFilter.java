package org.bytedeco.javacv;

import java.io.Closeable;
import java.io.IOException;

public abstract class FrameFilter implements Closeable {
    protected double aspectRatio;
    protected String filters;
    protected double frameRate;
    protected int imageHeight;
    protected int imageWidth;
    protected int pixelFormat;

    public static class Exception extends IOException {
        public Exception(String message) {
            super(message);
        }

        public Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public abstract Frame pull() throws Exception;

    public abstract void push(Frame frame) throws Exception;

    public abstract void release() throws Exception;

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;

    public static FrameFilter createDefault(String filtersDescr, int imageWidth, int imageHeight) throws Exception {
        return new FFmpegFrameFilter(filtersDescr, imageWidth, imageHeight);
    }

    public String getFilters() {
        return this.filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
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

    public int getPixelFormat() {
        return this.pixelFormat;
    }

    public void setPixelFormat(int pixelFormat) {
        this.pixelFormat = pixelFormat;
    }

    public double getFrameRate() {
        return this.frameRate;
    }

    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    public double getAspectRatio() {
        return this.aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void close() throws Exception {
        stop();
        release();
    }

    public void restart() throws Exception {
        stop();
        start();
    }

    public void flush() throws Exception {
        do {
        } while (pull() != null);
    }
}
