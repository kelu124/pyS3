package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = {opencv_imgcodecs.class}, target = "org.bytedeco.javacpp.opencv_videoio", value = {@Platform(include = {"<opencv2/videoio/videoio_c.h>", "<opencv2/videoio.hpp>"}, link = {"opencv_videoio@.3.1"}), @Platform(preload = {"native_camera_r2.2.0", "native_camera_r2.3.3", "native_camera_r3.0.1", "native_camera_r4.0.0", "native_camera_r4.0.3", "native_camera_r4.1.1", "native_camera_r4.2.0", "native_camera_r4.3.0", "native_camera_r4.4.0"}, value = {"android"}), @Platform(link = {"opencv_videoio310"}, preload = {"opencv_ffmpeg310", "opencv_ffmpeg310_64"}, value = {"windows"})})
public class opencv_videoio implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("CV_FOURCC_DEFAULT").cppTypes("int")).put(new Info("cvCaptureFromFile", "cvCaptureFromAVI").cppTypes("CvCapture*", "const char*")).put(new Info("cvCaptureFromCAM").cppTypes("CvCapture*", "int")).put(new Info("cvCreateAVIWriter").cppTypes("CvVideoWriter*", "const char*", "int", "double", "CvSize", "int")).put(new Info("cvWriteToAVI").cppTypes("int", "CvVideoWriter*", "IplImage*"));
    }
}
