package org.bytedeco.javacv;

import java.io.File;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_videoio;
import org.bytedeco.javacpp.opencv_videoio.VideoWriter;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat;

public class OpenCVFrameRecorder extends FrameRecorder {
    private static Exception loadingException = null;
    private static final boolean windows = Loader.getPlatform().startsWith("windows");
    private ToMat converter;
    private String filename;
    private VideoWriter writer;

    public static OpenCVFrameRecorder createDefault(File f, int w, int h) throws Exception {
        return new OpenCVFrameRecorder(f, w, h);
    }

    public static OpenCVFrameRecorder createDefault(String f, int w, int h) throws Exception {
        return new OpenCVFrameRecorder(f, w, h);
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            Loader.load(opencv_highgui.class);
        } catch (Throwable t) {
            loadingException = new Exception("Failed to load " + OpenCVFrameRecorder.class, t);
        }
    }

    public OpenCVFrameRecorder(File file, int imageWidth, int imageHeight) {
        this(file.getAbsolutePath(), imageWidth, imageHeight);
    }

    public OpenCVFrameRecorder(String filename, int imageWidth, int imageHeight) {
        this.writer = null;
        this.converter = new ToMat();
        this.filename = filename;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.pixelFormat = 1;
        this.videoCodec = windows ? -1 : opencv_videoio.CV_FOURCC_DEFAULT;
        this.frameRate = 30.0d;
    }

    public void release() throws Exception {
        if (this.writer != null) {
            this.writer.release();
            this.writer = null;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public void start() throws Exception {
        this.writer = new VideoWriter(this.filename, fourCCCodec(), this.frameRate, new Size(this.imageWidth, this.imageHeight), isColour());
    }

    private boolean isColour() {
        return this.pixelFormat != 0;
    }

    private int fourCCCodec() {
        return this.videoCodec;
    }

    public void stop() throws Exception {
        release();
    }

    public void record(Frame frame) throws Exception {
        Mat mat = this.converter.convert(frame);
        if (this.writer != null) {
            this.writer.write(mat);
            frame.keyFrame = true;
            return;
        }
        throw new Exception("Cannot record: There is no writer (Has start() been called?)");
    }
}
