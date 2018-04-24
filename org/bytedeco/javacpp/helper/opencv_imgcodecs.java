package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;

public class opencv_imgcodecs extends org.bytedeco.javacpp.presets.opencv_imgcodecs {
    public static IplImage cvLoadImageBGRA(String filename) {
        IplImage imageBGR = org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage(filename, 1);
        if (imageBGR == null) {
            return null;
        }
        IplImage imageBGRA = opencv_core.cvCreateImage(opencv_core.cvGetSize(imageBGR), imageBGR.depth(), 4);
        opencv_imgproc.cvCvtColor(imageBGR, imageBGRA, 0);
        opencv_core.cvReleaseImage(imageBGR);
        return imageBGRA;
    }

    public static IplImage cvLoadImageRGBA(String filename) {
        IplImage imageBGR = org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage(filename, 1);
        if (imageBGR == null) {
            return null;
        }
        IplImage imageRGBA = opencv_core.cvCreateImage(opencv_core.cvGetSize(imageBGR), imageBGR.depth(), 4);
        opencv_imgproc.cvCvtColor(imageBGR, imageRGBA, 2);
        opencv_core.cvReleaseImage(imageBGR);
        return imageRGBA;
    }
}
