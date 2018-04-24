package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(helper = "org.bytedeco.javacpp.helper.opencv_objdetect", inherit = {opencv_highgui.class, opencv_ml.class}, target = "org.bytedeco.javacpp.opencv_objdetect", value = {@Platform(include = {"<opencv2/objdetect/objdetect_c.h>", "<opencv2/objdetect.hpp>", "<opencv2/objdetect/detection_based_tracker.hpp>"}, link = {"opencv_objdetect@.3.1"}), @Platform(link = {"opencv_objdetect310"}, value = {"windows"})})
public class opencv_objdetect implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("CvHaarClassifierCascade").base("AbstractCvHaarClassifierCascade"));
    }
}
