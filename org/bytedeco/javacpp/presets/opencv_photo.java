package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = {opencv_imgproc.class}, target = "org.bytedeco.javacpp.opencv_photo", value = {@Platform(include = {"<opencv2/photo/photo_c.h>", "<opencv2/photo.hpp>", "<opencv2/photo/cuda.hpp>"}, link = {"opencv_photo@.3.1"}, preload = {"opencv_cuda@.3.1"}), @Platform(link = {"opencv_photo310"}, preload = {"opencv_cuda310"}, value = {"windows"})})
public class opencv_photo implements InfoMapper {
    public void map(InfoMap infoMap) {
    }
}
