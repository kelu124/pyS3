package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(target = "org.bytedeco.javacpp.avutil", value = {@Platform(cinclude = {"<libavutil/avutil.h>", "<libavutil/error.h>", "<libavutil/mem.h>", "<libavutil/mathematics.h>", "<libavutil/rational.h>", "<libavutil/log.h>", "<libavutil/buffer.h>", "<libavutil/pixfmt.h>", "<libavutil/frame.h>", "<libavutil/samplefmt.h>", "<libavutil/channel_layout.h>", "<libavutil/cpu.h>", "<libavutil/dict.h>", "<libavutil/opt.h>", "<libavutil/pixdesc.h>", "<libavutil/imgutils.h>", "<libavutil/downmix_info.h>", "<libavutil/stereo3d.h>", "<libavutil/ffversion.h>", "<libavutil/motion_vector.h>", "<libavutil/fifo.h>", "<libavutil/audio_fifo.h>", "log_callback.h"}, compiler = {"default", "nodeprecated"}, define = {"__STDC_CONSTANT_MACROS"}, includepath = {"/usr/local/include/ffmpeg/", "/opt/local/include/ffmpeg/", "/usr/include/ffmpeg/"}, link = {"avutil@.55"}), @Platform(includepath = {"C:/MinGW/local/include/ffmpeg/", "C:/MinGW/include/ffmpeg/"}, preload = {"avutil-55"}, value = {"windows"})})
public class avutil implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("AV_NOPTS_VALUE").cppTypes("int64_t").translate(false)).put(new Info("NAN", "INFINITY").cppTypes("double")).put(new Info("AV_TIME_BASE_Q", "PixelFormat", "CodecID").cppTypes(new String[0])).put(new Info("av_const").annotations("@Const")).put(new Info("FF_CONST_AVUTIL55").annotations(new String[0])).put(new Info("av_malloc_attrib", "av_alloc_size", "av_always_inline", "av_warn_unused_result").cppTypes(new String[0]).annotations(new String[0])).put(new Info("attribute_deprecated").annotations("@Deprecated")).put(new Info("AVPanScan", "AVCodecContext").cast().pointerTypes("Pointer")).put(new Info("FF_API_VAAPI").define()).put(new Info("AV_PIX_FMT_ABI_GIT_MASTER", "AV_HAVE_INCOMPATIBLE_LIBAV_ABI", "!FF_API_XVMC", "FF_API_GET_BITS_PER_SAMPLE_FMT", "FF_API_FIND_OPT").define(false)).put(new Info("AV_PIX_FMT_Y400A", "ff_check_pixfmt_descriptors").skip()).put(new Info("AV_CH_LAYOUT_HEXADECAGONAL").translate().cppTypes("long")).put(new Info("int (*)(void*, void*, int)").pointerTypes("Int_func_Pointer_Pointer_int"));
    }
}
