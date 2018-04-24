package org.bytedeco.javacv;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avcodec.AVPacket;
import org.bytedeco.javacpp.avcodec.AVPicture;
import org.bytedeco.javacpp.avfilter;
import org.bytedeco.javacpp.avfilter.AVFilter;
import org.bytedeco.javacpp.avfilter.AVFilterContext;
import org.bytedeco.javacpp.avfilter.AVFilterGraph;
import org.bytedeco.javacpp.avfilter.AVFilterInOut;
import org.bytedeco.javacpp.avformat;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.avutil$AVRational;
import org.bytedeco.javacpp.avutil.AVFrame;
import org.bytedeco.javacpp.postproc;
import org.bytedeco.javacpp.swresample;
import org.bytedeco.javacpp.swscale;
import org.bytedeco.javacv.FrameFilter.Exception;

public class FFmpegFrameFilter extends FrameFilter {
    private static Exception loadingException = null;
    AVFilterContext buffersink_ctx;
    AVFilterContext buffersrc_ctx;
    AVFrame filt_frame;
    AVFilterGraph filter_graph;
    Frame frame;
    Buffer[] image_buf;
    AVFrame image_frame;
    BytePointer[] image_ptr;
    AVPacket packet;

    static {
        try {
            tryLoad();
        } catch (Exception e) {
        }
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            Loader.load(avutil.class);
            Loader.load(avcodec.class);
            Loader.load(avformat.class);
            Loader.load(postproc.class);
            Loader.load(swresample.class);
            Loader.load(swscale.class);
            Loader.load(avfilter.class);
            avformat.av_register_all();
            avfilter.avfilter_register_all();
        } catch (Throwable t) {
            if (t instanceof Exception) {
                loadingException = (Exception) t;
            } else {
                loadingException = new Exception("Failed to load " + FFmpegFrameRecorder.class, t);
            }
        }
    }

    public FFmpegFrameFilter(String filters, int imageWidth, int imageHeight) {
        this.filters = filters;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.pixelFormat = 3;
        this.frameRate = 30.0d;
        this.aspectRatio = 0.0d;
    }

    public void release() throws Exception {
        synchronized (avfilter.class) {
            releaseUnsafe();
        }
    }

    void releaseUnsafe() throws Exception {
        if (this.filter_graph != null) {
            avfilter.avfilter_graph_free(this.filter_graph);
            this.buffersink_ctx = null;
            this.buffersrc_ctx = null;
            this.filter_graph = null;
        }
        if (this.image_frame != null) {
            avutil.av_frame_free(this.image_frame);
            this.image_frame = null;
        }
        if (this.filt_frame != null) {
            avutil.av_frame_free(this.filt_frame);
            this.filt_frame = null;
        }
        this.frame = null;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public void start() throws Exception {
        synchronized (avfilter.class) {
            startUnsafe();
        }
    }

    void startUnsafe() throws Exception {
        this.image_frame = avutil.av_frame_alloc();
        this.filt_frame = avutil.av_frame_alloc();
        this.image_ptr = new BytePointer[]{null};
        this.image_buf = new Buffer[]{null};
        this.frame = new Frame();
        if (this.image_frame == null || this.filt_frame == null) {
            throw new Exception("Could not allocate frame");
        }
        AVFilter buffersrc = avfilter.avfilter_get_by_name("buffer");
        AVFilter buffersink = avfilter.avfilter_get_by_name("buffersink");
        AVFilterInOut outputs = avfilter.avfilter_inout_alloc();
        AVFilterInOut inputs = avfilter.avfilter_inout_alloc();
        avutil$AVRational time_base = avutil.av_inv_q(avutil.av_d2q(this.frameRate, 1001000));
        int[] pix_fmts = new int[]{this.pixelFormat, -1};
        try {
            this.filter_graph = avfilter.avfilter_graph_alloc();
            if (outputs == null || inputs == null || this.filter_graph == null) {
                throw new Exception("Could not allocate filter graph: Out of memory?");
            }
            avutil$AVRational r = avutil.av_d2q(this.aspectRatio > 0.0d ? this.aspectRatio : 1.0d, 255);
            String args = String.format("video_size=%dx%d:pix_fmt=%d:time_base=%d/%d:pixel_aspect=%d/%d", new Object[]{Integer.valueOf(this.imageWidth), Integer.valueOf(this.imageHeight), Integer.valueOf(this.pixelFormat), Integer.valueOf(time_base.num()), Integer.valueOf(time_base.den()), Integer.valueOf(r.num()), Integer.valueOf(r.den())});
            AVFilterContext aVFilterContext = new AVFilterContext();
            this.buffersrc_ctx = aVFilterContext;
            if (avfilter.avfilter_graph_create_filter(aVFilterContext, buffersrc, "in", args, null, this.filter_graph) < 0) {
                throw new Exception("avfilter_graph_create_filter(): Cannot create buffer source.");
            }
            AVFilterContext aVFilterContext2 = new AVFilterContext();
            this.buffersink_ctx = aVFilterContext2;
            if (avfilter.avfilter_graph_create_filter(aVFilterContext2, buffersink, "out", null, null, this.filter_graph) < 0) {
                throw new Exception("avfilter_graph_create_filter(): Cannot create buffer sink.");
            }
            outputs.name(avutil.av_strdup(new BytePointer("in")));
            outputs.filter_ctx(this.buffersrc_ctx);
            outputs.pad_idx(0);
            outputs.next(null);
            inputs.name(avutil.av_strdup(new BytePointer("out")));
            inputs.filter_ctx(this.buffersink_ctx);
            inputs.pad_idx(0);
            inputs.next(null);
            if (avfilter.avfilter_graph_parse_ptr(this.filter_graph, this.filters, inputs, outputs, null) < 0) {
                throw new Exception("avfilter_graph_parse_ptr()");
            } else if (avfilter.avfilter_graph_config(this.filter_graph, null) < 0) {
                throw new Exception("avfilter_graph_config()");
            }
        } finally {
            avfilter.avfilter_inout_free(inputs);
            avfilter.avfilter_inout_free(outputs);
        }
    }

    public void stop() throws Exception {
        release();
    }

    public void push(Frame frame) throws Exception {
        push(frame, -1);
    }

    public void push(Frame frame, int pixelFormat) throws Exception {
        if (frame != null && frame.image != null) {
            pushImage(frame.imageWidth, frame.imageHeight, frame.imageDepth, frame.imageChannels, frame.imageStride, pixelFormat, frame.image);
        } else if (frame == null || frame.samples == null) {
            avfilter.av_buffersrc_add_frame_flags(this.buffersrc_ctx, null, 0);
        }
    }

    public void pushImage(int width, int height, int depth, int channels, int stride, int pixelFormat, Buffer... image) throws Exception {
        BytePointer data;
        int step = (Math.abs(depth) * stride) / 8;
        if (image[0] instanceof ByteBuffer) {
            data = new BytePointer((ByteBuffer) image[0].position(0));
        } else {
            data = new BytePointer(new Pointer(image[0].position(0)));
        }
        if (pixelFormat == -1) {
            if ((depth == 8 || depth == -8) && channels == 3) {
                pixelFormat = 3;
            } else if ((depth == 8 || depth == -8) && channels == 1) {
                pixelFormat = 8;
            } else if ((depth == 16 || depth == -16) && channels == 1) {
                pixelFormat = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN) ? 31 : 32;
            } else if ((depth == 8 || depth == -8) && channels == 4) {
                pixelFormat = 28;
            } else if ((depth == 8 || depth == -8) && channels == 2) {
                pixelFormat = 26;
                step = width;
            } else {
                throw new Exception("Could not guess pixel format of image: depth=" + depth + ", channels=" + channels);
            }
        }
        avcodec.avpicture_fill(new AVPicture(this.image_frame), data, pixelFormat, width, height);
        this.image_frame.linesize(0, step);
        this.image_frame.format(pixelFormat);
        this.image_frame.width(width);
        this.image_frame.height(height);
        if (avfilter.av_buffersrc_add_frame_flags(this.buffersrc_ctx, this.image_frame, 8) < 0) {
            throw new Exception("av_buffersrc_add_frame_flags(): Error while feeding the filtergraph.");
        }
    }

    public Frame pull() throws Exception {
        avutil.av_frame_unref(this.filt_frame);
        int ret = avfilter.av_buffersink_get_frame(this.buffersink_ctx, this.filt_frame);
        if (ret == -11 || ret == avutil.AVERROR_EOF) {
            return null;
        }
        if (ret < 0) {
            throw new Exception("av_buffersink_get_frame(): Error occurred: " + avutil.av_make_error_string(new BytePointer(256), 256, ret).getString());
        }
        this.frame.imageWidth = this.filt_frame.width();
        this.frame.imageHeight = this.filt_frame.height();
        this.frame.imageDepth = 8;
        if (this.filt_frame.data(1) == null) {
            this.frame.imageStride = this.filt_frame.linesize(0);
            BytePointer ptr = this.filt_frame.data(0);
            if (!(ptr == null || ptr.equals(this.image_ptr[0]))) {
                this.image_ptr[0] = ptr.capacity((long) (this.frame.imageHeight * this.frame.imageStride));
                this.image_buf[0] = ptr.asBuffer();
            }
            this.frame.image = this.image_buf;
            this.frame.image[0].position(0).limit(this.frame.imageHeight * this.frame.imageStride);
            this.frame.imageChannels = this.frame.imageStride / this.frame.imageWidth;
        } else {
            this.frame.imageStride = this.frame.imageWidth;
            int size = avcodec.avpicture_get_size(this.filt_frame.format(), this.frame.imageWidth, this.frame.imageHeight);
            if (this.image_buf[0] == null || this.image_buf[0].capacity() < size) {
                this.image_buf[0] = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
            }
            this.frame.image = this.image_buf;
            this.frame.image[0].position(0).limit(size);
            this.frame.imageChannels = (((this.frame.imageWidth * this.frame.imageHeight) + size) - 1) / (this.frame.imageWidth * this.frame.imageHeight);
            ret = avcodec.avpicture_layout(new AVPicture(this.filt_frame), this.filt_frame.format(), this.frame.imageWidth, this.frame.imageHeight, (ByteBuffer) this.frame.image[0].position(0), this.frame.image[0].capacity());
        }
        return this.frame;
    }
}
