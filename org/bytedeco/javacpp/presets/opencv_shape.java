package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = {opencv_video.class}, target = "org.bytedeco.javacpp.opencv_shape", value = {@Platform(include = {"<opencv2/shape.hpp>", "<opencv2/shape/emdL1.hpp>", "<opencv2/shape/shape_transformer.hpp>", "<opencv2/shape/hist_cost.hpp>", "<opencv2/shape/shape_distance.hpp>"}, link = {"opencv_shape@.3.1"}), @Platform(link = {"opencv_shape310"}, value = {"windows"})})
public class opencv_shape implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("cv::ChiHistogramCostExtractor", "cv::EMDL1HistogramCostExtractor").purify());
    }
}
