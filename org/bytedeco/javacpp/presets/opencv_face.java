package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = {opencv_objdetect.class}, target = "org.bytedeco.javacpp.opencv_face", value = {@Platform(include = {"<opencv2/face/predict_collector.hpp>", "<opencv2/face.hpp>", "<opencv2/face/facerec.hpp>"}, link = {"opencv_face@.3.1"}), @Platform(link = {"opencv_face310"}, value = {"windows"})})
public class opencv_face implements InfoMapper {
    public void map(InfoMap infoMap) {
    }
}
