package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = {opencv_imgproc.class}, target = "org.bytedeco.javacpp.opencv_dnn", value = {@Platform(include = {"<opencv2/dnn.hpp>", "<opencv2/dnn/dict.hpp>", "<opencv2/dnn/blob.hpp>", "<opencv2/dnn/dnn.hpp>", "<opencv2/dnn/layer.hpp>"}, link = {"opencv_dnn@.3.1"}), @Platform(link = {"opencv_dnn310"}, value = {"windows"})})
public class opencv_dnn implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("std::vector<cv::dnn::Blob>").pointerTypes("BlobVector").define()).put(new Info("std::vector<cv::dnn::Blob*>").pointerTypes("BlobPointerVector").define()).put(new Info("cv::dnn::Net::forward(cv::dnn::Net::LayerId, cv::dnn::Net::LayerId)", "cv::dnn::Net::forward(cv::dnn::Net::LayerId*, cv::dnn::Net::LayerId*)", "cv::dnn::Net::forwardOpt(cv::dnn::Net::LayerId)", "cv::dnn::Net::forwardOpt(cv::dnn::Net::LayerId*)", "cv::dnn::Net::setParam(cv::dnn::Net::LayerId, int, cv::dnn::Blob&)", "cv::dnn::readTorchBlob(cv::String&, bool)", "cv::dnn::Blob::fill(cv::InputArray)").skip()).put(new Info("cv::dnn::Layer* (*)(cv::dnn::LayerParams&)").annotations("@Convention(value=\"\", extern=\"C++\")"));
    }
}
