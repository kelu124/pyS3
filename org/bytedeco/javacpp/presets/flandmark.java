package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = {opencv_imgproc.class}, target = "org.bytedeco.javacpp.flandmark", value = {@Platform(define = {"FLANDMAR_MSVC_COMPAT"}, include = {"flandmark_detector.h", "liblbp.h"}, link = {"flandmark_static"})})
public class flandmark implements InfoMapper {
    public void map(InfoMap infoMap) {
    }
}
