package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.avutil.AVBufferRef;
import org.bytedeco.javacpp.avutil.AVClass;
import org.bytedeco.javacpp.avutil.AVDictionary;
import org.bytedeco.javacpp.avutil.AVFrame;
import org.bytedeco.javacpp.avutil.AVRational;

public class avfilter extends org.bytedeco.javacpp.presets.avfilter {
    public static final int AVFILTER_AUTO_CONVERT_ALL = 0;
    public static final int AVFILTER_AUTO_CONVERT_NONE = -1;
    public static final int AVFILTER_CMD_FLAG_FAST = 2;
    public static final int AVFILTER_CMD_FLAG_ONE = 1;
    public static final int AVFILTER_FLAG_DYNAMIC_INPUTS = 1;
    public static final int AVFILTER_FLAG_DYNAMIC_OUTPUTS = 2;
    public static final int AVFILTER_FLAG_SLICE_THREADS = 4;
    public static final int AVFILTER_FLAG_SUPPORT_TIMELINE = 196608;
    public static final int AVFILTER_FLAG_SUPPORT_TIMELINE_GENERIC = 65536;
    public static final int AVFILTER_FLAG_SUPPORT_TIMELINE_INTERNAL = 131072;
    public static final int AVFILTER_THREAD_SLICE = 1;
    public static final int AV_BUFFERSINK_FLAG_NO_REQUEST = 2;
    public static final int AV_BUFFERSINK_FLAG_PEEK = 1;
    public static final int AV_BUFFERSRC_FLAG_KEEP_REF = 8;
    public static final int AV_BUFFERSRC_FLAG_NO_CHECK_FORMAT = 1;
    public static final int AV_BUFFERSRC_FLAG_PUSH = 4;

    public static class AVABufferSinkParams extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int all_channel_counts();

        public native AVABufferSinkParams all_channel_counts(int i);

        @MemberGetter
        @Const
        public native IntPointer channel_counts();

        @MemberGetter
        @Cast({"const int64_t*"})
        public native LongPointer channel_layouts();

        @MemberGetter
        @Cast({"const AVSampleFormat*"})
        public native IntPointer sample_fmts();

        public native IntPointer sample_rates();

        public native AVABufferSinkParams sample_rates(IntPointer intPointer);

        static {
            Loader.load();
        }

        public AVABufferSinkParams() {
            super((Pointer) null);
            allocate();
        }

        public AVABufferSinkParams(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVABufferSinkParams(Pointer p) {
            super(p);
        }

        public AVABufferSinkParams position(long position) {
            return (AVABufferSinkParams) super.position(position);
        }
    }

    public static class AVBufferSinkParams extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @MemberGetter
        @Cast({"const AVPixelFormat*"})
        public native IntPointer pixel_fmts();

        static {
            Loader.load();
        }

        public AVBufferSinkParams() {
            super((Pointer) null);
            allocate();
        }

        public AVBufferSinkParams(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVBufferSinkParams(Pointer p) {
            super(p);
        }

        public AVBufferSinkParams position(long position) {
            return (AVBufferSinkParams) super.position(position);
        }
    }

    public static class AVBufferSrcParameters extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"uint64_t"})
        public native long channel_layout();

        public native AVBufferSrcParameters channel_layout(long j);

        public native int format();

        public native AVBufferSrcParameters format(int i);

        public native AVBufferSrcParameters frame_rate(AVRational aVRational);

        @ByRef
        public native AVRational frame_rate();

        public native int height();

        public native AVBufferSrcParameters height(int i);

        public native AVBufferSrcParameters hw_frames_ctx(AVBufferRef aVBufferRef);

        public native AVBufferRef hw_frames_ctx();

        public native AVBufferSrcParameters sample_aspect_ratio(AVRational aVRational);

        @ByRef
        public native AVRational sample_aspect_ratio();

        public native int sample_rate();

        public native AVBufferSrcParameters sample_rate(int i);

        public native AVBufferSrcParameters time_base(AVRational aVRational);

        @ByRef
        public native AVRational time_base();

        public native int width();

        public native AVBufferSrcParameters width(int i);

        static {
            Loader.load();
        }

        public AVBufferSrcParameters() {
            super((Pointer) null);
            allocate();
        }

        public AVBufferSrcParameters(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVBufferSrcParameters(Pointer p) {
            super(p);
        }

        public AVBufferSrcParameters position(long position) {
            return (AVBufferSrcParameters) super.position(position);
        }
    }

    public static class AVFilter extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer description();

        public native int flags();

        public native AVFilter flags(int i);

        public native Init_AVFilterContext init();

        public native AVFilter init(Init_AVFilterContext init_AVFilterContext);

        public native Init_dict_AVFilterContext_PointerPointer init_dict();

        public native AVFilter init_dict(Init_dict_AVFilterContext_PointerPointer init_dict_AVFilterContext_PointerPointer);

        public native Init_opaque_AVFilterContext_Pointer init_opaque();

        public native AVFilter init_opaque(Init_opaque_AVFilterContext_Pointer init_opaque_AVFilterContext_Pointer);

        @MemberGetter
        @Const
        public native AVFilterPad inputs();

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer name();

        public native AVFilter next();

        public native AVFilter next(AVFilter aVFilter);

        @MemberGetter
        @Const
        public native AVFilterPad outputs();

        @MemberGetter
        @Const
        public native AVClass priv_class();

        public native int priv_size();

        public native AVFilter priv_size(int i);

        public native Process_command_AVFilterContext_BytePointer_BytePointer_BytePointer_int_int process_command();

        public native AVFilter process_command(Process_command_AVFilterContext_BytePointer_BytePointer_BytePointer_int_int process_command_AVFilterContext_BytePointer_BytePointer_BytePointer_int_int);

        public native Query_formats_AVFilterContext query_formats();

        public native AVFilter query_formats(Query_formats_AVFilterContext query_formats_AVFilterContext);

        public native Uninit_AVFilterContext uninit();

        public native AVFilter uninit(Uninit_AVFilterContext uninit_AVFilterContext);

        static {
            Loader.load();
        }

        public AVFilter() {
            super((Pointer) null);
            allocate();
        }

        public AVFilter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVFilter(Pointer p) {
            super(p);
        }

        public AVFilter position(long position) {
            return (AVFilter) super.position(position);
        }
    }

    public static class AVFilterContext extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @MemberGetter
        @Const
        public native AVClass av_class();

        @Cast({"AVFilterCommand*"})
        public native Pointer command_queue();

        public native AVFilterContext command_queue(Pointer pointer);

        public native Pointer enable();

        public native AVFilterContext enable(Pointer pointer);

        @Cast({"char*"})
        public native BytePointer enable_str();

        public native AVFilterContext enable_str(BytePointer bytePointer);

        @MemberGetter
        @Const
        public native AVFilter filter();

        public native AVFilterContext graph(AVFilterGraph aVFilterGraph);

        public native AVFilterGraph graph();

        public native AVFilterContext hw_device_ctx(AVBufferRef aVBufferRef);

        public native AVBufferRef hw_device_ctx();

        public native AVFilterContext input_pads(AVFilterPad aVFilterPad);

        public native AVFilterPad input_pads();

        @MemberGetter
        @Cast({"AVFilterLink**"})
        public native PointerPointer inputs();

        public native AVFilterContext inputs(int i, AVFilterLink aVFilterLink);

        public native AVFilterLink inputs(int i);

        public native AVFilterContext internal(AVFilterInternal aVFilterInternal);

        public native AVFilterInternal internal();

        public native int is_disabled();

        public native AVFilterContext is_disabled(int i);

        @Cast({"char*"})
        public native BytePointer name();

        public native AVFilterContext name(BytePointer bytePointer);

        @Cast({"unsigned"})
        public native int nb_inputs();

        public native AVFilterContext nb_inputs(int i);

        @Cast({"unsigned"})
        public native int nb_outputs();

        public native AVFilterContext nb_outputs(int i);

        public native int nb_threads();

        public native AVFilterContext nb_threads(int i);

        public native AVFilterContext output_pads(AVFilterPad aVFilterPad);

        public native AVFilterPad output_pads();

        @MemberGetter
        @Cast({"AVFilterLink**"})
        public native PointerPointer outputs();

        public native AVFilterContext outputs(int i, AVFilterLink aVFilterLink);

        public native AVFilterLink outputs(int i);

        public native Pointer priv();

        public native AVFilterContext priv(Pointer pointer);

        public native int thread_type();

        public native AVFilterContext thread_type(int i);

        public native DoublePointer var_values();

        public native AVFilterContext var_values(DoublePointer doublePointer);

        static {
            Loader.load();
        }

        public AVFilterContext() {
            super((Pointer) null);
            allocate();
        }

        public AVFilterContext(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVFilterContext(Pointer p) {
            super(p);
        }

        public AVFilterContext position(long position) {
            return (AVFilterContext) super.position(position);
        }
    }

    @Opaque
    public static class AVFilterFormats extends Pointer {
        public AVFilterFormats() {
            super((Pointer) null);
        }

        public AVFilterFormats(Pointer p) {
            super(p);
        }
    }

    public static class AVFilterGraph extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"char*"})
        public native BytePointer aresample_swr_opts();

        public native AVFilterGraph aresample_swr_opts(BytePointer bytePointer);

        @MemberGetter
        @Const
        public native AVClass av_class();

        @Cast({"unsigned"})
        public native int disable_auto_convert();

        public native AVFilterGraph disable_auto_convert(int i);

        public native AVFilterGraph execute(avfilter_execute_func org_bytedeco_javacpp_avfilter_avfilter_execute_func);

        public native avfilter_execute_func execute();

        @MemberGetter
        @Cast({"AVFilterContext**"})
        public native PointerPointer filters();

        public native AVFilterContext filters(int i);

        public native AVFilterGraph filters(int i, AVFilterContext aVFilterContext);

        public native AVFilterGraph internal(AVFilterGraphInternal aVFilterGraphInternal);

        public native AVFilterGraphInternal internal();

        @Cast({"unsigned"})
        public native int nb_filters();

        public native AVFilterGraph nb_filters(int i);

        public native int nb_threads();

        public native AVFilterGraph nb_threads(int i);

        public native Pointer opaque();

        public native AVFilterGraph opaque(Pointer pointer);

        @Cast({"char*"})
        public native BytePointer resample_lavr_opts();

        public native AVFilterGraph resample_lavr_opts(BytePointer bytePointer);

        @Cast({"char*"})
        public native BytePointer scale_sws_opts();

        public native AVFilterGraph scale_sws_opts(BytePointer bytePointer);

        @MemberGetter
        @Cast({"AVFilterLink**"})
        public native PointerPointer sink_links();

        public native AVFilterGraph sink_links(int i, AVFilterLink aVFilterLink);

        public native AVFilterLink sink_links(int i);

        public native int sink_links_count();

        public native AVFilterGraph sink_links_count(int i);

        public native int thread_type();

        public native AVFilterGraph thread_type(int i);

        static {
            Loader.load();
        }

        public AVFilterGraph() {
            super((Pointer) null);
            allocate();
        }

        public AVFilterGraph(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVFilterGraph(Pointer p) {
            super(p);
        }

        public AVFilterGraph position(long position) {
            return (AVFilterGraph) super.position(position);
        }
    }

    @Opaque
    public static class AVFilterGraphInternal extends Pointer {
        public AVFilterGraphInternal() {
            super((Pointer) null);
        }

        public AVFilterGraphInternal(Pointer p) {
            super(p);
        }
    }

    public static class AVFilterInOut extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native AVFilterContext filter_ctx();

        public native AVFilterInOut filter_ctx(AVFilterContext aVFilterContext);

        @Cast({"char*"})
        public native BytePointer name();

        public native AVFilterInOut name(BytePointer bytePointer);

        public native AVFilterInOut next();

        public native AVFilterInOut next(AVFilterInOut aVFilterInOut);

        public native int pad_idx();

        public native AVFilterInOut pad_idx(int i);

        static {
            Loader.load();
        }

        public AVFilterInOut() {
            super((Pointer) null);
            allocate();
        }

        public AVFilterInOut(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVFilterInOut(Pointer p) {
            super(p);
        }

        public AVFilterInOut position(long position) {
            return (AVFilterInOut) super.position(position);
        }
    }

    @Opaque
    public static class AVFilterInternal extends Pointer {
        public AVFilterInternal() {
            super((Pointer) null);
        }

        public AVFilterInternal(Pointer p) {
            super(p);
        }
    }

    public static class AVFilterLink extends Pointer {
        public static final int AVLINK_INIT = 2;
        public static final int AVLINK_STARTINIT = 1;
        public static final int AVLINK_UNINIT = 0;

        private native void allocate();

        private native void allocateArray(long j);

        public native int age_index();

        public native AVFilterLink age_index(int i);

        @Cast({"uint64_t"})
        public native long channel_layout();

        public native AVFilterLink channel_layout(long j);

        public native int channels();

        public native AVFilterLink channels(int i);

        @Cast({"int64_t"})
        public native long current_pts();

        public native AVFilterLink current_pts(long j);

        @Cast({"int64_t"})
        public native long current_pts_us();

        public native AVFilterLink current_pts_us(long j);

        public native AVFilterContext dst();

        public native AVFilterLink dst(AVFilterContext aVFilterContext);

        public native AVFilterLink dstpad(AVFilterPad aVFilterPad);

        public native AVFilterPad dstpad();

        @Cast({"unsigned"})
        public native int flags();

        public native AVFilterLink flags(int i);

        public native int format();

        public native AVFilterLink format(int i);

        @Cast({"int64_t"})
        public native long frame_count();

        public native AVFilterLink frame_count(long j);

        public native AVFilterLink frame_rate(AVRational aVRational);

        @ByRef
        public native AVRational frame_rate();

        public native int frame_wanted_in();

        public native AVFilterLink frame_wanted_in(int i);

        public native int frame_wanted_out();

        public native AVFilterLink frame_wanted_out(int i);

        public native AVFilterGraph graph();

        public native AVFilterLink graph(AVFilterGraph aVFilterGraph);

        public native int m45h();

        public native AVFilterLink m46h(int i);

        public native AVFilterLink hw_frames_ctx(AVBufferRef aVBufferRef);

        public native AVBufferRef hw_frames_ctx();

        @Cast({"AVFilterChannelLayouts*"})
        public native Pointer in_channel_layouts();

        public native AVFilterLink in_channel_layouts(Pointer pointer);

        public native AVFilterFormats in_formats();

        public native AVFilterLink in_formats(AVFilterFormats aVFilterFormats);

        public native AVFilterFormats in_samplerates();

        public native AVFilterLink in_samplerates(AVFilterFormats aVFilterFormats);

        public native int max_samples();

        public native AVFilterLink max_samples(int i);

        public native int min_samples();

        public native AVFilterLink min_samples(int i);

        @Cast({"AVFilterChannelLayouts*"})
        public native Pointer out_channel_layouts();

        public native AVFilterLink out_channel_layouts(Pointer pointer);

        public native AVFilterFormats out_formats();

        public native AVFilterLink out_formats(AVFilterFormats aVFilterFormats);

        public native AVFilterFormats out_samplerates();

        public native AVFilterLink out_samplerates(AVFilterFormats aVFilterFormats);

        public native AVFilterLink partial_buf(AVFrame aVFrame);

        public native AVFrame partial_buf();

        public native int partial_buf_size();

        public native AVFilterLink partial_buf_size(int i);

        public native int request_samples();

        public native AVFilterLink request_samples(int i);

        public native AVFilterLink sample_aspect_ratio(AVRational aVRational);

        @ByRef
        public native AVRational sample_aspect_ratio();

        public native int sample_rate();

        public native AVFilterLink sample_rate(int i);

        public native AVFilterContext src();

        public native AVFilterLink src(AVFilterContext aVFilterContext);

        public native AVFilterLink srcpad(AVFilterPad aVFilterPad);

        public native AVFilterPad srcpad();

        public native int status();

        public native AVFilterLink status(int i);

        public native AVFilterLink time_base(AVRational aVRational);

        @ByRef
        public native AVRational time_base();

        @Cast({"AVMediaType"})
        public native int type();

        public native AVFilterLink type(int i);

        public native Pointer video_frame_pool();

        public native AVFilterLink video_frame_pool(Pointer pointer);

        public native int m47w();

        public native AVFilterLink m48w(int i);

        static {
            Loader.load();
        }

        public AVFilterLink() {
            super((Pointer) null);
            allocate();
        }

        public AVFilterLink(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVFilterLink(Pointer p) {
            super(p);
        }

        public AVFilterLink position(long position) {
            return (AVFilterLink) super.position(position);
        }
    }

    @Opaque
    public static class AVFilterPad extends Pointer {
        public AVFilterPad() {
            super((Pointer) null);
        }

        public AVFilterPad(Pointer p) {
            super(p);
        }
    }

    @NoException
    public static native AVABufferSinkParams av_abuffersink_params_alloc();

    @NoException
    public static native int av_buffersink_get_frame(AVFilterContext aVFilterContext, AVFrame aVFrame);

    @NoException
    public static native int av_buffersink_get_frame_flags(AVFilterContext aVFilterContext, AVFrame aVFrame, int i);

    @NoException
    @ByVal
    public static native AVRational av_buffersink_get_frame_rate(AVFilterContext aVFilterContext);

    @NoException
    public static native int av_buffersink_get_samples(AVFilterContext aVFilterContext, AVFrame aVFrame, int i);

    @NoException
    public static native AVBufferSinkParams av_buffersink_params_alloc();

    @NoException
    public static native void av_buffersink_set_frame_size(AVFilterContext aVFilterContext, @Cast({"unsigned"}) int i);

    @NoException
    public static native int av_buffersrc_add_frame(AVFilterContext aVFilterContext, AVFrame aVFrame);

    @NoException
    public static native int av_buffersrc_add_frame_flags(AVFilterContext aVFilterContext, AVFrame aVFrame, int i);

    @NoException
    @Cast({"unsigned"})
    public static native int av_buffersrc_get_nb_failed_requests(AVFilterContext aVFilterContext);

    @NoException
    public static native AVBufferSrcParameters av_buffersrc_parameters_alloc();

    @NoException
    public static native int av_buffersrc_parameters_set(AVFilterContext aVFilterContext, AVBufferSrcParameters aVBufferSrcParameters);

    @NoException
    public static native int av_buffersrc_write_frame(AVFilterContext aVFilterContext, @Const AVFrame aVFrame);

    @NoException
    @Deprecated
    @Cast({"AVFilter**"})
    public static native PointerPointer av_filter_next(@Cast({"AVFilter**"}) PointerPointer pointerPointer);

    @NoException
    @ByPtrPtr
    @Deprecated
    public static native AVFilter av_filter_next(@ByPtrPtr AVFilter aVFilter);

    @NoException
    public static native int avfilter_config_links(AVFilterContext aVFilterContext);

    @NoException
    @Cast({"const char*"})
    public static native BytePointer avfilter_configuration();

    @NoException
    public static native void avfilter_free(AVFilterContext aVFilterContext);

    @NoException
    @Const
    public static native AVFilter avfilter_get_by_name(String str);

    @NoException
    @Const
    public static native AVFilter avfilter_get_by_name(@Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    @Const
    public static native AVClass avfilter_get_class();

    @NoException
    @Deprecated
    public static native int avfilter_graph_add_filter(AVFilterGraph aVFilterGraph, AVFilterContext aVFilterContext);

    @NoException
    public static native AVFilterGraph avfilter_graph_alloc();

    @NoException
    public static native AVFilterContext avfilter_graph_alloc_filter(AVFilterGraph aVFilterGraph, @Const AVFilter aVFilter, String str);

    @NoException
    public static native AVFilterContext avfilter_graph_alloc_filter(AVFilterGraph aVFilterGraph, @Const AVFilter aVFilter, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native int avfilter_graph_config(AVFilterGraph aVFilterGraph, Pointer pointer);

    @NoException
    public static native int avfilter_graph_create_filter(@Cast({"AVFilterContext**"}) PointerPointer pointerPointer, @Const AVFilter aVFilter, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, Pointer pointer, AVFilterGraph aVFilterGraph);

    @NoException
    public static native int avfilter_graph_create_filter(@ByPtrPtr AVFilterContext aVFilterContext, @Const AVFilter aVFilter, String str, String str2, Pointer pointer, AVFilterGraph aVFilterGraph);

    @NoException
    public static native int avfilter_graph_create_filter(@ByPtrPtr AVFilterContext aVFilterContext, @Const AVFilter aVFilter, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, Pointer pointer, AVFilterGraph aVFilterGraph);

    @NoException
    @Cast({"char*"})
    public static native ByteBuffer avfilter_graph_dump(AVFilterGraph aVFilterGraph, String str);

    @NoException
    @Cast({"char*"})
    public static native BytePointer avfilter_graph_dump(AVFilterGraph aVFilterGraph, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native void avfilter_graph_free(@Cast({"AVFilterGraph**"}) PointerPointer pointerPointer);

    @NoException
    public static native void avfilter_graph_free(@ByPtrPtr AVFilterGraph aVFilterGraph);

    @NoException
    public static native AVFilterContext avfilter_graph_get_filter(AVFilterGraph aVFilterGraph, String str);

    @NoException
    public static native AVFilterContext avfilter_graph_get_filter(AVFilterGraph aVFilterGraph, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native int avfilter_graph_parse(AVFilterGraph aVFilterGraph, String str, AVFilterInOut aVFilterInOut, AVFilterInOut aVFilterInOut2, Pointer pointer);

    @NoException
    public static native int avfilter_graph_parse(AVFilterGraph aVFilterGraph, @Cast({"const char*"}) BytePointer bytePointer, AVFilterInOut aVFilterInOut, AVFilterInOut aVFilterInOut2, Pointer pointer);

    @NoException
    public static native int avfilter_graph_parse2(AVFilterGraph aVFilterGraph, String str, @ByPtrPtr AVFilterInOut aVFilterInOut, @ByPtrPtr AVFilterInOut aVFilterInOut2);

    @NoException
    public static native int avfilter_graph_parse2(AVFilterGraph aVFilterGraph, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"AVFilterInOut**"}) PointerPointer pointerPointer, @Cast({"AVFilterInOut**"}) PointerPointer pointerPointer2);

    @NoException
    public static native int avfilter_graph_parse2(AVFilterGraph aVFilterGraph, @Cast({"const char*"}) BytePointer bytePointer, @ByPtrPtr AVFilterInOut aVFilterInOut, @ByPtrPtr AVFilterInOut aVFilterInOut2);

    @NoException
    public static native int avfilter_graph_parse_ptr(AVFilterGraph aVFilterGraph, String str, @ByPtrPtr AVFilterInOut aVFilterInOut, @ByPtrPtr AVFilterInOut aVFilterInOut2, Pointer pointer);

    @NoException
    public static native int avfilter_graph_parse_ptr(AVFilterGraph aVFilterGraph, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"AVFilterInOut**"}) PointerPointer pointerPointer, @Cast({"AVFilterInOut**"}) PointerPointer pointerPointer2, Pointer pointer);

    @NoException
    public static native int avfilter_graph_parse_ptr(AVFilterGraph aVFilterGraph, @Cast({"const char*"}) BytePointer bytePointer, @ByPtrPtr AVFilterInOut aVFilterInOut, @ByPtrPtr AVFilterInOut aVFilterInOut2, Pointer pointer);

    @NoException
    public static native int avfilter_graph_queue_command(AVFilterGraph aVFilterGraph, String str, String str2, String str3, int i, double d);

    @NoException
    public static native int avfilter_graph_queue_command(AVFilterGraph aVFilterGraph, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, int i, double d);

    @NoException
    public static native int avfilter_graph_request_oldest(AVFilterGraph aVFilterGraph);

    @NoException
    public static native int avfilter_graph_send_command(AVFilterGraph aVFilterGraph, String str, String str2, String str3, @Cast({"char*"}) ByteBuffer byteBuffer, int i, int i2);

    @NoException
    public static native int avfilter_graph_send_command(AVFilterGraph aVFilterGraph, String str, String str2, String str3, @Cast({"char*"}) BytePointer bytePointer, int i, int i2);

    @NoException
    public static native int avfilter_graph_send_command(AVFilterGraph aVFilterGraph, String str, String str2, String str3, @Cast({"char*"}) byte[] bArr, int i, int i2);

    @NoException
    public static native int avfilter_graph_send_command(AVFilterGraph aVFilterGraph, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, @Cast({"char*"}) ByteBuffer byteBuffer, int i, int i2);

    @NoException
    public static native int avfilter_graph_send_command(AVFilterGraph aVFilterGraph, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, @Cast({"char*"}) BytePointer bytePointer4, int i, int i2);

    @NoException
    public static native int avfilter_graph_send_command(AVFilterGraph aVFilterGraph, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, @Cast({"char*"}) byte[] bArr, int i, int i2);

    @NoException
    public static native void avfilter_graph_set_auto_convert(AVFilterGraph aVFilterGraph, @Cast({"unsigned"}) int i);

    @NoException
    public static native int avfilter_init_dict(AVFilterContext aVFilterContext, @Cast({"AVDictionary**"}) PointerPointer pointerPointer);

    @NoException
    public static native int avfilter_init_dict(AVFilterContext aVFilterContext, @ByPtrPtr AVDictionary aVDictionary);

    @NoException
    @Deprecated
    public static native int avfilter_init_filter(AVFilterContext aVFilterContext, String str, Pointer pointer);

    @NoException
    @Deprecated
    public static native int avfilter_init_filter(AVFilterContext aVFilterContext, @Cast({"const char*"}) BytePointer bytePointer, Pointer pointer);

    @NoException
    public static native int avfilter_init_str(AVFilterContext aVFilterContext, String str);

    @NoException
    public static native int avfilter_init_str(AVFilterContext aVFilterContext, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native AVFilterInOut avfilter_inout_alloc();

    @NoException
    public static native void avfilter_inout_free(@Cast({"AVFilterInOut**"}) PointerPointer pointerPointer);

    @NoException
    public static native void avfilter_inout_free(@ByPtrPtr AVFilterInOut aVFilterInOut);

    @NoException
    public static native int avfilter_insert_filter(AVFilterLink aVFilterLink, AVFilterContext aVFilterContext, @Cast({"unsigned"}) int i, @Cast({"unsigned"}) int i2);

    @NoException
    @Cast({"const char*"})
    public static native BytePointer avfilter_license();

    @NoException
    public static native int avfilter_link(AVFilterContext aVFilterContext, @Cast({"unsigned"}) int i, AVFilterContext aVFilterContext2, @Cast({"unsigned"}) int i2);

    @NoException
    public static native void avfilter_link_free(@Cast({"AVFilterLink**"}) PointerPointer pointerPointer);

    @NoException
    public static native void avfilter_link_free(@ByPtrPtr AVFilterLink aVFilterLink);

    @NoException
    public static native int avfilter_link_get_channels(AVFilterLink aVFilterLink);

    @NoException
    @Deprecated
    public static native void avfilter_link_set_closed(AVFilterLink aVFilterLink, int i);

    @NoException
    @Const
    public static native AVFilter avfilter_next(@Const AVFilter aVFilter);

    @NoException
    @Deprecated
    public static native int avfilter_open(@Cast({"AVFilterContext**"}) PointerPointer pointerPointer, AVFilter aVFilter, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    @Deprecated
    public static native int avfilter_open(@ByPtrPtr AVFilterContext aVFilterContext, AVFilter aVFilter, String str);

    @NoException
    @Deprecated
    public static native int avfilter_open(@ByPtrPtr AVFilterContext aVFilterContext, AVFilter aVFilter, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native int avfilter_pad_count(@Const AVFilterPad aVFilterPad);

    @NoException
    @Cast({"const char*"})
    public static native BytePointer avfilter_pad_get_name(@Const AVFilterPad aVFilterPad, int i);

    @NoException
    @Cast({"AVMediaType"})
    public static native int avfilter_pad_get_type(@Const AVFilterPad aVFilterPad, int i);

    @NoException
    public static native int avfilter_process_command(AVFilterContext aVFilterContext, String str, String str2, @Cast({"char*"}) ByteBuffer byteBuffer, int i, int i2);

    @NoException
    public static native int avfilter_process_command(AVFilterContext aVFilterContext, String str, String str2, @Cast({"char*"}) BytePointer bytePointer, int i, int i2);

    @NoException
    public static native int avfilter_process_command(AVFilterContext aVFilterContext, String str, String str2, @Cast({"char*"}) byte[] bArr, int i, int i2);

    @NoException
    public static native int avfilter_process_command(AVFilterContext aVFilterContext, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"char*"}) ByteBuffer byteBuffer, int i, int i2);

    @NoException
    public static native int avfilter_process_command(AVFilterContext aVFilterContext, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"char*"}) BytePointer bytePointer3, int i, int i2);

    @NoException
    public static native int avfilter_process_command(AVFilterContext aVFilterContext, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"char*"}) byte[] bArr, int i, int i2);

    @NoException
    public static native int avfilter_register(AVFilter aVFilter);

    @NoException
    public static native void avfilter_register_all();

    @NoException
    @Deprecated
    public static native void avfilter_uninit();

    @NoException
    @Cast({"unsigned"})
    public static native int avfilter_version();

    static {
        Loader.load();
    }
}
