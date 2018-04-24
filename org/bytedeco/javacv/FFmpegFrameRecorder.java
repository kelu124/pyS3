package org.bytedeco.javacv;

import android.support.v4.media.session.PlaybackStateCompat;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.ShortPointer;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avcodec.AVCodec;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avcodec.AVPacket;
import org.bytedeco.javacpp.avcodec.AVPicture;
import org.bytedeco.javacpp.avformat;
import org.bytedeco.javacpp.avformat$Write_packet_Pointer_BytePointer_int;
import org.bytedeco.javacpp.avformat.AVFormatContext;
import org.bytedeco.javacpp.avformat.AVIOContext;
import org.bytedeco.javacpp.avformat.AVOutputFormat;
import org.bytedeco.javacpp.avformat.AVStream;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.avutil$AVRational;
import org.bytedeco.javacpp.avutil.AVDictionary;
import org.bytedeco.javacpp.avutil.AVFrame;
import org.bytedeco.javacpp.swresample;
import org.bytedeco.javacpp.swresample.SwrContext;
import org.bytedeco.javacpp.swscale;
import org.bytedeco.javacpp.swscale.SwsContext;
import org.bytedeco.javacv.FrameRecorder.Exception;

public class FFmpegFrameRecorder extends FrameRecorder {
    static final /* synthetic */ boolean $assertionsDisabled = (!FFmpegFrameRecorder.class.desiredAssertionStatus());
    private static Exception loadingException = null;
    static HashMap<Pointer, OutputStream> outputStreams = new HashMap();
    static WriteCallback writeCallback;
    private AVCodecContext audio_c;
    private AVCodec audio_codec;
    private int audio_input_frame_size;
    private BytePointer audio_outbuf;
    private int audio_outbuf_size;
    private AVPacket audio_pkt;
    private AVStream audio_st;
    private AVIOContext avio;
    private String filename;
    private AVFrame frame;
    private int[] got_audio_packet;
    private int[] got_video_packet;
    private AVFormatContext ifmt_ctx;
    private SwsContext img_convert_ctx;
    private AVFormatContext oc;
    private AVOutputFormat oformat;
    private OutputStream outputStream;
    private AVFrame picture;
    private BytePointer picture_buf;
    private int samples_channels;
    private SwrContext samples_convert_ctx;
    private int samples_format;
    private Pointer[] samples_in;
    private PointerPointer samples_in_ptr;
    private BytePointer[] samples_out;
    private PointerPointer samples_out_ptr;
    private int samples_rate;
    private AVFrame tmp_picture;
    private AVCodecContext video_c;
    private AVCodec video_codec;
    private BytePointer video_outbuf;
    private int video_outbuf_size;
    private AVPacket video_pkt;
    private AVStream video_st;

    static class WriteCallback extends avformat$Write_packet_Pointer_BytePointer_int {
        WriteCallback() {
        }

        public int call(Pointer opaque, BytePointer buf, int buf_size) {
            try {
                byte[] b = new byte[buf_size];
                OutputStream os = (OutputStream) FFmpegFrameRecorder.outputStreams.get(opaque);
                buf.get(b, 0, buf_size);
                os.write(b, 0, buf_size);
                return buf_size;
            } catch (Throwable t) {
                System.err.println("Error on OutputStream.write(): " + t);
                return -1;
            }
        }
    }

    static {
        try {
            tryLoad();
        } catch (Exception e) {
        }
    }

    public static FFmpegFrameRecorder createDefault(File f, int w, int h) throws Exception {
        return new FFmpegFrameRecorder(f, w, h);
    }

    public static FFmpegFrameRecorder createDefault(String f, int w, int h) throws Exception {
        return new FFmpegFrameRecorder(f, w, h);
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            Loader.load(avutil.class);
            Loader.load(swresample.class);
            Loader.load(avcodec.class);
            Loader.load(avformat.class);
            Loader.load(swscale.class);
            avformat.av_register_all();
            avformat.avformat_network_init();
        } catch (Throwable t) {
            if (t instanceof Exception) {
                loadingException = (Exception) t;
            } else {
                loadingException = new Exception("Failed to load " + FFmpegFrameRecorder.class, t);
            }
        }
    }

    public FFmpegFrameRecorder(File file, int audioChannels) {
        this(file, 0, 0, audioChannels);
    }

    public FFmpegFrameRecorder(String filename, int audioChannels) {
        this(filename, 0, 0, audioChannels);
    }

    public FFmpegFrameRecorder(File file, int imageWidth, int imageHeight) {
        this(file, imageWidth, imageHeight, 0);
    }

    public FFmpegFrameRecorder(String filename, int imageWidth, int imageHeight) {
        this(filename, imageWidth, imageHeight, 0);
    }

    public FFmpegFrameRecorder(File file, int imageWidth, int imageHeight, int audioChannels) {
        this(file.getAbsolutePath(), imageWidth, imageHeight, audioChannels);
    }

    public FFmpegFrameRecorder(String filename, int imageWidth, int imageHeight, int audioChannels) {
        this.filename = filename;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.audioChannels = audioChannels;
        this.pixelFormat = -1;
        this.videoCodec = 0;
        this.videoBitrate = 400000;
        this.frameRate = 30.0d;
        this.sampleFormat = -1;
        this.audioCodec = 0;
        this.audioBitrate = 64000;
        this.sampleRate = 44100;
        this.interleaved = true;
        this.video_pkt = new AVPacket();
        this.audio_pkt = new AVPacket();
    }

    public FFmpegFrameRecorder(OutputStream outputStream, int audioChannels) {
        this(outputStream.toString(), audioChannels);
        this.outputStream = outputStream;
    }

    public FFmpegFrameRecorder(OutputStream outputStream, int imageWidth, int imageHeight) {
        this(outputStream.toString(), imageWidth, imageHeight);
        this.outputStream = outputStream;
    }

    public FFmpegFrameRecorder(OutputStream outputStream, int imageWidth, int imageHeight, int audioChannels) {
        this(outputStream.toString(), imageWidth, imageHeight, audioChannels);
        this.outputStream = outputStream;
    }

    public void release() throws Exception {
        synchronized (avcodec.class) {
            releaseUnsafe();
        }
    }

    void releaseUnsafe() throws Exception {
        int i;
        if (this.video_c != null) {
            avcodec.avcodec_close(this.video_c);
            this.video_c = null;
        }
        if (this.audio_c != null) {
            avcodec.avcodec_close(this.audio_c);
            this.audio_c = null;
        }
        if (this.picture_buf != null) {
            avutil.av_free(this.picture_buf);
            this.picture_buf = null;
        }
        if (this.picture != null) {
            avutil.av_frame_free(this.picture);
            this.picture = null;
        }
        if (this.tmp_picture != null) {
            avutil.av_frame_free(this.tmp_picture);
            this.tmp_picture = null;
        }
        if (this.video_outbuf != null) {
            avutil.av_free(this.video_outbuf);
            this.video_outbuf = null;
        }
        if (this.frame != null) {
            avutil.av_frame_free(this.frame);
            this.frame = null;
        }
        if (this.samples_out != null) {
            for (BytePointer position : this.samples_out) {
                avutil.av_free(position.position(0));
            }
            this.samples_out = null;
        }
        if (this.audio_outbuf != null) {
            avutil.av_free(this.audio_outbuf);
            this.audio_outbuf = null;
        }
        if (!(this.video_st == null || this.video_st.metadata() == null)) {
            avutil.av_dict_free(this.video_st.metadata());
            this.video_st.metadata(null);
        }
        if (!(this.audio_st == null || this.audio_st.metadata() == null)) {
            avutil.av_dict_free(this.audio_st.metadata());
            this.audio_st.metadata(null);
        }
        this.video_st = null;
        this.audio_st = null;
        this.filename = null;
        if (!(this.oc == null || this.oc.isNull())) {
            if (this.outputStream == null && (this.oformat.flags() & 1) == 0) {
                avformat.avio_close(this.oc.pb());
            }
            int nb_streams = this.oc.nb_streams();
            for (i = 0; i < nb_streams; i++) {
                avutil.av_free(this.oc.streams(i).codec());
                avutil.av_free(this.oc.streams(i));
            }
            if (this.oc.metadata() != null) {
                avutil.av_dict_free(this.oc.metadata());
                this.oc.metadata(null);
            }
            avutil.av_free(this.oc);
            this.oc = null;
        }
        if (this.img_convert_ctx != null) {
            swscale.sws_freeContext(this.img_convert_ctx);
            this.img_convert_ctx = null;
        }
        if (this.samples_convert_ctx != null) {
            swresample.swr_free(this.samples_convert_ctx);
            this.samples_convert_ctx = null;
        }
        if (this.outputStream != null) {
            try {
                this.outputStream.close();
                this.outputStream = null;
                outputStreams.remove(this.oc);
                if (this.avio != null) {
                    if (this.avio.buffer() != null) {
                        avutil.av_free(this.avio.buffer());
                        this.avio.buffer(null);
                    }
                    avutil.av_free(this.avio);
                    this.avio = null;
                }
            } catch (IOException ex) {
                throw new Exception("Error on OutputStream.close(): ", ex);
            } catch (Throwable th) {
                this.outputStream = null;
                outputStreams.remove(this.oc);
                if (this.avio != null) {
                    if (this.avio.buffer() != null) {
                        avutil.av_free(this.avio.buffer());
                        this.avio.buffer(null);
                    }
                    avutil.av_free(this.avio);
                    this.avio = null;
                }
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public int getFrameNumber() {
        return this.picture == null ? super.getFrameNumber() : (int) this.picture.pts();
    }

    public void setFrameNumber(int frameNumber) {
        if (this.picture == null) {
            super.setFrameNumber(frameNumber);
        } else {
            this.picture.pts((long) frameNumber);
        }
    }

    public long getTimestamp() {
        return Math.round(((double) (((long) getFrameNumber()) * 1000000)) / getFrameRate());
    }

    public void setTimestamp(long timestamp) {
        setFrameNumber((int) Math.round((((double) timestamp) * getFrameRate()) / 1000000.0d));
    }

    public void start(AVFormatContext ifmt_ctx) throws Exception {
        this.ifmt_ctx = ifmt_ctx;
        start();
    }

    public void start() throws Exception {
        synchronized (avcodec.class) {
            startUnsafe();
        }
    }

    void startUnsafe() throws Exception {
        this.picture = null;
        this.tmp_picture = null;
        this.picture_buf = null;
        this.frame = null;
        this.video_outbuf = null;
        this.audio_outbuf = null;
        this.oc = new AVFormatContext(null);
        this.video_c = null;
        this.audio_c = null;
        this.video_st = null;
        this.audio_st = null;
        this.got_video_packet = new int[1];
        this.got_audio_packet = new int[1];
        String format_name = (this.format == null || this.format.length() == 0) ? null : this.format;
        AVOutputFormat av_guess_format = avformat.av_guess_format(format_name, this.filename, null);
        this.oformat = av_guess_format;
        if (av_guess_format == null) {
            int proto = this.filename.indexOf("://");
            if (proto > 0) {
                format_name = this.filename.substring(0, proto);
            }
            av_guess_format = avformat.av_guess_format(format_name, this.filename, null);
            this.oformat = av_guess_format;
            if (av_guess_format == null) {
                throw new Exception("av_guess_format() error: Could not guess output format for \"" + this.filename + "\" and " + this.format + " format.");
            }
        }
        format_name = this.oformat.name().getString();
        if (avformat.avformat_alloc_output_context2(this.oc, null, format_name, this.filename) < 0) {
            throw new Exception("avformat_alloc_context2() error:\tCould not allocate format context");
        }
        AVCodec avcodec_find_encoder_by_name;
        AVStream avformat_new_stream;
        int i;
        AVDictionary aVDictionary;
        int ret;
        AVFrame av_frame_alloc;
        if (this.outputStream != null) {
            if (writeCallback == null) {
                writeCallback = new WriteCallback();
            }
            this.avio = avformat.avio_alloc_context(new BytePointer(avutil.av_malloc(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM)), 4096, 1, this.oc, null, writeCallback, null);
            this.oc.pb(this.avio);
            this.filename = this.outputStream.toString();
            outputStreams.put(this.oc, this.outputStream);
        }
        this.oc.oformat(this.oformat);
        this.oc.filename().putString(this.filename);
        AVStream inpVideoStream = null;
        AVStream inpAudioStream = null;
        if (this.ifmt_ctx != null) {
            for (int idx = 0; idx < this.ifmt_ctx.nb_streams(); idx++) {
                AVStream inputStream = this.ifmt_ctx.streams(idx);
                if (inputStream.codec().codec_type() == 0) {
                    inpVideoStream = inputStream;
                    this.videoCodec = inpVideoStream.codec().codec_id();
                    if (!(((long) inpVideoStream.r_frame_rate().num()) == avutil.AV_NOPTS_VALUE || inpVideoStream.r_frame_rate().den() == 0)) {
                        this.frameRate = (double) (inpVideoStream.r_frame_rate().num() / inpVideoStream.r_frame_rate().den());
                    }
                } else if (inputStream.codec().codec_type() == 1) {
                    inpAudioStream = inputStream;
                    this.audioCodec = inpAudioStream.codec().codec_id();
                }
            }
        }
        if (this.imageWidth > 0 && this.imageHeight > 0) {
            if (this.videoCodec != 0) {
                this.oformat.video_codec(this.videoCodec);
            } else if ("flv".equals(format_name)) {
                this.oformat.video_codec(22);
            } else if ("mp4".equals(format_name)) {
                this.oformat.video_codec(13);
            } else if ("3gp".equals(format_name)) {
                this.oformat.video_codec(5);
            } else if ("avi".equals(format_name)) {
                this.oformat.video_codec(26);
            }
            avcodec_find_encoder_by_name = avcodec.avcodec_find_encoder_by_name(this.videoCodecName);
            this.video_codec = avcodec_find_encoder_by_name;
            if (avcodec_find_encoder_by_name == null) {
                avcodec_find_encoder_by_name = avcodec.avcodec_find_encoder(this.oformat.video_codec());
                this.video_codec = avcodec_find_encoder_by_name;
                if (avcodec_find_encoder_by_name == null) {
                    release();
                    throw new Exception("avcodec_find_encoder() error: Video codec not found.");
                }
            }
            this.oformat.video_codec(this.video_codec.id());
            avutil$AVRational frame_rate = avutil.av_d2q(this.frameRate, 1001000);
            avutil$AVRational supported_framerates = this.video_codec.supported_framerates();
            if (supported_framerates != null) {
                frame_rate = supported_framerates.position((long) avutil.av_find_nearest_q_idx(frame_rate, supported_framerates));
            }
            avformat_new_stream = avformat.avformat_new_stream(this.oc, this.video_codec);
            this.video_st = avformat_new_stream;
            if (avformat_new_stream == null) {
                release();
                throw new Exception("avformat_new_stream() error: Could not allocate video stream.");
            }
            this.video_c = this.video_st.codec();
            if (inpVideoStream != null) {
                if (avcodec.avcodec_copy_context(this.video_st.codec(), inpVideoStream.codec()) < 0) {
                    release();
                    throw new Exception("avcodec_copy_context() error:\tFailed to copy context from input to output stream codec context");
                }
                this.videoBitrate = (int) inpVideoStream.codec().bit_rate();
                this.pixelFormat = inpVideoStream.codec().pix_fmt();
                this.aspectRatio = ((double) (inpVideoStream.codec().sample_aspect_ratio().den() / inpVideoStream.codec().sample_aspect_ratio().den())) * 1.0d;
                this.videoQuality = (double) inpVideoStream.codec().global_quality();
                this.video_c.codec_tag(0);
            }
            this.video_c.codec_id(this.oformat.video_codec());
            this.video_c.codec_type(0);
            this.video_c.bit_rate((long) this.videoBitrate);
            if (this.imageWidth % 2 == 1) {
                int roundedWidth = this.imageWidth + 1;
                this.imageHeight = ((this.imageHeight * roundedWidth) + (this.imageWidth / 2)) / this.imageWidth;
                this.imageWidth = roundedWidth;
            }
            this.video_c.width(this.imageWidth);
            this.video_c.height(this.imageHeight);
            if (this.aspectRatio > 0.0d) {
                avutil$AVRational r = avutil.av_d2q(this.aspectRatio, 255);
                this.video_c.sample_aspect_ratio(r);
                this.video_st.sample_aspect_ratio(r);
            }
            this.video_c.time_base(avutil.av_inv_q(frame_rate));
            this.video_st.time_base(avutil.av_inv_q(frame_rate));
            if (this.gopSize >= 0) {
                this.video_c.gop_size(this.gopSize);
            }
            if (this.videoQuality >= 0.0d) {
                this.video_c.flags(this.video_c.flags() | 2);
                this.video_c.global_quality((int) Math.round(118.0d * this.videoQuality));
            }
            if (this.pixelFormat != -1) {
                this.video_c.pix_fmt(this.pixelFormat);
            } else if (this.video_c.codec_id() == 14 || this.video_c.codec_id() == 62 || this.video_c.codec_id() == 26 || this.video_c.codec_id() == 34) {
                this.video_c.pix_fmt(avutil.AV_PIX_FMT_RGB32);
            } else if (this.video_c.codec_id() == 12) {
                this.video_c.pix_fmt(3);
            } else if (this.video_c.codec_id() == 8 || this.video_c.codec_id() == 9) {
                this.video_c.pix_fmt(12);
            } else {
                this.video_c.pix_fmt(0);
            }
            if (this.video_c.codec_id() == 2) {
                this.video_c.max_b_frames(2);
            } else if (this.video_c.codec_id() == 1) {
                this.video_c.mb_decision(2);
            } else if (this.video_c.codec_id() == 5) {
                if (this.imageWidth <= 128 && this.imageHeight <= 96) {
                    this.video_c.width(128).height(96);
                } else if (this.imageWidth <= 176 && this.imageHeight <= 144) {
                    this.video_c.width(176).height(144);
                } else if (this.imageWidth <= 352 && this.imageHeight <= TIFFConstants.TIFFTAG_FREEOFFSETS) {
                    this.video_c.width(352).height(TIFFConstants.TIFFTAG_FREEOFFSETS);
                } else if (this.imageWidth > 704 || this.imageHeight > 576) {
                    this.video_c.width(1408).height(1152);
                } else {
                    this.video_c.width(704).height(576);
                }
            } else if (this.video_c.codec_id() == 28) {
                this.video_c.profile(578);
            }
            if ((this.oformat.flags() & 64) != 0) {
                this.video_c.flags(this.video_c.flags() | 4194304);
            }
            if ((this.video_codec.capabilities() & 512) != 0) {
                this.video_c.strict_std_compliance(-2);
            }
        }
        if (this.audioChannels > 0 && this.audioBitrate > 0 && this.sampleRate > 0) {
            if (this.audioCodec != 0) {
                this.oformat.audio_codec(this.audioCodec);
            } else if ("flv".equals(format_name) || "mp4".equals(format_name) || "3gp".equals(format_name)) {
                this.oformat.audio_codec(avcodec.AV_CODEC_ID_AAC);
            } else if ("avi".equals(format_name)) {
                this.oformat.audio_codec(65536);
            }
            avcodec_find_encoder_by_name = avcodec.avcodec_find_encoder_by_name(this.audioCodecName);
            this.audio_codec = avcodec_find_encoder_by_name;
            if (avcodec_find_encoder_by_name == null) {
                avcodec_find_encoder_by_name = avcodec.avcodec_find_encoder(this.oformat.audio_codec());
                this.audio_codec = avcodec_find_encoder_by_name;
                if (avcodec_find_encoder_by_name == null) {
                    release();
                    throw new Exception("avcodec_find_encoder() error: Audio codec not found.");
                }
            }
            this.oformat.audio_codec(this.audio_codec.id());
            avformat_new_stream = avformat.avformat_new_stream(this.oc, this.audio_codec);
            this.audio_st = avformat_new_stream;
            if (avformat_new_stream == null) {
                release();
                throw new Exception("avformat_new_stream() error: Could not allocate audio stream.");
            }
            this.audio_c = this.audio_st.codec();
            if (inpAudioStream != null && this.audioChannels > 0) {
                if (avcodec.avcodec_copy_context(this.audio_st.codec(), inpAudioStream.codec()) < 0) {
                    throw new Exception("avcodec_copy_context() error:\tFailed to copy context from input audio to output audio stream codec context\n");
                }
                this.audioBitrate = (int) inpAudioStream.codec().bit_rate();
                this.sampleRate = inpAudioStream.codec().sample_rate();
                this.audioChannels = inpAudioStream.codec().channels();
                this.sampleFormat = inpAudioStream.codec().sample_fmt();
                this.audioQuality = (double) inpAudioStream.codec().global_quality();
                this.audio_c.codec_tag(0);
                this.audio_st.pts(inpAudioStream.pts());
                this.audio_st.duration(inpAudioStream.duration());
                this.audio_st.time_base().num(inpAudioStream.time_base().num());
                this.audio_st.time_base().den(inpAudioStream.time_base().den());
            }
            this.audio_c.codec_id(this.oformat.audio_codec());
            this.audio_c.codec_type(1);
            this.audio_c.bit_rate((long) this.audioBitrate);
            this.audio_c.sample_rate(this.sampleRate);
            this.audio_c.channels(this.audioChannels);
            this.audio_c.channel_layout(avutil.av_get_default_channel_layout(this.audioChannels));
            if (this.sampleFormat != -1) {
                this.audio_c.sample_fmt(this.sampleFormat);
            } else {
                this.audio_c.sample_fmt(8);
                IntPointer formats = this.audio_c.codec().sample_fmts();
                for (i = 0; formats.get((long) i) != -1; i++) {
                    if (formats.get((long) i) == 1) {
                        this.audio_c.sample_fmt(1);
                        break;
                    }
                }
            }
            this.audio_c.time_base().num(1).den(this.sampleRate);
            this.audio_st.time_base().num(1).den(this.sampleRate);
            switch (this.audio_c.sample_fmt()) {
                case 0:
                case 5:
                    this.audio_c.bits_per_raw_sample(8);
                    break;
                case 1:
                case 6:
                    this.audio_c.bits_per_raw_sample(16);
                    break;
                case 2:
                case 7:
                    this.audio_c.bits_per_raw_sample(32);
                    break;
                case 3:
                case 8:
                    this.audio_c.bits_per_raw_sample(32);
                    break;
                case 4:
                case 9:
                    this.audio_c.bits_per_raw_sample(64);
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
            if (this.audioQuality >= 0.0d) {
                this.audio_c.flags(this.audio_c.flags() | 2);
                this.audio_c.global_quality((int) Math.round(118.0d * this.audioQuality));
            }
            if ((this.oformat.flags() & 64) != 0) {
                this.audio_c.flags(this.audio_c.flags() | 4194304);
            }
            if ((this.audio_codec.capabilities() & 512) != 0) {
                this.audio_c.strict_std_compliance(-2);
            }
        }
        avformat.av_dump_format(this.oc, 0, this.filename, 1);
        if (this.video_st != null && inpVideoStream == null) {
            aVDictionary = new AVDictionary(null);
            if (this.videoQuality >= 0.0d) {
                avutil.av_dict_set(aVDictionary, "crf", "" + this.videoQuality, 0);
            }
            for (Entry<String, String> e : this.videoOptions.entrySet()) {
                avutil.av_dict_set(aVDictionary, (String) e.getKey(), (String) e.getValue(), 0);
            }
            ret = avcodec.avcodec_open2(this.video_c, this.video_codec, aVDictionary);
            if (ret < 0) {
                release();
                avutil.av_dict_free(aVDictionary);
                throw new Exception("avcodec_open2() error " + ret + ": Could not open video codec.");
            }
            avutil.av_dict_free(aVDictionary);
            this.video_outbuf = null;
            if ((this.oformat.flags() & 32) == 0) {
                this.video_outbuf_size = Math.max(262144, (this.video_c.width() * 8) * this.video_c.height());
                this.video_outbuf = new BytePointer(avutil.av_malloc((long) this.video_outbuf_size));
            }
            av_frame_alloc = avutil.av_frame_alloc();
            this.picture = av_frame_alloc;
            if (av_frame_alloc == null) {
                release();
                throw new Exception("av_frame_alloc() error: Could not allocate picture.");
            }
            this.picture.pts(0);
            BytePointer bytePointer = new BytePointer(avutil.av_malloc((long) avcodec.avpicture_get_size(this.video_c.pix_fmt(), this.video_c.width(), this.video_c.height())));
            this.picture_buf = bytePointer;
            if (bytePointer.isNull()) {
                release();
                throw new Exception("av_malloc() error: Could not allocate picture buffer.");
            }
            av_frame_alloc = avutil.av_frame_alloc();
            this.tmp_picture = av_frame_alloc;
            if (av_frame_alloc == null) {
                release();
                throw new Exception("av_frame_alloc() error: Could not allocate temporary picture.");
            }
            aVDictionary = new AVDictionary(null);
            for (Entry<String, String> e2 : this.videoMetadata.entrySet()) {
                avutil.av_dict_set(aVDictionary, (String) e2.getKey(), (String) e2.getValue(), 0);
            }
            this.video_st.metadata(aVDictionary);
        }
        if (this.audio_st != null && inpAudioStream == null) {
            aVDictionary = new AVDictionary(null);
            if (this.audioQuality >= 0.0d) {
                avutil.av_dict_set(aVDictionary, "crf", "" + this.audioQuality, 0);
            }
            for (Entry<String, String> e22 : this.audioOptions.entrySet()) {
                avutil.av_dict_set(aVDictionary, (String) e22.getKey(), (String) e22.getValue(), 0);
            }
            ret = avcodec.avcodec_open2(this.audio_c, this.audio_codec, aVDictionary);
            if (ret < 0) {
                release();
                avutil.av_dict_free(aVDictionary);
                throw new Exception("avcodec_open2() error " + ret + ": Could not open audio codec.");
            }
            avutil.av_dict_free(aVDictionary);
            this.audio_outbuf_size = 262144;
            this.audio_outbuf = new BytePointer(avutil.av_malloc((long) this.audio_outbuf_size));
            if (this.audio_c.frame_size() <= 1) {
                this.audio_outbuf_size = 16384;
                this.audio_input_frame_size = this.audio_outbuf_size / this.audio_c.channels();
                switch (this.audio_c.codec_id()) {
                    case 65536:
                    case avcodec.AV_CODEC_ID_PCM_S16BE /*65537*/:
                    case avcodec.AV_CODEC_ID_PCM_U16LE /*65538*/:
                    case avcodec.AV_CODEC_ID_PCM_U16BE /*65539*/:
                        this.audio_input_frame_size >>= 1;
                        break;
                }
            }
            this.audio_input_frame_size = this.audio_c.frame_size();
            int planes = avutil.av_sample_fmt_is_planar(this.audio_c.sample_fmt()) != 0 ? this.audio_c.channels() : 1;
            int data_size = avutil.av_samples_get_buffer_size((IntPointer) null, this.audio_c.channels(), this.audio_input_frame_size, this.audio_c.sample_fmt(), 1) / planes;
            this.samples_out = new BytePointer[planes];
            for (i = 0; i < this.samples_out.length; i++) {
                this.samples_out[i] = new BytePointer(avutil.av_malloc((long) data_size)).capacity((long) data_size);
            }
            this.samples_in = new Pointer[8];
            this.samples_in_ptr = new PointerPointer(8);
            this.samples_out_ptr = new PointerPointer(8);
            av_frame_alloc = avutil.av_frame_alloc();
            this.frame = av_frame_alloc;
            if (av_frame_alloc == null) {
                release();
                throw new Exception("av_frame_alloc() error: Could not allocate audio frame.");
            }
            this.frame.pts(0);
            aVDictionary = new AVDictionary(null);
            for (Entry<String, String> e222 : this.audioMetadata.entrySet()) {
                avutil.av_dict_set(aVDictionary, (String) e222.getKey(), (String) e222.getValue(), 0);
            }
            this.audio_st.metadata(aVDictionary);
        }
        aVDictionary = new AVDictionary(null);
        for (Entry<String, String> e2222 : this.options.entrySet()) {
            avutil.av_dict_set(aVDictionary, (String) e2222.getKey(), (String) e2222.getValue(), 0);
        }
        if (this.outputStream == null && (this.oformat.flags() & 1) == 0) {
            AVIOContext aVIOContext = new AVIOContext(null);
            ret = avformat.avio_open2(aVIOContext, this.filename, 2, null, aVDictionary);
            if (ret < 0) {
                release();
                avutil.av_dict_free(aVDictionary);
                throw new Exception("avio_open2 error() error " + ret + ": Could not open '" + this.filename + "'");
            }
            this.oc.pb(aVIOContext);
        }
        aVDictionary = new AVDictionary(null);
        for (Entry<String, String> e22222 : this.metadata.entrySet()) {
            avutil.av_dict_set(aVDictionary, (String) e22222.getKey(), (String) e22222.getValue(), 0);
        }
        avformat.avformat_write_header(this.oc.metadata(aVDictionary), aVDictionary);
        avutil.av_dict_free(aVDictionary);
    }

    public void stop() throws Exception {
        if (this.oc != null) {
            while (this.video_st != null && this.ifmt_ctx == null) {
                try {
                    if (!recordImage(0, 0, 0, 0, 0, -1, (Buffer[]) null)) {
                        break;
                    }
                } finally {
                    release();
                }
            }
            while (this.audio_st != null && this.ifmt_ctx == null) {
                if (!recordSamples(0, 0, (Buffer[]) null)) {
                    break;
                }
            }
            if (!this.interleaved || this.video_st == null || this.audio_st == null) {
                avformat.av_write_frame(this.oc, null);
            } else {
                avformat.av_interleaved_write_frame(this.oc, null);
            }
            avformat.av_write_trailer(this.oc);
        }
    }

    public void record(Frame frame) throws Exception {
        record(frame, -1);
    }

    public void record(Frame frame, int pixelFormat) throws Exception {
        if (frame == null || (frame.image == null && frame.samples == null)) {
            recordImage(0, 0, 0, 0, 0, pixelFormat, (Buffer[]) null);
            return;
        }
        if (frame.image != null) {
            frame.keyFrame = recordImage(frame.imageWidth, frame.imageHeight, frame.imageDepth, frame.imageChannels, frame.imageStride, pixelFormat, frame.image);
        }
        if (frame.samples != null) {
            frame.keyFrame = recordSamples(frame.sampleRate, frame.audioChannels, frame.samples);
        }
    }

    public boolean recordImage(int width, int height, int depth, int channels, int stride, int pixelFormat, Buffer... image) throws Exception {
        if (this.video_st == null) {
            throw new Exception("No video output stream (Is imageWidth > 0 && imageHeight > 0 and has start() been called?)");
        }
        if (!(image == null || image.length == 0)) {
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
            if (this.video_c.pix_fmt() == pixelFormat && this.video_c.width() == width && this.video_c.height() == height) {
                avcodec.avpicture_fill(new AVPicture(this.picture), data, pixelFormat, width, height);
                this.picture.linesize(0, step);
                this.picture.format(pixelFormat);
                this.picture.width(width);
                this.picture.height(height);
            } else {
                this.img_convert_ctx = swscale.sws_getCachedContext(this.img_convert_ctx, width, height, pixelFormat, this.video_c.width(), this.video_c.height(), this.video_c.pix_fmt(), 2, null, null, (DoublePointer) null);
                if (this.img_convert_ctx == null) {
                    throw new Exception("sws_getCachedContext() error: Cannot initialize the conversion context.");
                }
                avcodec.avpicture_fill(new AVPicture(this.tmp_picture), data, pixelFormat, width, height);
                avcodec.avpicture_fill(new AVPicture(this.picture), this.picture_buf, this.video_c.pix_fmt(), this.video_c.width(), this.video_c.height());
                this.tmp_picture.linesize(0, step);
                this.tmp_picture.format(pixelFormat);
                this.tmp_picture.width(width);
                this.tmp_picture.height(height);
                this.picture.format(this.video_c.pix_fmt());
                this.picture.width(this.video_c.width());
                this.picture.height(this.video_c.height());
                swscale.sws_scale(this.img_convert_ctx, new PointerPointer(this.tmp_picture), this.tmp_picture.linesize(), 0, height, new PointerPointer(this.picture), this.picture.linesize());
            }
        }
        if ((this.oformat.flags() & 32) == 0) {
            avcodec.av_init_packet(this.video_pkt);
            this.video_pkt.data(this.video_outbuf);
            this.video_pkt.size(this.video_outbuf_size);
            this.picture.quality(this.video_c.global_quality());
            AVCodecContext aVCodecContext = this.video_c;
            AVPacket aVPacket = this.video_pkt;
            AVFrame aVFrame = (image == null || image.length == 0) ? null : this.picture;
            int ret = avcodec.avcodec_encode_video2(aVCodecContext, aVPacket, aVFrame, this.got_video_packet);
            if (ret < 0) {
                throw new Exception("avcodec_encode_video2() error " + ret + ": Could not encode video packet.");
            }
            this.picture.pts(this.picture.pts() + 1);
            if (this.got_video_packet[0] == 0) {
                return false;
            }
            if (this.video_pkt.pts() != avutil.AV_NOPTS_VALUE) {
                this.video_pkt.pts(avutil.av_rescale_q(this.video_pkt.pts(), this.video_c.time_base(), this.video_st.time_base()));
            }
            if (this.video_pkt.dts() != avutil.AV_NOPTS_VALUE) {
                this.video_pkt.dts(avutil.av_rescale_q(this.video_pkt.dts(), this.video_c.time_base(), this.video_st.time_base()));
            }
            this.video_pkt.stream_index(this.video_st.index());
        } else if (image == null || image.length == 0) {
            return false;
        } else {
            avcodec.av_init_packet(this.video_pkt);
            this.video_pkt.flags(this.video_pkt.flags() | 1);
            this.video_pkt.stream_index(this.video_st.index());
            this.video_pkt.data(new BytePointer(this.picture));
            this.video_pkt.size(Loader.sizeof(AVPicture.class));
        }
        writePacket(0, this.video_pkt);
        if (image == null) {
            return this.got_video_packet[0] != 0;
        } else {
            if ((this.video_pkt.flags() & 1) != 0) {
                return true;
            }
            return false;
        }
    }

    public boolean recordSamples(Buffer... samples) throws Exception {
        return recordSamples(0, 0, samples);
    }

    public boolean recordSamples(int sampleRate, int audioChannels, Buffer... samples) throws Exception {
        if (this.audio_st == null) {
            throw new Exception("No audio output stream (Is audioChannels > 0 and has start() been called?)");
        }
        int inputChannels;
        int i;
        int ret;
        if (sampleRate <= 0) {
            sampleRate = this.audio_c.sample_rate();
        }
        if (audioChannels <= 0) {
            audioChannels = this.audio_c.channels();
        }
        int inputSize = samples != null ? samples[0].limit() - samples[0].position() : 0;
        int inputFormat = this.samples_format;
        if (samples == null || samples.length <= 1) {
            inputChannels = audioChannels;
        } else {
            inputChannels = 1;
        }
        int inputDepth = 0;
        int outputFormat = this.audio_c.sample_fmt();
        int outputChannels = this.samples_out.length > 1 ? 1 : this.audio_c.channels();
        int outputDepth = avutil.av_get_bytes_per_sample(outputFormat);
        if (samples != null && (samples[0] instanceof ByteBuffer)) {
            inputFormat = samples.length > 1 ? 5 : 0;
            inputDepth = 1;
            i = 0;
            while (i < samples.length) {
                ByteBuffer b = samples[i];
                if ((this.samples_in[i] instanceof BytePointer) && this.samples_in[i].capacity() >= ((long) inputSize) && b.hasArray()) {
                    ((BytePointer) this.samples_in[i]).position(0).put(b.array(), b.position(), inputSize);
                } else {
                    this.samples_in[i] = new BytePointer(b);
                }
                i++;
            }
        } else if (samples != null && (samples[0] instanceof ShortBuffer)) {
            inputFormat = samples.length > 1 ? 6 : 1;
            inputDepth = 2;
            i = 0;
            while (i < samples.length) {
                ShortBuffer b2 = samples[i];
                if ((this.samples_in[i] instanceof ShortPointer) && this.samples_in[i].capacity() >= ((long) inputSize) && b2.hasArray()) {
                    ((ShortPointer) this.samples_in[i]).position(0).put(b2.array(), samples[i].position(), inputSize);
                } else {
                    this.samples_in[i] = new ShortPointer(b2);
                }
                i++;
            }
        } else if (samples != null && (samples[0] instanceof IntBuffer)) {
            inputFormat = samples.length > 1 ? 7 : 2;
            inputDepth = 4;
            i = 0;
            while (i < samples.length) {
                IntBuffer b3 = samples[i];
                if ((this.samples_in[i] instanceof IntPointer) && this.samples_in[i].capacity() >= ((long) inputSize) && b3.hasArray()) {
                    ((IntPointer) this.samples_in[i]).position(0).put(b3.array(), samples[i].position(), inputSize);
                } else {
                    this.samples_in[i] = new IntPointer(b3);
                }
                i++;
            }
        } else if (samples != null && (samples[0] instanceof FloatBuffer)) {
            inputFormat = samples.length > 1 ? 8 : 3;
            inputDepth = 4;
            i = 0;
            while (i < samples.length) {
                FloatBuffer b4 = samples[i];
                if ((this.samples_in[i] instanceof FloatPointer) && this.samples_in[i].capacity() >= ((long) inputSize) && b4.hasArray()) {
                    ((FloatPointer) this.samples_in[i]).position(0).put(b4.array(), b4.position(), inputSize);
                } else {
                    this.samples_in[i] = new FloatPointer(b4);
                }
                i++;
            }
        } else if (samples != null && (samples[0] instanceof DoubleBuffer)) {
            inputFormat = samples.length > 1 ? 9 : 4;
            inputDepth = 8;
            i = 0;
            while (i < samples.length) {
                DoubleBuffer b5 = samples[i];
                if ((this.samples_in[i] instanceof DoublePointer) && this.samples_in[i].capacity() >= ((long) inputSize) && b5.hasArray()) {
                    ((DoublePointer) this.samples_in[i]).position(0).put(b5.array(), b5.position(), inputSize);
                } else {
                    this.samples_in[i] = new DoublePointer(b5);
                }
                i++;
            }
        } else if (samples != null) {
            throw new Exception("Audio samples Buffer has unsupported type: " + samples);
        }
        if (!(this.samples_convert_ctx != null && this.samples_channels == audioChannels && this.samples_format == inputFormat && this.samples_rate == sampleRate)) {
            this.samples_convert_ctx = swresample.swr_alloc_set_opts(this.samples_convert_ctx, this.audio_c.channel_layout(), outputFormat, this.audio_c.sample_rate(), avutil.av_get_default_channel_layout(audioChannels), inputFormat, sampleRate, 0, null);
            if (this.samples_convert_ctx == null) {
                throw new Exception("swr_alloc_set_opts() error: Cannot allocate the conversion context.");
            }
            ret = swresample.swr_init(this.samples_convert_ctx);
            if (ret < 0) {
                throw new Exception("swr_init() error " + ret + ": Cannot initialize the conversion context.");
            }
            this.samples_channels = audioChannels;
            this.samples_format = inputFormat;
            this.samples_rate = sampleRate;
        }
        i = 0;
        while (samples != null && i < samples.length) {
            this.samples_in[i].position(this.samples_in[i].position() * ((long) inputDepth)).limit((this.samples_in[i].position() + ((long) inputSize)) * ((long) inputDepth));
            i++;
        }
        while (true) {
            int outputCount = (int) Math.min((this.samples_out[0].limit() - this.samples_out[0].position()) / ((long) (outputChannels * outputDepth)), 2147483647L);
            int inputCount = Math.min((int) Math.min(samples != null ? (this.samples_in[0].limit() - this.samples_in[0].position()) / ((long) (inputChannels * inputDepth)) : 0, 2147483647L), (((outputCount * sampleRate) + this.audio_c.sample_rate()) - 1) / this.audio_c.sample_rate());
            i = 0;
            while (samples != null && i < samples.length) {
                this.samples_in_ptr.put((long) i, this.samples_in[i]);
                i++;
            }
            for (i = 0; i < this.samples_out.length; i++) {
                this.samples_out_ptr.put((long) i, this.samples_out[i]);
            }
            ret = swresample.swr_convert(this.samples_convert_ctx, this.samples_out_ptr, outputCount, this.samples_in_ptr, inputCount);
            if (ret < 0) {
                throw new Exception("swr_convert() error " + ret + ": Cannot convert audio samples.");
            } else if (ret == 0) {
                break;
            } else {
                i = 0;
                while (samples != null && i < samples.length) {
                    this.samples_in[i].position(this.samples_in[i].position() + ((long) ((inputCount * inputChannels) * inputDepth)));
                    i++;
                }
                for (i = 0; i < this.samples_out.length; i++) {
                    this.samples_out[i].position(this.samples_out[i].position() + ((long) ((ret * outputChannels) * outputDepth)));
                }
                if (samples == null || this.samples_out[0].position() >= this.samples_out[0].limit()) {
                    this.frame.nb_samples(this.audio_input_frame_size);
                    avcodec.avcodec_fill_audio_frame(this.frame, this.audio_c.channels(), outputFormat, this.samples_out[0], (int) Math.min(this.samples_out[0].limit(), 2147483647L), 0);
                    for (i = 0; i < this.samples_out.length; i++) {
                        this.frame.data(i, this.samples_out[i].position(0));
                        this.frame.linesize(i, (int) Math.min(this.samples_out[i].limit(), 2147483647L));
                    }
                    this.frame.quality(this.audio_c.global_quality());
                    record(this.frame);
                }
            }
        }
        if (samples == null) {
            return record((AVFrame) null);
        }
        if (this.frame.key_frame() != 0) {
            return true;
        }
        return false;
    }

    boolean record(AVFrame frame) throws Exception {
        avcodec.av_init_packet(this.audio_pkt);
        this.audio_pkt.data(this.audio_outbuf);
        this.audio_pkt.size(this.audio_outbuf_size);
        int ret = avcodec.avcodec_encode_audio2(this.audio_c, this.audio_pkt, frame, this.got_audio_packet);
        if (ret < 0) {
            throw new Exception("avcodec_encode_audio2() error " + ret + ": Could not encode audio packet.");
        }
        if (frame != null) {
            frame.pts(frame.pts() + ((long) frame.nb_samples()));
        }
        if (this.got_audio_packet[0] == 0) {
            return false;
        }
        if (this.audio_pkt.pts() != avutil.AV_NOPTS_VALUE) {
            this.audio_pkt.pts(avutil.av_rescale_q(this.audio_pkt.pts(), this.audio_c.time_base(), this.audio_st.time_base()));
        }
        if (this.audio_pkt.dts() != avutil.AV_NOPTS_VALUE) {
            this.audio_pkt.dts(avutil.av_rescale_q(this.audio_pkt.dts(), this.audio_c.time_base(), this.audio_st.time_base()));
        }
        this.audio_pkt.flags(this.audio_pkt.flags() | 1);
        this.audio_pkt.stream_index(this.audio_st.index());
        writePacket(1, this.audio_pkt);
        return true;
    }

    private void writePacket(int mediaType, AVPacket avPacket) throws Exception {
        AVStream avStream = mediaType == 0 ? this.audio_st : mediaType == 1 ? this.video_st : null;
        String mediaTypeStr = mediaType == 0 ? "video" : mediaType == 1 ? "audio" : "unsupported media stream type";
        synchronized (this.oc) {
            int ret;
            if (!this.interleaved || avStream == null) {
                ret = avformat.av_write_frame(this.oc, avPacket);
                if (ret < 0) {
                    throw new Exception("av_write_frame() error " + ret + " while writing " + mediaTypeStr + " packet.");
                }
            }
            ret = avformat.av_interleaved_write_frame(this.oc, avPacket);
            if (ret < 0) {
                throw new Exception("av_interleaved_write_frame() error " + ret + " while writing interleaved " + mediaTypeStr + " packet.");
            }
        }
    }

    public boolean recordPacket(AVPacket pkt) throws Exception {
        if (pkt == null) {
            return false;
        }
        AVStream in_stream = this.ifmt_ctx.streams(pkt.stream_index());
        pkt.dts(avutil.AV_NOPTS_VALUE);
        pkt.pts(avutil.AV_NOPTS_VALUE);
        pkt.pos(-1);
        if (in_stream.codec().codec_type() == 0 && this.video_st != null) {
            pkt.stream_index(this.video_st.index());
            pkt.duration((long) ((int) avutil.av_rescale_q(pkt.duration(), in_stream.codec().time_base(), this.video_st.codec().time_base())));
            writePacket(0, pkt);
        } else if (in_stream.codec().codec_type() == 1 && this.audio_st != null && this.audioChannels > 0) {
            pkt.stream_index(this.audio_st.index());
            pkt.duration((long) ((int) avutil.av_rescale_q(pkt.duration(), in_stream.codec().time_base(), this.audio_st.codec().time_base())));
            writePacket(1, pkt);
        }
        avcodec.av_free_packet(pkt);
        return true;
    }
}
