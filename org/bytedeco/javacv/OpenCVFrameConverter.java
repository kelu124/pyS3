package org.bytedeco.javacv;

import java.nio.Buffer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;

public abstract class OpenCVFrameConverter<F> extends FrameConverter<F> {
    IplImage img;
    Mat mat;

    public static class ToIplImage extends OpenCVFrameConverter<IplImage> {
        public Frame convert(IplImage img) {
            return super.convert(img);
        }

        public IplImage convert(Frame frame) {
            return convertToIplImage(frame);
        }
    }

    public static class ToMat extends OpenCVFrameConverter<Mat> {
        public Frame convert(Mat mat) {
            return super.convert(mat);
        }

        public Mat convert(Frame frame) {
            return convertToMat(frame);
        }
    }

    public static int getFrameDepth(int depth) {
        switch (depth) {
            case -2147483640:
            case 1:
                return -8;
            case opencv_core.IPL_DEPTH_16S /*-2147483632*/:
            case 3:
                return -16;
            case opencv_core.IPL_DEPTH_32S /*-2147483616*/:
            case 4:
                return -32;
            case 0:
            case 8:
                return 8;
            case 2:
            case 16:
                return 16;
            case 5:
            case 32:
                return 32;
            case 6:
            case 64:
                return 64;
            default:
                return -1;
        }
    }

    public static int getIplImageDepth(int depth) {
        switch (depth) {
            case -32:
                return opencv_core.IPL_DEPTH_32S;
            case -16:
                return opencv_core.IPL_DEPTH_16S;
            case -8:
                return -2147483640;
            case 8:
                return 8;
            case 16:
                return 16;
            case 32:
                return 32;
            case 64:
                return 64;
            default:
                return -1;
        }
    }

    static boolean isEqual(Frame frame, IplImage img) {
        if (img != null && frame != null && frame.image != null && frame.image.length > 0 && frame.imageWidth == img.width() && frame.imageHeight == img.height() && frame.imageChannels == img.nChannels() && getIplImageDepth(frame.imageDepth) == img.depth() && new Pointer(frame.image[0]).address() == img.imageData().address() && (frame.imageStride * Math.abs(frame.imageDepth)) / 8 == img.widthStep()) {
            return true;
        }
        return false;
    }

    public IplImage convertToIplImage(Frame frame) {
        IplImage iplImage = null;
        if (frame == null || frame.image == null) {
            return null;
        }
        if (frame.opaque instanceof IplImage) {
            return (IplImage) frame.opaque;
        }
        if (!isEqual(frame, this.img)) {
            int depth = getIplImageDepth(frame.imageDepth);
            if (depth >= 0) {
                iplImage = IplImage.createHeader(frame.imageWidth, frame.imageHeight, depth, frame.imageChannels).imageData(new BytePointer(new Pointer(frame.image[0].position(0)))).widthStep((frame.imageStride * Math.abs(frame.imageDepth)) / 8).imageSize((frame.image[0].capacity() * Math.abs(frame.imageDepth)) / 8);
            }
            this.img = iplImage;
        }
        return this.img;
    }

    public Frame convert(IplImage img) {
        if (img == null) {
            return null;
        }
        if (!isEqual(this.frame, img)) {
            this.frame = new Frame();
            this.frame.imageWidth = img.width();
            this.frame.imageHeight = img.height();
            this.frame.imageDepth = getFrameDepth(img.depth());
            this.frame.imageChannels = img.nChannels();
            this.frame.imageStride = (img.widthStep() * 8) / Math.abs(this.frame.imageDepth);
            Buffer[] bufferArr = new Buffer[]{img.createBuffer()};
            this.frame.image = bufferArr;
            this.frame.opaque = img;
        }
        return this.frame;
    }

    public static int getMatDepth(int depth) {
        switch (depth) {
            case -32:
                return 4;
            case -16:
                return 3;
            case -8:
                return 1;
            case 8:
                return 0;
            case 16:
                return 2;
            case 32:
                return 5;
            case 64:
                return 6;
            default:
                return -1;
        }
    }

    static boolean isEqual(Frame frame, Mat mat) {
        if (mat != null && frame != null && frame.image != null && frame.image.length > 0 && frame.imageWidth == mat.cols() && frame.imageHeight == mat.rows() && frame.imageChannels == mat.channels() && getMatDepth(frame.imageDepth) == mat.depth() && new Pointer(frame.image[0]).address() == mat.data().address() && (frame.imageStride * Math.abs(frame.imageDepth)) / 8 == ((int) mat.step())) {
            return true;
        }
        return false;
    }

    public Mat convertToMat(Frame frame) {
        Mat mat = null;
        if (frame == null || frame.image == null) {
            return null;
        }
        if (frame.opaque instanceof Mat) {
            return (Mat) frame.opaque;
        }
        if (!isEqual(frame, this.mat)) {
            int depth = getMatDepth(frame.imageDepth);
            if (depth >= 0) {
                mat = new Mat(frame.imageHeight, frame.imageWidth, opencv_core.CV_MAKETYPE(depth, frame.imageChannels), new Pointer(frame.image[0].position(0)), (long) ((frame.imageStride * Math.abs(frame.imageDepth)) / 8));
            }
            this.mat = mat;
        }
        return this.mat;
    }

    public Frame convert(Mat mat) {
        if (mat == null) {
            return null;
        }
        if (!isEqual(this.frame, mat)) {
            this.frame = new Frame();
            this.frame.imageWidth = mat.cols();
            this.frame.imageHeight = mat.rows();
            this.frame.imageDepth = getFrameDepth(mat.depth());
            this.frame.imageChannels = mat.channels();
            this.frame.imageStride = (((int) mat.step()) * 8) / Math.abs(this.frame.imageDepth);
            Buffer[] bufferArr = new Buffer[]{mat.createBuffer()};
            this.frame.image = bufferArr;
            this.frame.opaque = mat;
        }
        return this.frame;
    }
}
