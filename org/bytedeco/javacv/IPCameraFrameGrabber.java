package org.bytedeco.javacv;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

public class IPCameraFrameGrabber extends FrameGrabber {
    private static Exception loadingException = null;
    private final int connectionTimeout;
    private final FrameConverter converter;
    private IplImage decoded;
    private DataInputStream input;
    private byte[] pixelBuffer;
    private final int readTimeout;
    private final URL url;

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            Loader.load(opencv_highgui.class);
        } catch (Throwable t) {
            loadingException = new Exception("Failed to load " + IPCameraFrameGrabber.class, t);
        }
    }

    public IPCameraFrameGrabber(URL url, int startTimeout, int grabTimeout, TimeUnit timeUnit) {
        this.converter = new ToIplImage();
        this.pixelBuffer = new byte[1024];
        this.decoded = null;
        if (url == null) {
            throw new IllegalArgumentException("URL can not be null");
        }
        this.url = url;
        if (timeUnit != null) {
            this.connectionTimeout = toIntExact(TimeUnit.MILLISECONDS.convert((long) startTimeout, timeUnit));
            this.readTimeout = toIntExact(TimeUnit.MILLISECONDS.convert((long) grabTimeout, timeUnit));
            return;
        }
        this.connectionTimeout = -1;
        this.readTimeout = -1;
    }

    public IPCameraFrameGrabber(String urlstr, int connectionTimeout, int readTimeout, TimeUnit timeUnit) throws MalformedURLException {
        this(new URL(urlstr), connectionTimeout, readTimeout, timeUnit);
    }

    @Deprecated
    public IPCameraFrameGrabber(String urlstr) throws MalformedURLException {
        this(new URL(urlstr), -1, -1, null);
    }

    public void start() throws Exception {
        try {
            URLConnection connection = this.url.openConnection();
            if (this.connectionTimeout >= 0) {
                connection.setConnectTimeout(this.connectionTimeout);
            }
            if (this.readTimeout >= 0) {
                connection.setReadTimeout(this.readTimeout);
            }
            this.input = new DataInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    public void stop() throws Exception {
        if (this.input != null) {
            try {
                this.input.close();
                this.input = null;
                releaseDecoded();
            } catch (IOException e) {
                throw new Exception(e.getMessage(), e);
            } catch (Throwable th) {
                this.input = null;
                releaseDecoded();
            }
        }
    }

    public void trigger() throws Exception {
    }

    public Frame grab() throws Exception {
        try {
            byte[] b = readImage();
            CvMat mat = opencv_core.cvMat(1, b.length, opencv_core.CV_8UC1, new BytePointer(b));
            releaseDecoded();
            FrameConverter frameConverter = this.converter;
            IplImage cvDecodeImage = opencv_imgcodecs.cvDecodeImage(mat);
            this.decoded = cvDecodeImage;
            return frameConverter.convert(cvDecodeImage);
        } catch (IOException e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    public BufferedImage grabBufferedImage() throws IOException {
        return ImageIO.read(new ByteArrayInputStream(readImage()));
    }

    private void releaseDecoded() {
        if (this.decoded != null) {
            opencv_core.cvReleaseImage(this.decoded);
            this.decoded = null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] readImage() throws java.io.IOException {
        /*
        r9 = this;
        r7 = 13;
        r4 = new java.lang.StringBuffer;
        r4.<init>();
    L_0x0007:
        r6 = r9.input;
        r0 = r6.read();
        r6 = -1;
        if (r0 == r6) goto L_0x0038;
    L_0x0010:
        if (r0 <= 0) goto L_0x0007;
    L_0x0012:
        r6 = (char) r0;
        r4.append(r6);
        if (r0 != r7) goto L_0x0007;
    L_0x0018:
        r6 = r9.input;
        r6 = r6.read();
        r6 = (char) r6;
        r4.append(r6);
        r6 = r9.input;
        r0 = r6.read();
        r6 = (char) r0;
        r4.append(r6);
        if (r0 != r7) goto L_0x0007;
    L_0x002e:
        r6 = r9.input;
        r6 = r6.read();
        r6 = (char) r6;
        r4.append(r6);
    L_0x0038:
        r6 = r4.toString();
        r5 = r6.toLowerCase();
        r6 = "content-length: ";
        r1 = r5.indexOf(r6);
        r2 = r5.indexOf(r7, r1);
        if (r1 >= 0) goto L_0x0054;
    L_0x004c:
        r6 = new java.io.EOFException;
        r7 = "The camera stream ended unexpectedly";
        r6.<init>(r7);
        throw r6;
    L_0x0054:
        r1 = r1 + 16;
        r6 = r5.substring(r1, r2);
        r6 = r6.trim();
        r3 = java.lang.Integer.parseInt(r6);
        r9.ensureBufferCapacity(r3);
        r6 = r9.input;
        r7 = r9.pixelBuffer;
        r8 = 0;
        r6.readFully(r7, r8, r3);
        r6 = r9.input;
        r6.read();
        r6 = r9.input;
        r6.read();
        r6 = r9.input;
        r6.read();
        r6 = r9.input;
        r6.read();
        r6 = r9.pixelBuffer;
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacv.IPCameraFrameGrabber.readImage():byte[]");
    }

    public void release() throws Exception {
    }

    private void ensureBufferCapacity(int desiredCapacity) {
        int capacity = this.pixelBuffer.length;
        while (capacity < desiredCapacity) {
            capacity *= 2;
        }
        if (capacity > this.pixelBuffer.length) {
            this.pixelBuffer = new byte[capacity];
        }
    }

    private static int toIntExact(long value) {
        if (((long) ((int) value)) == value) {
            return (int) value;
        }
        throw new ArithmeticException("integer overflow");
    }
}
