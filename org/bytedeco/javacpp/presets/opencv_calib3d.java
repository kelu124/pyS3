package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(helper = "org.bytedeco.javacpp.helper.opencv_calib3d", inherit = {opencv_highgui.class, opencv_features2d.class}, target = "org.bytedeco.javacpp.opencv_calib3d", value = {@Platform(include = {"<opencv2/calib3d/calib3d_c.h>", "<opencv2/calib3d.hpp>"}, link = {"opencv_calib3d@.3.1"}), @Platform(link = {"opencv_calib3d310"}, value = {"windows"})})
public class opencv_calib3d implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("CvPOSITObject").base("AbstractCvPOSITObject")).put(new Info("CvStereoBMState").base("AbstractCvStereoBMState")).put(new Info("cv::fisheye::CALIB_USE_INTRINSIC_GUESS").javaNames("FISHEYE_CALIB_USE_INTRINSIC_GUESS")).put(new Info("cv::fisheye::CALIB_RECOMPUTE_EXTRINSIC").javaNames("FISHEYE_CALIB_RECOMPUTE_EXTRINSIC")).put(new Info("cv::fisheye::CALIB_CHECK_COND").javaNames("FISHEYE_CALIB_CHECK_COND")).put(new Info("cv::fisheye::CALIB_FIX_SKEW").javaNames("FISHEYE_CALIB_FIX_SKEW")).put(new Info("cv::fisheye::CALIB_FIX_K1").javaNames("FISHEYE_CALIB_FIX_K1")).put(new Info("cv::fisheye::CALIB_FIX_K2").javaNames("FISHEYE_CALIB_FIX_K2")).put(new Info("cv::fisheye::CALIB_FIX_K3").javaNames("FISHEYE_CALIB_FIX_K3")).put(new Info("cv::fisheye::CALIB_FIX_K4").javaNames("FISHEYE_CALIB_FIX_K4")).put(new Info("cv::fisheye::CALIB_FIX_INTRINSIC").javaNames("FISHEYE_CALIB_FIX_INTRINSIC")).put(new Info("Affine3d").pointerTypes("Mat"));
    }
}
