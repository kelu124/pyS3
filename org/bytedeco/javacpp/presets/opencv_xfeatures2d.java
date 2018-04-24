package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = {opencv_calib3d.class, opencv_features2d.class, opencv_shape.class}, target = "org.bytedeco.javacpp.opencv_xfeatures2d", value = {@Platform(include = {"<opencv2/xfeatures2d.hpp>", "<opencv2/xfeatures2d/nonfree.hpp>"}, link = {"opencv_xfeatures2d@.3.1"}), @Platform(link = {"opencv_xfeatures2d310"}, value = {"windows"})})
public class opencv_xfeatures2d implements InfoMapper {
    public void map(InfoMap infoMap) {
    }
}
